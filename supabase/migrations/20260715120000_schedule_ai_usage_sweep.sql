create extension if not exists pg_cron;

do $do$
begin
  if exists (select 1 from cron.job where jobname = 'sweep-ai-usage-events') then
    perform cron.unschedule('sweep-ai-usage-events');
  end if;

  perform cron.schedule(
    'sweep-ai-usage-events',
    '17 3 * * *',
    'select public.sweep_ai_usage_events()'
  );
end
$do$;
