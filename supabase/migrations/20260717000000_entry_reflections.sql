create table if not exists public.entry_reflections (
  moment_id         text primary key,
  user_id           uuid not null references auth.users(id) on delete cascade,
  summary           text not null,
  mood_valence      text not null check (mood_valence in
                        ('VERY_LOW','LOW','NEUTRAL','POSITIVE','VERY_POSITIVE')),
  mood_confidence   double precision not null,
  dominant_emotions text[] not null default '{}',
  themes            text[] not null default '{}',
  distress          text not null check (distress in ('NONE','MILD','ELEVATED','CRISIS')),
  feedback          text,
  question          text,
  cover_image_url   text,
  created_at        timestamptz not null default now(),
  updated_at        timestamptz not null default now()
);

create index if not exists entry_reflections_user_created_idx
  on public.entry_reflections (user_id, created_at desc);

create trigger entry_reflections_set_updated_at
  before update on public.entry_reflections
  for each row execute function public.set_updated_at();

alter table public.entry_reflections enable row level security;

create policy "entry_reflections_select_own" on public.entry_reflections
  for select using (auth.uid() = user_id);
create policy "entry_reflections_insert_own" on public.entry_reflections
  for insert with check (auth.uid() = user_id);
create policy "entry_reflections_update_own" on public.entry_reflections
  for update using (auth.uid() = user_id) with check (auth.uid() = user_id);
create policy "entry_reflections_delete_own" on public.entry_reflections
  for delete using (auth.uid() = user_id);

grant all on public.entry_reflections to anon, authenticated, service_role;
