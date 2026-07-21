#!/usr/bin/env bash
#
# One-shot release: bump Android + iOS versions together, commit, tag, and push.
#
# Usage:
#   scripts/release.sh 1.2.3          # set an explicit version
#   scripts/release.sh patch          # 1.0.4 -> 1.0.5
#   scripts/release.sh minor          # 1.0.4 -> 1.1.0
#   scripts/release.sh major          # 1.0.4 -> 2.0.0
#   scripts/release.sh patch nopush   # do everything except push (review first)
#
# develop is the single source of truth; the tag is created on the current branch.

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
TOML="$ROOT/gradle/libs.versions.toml"
XCCONFIG="$ROOT/iosApp/Configuration/Config.xcconfig"

bump="${1:-patch}"
push="${2:-push}"

current="$(grep -E '^versionName = ' "$TOML" | sed -E 's/.*"([0-9]+\.[0-9]+\.[0-9]+)".*/\1/')"
IFS=. read -r MAJ MIN PAT <<< "$current"

case "$bump" in
  major) MAJ=$((MAJ + 1)); MIN=0; PAT=0 ;;
  minor) MIN=$((MIN + 1)); PAT=0 ;;
  patch) PAT=$((PAT + 1)) ;;
  [0-9]*.[0-9]*.[0-9]*) IFS=. read -r MAJ MIN PAT <<< "$bump" ;;
  *) echo "Usage: $0 <major|minor|patch|X.Y.Z> [nopush]" >&2; exit 1 ;;
esac

VERSION="$MAJ.$MIN.$PAT"
BUILD=$((MAJ * 10000 + MIN * 100 + PAT))   # matches the Android versionCode scheme
TAG="v$VERSION"
BRANCH="$(git -C "$ROOT" rev-parse --abbrev-ref HEAD)"

echo "Release: $current -> $VERSION  (build $BUILD, tag $TAG, branch $BRANCH)"

# --- safety ---
[ -z "$(git -C "$ROOT" status --porcelain)" ] || { echo "Working tree is not clean — commit or stash first." >&2; exit 1; }
if git -C "$ROOT" rev-parse -q --verify "refs/tags/$TAG" >/dev/null; then
  echo "Tag $TAG already exists." >&2; exit 1
fi

# --- bump versions (Android versionCode is derived from versionName by the convention plugin) ---
sed -i.bak -E "s/^versionName = \"[^\"]*\"/versionName = \"$VERSION\"/" "$TOML"
sed -i.bak -E "s/^MARKETING_VERSION=.*/MARKETING_VERSION=$VERSION/" "$XCCONFIG"
sed -i.bak -E "s/^CURRENT_PROJECT_VERSION=.*/CURRENT_PROJECT_VERSION=$BUILD/" "$XCCONFIG"
rm -f "$TOML.bak" "$XCCONFIG.bak"

# --- commit + tag ---
git -C "$ROOT" add "$TOML" "$XCCONFIG"
git -C "$ROOT" commit -m "chore: bump version to $VERSION"
git -C "$ROOT" tag -a "$TAG" -m "Release $VERSION"
echo "Committed and tagged $TAG."

# --- push ---
if [ "$push" = "nopush" ]; then
  echo "Skipped push. To release: git push origin $BRANCH && git push origin $TAG"
  exit 0
fi

git -C "$ROOT" push origin "$BRANCH"
git -C "$ROOT" push origin "$TAG"
echo "Pushed $BRANCH + $TAG. CI will build & upload $VERSION ($BUILD) — Android to Play closed testing, iOS to TestFlight."
