


SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


COMMENT ON SCHEMA "public" IS 'standard public schema';



CREATE EXTENSION IF NOT EXISTS "pg_stat_statements" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "pgcrypto" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "supabase_vault" WITH SCHEMA "vault";






CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA "extensions";






CREATE OR REPLACE FUNCTION "public"."consume_ai_credit"("p_user_id" "uuid", "p_kind" "text") RETURNS "jsonb"
    LANGUAGE "plpgsql" SECURITY DEFINER
    SET "search_path" TO 'public'
    AS $$
declare
  v_premium     boolean;
  v_month       text := to_char((now() at time zone 'utc'), 'YYYY-MM');
  v_day         text := to_char((now() at time zone 'utc'), 'YYYY-MM-DD');
  v_daily_limit int  := 20;
  v_limit       int;
  v_used        int;
  v_daily_used  int;
begin
  if p_kind not in ('analyze', 'weekly') then
    return jsonb_build_object('allowed', false, 'error', 'unknown_kind', 'kind', p_kind);
  end if;
  v_premium := public.is_premium(p_user_id);
  if v_premium then
    if p_kind = 'analyze' then
      insert into public.ai_usage_daily (user_id, period_day, analyze_count)
      values (p_user_id, v_day, 1)
      on conflict (user_id, period_day) do update
        set analyze_count = public.ai_usage_daily.analyze_count + 1, updated_at = now()
        where public.ai_usage_daily.analyze_count < v_daily_limit
      returning analyze_count into v_daily_used;
      if v_daily_used is null then
        return jsonb_build_object('allowed', false, 'reason', 'daily_limit', 'premium', true,
                                  'daily_remaining', 0, 'daily_limit', v_daily_limit, 'kind', p_kind);
      end if;
    end if;
    insert into public.ai_usage (user_id, period_month, analyze_count, weekly_count)
    values (p_user_id, v_month,
            case when p_kind = 'analyze' then 1 else 0 end,
            case when p_kind = 'weekly'  then 1 else 0 end)
    on conflict (user_id, period_month) do update
      set analyze_count = public.ai_usage.analyze_count + (case when p_kind = 'analyze' then 1 else 0 end),
          weekly_count  = public.ai_usage.weekly_count  + (case when p_kind = 'weekly'  then 1 else 0 end),
          updated_at    = now();
    return jsonb_build_object('allowed', true, 'premium', true, 'remaining', -1, 'limit', -1,
                              'daily_remaining', case when p_kind = 'analyze' then greatest(v_daily_limit - v_daily_used, 0) else -1 end,
                              'daily_limit',     case when p_kind = 'analyze' then v_daily_limit else -1 end,
                              'kind', p_kind);
  end if;
  v_limit := case when p_kind = 'analyze' then 3 else 0 end;
  if v_limit = 0 then
    return jsonb_build_object('allowed', false, 'premium', false, 'remaining', 0, 'limit', 0, 'kind', p_kind);
  end if;
  insert into public.ai_usage (user_id, period_month, analyze_count, weekly_count)
  values (p_user_id, v_month, 1, 0)
  on conflict (user_id, period_month) do update
    set analyze_count = public.ai_usage.analyze_count + 1, updated_at = now()
    where public.ai_usage.analyze_count < v_limit
  returning analyze_count into v_used;
  if v_used is null then
    return jsonb_build_object('allowed', false, 'premium', false, 'remaining', 0, 'limit', v_limit, 'kind', p_kind);
  end if;
  return jsonb_build_object('allowed', true, 'premium', false,
                            'remaining', greatest(v_limit - v_used, 0),
                            'limit', v_limit, 'kind', p_kind);
end;
$$;


ALTER FUNCTION "public"."consume_ai_credit"("p_user_id" "uuid", "p_kind" "text") OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."handle_new_user"() RETURNS "trigger"
    LANGUAGE "plpgsql" SECURITY DEFINER
    SET "search_path" TO ''
    AS $$
declare
    v_policy_version text;
    v_accepted_at timestamptz;
begin
    insert into public.profiles (id, display_name, avatar_url)
    values (
        new.id,
        coalesce(new.raw_user_meta_data ->> 'full_name', new.raw_user_meta_data ->> 'name'),
        new.raw_user_meta_data ->> 'avatar_url'
    )
    on conflict (id) do nothing;

    begin
        v_policy_version := nullif(new.raw_user_meta_data ->> 'policy_version', '');
        if v_policy_version is not null then
            v_accepted_at := coalesce(
                nullif(new.raw_user_meta_data ->> 'terms_accepted_at', '')::timestamptz,
                now()
            );
            insert into public.consent_records (user_id, document, version, method, platform, accepted_at)
            values
                (new.id, 'terms_of_service', v_policy_version, 'explicit', new.raw_user_meta_data ->> 'consent_platform', v_accepted_at),
                (new.id, 'privacy_policy',   v_policy_version, 'explicit', new.raw_user_meta_data ->> 'consent_platform', v_accepted_at);
        end if;
    exception when others then
        null;
    end;

    return new;
end;
$$;


