#!/usr/bin/env python3
"""Imports Meteocons icons (MIT, https://github.com/basmilius/meteocons).

Two sets land in the project:

  res/drawable/wi_*.xml   every icon, as a static Android vector drawable
  res/raw/wi_*.json       the weather conditions, as Lottie animations

The upstream SVGs published under production/ are animated with SMIL, which a
VectorDrawable cannot express, so the drawables come from the static design
exports. The original motion is kept by the Lottie set instead.

Usage: python3 tools/import_meteocons.py [--variant line|fill]
"""

import argparse
import os
import re
import urllib.request
import xml.etree.ElementTree as ET

SVG_VERSION = "2.0.0"
SVG_BASE = "https://cdn.jsdelivr.net/npm/@bybas/weather-icons@%s/design/%s/export/wi_%s.svg"
LOTTIE_VERSION = "0.1.0"
LOTTIE_BASE = "https://cdn.jsdelivr.net/npm/@meteocons/lottie@%s/%s/%s.json"
DRAWABLE_DIR = "app/src/main/res/drawable"
RAW_DIR = "app/src/main/res/raw"
PREFIX = "wi_"

# Weather conditions, which is what the big icon on the weather screen shows.
CONDITIONS = [
    "clear-day", "clear-night", "partly-cloudy-day", "partly-cloudy-night",
    "overcast", "overcast-day", "overcast-night", "cloudy",
    "fog", "fog-day", "fog-night", "haze", "mist",
    "drizzle", "rain", "sleet", "snow", "hail",
    "thunderstorms", "thunderstorms-rain", "wind", "tornado", "not-available",
]

# Icons that label a single measurement. These stay static - small animations
# next to the numbers would only pull the driver's eye away from the road.
MEASUREMENTS = [
    "thermometer", "barometer", "humidity", "raindrops", "windsock", "compass",
    "horizon", "sunrise", "sunset", "umbrella", "uv-index",
    "pressure-high", "pressure-low",
]

SVG = "{http://www.w3.org/2000/svg}"
XLINK_HREF = "{http://www.w3.org/1999/xlink}href"
INHERITED = (
    "fill", "stroke", "stroke-width", "stroke-linecap", "stroke-linejoin",
    "stroke-miterlimit",
)
STROKE_ATTRS = {
    "stroke": "strokeColor",
    "stroke-width": "strokeWidth",
    "stroke-linecap": "strokeLineCap",
    "stroke-linejoin": "strokeLineJoin",
    "stroke-miterlimit": "strokeMiterLimit",
}


def gradients(root):
    """Collects the linear gradients, resolving the xlink:href inheritance the
    fill variant uses to share stops between gradients."""
    raw = {}
    for element in root.iter(SVG + "linearGradient"):
        stops = []
        for stop in element.iter(SVG + "stop"):
            color = stop.get("stop-color", "#000000")
            opacity = stop.get("stop-opacity")
            if opacity is not None and color.startswith("#") and len(color) == 7:
                color = "#%02x%s" % (round(float(opacity) * 255), color[1:])
            stops.append((stop.get("offset", "0"), color))
        raw[element.get("id")] = {
            "coords": {k: element.get(k) for k in ("x1", "y1", "x2", "y2")},
            "stops": stops,
            "parent": (element.get(XLINK_HREF) or element.get("href") or "").lstrip("#"),
        }

    resolved = {}
    for name, gradient in raw.items():
        parent = raw.get(gradient["parent"], {})
        coords = dict(parent.get("coords", {}))
        coords.update({k: v for k, v in gradient["coords"].items() if v is not None})
        resolved[name] = {
            "coords": coords,
            "stops": gradient["stops"] or parent.get("stops", []),
        }
    return resolved


def gradient_xml(gradient, attribute, indent):
    coords = gradient["coords"]
    lines = [
        '%s<aapt:attr name="android:%s">' % (indent, attribute),
        '%s    <gradient' % indent,
        '%s        android:type="linear"' % indent,
    ]
    for svg_key, android_key in (("x1", "startX"), ("y1", "startY"),
                                 ("x2", "endX"), ("y2", "endY")):
        lines.append('%s        android:%s="%s"'
                     % (indent, android_key, coords.get(svg_key, "0")))
    lines.append('%s        android:tileMode="clamp">' % indent)
    for offset, color in gradient["stops"]:
        lines.append('%s        <item android:offset="%s" android:color="%s" />'
                     % (indent, offset, color))
    lines += ['%s    </gradient>' % indent, '%s</aapt:attr>' % indent]
    return lines


def numbers(text):
    return [float(n) for n in re.findall(r"-?\d*\.?\d+(?:e-?\d+)?", text)]


