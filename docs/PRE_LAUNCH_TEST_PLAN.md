# Lumen — Pre-Submission Smoke + E2E Test Plan (iOS + Android)

Scope: Kotlin Multiplatform / Compose Multiplatform app "Lumen" (Play: "Lumen Moments"). Backend Supabase (Auth, Postgres+RLS, Storage, Edge Functions), Gemini AI via Edge Functions, RevenueCat entitlement (`rcPlus || serverPlus`), Sentry. Targets Apple App Store + Google Play, Turkey/KVKK + EU/GDPR. Demo reviewer account has premium granted server-side (`profiles.is_premium=true`).

---

## 1. Strategy

**Two passes, different cadence and depth.**

| | SMOKE pass | E2E pass |
|---|---|---|
| Goal | "Is this build alive and shippable?" | "Do full user journeys work end-to-end across subsystems?" |
| Scope | P0 happy-paths only, one screen/action per check | Multi-step journeys crossing auth → journal → media → AI → premium → sync |
| Duration | ~30–45 min per platform, manual | ~2–3 hr per platform, manual, plus automated Maestro subset |
| When | Every release-candidate build, both platforms, before any upload | Before each store submission and after any change to a known-fragile area (§5) |
| Environment | **Production Supabase project** (ref `bttybldojievovvnfnox`) with real Edge Functions/secrets, Play-signed / distribution-signed build | Same prod backend; at least one real device per platform on real cellular + one on Wi-Fi; one device in airplane mode for offline legs |
| Blocking? | Any P0 smoke fail blocks the build | Any release-gating E2E (§7) fail blocks submission |

**Manual-first.** These flows depend on native pickers, biometrics, StoreKit/Play Billing sheets, OS permission prompts, external browsers (legal links), and email confirmation — all awkward or impossible to fully automate. Run the smoke suite manually first; automate the stable, deterministic subset (§9) to guard regressions between manual passes.

**Critical rule: test the RELEASE artifact, not a debug build.** Several risks (Android RevenueCat TEST key, Google release SHA-1, empty `config.properties` baked into the iOS framework, Sentry gated to `!DEBUG`, distribution signing) only manifest in the release/distribution build. Smoke-testing a debug build gives false confidence.

---

## 2. Pre-submission SMOKE suite (P0)

One line each: action → expected. `[iOS]` / `[Android]` / `[Both]`. Run top-to-bottom on the release-candidate build.

### A. Launch / Auth
1. `[Both]` Cold launch, fresh install → splash holds until `isReady`, then lands on **SignIn** (no flash of Journal). Pass = SignIn shown, no blank hang.
2. `[Both]` Email sign-in with confirmed demo account, tick **Remember me** → WelcomeGreeting.RETURNING then **Journal** (`clearBackstackAndNavigate(Route.Main.Journal)`).
3. `[Both]` Kill + cold-relaunch remembered user → lands on **Journal**, NOT SignIn (session-restore race guard: `awaitReady()` before `isLoggedIn()`).
4. `[Both]` Sign in WITHOUT remember-me, kill + relaunch → lands on **SignIn** (silently signed out, intended).
5. `[Both]` Wrong password → inline `error_auth_invalid_credentials`, no crash, button re-enables.
6. `[Both]` Google sign-in on **release build** → completes to Journal (Android: release SHA-1 registered; iOS: iOS client id accepted by Supabase provider).
7. `[iOS]` "Continue with Apple" is **visible** and completes end-to-end to Journal (4.8 blocker; verify Supabase Apple provider is live).
8. `[Android]` "Continue with Apple" is **hidden** (null launcher).
9. `[Both]` New authenticated account (or fresh Google/Apple user) → **ConsentGate** overlay appears; tap "I Agree & Continue" → gate dismisses (two `consent_records` rows written).

### B. Journaling
10. `[Both]` Journal feed loads: `CircularProgressIndicator` → grouped sections (Today/Yesterday/date) with real cached + synced moments.
11. `[Both]` Tap "+" → CreateMoment; type body, select ≥1 mood → Save enabled; Save → `success_moment_saved` snackbar + NavigateBack; entry appears in feed.
12. `[Both]` Text-only save with **no network** (airplane) → still saves locally (offline-first), appears in feed.
13. `[Both]` Open a moment (MomentDetailReadView) → title, moods, body, word count, `DetailBottomActionBar` (Delete/Edit/Favorite) render.
14. `[Both]` Edit a **text-only** moment (title/body/mood) → "Changes saved", feed reflects change.
15. `[Both]` Delete a moment (immediate, no confirm dialog) → disappears from feed.
16. `[Both]` Favorite toggle on a **text-only** moment → heart flips and persists across app relaunch.

