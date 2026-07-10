#!/bin/sh
set -e

brew install openjdk@21
sudo ln -sfn "$(brew --prefix openjdk@21)/libexec/openjdk.jdk" /Library/Java/JavaVirtualMachines/openjdk-21.jdk

: "${SUPABASE_URL:?SUPABASE_URL env var not set}"
: "${SUPABASE_ANON_KEY:?SUPABASE_ANON_KEY env var not set}"

cat > "$CI_PRIMARY_REPOSITORY_PATH/config.properties" <<EOF
SUPABASE_URL=$SUPABASE_URL
SUPABASE_ANON_KEY=$SUPABASE_ANON_KEY
SENTRY_DSN_ANDROID=${SENTRY_DSN_ANDROID:-}
EOF
