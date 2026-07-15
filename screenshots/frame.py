#!/usr/bin/env python3
"""
Frame raw app captures into store-ready screenshots.

Workflow:
  1. Capture raw screens (see README.md) into screenshots/raw/<name>.png
  2. Edit SHOTS below (filename -> headline caption).
  3. python3 screenshots/frame.py
  4. Framed output lands in screenshots/framed/{ios,android}/.

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

    # screenshot: fit width, rounded + shadow, below the caption
    shot = Image.open(raw_path).convert("RGBA")
    target_w = int(w * 0.80)
    scale = target_w / shot.width
    shot = shot.resize((target_w, int(shot.height * scale)), Image.LANCZOS)
    shot = rounded(shot, int(target_w * 0.055))
    sx = (w - shot.width) // 2
    sy = int(y + h * 0.03)

    shadow = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
    sh = Image.new("RGBA", shot.size, (0, 0, 0, 150))
    sh.putalpha(rounded(sh, int(target_w * 0.055)).split()[3])
    shadow.paste(sh, (sx, sy + 18), sh)
    shadow = shadow.filter(ImageFilter.GaussianBlur(26))
    canvas = Image.alpha_composite(canvas, shadow)
    canvas.paste(shot, (sx, sy), shot)
    return canvas.convert("RGB")


def main():
    made = 0
    for platform, (w, h) in PRESETS.items():
        os.makedirs(os.path.join(OUT, platform), exist_ok=True)
        for fname, caption in SHOTS:
            src = os.path.join(RAW, fname)
            if not os.path.exists(src):
                continue
            frame(src, caption, w, h).save(
                os.path.join(OUT, platform, fname))
            made += 1
    print(f"framed {made} image(s) -> {OUT}/  (add raw/*.png first if 0)")


if __name__ == "__main__":
    main()