### C. Media / Permissions
17. `[Both]` Attach photo from library (free, not gated) with network → uploads to MEDIA bucket, moment saves, thumbnail renders via signed URL.
18. `[Both]` Photo attachment save with **no network** → aborts with `error_attachment_save_failed` (upload precedes row write) and cleans up; no half-saved entry.
19. `[Both]` As **premium** demo account: tap Camera / Mic / Video → opens capture (does NOT route to Paywall).
20. `[Both]` As **free** account: tap Camera / Mic / Video → routes to **Paywall** before OS permission prompt.
21. `[Both]` First camera/mic/location use shows correct OS permission prompt with the right purpose string.
22. `[iOS]` Verify camera on a **real device** (Simulator has no camera).

### D. AI / Premium
23. `[Both]` Create moment with "Analyze with AI" ON (premium) → open detail → `EntryAnalysisLoadingCard` (Pending) then `EntryAnalysisCard` (Ready) with reflection text.
24. `[Both]` AI cover banner renders in the 264dp header when generated (else gradient-only, no broken image).
25. `[Both]` Insights tab (premium) → Weekly Reflection auto-generates (`WeeklyReflectionLoadingCard` → `WeeklyReflectionCard`).
26. `[Both]` Insights tab (free) → `WeeklyReflectionLockedCard` "Unlock Plus" → tap → **Paywall**.
27. `[Both]` Paywall shows pricing tiers; primary button reachable; **Restore** and **Terms/Privacy** links present.
28. `[Both]` Manage Premium for demo account resolves **Plus** (serverPlus), "Manage subscription" deep-links to store.
29. `[Both]` Insights renders **real** metrics (streaks/entries/word counts), no sample data (`SampleInsightsState` must not leak: e.g. "15 Weeks", 83 entries).

### E. Settings
30. `[Both]` Open Profile → hero card populated (name, entriesCount, weeklyStreak); premium badge consistent with Manage Premium.
31. `[Both]` Sign out from Profile → local data wiped, lands on SignIn; relaunch does not silently re-restore.
32. `[Both]` Delete account → confirm dialog → `delete-account` edge fn 2xx → local wipe → SignIn (App Store 5.1.1(v) / Play requirement). **Use a throwaway account, not the demo account.**
33. `[Both]` Enable biometric App Lock in Lock & Privacy → toggle persists; background + reopen → **biometric gate** appears and unlocks on success.
34. `[Both]` Export Journal to PDF (account with entries) → PDF generates → native share sheet opens; PDF opens and is readable.
35. `[Both]` Theme light/dark/system → whole app recolors.

### F. Sync / Data
36. `[Both]` Create moment offline → go online → reopen Journal/Insights (or Cloud Sync "Sync now") → `flushPendingSync` pushes it remotely (appears on a second device).
37. `[Both]` Cloud Sync "Sync now" → `cloud_sync_success` snackbar; entry count correct.
38. `[Both]` Sign in as User A then User B on same device → B does **not** see A's moments (`ensureLocalOwner` wipe + RLS).

### G. Legal
39. `[Both]` Profile → Legal → Terms of Service → in-app WebView renders `lumenjournalapp.com/terms` (not blank).
40. `[Both]` Profile → Legal → Privacy Policy → renders `/privacy`; external "OpenInNew" launches browser.
41. `[Both]` Store-console deletion URL points to `lumenjournalapp.com/delete-account` and the page loads.

---

## 3. E2E scenarios

Each is a full journey; run at least once per platform. Steps → expected outcome.

**E1 — New user onboarding to first AI reflection (free).**
SignUp (name/email/password ≥8, tick Terms checkbox) → "Check your email" confirmation panel → confirm via emailed link in browser → return, sign in → **ConsentGate** appears → I Agree → Journal → create first text moment with mood + "Analyze with AI" ON → open detail → Pending → Ready reflection.
*Expected:* Sign-up never routes straight to Journal (confirmation mandatory); consent rows written; reflection renders. *Watch:* unconfirmed sign-in shows misleading "invalid email or password".

