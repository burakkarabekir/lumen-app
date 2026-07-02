# Lumen Refactoring Plan — Design System Hardening

Four codebase-wide smells, fixed cleanly and incrementally:

1. **Hardcoded colors** → route through `MaterialTheme.colorScheme` + `ExtendedColors` (and typed brand/mood palettes).
2. **Unstable composable params** → make reflection/AI cards skippable via `@Immutable` on domain models.
3. **Hardcoded strings** → Compose Resources per module, with plurals.
4. **No dimension system** → `AppDimens` provided via `LocalAppDimens`, wired through `AppTheme`.

---

## Guiding principles

- **Backwards-compatible.** New infrastructure is additive. Existing `LumenSpacing`, `LumenRadius`, `ExtendedColors`, and `AppTheme` stay working throughout; nothing is deleted until every caller has migrated.
- **Incremental, one concern per PR-sized phase.** Never mix "add colors" with "migrate a screen". Foundation lands first; migrations follow, each scoped to named files.
- **No behavior or visual change.** Migrations swap literals for tokens whose values are *identical* to today's literals. If a token value must differ from the literal, that is a design decision — flag it, don't silently change it.
- **Compile + verify both targets after every phase.** Run `:composeApp:compileKotlin` (Android) and the iOS framework link (`DEVELOPER_DIR=<full Xcode>` — see project memory; `xcrun` fails with exit 72 on Command Line Tools only). No phase merges red.
- **Screenshot the hot cards before/after.** For any phase touching reflection/AI cards, capture light + dark previews before and after; diff for color/dimension drift.

---

## Phase 0 — Foundation (core/design_system)

Build all shared infra first. **No feature files change in this phase.** Effort: **L**.

Base path: `core/design_system/src/commonMain/kotlin/com/bksd/core/design_system/theme/`

### 0a. Extend `ExtendedColors` with semantic + brand/mood tokens

The `ExtendedColors` data class + `LocalExtendedColors` (`staticCompositionLocalOf`, default `LightExtendedColors`) and the `ColorScheme.extended` accessor already exist in `Theme.kt`. Extend that, don't replace it.

**Steps:**

1. In `Color.kt`, promote the recurring literals to named `val`s so each hex lives once. Consolidate by semantic group (values from the audit):
   - **Brand accents:** `0xFF7682D6` (20+ sites), `0xFF5B6AD0`, `0xFF678BF4`, `0xFF9281C6`.
   - **Support green:** `0xFF57B98F`, `0xFF3E9B77`, `0xFF2FA876` (10+ sites), `0xFF2E7D57`, `0xFF8FD8B4`.
   - **Crisis/warning coral:** `0xFFE58060`, `0xFFCD5A48`, `0xFFE0524A`, `0xFFF0A08F`, `0xFFB5473A`.
   - **Theme-chip hues:** `0xFF2FA876`, `0xFFE0A21A`, `0xFF6E7AD0`, `0xFFC77FA8`, `0xFFCF6F64`.
2. Add nested `@Immutable` typed sub-palettes to `ExtendedColors`, each with a **light and dark instance** (mirrors existing `LightExtendedColors`/`DarkExtendedColors`):
   - `EntryAnalysisCardColors` (15 slots: surface/border/iconStart/iconEnd/title/meta/body/chipBg/chipText/promptBg/promptBorder/promptLabel/promptText/hairline/disclaimer).
   - `SupportCardColors`, `CrisisCardColors` (6 slots each).
   - `WeeklyCardColors` (15 slots), `StreakCardColors`, `InsightsCardColors`.
   - `MoodColorPalette` — typed map for the 12 hardcoded moods (PROUD, HOPEFUL, REFLECTIVE, INSPIRED, FOCUSED, LOVED, NOSTALGIC, STRESSED, FRUSTRATED, SAD, MELANCHOLIC, BORED), each `(bg, text)`.
   - `AttachmentBadgeColors` (photo/voice/video/link/remove), `MediaTypeAccents` (5 slots).
   - `InsightsGradients` — expose gradient pairs as `Brush` helpers on the `ColorScheme.extended` accessor.
3. **Move dark-mode detection to the theme layer.** Today each card runs a local `luminance()` check and branches. Instead, resolve the correct light/dark sub-palette *once* when constructing `LightExtendedColors` vs `DarkExtendedColors`. Cards then read `colorScheme.extended.entryAnalysisCard.surface` with no branching.
4. **Keep `reflectionHexColor()` as-is.** `ArcPoint.colorHex` / `ReflectionTheme` / `StandoutEntry.colorHex` are *data-driven* (server-supplied); they must stay dynamic. Document the constrained brand-hue set and keep the `0xFF7682D6` fallback.

