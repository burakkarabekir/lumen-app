# Store screenshots

Turn raw app captures into polished, on-brand App Store / Play Store screenshots.

## 1. Capture raw screens

Run the app (logged in, with real entries so it looks alive) and capture each key
screen into `screenshots/raw/<name>.png`.

**iOS** (use a 6.7"/6.9" device — e.g. iPhone 15 Pro Max / iPhone 17 — for the required App Store size):
```bash
xcrun simctl io booted screenshot screenshots/raw/journal.png
```

**Android** (a phone emulator):
```bash
adb exec-out screencap -p > screenshots/raw/journal.png
```

Recommended set (5–8 shots tell the story):

| file | screen |
|------|--------|
| `journal.png` | Journal feed with entries |
| `moment.png`  | A moment with an AI reflection |
| `insights.png`| Insights / streaks |
| `lock.png`    | Lock & Privacy (biometric) |
| `premium.png` | Lumen Plus / paywall |

## 2. Frame them

Edit the `SHOTS` list in `frame.py` (filename → headline), then:
```bash
python3 screenshots/frame.py
```
Output lands in `screenshots/framed/ios/` (1290×2796) and `screenshots/framed/android/` (1080×1920).

## 3. Upload

- **App Store Connect** → 6.7" iPhone set = `framed/ios/*` (add an iPad set if you ship iPad).
- **Play Console** → phone screenshots = `framed/android/*` (2–8), plus a **1024×500 feature graphic** (make one separately).

`raw/` and `framed/` are gitignored — only the script + this README are tracked.