**E2 — Free user hits AI quota → paywall → purchase → unlock.**
As free user, exhaust monthly analyze quota → new moment with AI ON returns 402 → open detail → `EntryReflectionUpsellCard` → Unlock → Paywall → complete purchase → return → `rcPlus` flips → premium features (media, weekly reflection) unlock.
*Expected:* Upsell (not generic error) on quota; purchase unlocks. *Watch (§5):* Android release build cannot really purchase (TEST key); Profile badge may still show Free until webhook mirrors `is_premium`.

**E3 — Premium reviewer full happy path.**
Sign in with **demo premium account** → Journal loads seeded entries → create moment with photo + mood + AI → reflection Ready with cover → Insights shows real streaks + auto weekly reflection → View full → open standout moment → share moment as image card → export PDF.
*Expected:* Every gated feature unlocked via serverPlus; no paywalls; media and AI work. This is the exact reviewer path — must be flawless.

**E4 — Remember-me cold-start cycle.**
Sign in with remember-me → use app → force-kill → relaunch (Wi-Fi) → Journal directly → force-kill → relaunch on **flaky/cellular** → still Journal (no wrong-screen flash, no permanent splash hang).
*Expected:* Session restore robust; validates the fixed race and the no-timeout `awaitReady` hang risk.

**E5 — Offline-first create + reconcile.**
Airplane mode → create text moment (saves) → attempt photo moment (fails `error_attachment_save_failed`) → back online → reopen Journal → pending text moment syncs to server (verify on 2nd device).
*Expected:* Text offline OK; media offline blocked; pending flush works.

**E6 — Edit/favorite a photo moment (regression trap).**
Create photo moment (online) → confirm image renders → edit its title OR toggle favorite → save → close app → reopen after a while → open the moment.
*Expected (must hold):* Image still loads. *Risk (§5 HIGH):* signed-URL write-back can persist a 7-day signed URL into `attachments` jsonb, corrupting the storage path → image 403s permanently after TTL. Also verify favorite survives a full `syncAllMoments` (is_favorite-not-fetched risk).

**E7 — Distress/crisis reflection surfaces resources.**
Create an entry with distress-signal text + AI ON → open detail.
*Expected:* `EntrySupportCard` (ELEVATED) or `EntryCrisisCard` (CRISIS). *Watch (§safety):* crisis/support helpline lists are `emptyList()` at ship — CRISIS shows only "112", ELEVATED hides help section. Confirm whether real localized TR/EU hotlines were added before submit (safety + review risk).

**E8 — Account deletion (GDPR/KVKK erasure).**
Throwaway account with a photo moment + AI reflection → Profile → Delete account → confirm → edge fn purges storage + `auth.admin.deleteUser` → local wipe → SignIn. Then verify the deleted email cannot sign in and (spot-check) storage folders removed.
*Expected:* Full erasure, returns to SignIn. *Watch:* Postgres rows rely on FK cascade; Apple token not revoked (5.1.1(v) iOS risk).

**E9 — Biometric lock lifecycle.**
Enable App Lock → background app → reopen → biometric prompt → unlock → Journal. Then disable a device biometric/passcode and reopen with lock still enabled.
*Expected:* Re-locks on background, unlocks on success; if biometrics removed, `isAvailable()==false` auto-unlocks (document as known bypass). *iOS watch:* confirm ON_STOP re-lock actually fires when SwiftUI-hosted Compose backgrounds.

**E10 — Session expiry mid-use.**
Signed in and active → revoke session server-side (or expire token) → continue tapping.
*Expected:* `MainEvent.OnSessionExpired` → forced `clearBackstackAndNavigate(SignIn)`; RevenueCat user cleared.

**E11 — Password reset dead-end awareness.**
SignIn → Forgot password → enter email → "Send link" → success message.
*Expected:* Email dispatched; confirm there is NO in-app completion (reset link opens web only). Ensure demo/reviewer instructions don't depend on completing reset in-app.

**E12 — Share moment card capture.**
Open a moment with photo → Share → ShareMomentSheet → pick format/style, toggle date/location/mood → System share / Save to Photos / Copy.
*Expected:* Off-screen `ShareableMomentCard` (1080px) captures a **non-blank** PNG; "Saved to your photos" / "Copied". *Watch (§5):* blank/black PNG if captured before off-screen draw; very large card on high-density devices; `NSPhotoLibraryAddUsageDescription` present.