ALTER FUNCTION "public"."handle_new_user"() OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."is_premium"("p_user_id" "uuid") RETURNS boolean
    LANGUAGE "sql" STABLE SECURITY DEFINER
    SET "search_path" TO 'public'
    AS $$
  select exists (
    select 1
    from public.subscriptions s
    where s.user_id = p_user_id
      and coalesce(s.entitlement, '') <> ''
      and s.status in ('active', 'trialing', 'in_grace_period')
      and (s.period_end is null or s.period_end > now())
  );
$$;


ALTER FUNCTION "public"."is_premium"("p_user_id" "uuid") OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."set_updated_at"() RETURNS "trigger"
    LANGUAGE "plpgsql"
    SET "search_path" TO ''
    AS $$
begin
  new.updated_at = now();
  return new;
end;
$$;


ALTER FUNCTION "public"."set_updated_at"() OWNER TO "postgres";

SET default_tablespace = '';

SET default_table_access_method = "heap";


CREATE TABLE IF NOT EXISTS "public"."ai_usage" (
    "user_id" "uuid" NOT NULL,
    "period_month" "text" NOT NULL,
    "analyze_count" integer DEFAULT 0 NOT NULL,
    "weekly_count" integer DEFAULT 0 NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL
);


ALTER TABLE "public"."ai_usage" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."ai_usage_daily" (
    "user_id" "uuid" NOT NULL,
    "period_day" "text" NOT NULL,
    "analyze_count" integer DEFAULT 0 NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL
);


ALTER TABLE "public"."ai_usage_daily" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."consent_records" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "user_id" "uuid" NOT NULL,
    "document" "text" NOT NULL,
    "version" "text" NOT NULL,
    "method" "text" DEFAULT 'explicit'::"text" NOT NULL,
    "platform" "text",
    "accepted_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL
);


ALTER TABLE "public"."consent_records" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."moments" (
    "id" "text" NOT NULL,
    "user_id" "uuid" NOT NULL,
    "title" "text" NOT NULL,
    "body" "text",
    "created_at_ms" bigint NOT NULL,
    "moods" "text"[] DEFAULT '{}'::"text"[] NOT NULL,
    "tags" "text"[] DEFAULT '{}'::"text"[] NOT NULL,
    "location" "jsonb",
    "attachments" "jsonb" DEFAULT '[]'::"jsonb" NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "is_favorite" boolean DEFAULT false NOT NULL
);


ALTER TABLE "public"."moments" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."profiles" (
    "id" "uuid" NOT NULL,
    "display_name" "text",
    "avatar_url" "text",
    "job_title" "text" DEFAULT ''::"text" NOT NULL,
    "join_year" "text" DEFAULT ''::"text" NOT NULL,
    "is_premium" boolean DEFAULT false NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL
);


ALTER TABLE "public"."profiles" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."subscriptions" (
    "user_id" "uuid" NOT NULL,
    "entitlement" "text",
    "status" "text" DEFAULT 'unknown'::"text" NOT NULL,
    "product_id" "text",
    "store" "text",
    "rc_app_user_id" "text",
    "period_end" timestamp with time zone,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL
);


ALTER TABLE "public"."subscriptions" OWNER TO "postgres";


ALTER TABLE ONLY "public"."ai_usage_daily"
    ADD CONSTRAINT "ai_usage_daily_pkey" PRIMARY KEY ("user_id", "period_day");



ALTER TABLE ONLY "public"."ai_usage"
    ADD CONSTRAINT "ai_usage_pkey" PRIMARY KEY ("user_id", "period_month");



ALTER TABLE ONLY "public"."consent_records"
    ADD CONSTRAINT "consent_records_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."moments"
    ADD CONSTRAINT "moments_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."profiles"
    ADD CONSTRAINT "profiles_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."subscriptions"
    ADD CONSTRAINT "subscriptions_pkey" PRIMARY KEY ("user_id");



CREATE INDEX "consent_records_user_id_idx" ON "public"."consent_records" USING "btree" ("user_id");



CREATE INDEX "moments_user_created_idx" ON "public"."moments" USING "btree" ("user_id", "created_at_ms" DESC);



CREATE OR REPLACE TRIGGER "moments_set_updated_at" BEFORE UPDATE ON "public"."moments" FOR EACH ROW EXECUTE FUNCTION "public"."set_updated_at"();



CREATE OR REPLACE TRIGGER "profiles_set_updated_at" BEFORE UPDATE ON "public"."profiles" FOR EACH ROW EXECUTE FUNCTION "public"."set_updated_at"();



