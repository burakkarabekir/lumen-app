#!/usr/bin/env python3
"""
Frame raw app captures into store-ready screenshots.

Workflow:
  1. Capture raw screens (see README.md) into screenshots/raw/<platform>/<name>.png
     iOS:     xcrun simctl io booted screenshot screenshots/raw/ios/<name>.png
     Android: adb exec-out screencap -p > screenshots/raw/android/<name>.png
     A bare screenshots/raw/<name>.png is used as a fallback for either platform.
  2. Edit SHOTS below (filename -> headline caption).
  3. python3 screenshots/frame.py
  4. Framed output lands in screenshots/framed/{ios,android}/, and the
     Play feature graphic in screenshots/framed/play/.

No device bezel — a clean caption-over-brand-gradient layout (what most
top App Store listings use). Brand palette matches the app icon.
"""
import os
from PIL import Image, ImageDraw, ImageFilter, ImageFont

HERE = os.path.dirname(os.path.abspath(__file__))
RAW = os.path.join(HERE, "raw")
OUT = os.path.join(HERE, "framed")

# filename in raw/  ->  marketing headline
SHOTS = [
    ("journal.png", "Capture every moment"),
    ("moment.png", "Reflect with AI insight"),
    ("insights.png", "See your patterns"),
    ("lock.png", "Private by design"),
    ("premium.png", "7 reflections a day"),
]

PRESETS = {"ios": (1290, 2796), "android": (1080, 1920)}

TOP = (8, 20, 74)      # deep indigo  (brand)
BOT = (19, 73, 236)    # brand blue   (#1349EC)
FONTS = [
    "/System/Library/Fonts/SFNS.ttf",
    "/System/Library/Fonts/HelveticaNeue.ttc",
    "/System/Library/Fonts/Supplemental/Arial Bold.ttf",
    "/Library/Fonts/Arial.ttf",
]


def lerp(a, b, t):
    return tuple(int(a[i] + (b[i] - a[i]) * t) for i in range(3))


def load_font(size):
    for p in FONTS:
        if os.path.exists(p):
            try:
                return ImageFont.truetype(p, size)
            except Exception:
                continue
    return ImageFont.load_default()


def gradient(w, h):
    strip = Image.new("RGB", (1, h))
    px = strip.load()
    for y in range(h):
        px[0, y] = lerp(TOP, BOT, y / (h - 1))
    img = strip.resize((w, h)).convert("RGBA")
    glow = Image.new("L", (w, h), 0)
    ImageDraw.Draw(glow).ellipse(
        [w * 0.1, h * 0.05, w * 0.9, h * 0.5], fill=90)
    glow = glow.filter(ImageFilter.GaussianBlur(w * 0.18))
    tint = Image.new("RGBA", (w, h), (150, 180, 255, 0))
    tint.putalpha(glow)
    return Image.alpha_composite(img, tint)


def rounded(img, radius):
    mask = Image.new("L", img.size, 0)
    ImageDraw.Draw(mask).rounded_rectangle([0, 0, *img.size], radius, fill=255)
    out = img.convert("RGBA")
    out.putalpha(mask)
    return out


def wrap(draw, text, font, max_w):
    words, lines, cur = text.split(), [], ""
    for wd in words:
        t = f"{cur} {wd}".strip()
        if draw.textlength(t, font=font) <= max_w:
            cur = t
        else:
            lines.append(cur)
            cur = wd
    if cur:
        lines.append(cur)
    return lines