**E13 — Multi-account isolation (RLS).**
Sign in User A (has entries) → sign out → sign in User B (different data) on same device → confirm B sees only B's data, no A leakage, favorites/media correct.
*Expected:* `ensureLocalOwner` wipe + server RLS enforced.

**E14 — Legal reachability under poor network.**
On a throttled/flaky connection, open Terms and Privacy in-app WebView.
*Expected:* Pages load. *Watch (§5):* WebView has **no** loading/error/offline state — if unreachable, reviewer sees blank. Confirm host uptime + HTTPS + JS-independent rendering (Android WebView has JS disabled).

**E15 — Theme + status-bar legibility sweep.**
Toggle dark then light → walk Journal (scrollable large-title), MomentDetail (cover under status bar), Insights, Paywall, Auth footer link.
*Expected:* Readable status-bar glyphs and no clipping. *iOS watch:* `SystemBarsAppearance.ios` is a no-op → dark theme can yield illegible status-bar text; check notch/Dynamic Island clearance and the auth footer sitting near the home indicator.

**E16 — Reminders + notification permission.**
Profile → Reminders sheet → toggle Daily reminder ON → OS notification prompt → grant → set time/days.
*Expected:* Toggle applies only after permission granted. *Watch:* Android 13+ needs `POST_NOTIFICATIONS`; iOS has no push entitlement (local only — remote registration silently fails), and Insights `EmptyStreakCard` "Set reminder" is a **no-op** (`onSetReminder = {}`) — verify it isn't a visible dead control reviewers hit.

---

## 4. Platform-specific checks

| iOS | Android |
|---|---|
| **Apple Sign In** works end-to-end (Supabase provider + Service ID/Key configured) — 4.8 blocker | **Google Sign In in RELEASE build** (Play-signed SHA-1 registered in Google Cloud) — debug-works/release-breaks classic |
| Face ID unlock uses `NSFaceIDUsageDescription`; no crash on prompt | Biometric prompt works because **MainActivity is FragmentActivity** — confirm not regressed to ComponentActivity (else silent bypass) |
| Scrollable large-title Journal header scrolls under status bar; MomentDetail cover + circular back/share buttons clear the Dynamic Island | Predictive/system **back gesture**: back on Journal home does NOT exit to launcher (resets to start route) — confirm behavior acceptable |
| Status-bar glyph legibility in dark theme (`SystemBarsAppearance.ios` no-op) | `enableEdgeToEdge()` + status/nav bar icon contrast flips with theme (`isAppearanceLightStatusBars`) |
| Safe-area: auth footer link not under home indicator (`ContentView.ignoresSafeArea`, no scaffold inset) | targetSdk/manifest permissions declared: INTERNET, CAMERA, RECORD_AUDIO, ACCESS_FINE/COARSE_LOCATION, POST_NOTIFICATIONS |
| All Info.plist usage strings present: Camera, Mic, PhotoLibrary (read + add), Location, Face ID | **RevenueCat key is a TEST key on Android** — real Play Billing will NOT work in release; swap to `goog_` key or purchases are broken (Play launch blocker) |
| `IPHONEOS_DEPLOYMENT_TARGET = 18.2` — verify test devices are ≥18.2; audience impact acknowledged | Ship artifact is the **AAB from the closed-test track** promoted to production (12-tester requirement met) |
| **Xcode Cloud archive**: `config.properties` is gitignored and NOT recreated by `ci_post_clone.sh` → empty `SUPABASE_URL`/anon key baked in → dead backend → reviewer can't sign in. **Highest-risk item — verify a fresh CI archive can actually sign in.** | Android Sentry DSN from `config.properties`; blank → crash reporting silently disabled in release |
| Distribution signing on Release (not "Apple Development"); bump `CURRENT_PROJECT_VERSION` on resubmit | Play-signed build maps to registered SHA-1; confirm ProGuard/R8 didn't break Supabase/serialization |
| AppIcon 1024 PNG has **no alpha channel** (upload rejection); dSYM upload to Sentry (sentry-cli/token) | FileProvider authority `${applicationId}.fileprovider` matches runtime package (PDF/share crash otherwise) |
| StoreKit products approved + paid-apps agreement active (else empty paywall) | RevenueCat products configured for Play; webhook maps PLAY_STORE |

---

## 5. Known-risk regression hotspots (targeted tests)