ALTER TABLE ONLY "public"."ai_usage_daily"
    ADD CONSTRAINT "ai_usage_daily_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."ai_usage"
    ADD CONSTRAINT "ai_usage_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."consent_records"
    ADD CONSTRAINT "consent_records_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."moments"
    ADD CONSTRAINT "moments_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."profiles"
    ADD CONSTRAINT "profiles_id_fkey" FOREIGN KEY ("id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."subscriptions"
    ADD CONSTRAINT "subscriptions_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE "public"."ai_usage" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."ai_usage_daily" ENABLE ROW LEVEL SECURITY;


CREATE POLICY "ai_usage_daily_select_own" ON "public"."ai_usage_daily" FOR SELECT USING (("auth"."uid"() = "user_id"));



CREATE POLICY "ai_usage_select_own" ON "public"."ai_usage" FOR SELECT USING (("auth"."uid"() = "user_id"));



ALTER TABLE "public"."consent_records" ENABLE ROW LEVEL SECURITY;


CREATE POLICY "consent_records_insert_own" ON "public"."consent_records" FOR INSERT TO "authenticated" WITH CHECK (("auth"."uid"() = "user_id"));



CREATE POLICY "consent_records_select_own" ON "public"."consent_records" FOR SELECT TO "authenticated" USING (("auth"."uid"() = "user_id"));



ALTER TABLE "public"."moments" ENABLE ROW LEVEL SECURITY;


CREATE POLICY "moments_delete_own" ON "public"."moments" FOR DELETE USING ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "moments_insert_own" ON "public"."moments" FOR INSERT WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "moments_select_own" ON "public"."moments" FOR SELECT USING ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "moments_update_own" ON "public"."moments" FOR UPDATE USING ((( SELECT "auth"."uid"() AS "uid") = "user_id")) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



ALTER TABLE "public"."profiles" ENABLE ROW LEVEL SECURITY;


CREATE POLICY "profiles_insert_own" ON "public"."profiles" FOR INSERT WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "id"));



CREATE POLICY "profiles_select_own" ON "public"."profiles" FOR SELECT USING ((( SELECT "auth"."uid"() AS "uid") = "id"));



CREATE POLICY "profiles_update_own" ON "public"."profiles" FOR UPDATE USING ((( SELECT "auth"."uid"() AS "uid") = "id")) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "id"));



ALTER TABLE "public"."subscriptions" ENABLE ROW LEVEL SECURITY;


CREATE POLICY "subscriptions_select_own" ON "public"."subscriptions" FOR SELECT USING (("auth"."uid"() = "user_id"));





ALTER PUBLICATION "supabase_realtime" OWNER TO "postgres";


GRANT USAGE ON SCHEMA "public" TO "postgres";
GRANT USAGE ON SCHEMA "public" TO "anon";
GRANT USAGE ON SCHEMA "public" TO "authenticated";
GRANT USAGE ON SCHEMA "public" TO "service_role";






















































































































































REVOKE ALL ON FUNCTION "public"."consume_ai_credit"("p_user_id" "uuid", "p_kind" "text") FROM PUBLIC;
GRANT ALL ON FUNCTION "public"."consume_ai_credit"("p_user_id" "uuid", "p_kind" "text") TO "anon";
GRANT ALL ON FUNCTION "public"."consume_ai_credit"("p_user_id" "uuid", "p_kind" "text") TO "authenticated";
GRANT ALL ON FUNCTION "public"."consume_ai_credit"("p_user_id" "uuid", "p_kind" "text") TO "service_role";



REVOKE ALL ON FUNCTION "public"."handle_new_user"() FROM PUBLIC;
GRANT ALL ON FUNCTION "public"."handle_new_user"() TO "service_role";



REVOKE ALL ON FUNCTION "public"."is_premium"("p_user_id" "uuid") FROM PUBLIC;
GRANT ALL ON FUNCTION "public"."is_premium"("p_user_id" "uuid") TO "anon";
GRANT ALL ON FUNCTION "public"."is_premium"("p_user_id" "uuid") TO "authenticated";
GRANT ALL ON FUNCTION "public"."is_premium"("p_user_id" "uuid") TO "service_role";



GRANT ALL ON FUNCTION "public"."set_updated_at"() TO "anon";
GRANT ALL ON FUNCTION "public"."set_updated_at"() TO "authenticated";
GRANT ALL ON FUNCTION "public"."set_updated_at"() TO "service_role";


















GRANT ALL ON TABLE "public"."ai_usage" TO "anon";
GRANT ALL ON TABLE "public"."ai_usage" TO "authenticated";
GRANT ALL ON TABLE "public"."ai_usage" TO "service_role";



GRANT ALL ON TABLE "public"."ai_usage_daily" TO "anon";
GRANT ALL ON TABLE "public"."ai_usage_daily" TO "authenticated";
GRANT ALL ON TABLE "public"."ai_usage_daily" TO "service_role";



GRANT ALL ON TABLE "public"."consent_records" TO "authenticated";
GRANT ALL ON TABLE "public"."consent_records" TO "service_role";



GRANT ALL ON TABLE "public"."moments" TO "anon";
GRANT ALL ON TABLE "public"."moments" TO "authenticated";
GRANT ALL ON TABLE "public"."moments" TO "service_role";



GRANT ALL ON TABLE "public"."profiles" TO "anon";
GRANT ALL ON TABLE "public"."profiles" TO "authenticated";
GRANT ALL ON TABLE "public"."profiles" TO "service_role";



GRANT ALL ON TABLE "public"."subscriptions" TO "anon";
GRANT ALL ON TABLE "public"."subscriptions" TO "authenticated";
GRANT ALL ON TABLE "public"."subscriptions" TO "service_role";









ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "service_role";