def path_data(element):
    """Every SVG shape Meteocons uses, flattened to VectorDrawable path data."""
    tag = element.tag.replace(SVG, "")
    if tag == "path":
        return element.get("d")
    if tag == "line":
        x1, y1, x2, y2 = (element.get(k, "0") for k in ("x1", "y1", "x2", "y2"))
        return "M%s,%s L%s,%s" % (x1, y1, x2, y2)
    if tag == "rect":
        x, y = float(element.get("x", 0)), float(element.get("y", 0))
        w, h = float(element.get("width", 0)), float(element.get("height", 0))
        return "M%g,%g h%g v%g h%g z" % (x, y, w, h, -w)
    if tag in ("polygon", "polyline"):
        points = numbers(element.get("points", ""))
        pairs = list(zip(points[0::2], points[1::2]))
        data = "M%g,%g " % pairs[0] + " ".join("L%g,%g" % p for p in pairs[1:])
        return data + " z" if tag == "polygon" else data
    if tag == "circle":
        cx, cy = float(element.get("cx", 0)), float(element.get("cy", 0))
        r = float(element.get("r", 0))
        return "M%g,%g a%g,%g 0 1,0 %g,0 a%g,%g 0 1,0 %g,0 z" % (
            cx - r, cy, r, r, 2 * r, r, r, -2 * r,
        )
    return None


def path_xml(element, inherited, indent, gradients_by_id):
    data = path_data(element)
    if data is None:
        return []
    style = dict(inherited)
    style.update({k: v for k, v in element.attrib.items() if k in INHERITED})
    stroke = style.get("stroke", "none")
    fill = style.get("fill", "none")
    if stroke == "none" and fill == "none":
        return []  # shapes that only feed a clipPath

    def gradient_of(value):
        reference = re.match(r"url\(#(.+)\)", value or "")
        return gradients_by_id.get(reference.group(1)) if reference else None

    fill_gradient = gradient_of(fill)
    stroke_gradient = gradient_of(stroke)

    lines = ['%s<path' % indent, '%s    android:pathData="%s"' % (indent, data)]
    if fill != "none" and fill_gradient is None:
        lines.append('%s    android:fillColor="%s"' % (indent, fill))
    if stroke != "none":
        for svg_attr, android_attr in STROKE_ATTRS.items():
            if svg_attr == "stroke" and stroke_gradient is not None:
                continue
            # A VectorDrawable defaults to a zero-width stroke, SVG to 1.
            default = "1" if svg_attr == "stroke-width" else None
            value = style.get(svg_attr, default)
            if value is not None:
                lines.append('%s    android:%s="%s"' % (indent, android_attr, value))

    children = []
    if fill_gradient is not None:
        children += gradient_xml(fill_gradient, "fillColor", indent + "    ")
    if stroke_gradient is not None:
        children += gradient_xml(stroke_gradient, "strokeColor", indent + "    ")
    if children:
        lines[-1] += ">"
        return lines + children + ['%s</path>' % indent]
    lines[-1] += " />"
    return lines


def convert(svg_text):
    root = ET.fromstring(svg_text)
    viewport = numbers(root.get("viewBox", "0 0 64 64"))[2:]
    gradients_by_id = gradients(root)
    clips = {
        clip.get("id"): next(
            (path_data(shape) for shape in clip if path_data(shape)), None
        )
        for clip in root.iter(SVG + "clipPath")
    }

    body = []

    def walk(parent, inherited, indent):
        for child in parent:
            tag = child.tag.replace(SVG, "")
            if tag == "defs":
                continue
            style = dict(inherited)
            style.update({k: v for k, v in child.attrib.items() if k in INHERITED})
            if tag == "g":
                clip = re.match(r"url\(#(.+)\)", child.get("clip-path", "") or "")
                if clip and clips.get(clip.group(1)):
                    body.append('%s<group>' % indent)
                    body.append('%s    <clip-path android:pathData="%s" />'
                                % (indent, clips[clip.group(1)]))
                    walk(child, style, indent + "    ")
                    body.append('%s</group>' % indent)
                else:
                    walk(child, style, indent)
            else:
                body.extend(path_xml(child, inherited, indent, gradients_by_id))

    walk(root, {}, "    ")

    header = [
        '<?xml version="1.0" encoding="utf-8"?>',
        '<!-- Meteocons %s by Bas Milius, MIT. https://github.com/basmilius/meteocons -->' % SVG_VERSION,
        '<vector xmlns:android="http://schemas.android.com/apk/res/android"',
        '    xmlns:aapt="http://schemas.android.com/aapt"',
        '    android:width="%gdp"' % viewport[0],
        '    android:height="%gdp"' % viewport[1],
        '    android:viewportWidth="%g"' % viewport[0],
        '    android:viewportHeight="%g">' % viewport[1],
    ]
    return "\n".join(header + body + ["</vector>", ""])


def download(url):
    with urllib.request.urlopen(url, timeout=30) as response:
        return response.read().decode("utf-8")


def resource_name(name):
    return PREFIX + name.replace("-", "_")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--variant", choices=("line", "fill"), default="fill")
    args = parser.parse_args()

    os.makedirs(DRAWABLE_DIR, exist_ok=True)
    os.makedirs(RAW_DIR, exist_ok=True)

    for name in CONDITIONS + MEASUREMENTS:
        svg = download(SVG_BASE % (SVG_VERSION, args.variant, name))
        target = os.path.join(DRAWABLE_DIR, resource_name(name) + ".xml")
        with open(target, "w", encoding="utf-8") as handle:
            handle.write(convert(svg))
        print("wrote", target)

    for name in CONDITIONS:
        lottie = download(LOTTIE_BASE % (LOTTIE_VERSION, args.variant, name))
        target = os.path.join(RAW_DIR, resource_name(name) + ".json")
        with open(target, "w", encoding="utf-8") as handle:
            handle.write(lottie)
        print("wrote", target)


if __name__ == "__main__":
    main()
