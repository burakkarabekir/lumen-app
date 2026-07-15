create table if not exists public.ai_usage_events (
  id bigserial primary key,
  user_id uuid not null references auth.users (id) on delete cascade,
  kind text not null check (kind in ('analyze', 'weekly')),
  created_at timestamptz not null default now()
);

create index if not exists ai_usage_events_user_kind_created_idx
  on public.ai_usage_events (user_id, kind, created_at desc);

alter table public.ai_usage_events enable row level security;

drop policy if exists ai_usage_events_select_own on public.ai_usage_events;
create policy ai_usage_events_select_own
  on public.ai_usage_events
  for select
  using (auth.uid() = user_id);

create or replace function public.consume_ai_credit(p_user_id uuid, p_kind text)
returns jsonb
language plpgsql
security definer
set search_path to 'public'
as $function$
declare
  v_premium boolean;
  v_daily_limit int := 7;
  v_free_weekly_limit int := 1;
  v_used int;
begin
  if p_kind not in ('analyze', 'weekly') then
    return jsonb_build_object('allowed', false, 'reason', 'unknown_kind', 'kind', p_kind);
  end if;

  perform pg_advisory_xact_lock(hashtextextended(p_user_id::text, 0));

  v_premium := public.is_premium(p_user_id);

  if v_premium then
    if p_kind = 'weekly' then
      insert into public.ai_usage_events (user_id, kind) values (p_user_id, p_kind);
      return jsonb_build_object('allowed', true, 'premium', true, 'kind', p_kind);
    end if;

    select count(*) into v_used
      from public.ai_usage_events
     where user_id = p_user_id
       and kind = 'analyze'
       and created_at >= date_trunc('day', (now() at time zone 'utc')) at time zone 'utc';

    if v_used >= v_daily_limit then
      return jsonb_build_object('allowed', false, 'reason', 'daily_limit', 'premium', true,
                                'limit', v_daily_limit, 'remaining', 0, 'kind', p_kind);
    end if;

    insert into public.ai_usage_events (user_id, kind) values (p_user_id, p_kind);
    return jsonb_build_object('allowed', true, 'premium', true,
                              'limit', v_daily_limit, 'remaining', v_daily_limit - v_used - 1, 'kind', p_kind);
  end if;

  if p_kind = 'weekly' then
    return jsonb_build_object('allowed', false, 'reason', 'premium_required', 'premium', false, 'kind', p_kind);
  end if;

  select count(*) into v_used
    from public.ai_usage_events
   where user_id = p_user_id
     and kind = 'analyze'
     and created_at >= date_trunc('week', (now() at time zone 'utc')) at time zone 'utc';

  if v_used >= v_free_weekly_limit then
    return jsonb_build_object('allowed', false, 'reason', 'weekly_limit', 'premium', false,
                              'limit', v_free_weekly_limit, 'remaining', 0, 'kind', p_kind);
  end if;

  insert into public.ai_usage_events (user_id, kind) values (p_user_id, p_kind);
  return jsonb_build_object('allowed', true, 'premium', false,
                            'limit', v_free_weekly_limit, 'remaining', v_free_weekly_limit - v_used - 1, 'kind', p_kind);
end;
$function$;

create or replace function public.sweep_ai_usage_events()
returns integer
language plpgsql
security definer
set search_path to 'public'
as $function$
declare
  v_deleted integer;
begin
  delete from public.ai_usage_events where created_at < now() - interval '30 days';
  get diagnostics v_deleted = row_count;
  return v_deleted;
end;
$function$;

do $do$
begin
  if exists (select 1 from pg_extension where extname = 'pg_cron') then
    perform cron.unschedule('sweep-ai-usage-events')
      where exists (select 1 from cron.job where jobname = 'sweep-ai-usage-events');
    perform cron.schedule('sweep-ai-usage-events', '17 3 * * *', 'select public.sweep_ai_usage_events()');
  end if;
end
$do$;

drop table if exists public.ai_usage_daily;
drop table if exists public.ai_usage;