1. **Session-restore race** — Remembered user, cold start on Wi-Fi, cellular, and flaky/airplane. Must land on Journal, never SignIn, and never hang on blank splash (no `awaitInitialization` timeout). Re-run after ANY change to `MainViewModel.init`/`sessionStorage`.
2. **Entitlement `rcPlus || serverPlus`** — (a) Demo account (serverPlus) shows Plus on BOTH Profile and Manage Premium. (b) Fresh purchase: features unlock via rcPlus even before `is_premium` mirrors (accept Profile lag). (c) Cold start offline: confirm transient "Free"/locked resolves to Plus once online (fail-closed `getOrDefault(false)`). (d) First-frame media tap before entitlement stream emits doesn't wrongly route premium user to Paywall.
3. **Edge-fn `contentType(Application.Json)`** — Confirm analyze-entry and weekly-reflection succeed (200, not 400 "invalid json body"). This header is mandatory and easy to drop; a silent regression turns AI into a blank Failed state.
4. **Share-moment capture** — Trigger share immediately after opening a photo moment on a high-density device; confirm PNG is not blank/black and not absurdly large. Save-to-Photos + Copy both show success toast.
5. **PDF export** — Large journal (many entries) → multi-page PDF off main thread, no ANR; entry with a long unbroken URL in body → iOS word-wrap doesn't overflow catastrophically; empty-journal button disabled/empty snackbar.
6. **Offline sync** — Text create offline → flush online; delete offline → tombstone reconciles; media offline → blocked cleanly; cold-start offline → media shows raw path/broken (document), text viewable.
7. **Theme light/dark** — Full sweep both themes (E15); no first-frame wrong-theme flash beyond one frame; status bars legible each platform.
8. **Deep RLS / data isolation** — Multi-account (E13); verify a user can `SELECT` own `profiles.is_premium` (else demo reviewer silently sees no premium); verify moments/storage never cross users.
9. **is_favorite persistence** — Favorite a moment → run full `syncAllMoments` (Cloud Sync) → favorite must survive (column-not-fetched risk).
10. **Signed-URL write-back corruption** — E6: edit/favorite a photo moment, then verify image still loads after cache loss / TTL window.
11. **Consent gate silent stick** — Force a `recordConsent` failure path (e.g., RLS block in a test project) → confirm behavior; ensure prod `consent_records` INSERT/RLS actually works so real users aren't trapped with a dead "I Agree" button.
12. **AI fails-closed for everyone** — If `consume_ai_credit` errors, premium users also see 402 upsell — confirm the RPC is healthy in prod before submit.

---

## 6. Device / OS coverage matrix

Minimum recommended physical/simulator matrix (each cell: run smoke §2; light + dark each):

| Axis | iOS | Android |
|---|---|---|
| Min OS | iOS **18.2** (hard floor per `IPHONEOS_DEPLOYMENT_TARGET`) | Android = declared `minSdk` (verify), e.g. API 26/28 |
| Mid OS | iOS 18.x current | Android 13 (API 33 — POST_NOTIFICATIONS runtime) |
| Latest OS | Latest iOS | Latest Android (API 34/35) |
| Small screen | iPhone SE-class / notched compact | ~5.x" phone |
| Large screen | iPhone Pro Max + one iPad (family 1,2) | Large phone / foldable |
| Theme | Light + Dark on each | Light + Dark on each |
| Network | 1 Wi-Fi, 1 cellular, 1 airplane leg | Same |

Priority devices for the release gate: **min-OS small screen** (layout/notch, deployment floor) and **latest-OS large screen** (screenshots), both themes, plus one real device per platform for camera/biometric/purchase.

---

## 7. Release-gating checklist (MUST PASS before submit)

Correctness:
- [ ] Cold start on fresh install → SignIn, no crash, no permanent splash hang (both platforms, release build).
- [ ] Demo premium account signs in and resolves **Plus** on Profile AND Manage Premium; all gated features work (E3).
- [ ] Remember-me returning user lands on Journal on cold start (E4).
- [ ] Create text moment + create photo moment (online) both succeed; feed updates.
- [ ] AI reflection generates for premium demo account (edge fns + secrets live in prod project).
- [ ] Delete account works end-to-end via `delete-account` edge fn (throwaway account) (E8).
- [ ] Multi-account isolation holds; no cross-user data (E13).