**Helper accessor:** everything is reachable via the existing `ColorScheme.extended` extension, e.g. `MaterialTheme.colorScheme.extended.mood(mood)` and `.entryAnalysisCard`.

**Risk:** semantic overlap in the existing 99 slots (e.g. `textPrimary` dup). Do **not** rename/dedup existing slots in this phase — additive only. A dedup pass is a separate, later cleanup.

**Verify:** `:core:design_system:compileKotlin` + iOS link. Add a `@Preview` that renders every new sub-palette as swatches in light + dark; eyeball against current card screenshots.

### 0b. Add `AppDimens` + `LocalAppDimens`, wired through `AppTheme`

No dimension `CompositionLocal` exists today (`LumenSpacing`/`LumenRadius` are bare objects). Add one, parallel to `LocalExtendedColors`.

**Steps:**

1. New file `AppDimens.kt`: `@Immutable data class AppDimens` with nested groups mapped from **actual usage** (audit-confirmed values, no new visual values):
   - `spacing`: 2/4/8/12/16/20/24/32/48/64 dp — delegate to existing `LumenSpacing` so there's one source of truth.
   - `radius`: 8/12/**14**/16/18/**22** dp — `LumenRadius` plus the two real card radii (`14`, `22`) the audit found hardcoded. Name them semantically (`card = 22.dp`, `cardTight = 14.dp`).
   - `icon`: 16/18/20/24/36/38/48/56 dp.
   - `size`: FAB (`56.dp`), cancel icon (`48.dp`), top bar height (`64.dp`).
2. New file `LocalAppDimens.kt`: `val LocalAppDimens = staticCompositionLocalOf { AppDimens() }` with sensible defaults, plus a `MaterialTheme`-style accessor (e.g. `@Composable @ReadOnlyComposable fun appDimens(): AppDimens = LocalAppDimens.current`).
3. In `AppTheme.kt`, add `LocalAppDimens provides AppDimens()` to the existing `CompositionLocalProvider` block (which already provides `LocalExtendedColors` / `LocalThemeController`).

**Typography note:** the 50+ ad-hoc `.sp` sizes are out of scope for Phase 0 wiring. Add the handful of genuinely-recurring custom sizes as named `TextStyle`s in `Type.kt` (following the existing `labelXSmall`/`titleXSmall` pattern) *during* the screen-migration phases, not here. Do not invent a full semantic type scale up front.

**Verify:** compile both targets. Add a preview using `appDimens()` to confirm the local resolves.

### 0c. Confirm the stability approach

**Recommendation: annotate domain models with `@Immutable`. Do NOT add a `stabilityConfigurationFile`.**

Justification (from the Compose stability audit):
- The unstable params (`EntryAnalysis`, `MomentReflection.*`, `WeeklyReflection`, `ArcPoint`, `StandoutEntry`, `SupportResource`, `ReflectionTheme`, `Moment`) are pure data classes of primitives/enums/sealed types with `List<T>` fields that are **read, never mutated** at composition.
- `@Immutable` is **additive, compiler-validated, zero runtime cost**, and matches the existing precedent (`NewEntryPalette`, `InsightsPalette`, `VisualizerStyle`, `Theme` are already `@Immutable`).
- Converting domain `List<T>` → `ImmutableList<T>` is rejected: these models are serialized (Supabase / DataStore), and that change would force serializer rework for no skippability gain once `@Immutable` is present.
- A stability config file adds opaque, hard-to-sync build state — overkill for this codebase.

Do this as its own small phase (below), not in Foundation, since it touches domain modules.

### 0d. String-resource conventions

Infra already exists per module (`composeResources/values/strings.xml`, `packageOfResClass = "com.bksd.{feature}.presentation"`, `stringResource`/`pluralStringResource`). Establish conventions, don't build new infra:

- **Per-module scoping stays.** Do *not* consolidate into `core:design_system` (would churn ~100 imports across 7 modules and block per-feature locale overrides). Strings live in the feature that uses them.
- **Key naming:** `snake_case`, grouped by section (`reflection_title`, `ai_analysis_title`, `ai_can_make_mistakes_disclaimer`).
- **Plurals:** add `composeResources/values/plurals.xml`; replace every inline `if (count == 1) "entry" else "entries"` with `pluralStringResource(Res.plurals.entry_count, count, count)`.
- **contentDescription is resourceized too** (accessibility copy is user-facing).

**Verify:** `Res` classes still generate; no compile change (conventions only).

---

## Phase 1 — Domain stability (`@Immutable`)

**Scope:** `feature/reflection/domain` + `core/domain`. Effort: **S**.

**Steps:** Add `import androidx.compose.runtime.Immutable` and `@Immutable` to:
- `feature/reflection/domain/.../model/EntryAnalysis.kt`
- `.../model/MomentReflection.kt` (and its `Reflection` / `Support` / `Crisis` subtypes)
- `.../model/WeeklyReflection.kt` (+ `ReflectionTheme`)
- `.../model/WeeklyMomentInsights.kt` (`ArcPoint`, `StandoutEntry`)
- `SupportResource.kt`
- `core/domain/.../model/Moment.kt`
- `MomentAnalysisState` (and its `.Ready` wrapper) in presentation.

Also annotate presentation states that wrap domain models but aren't marked: `ReflectionState`, `WeeklyReflectionDetailState`.

**Risks:** none functional — additive and compiler-checked. Only risk is `@Immutable` being *wrong* (a mutable field slips in); the compiler / a strong-skipping check catches it.

**Verify:** compile both targets. Optionally enable Compose compiler metrics on `:feature:journal:presentation` and confirm `EntryAnalysisCard`, `WeeklyReflectionCard`, etc. are now reported **skippable**.

---

## Phase 2 — Colors: the hot reflection/AI cards

**Do the freshest, highest-value files first.** These are the ones the user cares about most. Effort: **M**.

**Scope (hot files):**
- `feature/journal/presentation/detail/components/EntryAnalysisCard.kt` — `ChipDotColors`, `reflectionCardColors(dark)` (15 props), `EntryAnalysisLoadingCard.kt`.
- `feature/journal/presentation/detail/components/EntrySupportCard.kt`, `EntryCrisisCard.kt` (6 literals each).
- `feature/insights/presentation/reflection/components/WeeklyReflectionCard.kt` — `weeklyCardColors(dark)`, `WeeklyReflectionLoadingCard.kt`.
- `feature/insights/presentation/reflection/full/WeeklyReflectionScreen.kt` — pill colors.
- `feature/insights/presentation/reflection/full/components/RecurringThemesCard.kt`, `EmotionalArcCard.kt`, `StandoutMomentCard.kt`, `QuestionsToSitWithCard.kt`.
- `feature/journal/presentation/detail/MomentDetailReadView.kt` (renders all of the above).

**Steps:**
1. Replace each local `reflectionCardColors(dark)` / `weeklyCardColors(dark)` lookup with a read of the Phase-0a sub-palette: `val c = MaterialTheme.colorScheme.extended.entryAnalysisCard`.
2. Delete the per-card `luminance()`/`dark:` branching — the palette is already resolved light/dark at theme level.
3. Keep `reflectionHexColor()` calls for `ArcPoint`/`ReflectionTheme`/`StandoutEntry` data-driven colors.
4. Every replaced token value must equal today's literal exactly.

**Risks:** losing per-card dark-mode logic if a card branched on something other than system dark (verify each). Data-driven hex must not be accidentally themed.

**Verify:** light + dark screenshots of each card before/after; pixel-diff. Compile both targets.

---

## Phase 3 — Colors: moods, attachments, insights, core

**Scope:** the remaining color literals. Effort: **M**.

- `feature/journal/presentation/journal/components/MoodColors.kt` → `moodColors()` reads `colorScheme.extended.mood(mood)`; migrate the 12 hardcoded moods (HAPPY/CALM/TIRED/ANXIOUS already use `extended`).
- `feature/journal/presentation/journal/components/AttachmentChips.kt`, `MediaTypeIcon.kt` → `extended.attachmentBadge`, `extended.mediaTypeAccents`.
- `feature/insights/presentation/components/*` (`StreakColors.kt`, `CurrentStreakCard`, `EntriesBarChart`, `ReflectionThemeChip.kt`, gradients) → `extended.streakCard`, `extended.insightsGradients`.
- `core/presentation/attachment/*` (`AttachmentColors.kt`, `LinkAttachmentCard.kt`, `AttachmentCardChrome.kt`, `VideoThumbnail.kt`) → `extended.attachmentBadge` + gradient/overlay helpers.
- `MomentDetailEditView`/`ReadView` cover/shadow colors → gradient helpers.

**Steps:** same pattern — literal → `extended.*` token of identical value; gradient pairs → `Brush` helpers.

**Risks:** verify Tailwind-ish media-type hues (`#2563EB`, `#7C3AED`, `#9333EA`, `#0EA5E9`, `#475569`) are intentional brand choices before enshrining as tokens — flag to design if unsure, but migrate the value unchanged.

**Verify:** screenshot mood chips, attachment badges, insights cards (light + dark). Compile both.

---

## Phase 4 — Strings: insights reflection + moment AI opt-in (highest priority)

**Scope (freshest, user-facing AI copy):** Effort: **M**.

- **`feature/insights/presentation`** — 45+ literals across `WeeklyReflectionCard.kt` ("Weekly Reflection", "A QUESTION TO SIT WITH", "View full reflection", "Private to you"), `WeeklyReflectionScreen.kt` ("RECURRING THEMES", "A MOMENT THAT STOOD OUT", "QUESTIONS TO SIT WITH", empty state), `EmotionalArcCard.kt`, `RecurringThemesCard.kt`, `StandoutMomentCard.kt` ("Open"), `WeeklyReflectionLoadingCard.kt` ("Reflecting on your week…").
- **`feature/journal/presentation/detail/components/EntryAnalysisCard.kt`** — "AI Analysis", "Lumen AI can make mistakes. Reflections are private to you."
- **`feature/moment/presentation/create/components/AiAnalysisOptInCard.kt`** — "Yes, analyze this entry with AI", "Get a reflection on your mood and themes right after you save."

**Steps:**
1. Add `feature/insights/presentation/src/commonMain/composeResources/values/plurals.xml`:
   ```xml
   <plurals name="entry_count">
     <item quantity="one">entry</item>
     <item quantity="other">entries</item>
   </plurals>
   ```
2. Expand each module's `strings.xml` with grouped keys (titles, section labels, ai_*, empty/loading, actions).
3. Replace literals: `Text(stringResource(Res.string.reflection_title))`.
4. Replace all three inline plural sites (`entryWord()` in `WeeklyReflectionCard.kt`, ternaries in `RecurringThemesCard.kt` and `WeeklyReflectionScreen.kt`) with `pluralStringResource(Res.plurals.entry_count, count, count)`.

**Risk:** plurals.xml syntax if unfamiliar — the snippet above is the template.

**Verify:** each screen renders identical copy; plural switches at count 1↔2. Compile both.

---

## Phase 5 — Strings: journal + profile

**Scope:** Effort: **M**.

- **`feature/journal/presentation`** (27): contentDescriptions ("Favorited", "More actions", "Search", "Profile", "Back", "Clear search", "Sparkle") in `MomentCard.kt`, `JournalTopBar.kt`; dialog buttons ("Next"/"Cancel"/"OK") + `"https://..."` placeholder in `MomentDetailEditView.kt`.
- **`feature/profile/presentation`** (24): `ThemeSelectorSheet.kt` ("System"/"Light"/"Dark"), `SettingsGroup.kt` ("Privacy & Security", "Notifications"), `EditProfileScreen.kt` contentDescriptions.
- **Low priority sweep:** `core/presentation/attachment/AttachmentCardChrome.kt` ("Remove attachment"), onboarding (3).

**Steps:** same as Phase 4, per module `strings.xml`, following the proven `moment` module pattern.

**Verify:** compile both; spot-check labels + TalkBack/VoiceOver descriptions.

---

## Phase 6 — Dimensions: core design_system components

Migrate infra components first so tokens are enforceable before screens adopt them. Effort: **M**.

**Scope:** `core/design_system/component/**` — `AppScaffold.kt`, `AppTopBar.kt` (64.dp height, 16/12/8 padding), `AppButton.kt` (12.dp radius, 6.dp padding, 1.5.dp stroke), `AppAvatar.kt`, `HashtagChip.kt`, plus config objects `SpeakingFabConfig.kt` (56/48 dp), `MomentCardDefaults.kt`.

**Steps:** replace inline `.dp` with `appDimens().spacing.*` / `.radius.*` / `.icon.*` / `.size.*`. Keep animation constants (`REVEAL_DURATION_MS`, thresholds) as plain vals — those aren't dimensions. Token values must equal current literals.

**Risk:** the very specific `22.dp` / `14.dp` card radii — confirm intentional (Phase 0b already reserves `radius.card`/`radius.cardTight`), don't collapse to a generic value.

**Verify:** screenshot buttons, top bar, avatar, FAB (light + dark). Compile both.

---

## Phase 7 — Dimensions: feature screens

**Scope:** the high-density screens from the audit. Effort: **L** (volume, not complexity). Split into per-module PRs.

- `feature/journal/presentation/journal/JournalScreen.kt`, `journal/components/MomentCard.kt` (16/15/13/14/7/6/4 dp gaps; 18.dp radius; inline `.sp`).
- The detail cards already touched in Phase 2 (`EntryAnalysisCard.kt` 22/14/12/13 dp) — migrate their dimensions now.
- Remaining insights/moment/profile screens.

**Steps:**
1. `.dp` → `appDimens().spacing/radius/icon`.
2. For the ad-hoc `.sp` values: prefer `MaterialTheme.typography.*`. Only the genuinely-recurring custom sizes (e.g. 12.5, 13.5, 14.5, 23.5 sp with their line-heights/letter-spacing) get named `TextStyle`s added to `Type.kt` (extend the existing `labelXSmall`/`titleXSmall` pattern). Do not manufacture a token for every one-off size — leave true one-offs inline rather than pollute the scale.

**Risk:** typography is where visual drift hides — migrate a screen, screenshot-diff, repeat. Don't batch all screens in one PR.

**Verify:** per-screen before/after screenshots (light + dark); compile both.

---

## Sequencing summary

- [ ] **Phase 0 — Foundation** (core/design_system): extend `ExtendedColors` sub-palettes + `.extended` accessor; add `AppDimens` + `LocalAppDimens` wired into `AppTheme`; confirm `@Immutable` approach; string/plural conventions.
- [ ] **Phase 1 — Stability:** `@Immutable` on reflection + `core:domain` models and wrapping states.
- [ ] **Phase 2 — Colors (hot cards):** EntryAnalysis/Support/Crisis, WeeklyReflection card + screen, MomentDetailReadView.
- [ ] **Phase 3 — Colors (rest):** moods, attachments, insights, core/presentation, MomentDetail cover/shadow.
- [ ] **Phase 4 — Strings (hot):** insights reflection + `EntryAnalysisCard` AI copy + `AiAnalysisOptInCard`; add plurals.xml.
- [ ] **Phase 5 — Strings (rest):** journal + profile + low-priority sweep.
- [ ] **Phase 6 — Dimensions (core components):** AppScaffold/TopBar/Button/Avatar/FAB.
- [ ] **Phase 7 — Dimensions (feature screens):** JournalScreen, MomentCard, detail cards, remaining screens; typography tokens.

Each box = one PR-sized phase. Compile Android + iOS and screenshot-diff after each.

---

## Definition of done

**Colors.** No `Color(0x…)` literals in presentation composables except data-driven `reflectionHexColor()` inputs. All card/mood/attachment/insights colors read from `MaterialTheme.colorScheme.extended.*`. Light/dark resolved once at theme level — zero in-card `luminance()` branching. Before/after screenshots identical.

**Stability.** All listed reflection + `core:domain` models and wrapping states carry `@Immutable`. Compose compiler reports `EntryAnalysisCard`, `WeeklyReflectionCard`, `EntrySupportCard`, `EntryCrisisCard`, `EmotionalArcCard`, `RecurringThemesCard`, `StandoutMomentCard`, `QuestionsToSitWithCard` as **skippable**. No `stabilityConfigurationFile` added.

**Strings.** No user-facing literals (including `contentDescription`) in insights/journal/profile/moment presentation or `EntryAnalysisCard`/`AiAnalysisOptInCard`. All go through `stringResource`. Every "entry/entries" plural uses `pluralStringResource(Res.plurals.entry_count, …)`; no inline count ternaries remain.

**Dimensions.** `LocalAppDimens` is provided by `AppTheme` and consumed in every migrated core component and feature screen. No inline `.dp` in `core/design_system/component/**` or the listed screens (animation durations/thresholds excepted). Recurring custom `.sp` values live as named `TextStyle`s in `Type.kt`; the two specific card radii are preserved as `radius.card` (22.dp) / `radius.cardTight` (14.dp). Both Android and iOS compile; no visual drift.