def frame(raw_path, caption, w, h):
    canvas = gradient(w, h)
    draw = ImageDraw.Draw(canvas)

    # headline
    font = load_font(int(w * 0.072))
    lines = wrap(draw, caption, font, w * 0.86)
    lh = font.size * 1.18
    y = h * 0.055
    for line in lines:
        tw = draw.textlength(line, font=font)
        draw.text(((w - tw) / 2, y), line, font=font, fill=(255, 255, 255))
        y += lh

    # screenshot: fit below the caption, rounded + shadow
    shot = Image.open(raw_path).convert("RGBA")
    sy = int(y + h * 0.03)
    box_w = w * 0.80
    box_h = h - sy - h * 0.04
    scale = min(box_w / shot.width, box_h / shot.height)
    target_w = max(1, int(shot.width * scale))
    target_h = max(1, int(shot.height * scale))
    shot = shot.resize((target_w, target_h), Image.LANCZOS)
    shot = rounded(shot, int(target_w * 0.055))
    sx = (w - shot.width) // 2

    shadow = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
    sh = Image.new("RGBA", shot.size, (0, 0, 0, 150))
    sh.putalpha(rounded(sh, int(target_w * 0.055)).split()[3])
    shadow.paste(sh, (sx, sy + 18), sh)
    shadow = shadow.filter(ImageFilter.GaussianBlur(26))
    canvas = Image.alpha_composite(canvas, shadow)
    canvas.paste(shot, (sx, sy), shot)
    return canvas.convert("RGB")


ADAPTIVE_ICON = os.path.join(
    HERE, os.pardir, "androidApp", "src", "main", "res", "mipmap-xxxhdpi")


def play_icon(size=512):
    bg = Image.open(os.path.join(ADAPTIVE_ICON, "ic_launcher_background.png"))
    fg = Image.open(os.path.join(ADAPTIVE_ICON, "ic_launcher_foreground.png"))
    icon = Image.alpha_composite(bg.convert("RGBA"), fg.convert("RGBA"))
    return icon.resize((size, size), Image.LANCZOS)


def feature_graphic(w=1024, h=500):
    canvas = gradient(w, h)
    draw = ImageDraw.Draw(canvas)
    safe_w = w * 0.80

    title_font = load_font(int(h * 0.21))
    tag_font = load_font(int(h * 0.072))
    title, tag = "Lumen", "Journal your moments. Reflect with AI."

    tw = draw.textlength(title, font=title_font)
    gw = draw.textlength(tag, font=tag_font)
    block_h = title_font.size + h * 0.06 + tag_font.size
    y = (h - block_h) / 2

    draw.text(((w - tw) / 2, y), title, font=title_font, fill=(255, 255, 255))
    y += title_font.size + h * 0.06
    draw.text(((w - gw) / 2, y), tag, font=tag_font, fill=(196, 214, 255))

    if max(tw, gw) > safe_w:
        print(f"  ! feature graphic: text exceeds the central 80% safe area "
              f"({int(max(tw, gw))}px > {int(safe_w)}px)")
    return canvas.convert("RGB")


def source_for(platform, fname):
    per_platform = os.path.join(RAW, platform, fname)
    if os.path.exists(per_platform):
        return per_platform, True
    shared = os.path.join(RAW, fname)
    if os.path.exists(shared):
        return shared, False
    return None, False


def main():
    made = 0
    for platform, (w, h) in PRESETS.items():
        os.makedirs(os.path.join(OUT, platform), exist_ok=True)
        missing, borrowed = [], []
        for fname, caption in SHOTS:
            src, native = source_for(platform, fname)
            if src is None:
                missing.append(fname)
                continue
            if not native:
                borrowed.append(fname)
            frame(src, caption, w, h).save(os.path.join(OUT, platform, fname))
            made += 1
        if borrowed:
            print(f"  ! {platform}: not captured on {platform}, using shared raw/: "
                  f"{', '.join(borrowed)}")
        if missing:
            print(f"  ! {platform}: no source found, skipped: {', '.join(missing)}")
    print(f"framed {made} image(s) -> {OUT}/  (add raw/<platform>/*.png first if 0)")

    os.makedirs(os.path.join(OUT, "play"), exist_ok=True)
    fg = os.path.join(OUT, "play", "feature-graphic.png")
    feature_graphic().save(fg)
    print(f"feature graphic 1024x500 -> {fg}")

    icon = os.path.join(OUT, "play", "icon-512.png")
    play_icon().save(icon)
    print(f"hi-res icon 512x512     -> {icon}")


if __name__ == "__main__":
    main()