Store-review items:
- [ ] `[iOS]` Sign in with Apple works end-to-end (4.8) — Supabase Apple provider configured.
- [ ] `[Android]` Real Play Billing works — **RevenueCat production `goog_` key**, not the TEST key.
- [ ] `[Both]` Google sign-in works in the **release/distribution** build (SHA-1 / client-id).
- [ ] Terms + Privacy in-app WebView load reliably; store deletion URL `= /delete-account` and loads.
- [ ] Restore purchases + price/renewal disclosure functional on Paywall.
- [ ] `[iOS]` CI archive built from Xcode Cloud can actually sign in (config.properties NOT empty in the shipped framework).
- [ ] `[iOS]` Distribution-signed, build number bumped, AppIcon no alpha.
- [ ] `[Android]` Ships the closed-test AAB; Sentry DSN present.

Hygiene:
- [ ] No sample/placeholder data leaks (Insights sample stats, hardcoded paywall pricing where real offerings should load).
- [ ] Crisis/support helplines populated with verified TR/EU numbers (or self-harm content path reviewed) — safety.
- [ ] No dead visible controls surprising reviewers (e.g. "Set reminder" no-op on default empty state — decide hide vs wire).
- [ ] Sentry fires only in release, PII scrubbed; no debug logging of user content.

---

## 8. Test data & accounts

Prepare and label these before the pass:

1. **Fresh account** — unconfirmed + confirmed variants; used for E1 (sign-up/consent) and to prove first-run empty state (`no_moments_day`).
2. **Existing-data account** — 30+ moments spanning multiple days/weeks, some with photo/voice/video/link/location, some favorited, some with AI reflections and a distress-flagged entry; used for feed pagination, search, Insights streaks, PDF multi-page, share card, RLS-isolation "User A".
3. **Premium demo reviewer account** — `profiles.is_premium=true`, seeded with a few moments; verify it resolves Plus everywhere. This is the exact account handed to reviewers — keep its state pristine.
4. **Second distinct account** ("User B") — different data, for multi-account isolation (E13).
5. **Throwaway deletable account** — for the destructive Delete-account E2E (never use the demo account).
6. **Offline device** — one device kept in airplane mode for offline create/reconcile legs (E5).
7. **Quota-exhausted free account** — one that has already consumed its monthly AI credits, for the 402 upsell path (E2). If not reproducible from client, coordinate a server-side quota state.
8. **Backend readiness** (verify before pass, not an "account"): prod Supabase has Edge Functions deployed with secrets (`GEMINI_API_KEY`, `REVENUECAT_WEBHOOK_SECRET`, `SENTRY_DSN`), `entry-covers`/`media`/`avatars` buckets with correct public/private policies, RLS on `moments`/`profiles`/`consent_records`, `revenuecat-webhook` deployed with `verify_jwt=false`, others with `verify_jwt=true`.

---

## 9. Automation recommendation

Automate the deterministic core; keep native-native flows manual.

**Tier 1 — Maestro (cross-platform E2E smoke), automate first:**
These are stable, deterministic, and highest-value regressions:
- Launch → SignIn renders; email sign-in (test account) → Journal.
- Remember-me cold-start restore (kill + relaunch) → Journal (guards the session-restore race, #1 fragile area).
- Create text moment (mood + body) → appears in feed.
- Open moment detail → back.
- Delete text moment → gone from feed.
- Tab nav Journal ↔ Insights; open Profile.
- Free-user premium gate: tap Camera → Paywall appears.
Run these on both iOS and Android in CI on every RC build.

**Tier 2 — Compose UI tests (`ComposeTestRule`)** for stateless `XxxScreen` composables against fake state: SignInScreen validation/enablement, CreateMomentScreen Save-enabled logic (body-or-attachment AND ≥1 mood), Paywall tier selection, ConsentGateScreen, empty-state rendering. Fast, no backend.

**Tier 3 — JUnit5 + Turbine + AssertK ViewModel unit tests** (leverage existing suite) for the logic-heavy fragile areas that are painful to hit via UI:
- `MainViewModel` session-restore ordering (awaitReady before isLoggedIn; remember-me signOut).
- Entitlement `rcPlus || serverPlus` resolution incl. offline `getOrDefault(false)`.
- Error mapping (`mapSupabaseError`, 402→QUOTA_EXCEEDED→QuotaExceeded state).
- MediatorMomentRepository offline-first save (remote-failure-swallowed → Success + pendingSync) and flushPendingSync.
- Signed-URL / `storagePath()` behavior (guard the write-back corruption regression, E6/#10).

**Tier 4 — XCUITest (iOS-only natives):** Apple Sign In presence + basic flow shell, Face ID gate (simulated biometric), safe-area/status-bar snapshot checks. Keep minimal — these are brittle.

**Do NOT automate (manual every pass):** real StoreKit/Play Billing purchase + restore, real camera capture, share-sheet/save-to-Photos, email-confirmation link, external legal-browser links, and the Xcode Cloud archive backend-config verification — all require real devices, stores, or human confirmation.

Ordering: build Tier 1 Maestro smoke + Tier 3 ViewModel tests for the session-restore and entitlement fragile areas first — they cover the two highest-frequency regression sources with the least maintenance cost.

---

## 10. Additional coverage — adversarial gap review

A second reviewer checked the plan above against the subsystem maps for missing flows and edge cases. Fold these in before submission (ordered by severity):

### [MEDIUM] Journaling / Search

- **Gap:** The Search moments flow (P1) is not exercised anywhere in the smoke suite or E2E scenarios, even though the maps flag a real correctness trap: applySearch only filters the currently-loaded page window, so searching for any entry older than the loaded LIMIT returns a misleading 'No results for "..."'. Test-data account 2 is prepared 'for search' but no step ever searches.
- **Test:** On the 30+ entry account: open Search, query a term known to exist only in an OLD entry beyond the first page (do not scroll-load it first) → confirm whether it is found; query a term in a loaded entry → confirm match; clear/exit search resets query. Document the paged-window limitation.

### [MEDIUM] Journaling / Edit date-time (timezone)

- **Gap:** Editing a moment's createdAt via the DatePicker + DetailTimePickerDialog (the ONLY date picker in the app — create has none, so back-dating is impossible) is never tested. The maps call out a UTC↔system-tz conversion (initialDateMillis via TimeZone.UTC recombined with currentSystemDefault) as an off-by-one risk.
- **Test:** In a non-UTC timezone, edit a moment's date to a specific day and its time, save, reopen → verify the displayed date/time matches what was picked (no ±1 day shift) and the feed section groups it under the correct day. Also confirm create has no date field (back-dating not possible) so reviewer instructions don't assume it.

### [MEDIUM] Journaling / Voice note recording

- **Gap:** Voice recording is only covered as 'tap Mic → opens capture'. The actual VoiceRecordingBottomSheet lifecycle (elapsed timer/amplitudes, Done→Audio attachment, Cancel/dismiss-while-recording, and VoiceAttachmentCard playback) and its error states (recording_start_failed, save_failed, playback_failed) are never exercised end-to-end.
- **Test:** As premium on a real device: record a voice note, watch timer, tap Done → confirm audio attachment saves and uploads; reopen moment and play/pause it; separately start recording then dismiss the sheet → confirm recording is cancelled with no orphaned attachment.

### [MEDIUM] Media / Location permission-denied & services-off paths

- **Gap:** Only the location happy-path permission prompt is tested (#21). The permission-DENIED path (openAppSettings snackbar), the PERMANENTLY_DENIED short-circuit, and the 'permission granted but device Location Services OFF' path (permission_location_services_required snackbar → 'Enable location' → openLocationSettings) are untested — these are the common real-world outcomes.
- **Test:** Deny location when prompted → verify error_location_permission_denied + settings deep-link; then with permission granted but device Location Services globally OFF → verify the services-disabled snackbar and its 'Enable location' action opens Location settings; confirm the moment still saves without location.

### [MEDIUM] AI reflection / non-quota silent Failure

- **Gap:** Plan tests the 402 quota→upsell path but never the generic Failed path. For MomentAnalysisState.Failed AND for ReflectionState.error on weekly reflection, the UI renders NOTHING (loading card vanishes, no error, no retry) — a reviewer-visible 'broken/blank' impression distinct from the quota upsell. Only 'AI fails-closed' (RPC error→402) is noted in §5.
- **Test:** Force a non-quota failure (e.g., invalid GEMINI key or induced 500 in a test project) for both analyze-entry and weekly-reflection → confirm current behavior (blank space) and decide whether an error/retry affordance is required before ship, since a paying user's weekly card simply disappears.

### [MEDIUM] App shell / Process death & state restoration

- **Gap:** No test covers Android process-death restoration of the nav backstack (rememberSerializable polymorphic NavKey) or the rememberSaveable LockGate 'locked' flag. Maps flag two concrete risks: any Route not registered in serializersConfig crashes on restore, and after process death without ON_STOP the app can reopen UNLOCKED despite app-lock enabled (privacy breach).
- **Test:** With app-lock enabled, navigate deep (MomentDetail/LegalDocument with args), force process death via 'Don't keep activities' / adb kill, relaunch → verify no deserialization crash, the route/args restore, and the biometric gate still blocks the journal (not auto-unlocked).

### [MEDIUM] iOS / Backup exposure (privacy/KVKK-GDPR)

- **Gap:** No verification that journal.db and the DataStore (in iOS Documents) and the persisted Supabase session (NSUserDefaults) are excluded from iCloud/device backups. For a private journaling app targeting KVKK/GDPR, plaintext entries + session syncing to iCloud backups is a privacy/store-listing concern that the plan never checks.
- **Test:** Confirm whether journal.db/DataStore are marked isExcludedFromBackup (or documented as backed-up in the privacy policy). Inspect an encrypted device backup or check the NSURLIsExcludedFromBackupKey attribute; decide if entries should be excluded or the disclosure updated.

### [LOW] Journaling / Feed pagination (load-more)

- **Gap:** View-feed smoke (#10) only loads the first page; the OnLoadMore scroll-to-bottom pagination (P0 flow, with the documented page-size-multiple boundary that triggers one extra empty load-more) is never exercised, despite test data prepping a 30+ entry account 'for pagination'.
- **Test:** On the 30+ entry account, scroll to near-bottom → verify isLoadingMore spinner then additional grouped items append; scroll to a page-size multiple (e.g., exactly 20/30) → verify no crash/duplicate on the extra empty load and hasMorePages resolves correctly.

### [LOW] Settings / Avatar upload (EditProfile)

- **Gap:** The avatar change flow (EditProfile → pick image → SetProfileAvatarUseCase upload to avatars bucket → public URL reflected in Profile/Journal header) is a user-facing feature that is entirely untested, including its error path (PermissionError → error_avatar_update_failed) and the enlarged-photo viewer.
- **Test:** In EditProfile, change the profile photo → verify upload succeeds and the new avatar appears on Profile hero and Journal top bar after refresh; tap the hero avatar → verify the enlarged photo viewer opens and dismisses; test picker cancel and a failed upload.

### [LOW] Auth / Sign-up + ConsentGate double-state

- **Gap:** If the prod Supabase project has 'Confirm email' DISABLED, signUp returns a live session and the maps warn the awaitingConfirmation panel AND the ConsentGate overlay show simultaneously, with the panel's 'Sign in' navigating while already authenticated — a confusing, reviewer-visible state. The plan assumes confirmation is mandatory (E1) and never verifies the server setting or this branch.
- **Test:** Verify the prod project's Confirm-email setting is ON (matching E1's assumption); if OFF, sign up and confirm no double-overlay/confusing navigation occurs. Lock the setting before submission so reviewer behavior is deterministic.

### [LOW] Journaling / Silent feed error feedback

- **Gap:** JournalRoot handles JournalEvent.ShowError with only println() — sync, delete, and favorite-toggle failures surface NO snackbar/toast (e.g., UNAUTHORIZED from a session-restore race). The plan tests these happy paths but never the failure feedback, so a reviewer hitting a transient sync/delete error gets zero feedback and may report the app as broken.
- **Test:** Induce a failing sync/delete/favorite (offline mid-action or forced auth error) → confirm what the user sees; if nothing, decide whether a visible error/toast is required before ship rather than a silent println.

---

## Reviewer verdict

The plan is strong and covers the highest-risk store-review and release-gating items well (Apple 4.8, Android TEST billing key, empty config.properties CI archive, session-restore race, entitlement split, signed-URL write-back, delete-account, RLS isolation, consent gate). The gaps that remain are mostly under-covered secondary user flows and edge/error paths rather than missing headline journeys: Search is entirely untested (including its known paged-window bug), the edit-only date/time picker's timezone off-by-one is never exercised, voice recording and location denial/services-off paths are only shallowly touched, non-quota AI Failed states render blank with no test, and process-death restoration (nav + lock flag) plus iOS backup exposure are unaddressed. None of these alone blocks a release, but the reflection silent-failure, search bug, timezone edit, and process-death/lock restoration are worth adding before submission because each can produce a 'looks broken' reviewer or user impression.
