# -*- coding: utf-8 -*-
"""Generate all item textures + JSON assets for the oremygod mod (16x16 MC style).

Outputs into ../src/main/resources/assets/oremygod/.
Block texture (animated strip) is copied from the workspace root.
"""
import json
import os
import shutil

ROOT = os.path.join(os.path.dirname(__file__), "..")
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "oremygod")
TEX_ITEM = os.path.join(ASSETS, "textures", "item")
TEX_BLOCK = os.path.join(ASSETS, "textures", "block")
TEX_SMITH = os.path.join(ASSETS, "textures", "gui", "sprites", "item")
MODEL_ITEM = os.path.join(ASSETS, "models", "item")
MODEL_BLOCK = os.path.join(ASSETS, "models", "block")
BLOCKSTATES = os.path.join(ASSETS, "blockstates")
LANG = os.path.join(ASSETS, "lang")
for d in (TEX_ITEM, TEX_BLOCK, TEX_SMITH, MODEL_ITEM, MODEL_BLOCK, BLOCKSTATES, LANG):
    os.makedirs(d, exist_ok=True)

from PIL import Image

# ---- palette (RGBA) ----
P = {
    ".": (0, 0, 0, 0),
    "o": (18, 11, 30, 255),     # outline
    "d": (48, 36, 78, 255),     # dark purple metal
    "m": (86, 64, 134, 255),    # mid metal
    "l": (132, 98, 190, 255),   # lit metal
    "h": (180, 144, 226, 255),  # highlight
    "w": (240, 232, 255, 255),  # white glint
    "g": (214, 168, 255, 255),  # glow
    "b": (44, 32, 42, 255),     # dark grip
    "B": (74, 56, 66, 255),     # grip highlight
    "p": (198, 186, 152, 255),  # template paper
    "q": (108, 84, 66, 255),    # paper border
    "r": (150, 120, 92, 255),   # paper shading
}

ITEMS = {}

ITEMS["end_meteorite_debris"] = [
    "................",
    ".....oooo.......",
    "....oddddo......",
    "...oddmggdo.....",
    "..oddmggmddo....",
    "..odmggmdmdo....",
    ".oddmdmggmddo...",
    ".odddmggdmddo...",
    ".odmddmggddo....",
    "..odmddmddo.....",
    "..oddddmdoo.....",
    "...oddmddo......",
    "....odddo.......",
    ".....ooo........",
    "................",
    "................",
]

ITEMS["end_meteorite_scrap"] = [
    "................",
    ".........ooo....",
    "........odddo...",
    ".......oddmdo...",
    "......oddlhdo...",
    ".....odddmhdo...",
    "....odddlmdo....",
    "...odddmddo.....",
    "..odddmddo......",
    ".odddmddo.......",
    ".oddmddo........",
    ".oddmdo.........",
    ".oddoo..........",
    ".oo.............",
    "................",
    "................",
]

ITEMS["end_meteorite_ingot"] = [
    "................",
    "................",
    "..........oo....",
    ".........oddo...",
    "........odddo...",
    "......odddddo...",
    "....odddddddo...",
    "..odddddddddo...",
    ".odmhhhlmddddo..",
    ".oooooooooooo...",
    "................",
    "................",
    "................",
    "................",
    "................",
    "................",
]

ITEMS["end_meteorite_upgrade_smithing_template"] = [
    "................",
    "..oooooooooooo..",
    ".opqqqqqqqqqqpo.",
    ".oqp........pqo.",
    ".oqp..oGG o..pqo.".replace(" ", ""),
    ".oqp.oGwwGo.pqo.",
    ".oqp.oGwwGo.pqo.",
    ".oqp..oGG o..pqo.".replace(" ", ""),
    ".oqp........pqo.",
    ".oqp..mmmm..pqo.",
    ".oqp.mloo lm.pqo.".replace(" ", ""),
    ".oqp.mloolm.pqo.",
    ".oqp..mmll..pqo.",
    ".oqp........pqo.",
    ".opqqqqqqqqqqpo.",
    "..oooooooooooo..",
]
# fix template gem letters to palette
_TPL_FIX = {"G": "l"}

ITEMS["end_meteorite_sword"] = [
    ".............oo.",
    "............oll.",
    "...........oll..",
    "..........oll...",
    ".........oll....",
    "........oll.....",
    ".......oll......",
    "......oll.......",
    ".....olbB.......",
    "....olbB........",
    "...olbB.........",
    "..obBb..........",
    ".oBbBo..........",
    ".oBo............",
    "oo..............",
    "o...............",
]

ITEMS["end_meteorite_pickaxe"] = [
    "................",
    "..oll......llo..",
    ".ohll......llho.",
    ".ohhll....llhho.",
    ".odhhllllllhhdo.",
    "..odddddddddo...",
    ".....ommo.......",
    ".....ombB.......",
    ".....ombB.......",
    "....ombB........",
    "....obBb........",
    "...oBbBo........",
    "...oBb..........",
    "...ob...........",
    "..oo............",
    "..o.............",
]

ITEMS["end_meteorite_axe"] = [
    "................",
    ".ollloo.........",
    ".ohllldo........",
    ".ohhllmdo.......",
    ".odhhllmdo......",
    "..odlllmdo......",
    "...odddmdo......",
    "....ommo........",
    "....ombB........",
    "...ombB.........",
    "..ombB..........",
    "..obBb..........",
    ".oBbBo..........",
    ".oBb............",
    "oo..............",
    "o...............",
]

ITEMS["end_meteorite_shovel"] = [
    "................",
    ".........oo.....",
    "........oddo....",
    ".......oddllo...",
    ".......odlhdo...",
    "......oddmdo....",
    "......odddo.....",
    ".......omo......",
    "......ombB......",
    ".....ombB.......",
    ".....ombB.......",
    "....obBb........",
    "...oBbBo........",
    "...ob...........",
    "..oo............",
    "..o.............",
]

ITEMS["end_meteorite_hoe"] = [
    "................",
    "..ollloo........",
    ".ohlllddo.......",
    ".odhhlllo.......",
    "..odddo.........",
    "....omo.........",
    "....ombB........",
    "...ombB.........",
    "...ombB.........",
    "..ombB..........",
    "..obBb..........",
    ".oBbBo..........",
    ".oBb............",
    ".ob.............",
    "oo..............",
    "o...............",
]

ITEMS["end_meteorite_helmet"] = [
    "................",
    "....oooooooo....",
    "...odddddddo....",
    "..odmhhhhhmdo...",
    "..odmhhhwhmdo...",
    ".odlm" + "h" * 7 + "mldo",
    ".odm" + "d" * 6 + "mdo...",
    ".oddddddddddo...",
    ".oddddddddddo...",
    "..oooooooooo....",
    "................",
    "................",
    "................",
    "................",
    "................",
    "................",
]

_cp = "." + "o" + "d" * 12 + "o" + "."
_cp_split = "." + "o" + "dmddo" + "d" * 7 + "o" + "."
_cp_split2 = "." + "o" + "ddddo" + "d" * 7 + "o" + "."
ITEMS["end_meteorite_chestplate"] = [
    "................",
    "..oooooooooooo..",
    ".odm" + "h" * 8 + "mdo.",
    ".odm" + "hhwwwwhh" + "mdo.",
    ".odm" + "h" * 8 + "mdo.",
    _cp,
    _cp,
    _cp_split,
    _cp_split,
    _cp_split,
    _cp_split2,
    _cp_split2,
    "." + "o" * 14 + ".",
    "................",
    "................",
    "................",
]

ITEMS["end_meteorite_leggings"] = [
    "................",
    ".ooooooooooooo..",
    ".odddddddddddo..",
    ".odm" + "h" * 8 + "mdo.",
    ".odddddddddddo..",
    ".odddddddddddo..",
    ".oddddd.oddddo..",
    ".odddd..oddddo..",
    ".oddd...oddddo..",
    ".odd.....odddo..",
    ".odd.....odddo..",
    ".odd.....odddo..",
    ".ooo.....oooo...",
    "................",
    "................",
    "................",
]

ITEMS["end_meteorite_boots"] = [
    "................",
    "....oooooooo....",
    "....oddddddo....",
    "....odhhhhmdo...",
    "....oddddddo....",
    "....oddddddo....",
    "....oddddddo....",
    ".oo.odddddddo...",
    ".oddoooddddddo..",
    ".odddddddddddo..",
    ".oooooooooooo...",
    "................",
    "................",
    "................",
    "................",
    "................",
]

# ---- render ----
def render(rows, fix=None, name=""):
    assert len(rows) == 16, f"{name}: {len(rows)} rows"
    data = []
    for i, r in enumerate(rows):
        assert len(r) == 16, f"{name} row {i} len {len(r)}: {r}"
        for ch in r:
            if fix:
                ch = fix.get(ch, ch)
            assert ch in P, f"{name} bad char {ch}"
            data.append(P[ch])
    img = Image.new("RGBA", (16, 16))
    img.putdata(data)
    return img

for name, rows in ITEMS.items():
    fix = _TPL_FIX if name.endswith("smithing_template") else None
    render(rows, fix, name).save(os.path.join(TEX_ITEM, name + ".png"))

# ---- smithing GUI: empty addition slot (ingot silhouette) ----
SLOT = [
    "................",
    "................",
    "..........oo....",
    ".........oddo...",
    "........odddo...",
    "......odddddo...",
    "....odddddddo...",
    "..odddddddddo...",
    ".odmhhhlmddddo..",
    ".oooooooooooo...",
    "................",
    "................",
    "................",
    "................",
    "................",
    "................",
]
# dim it to an empty-slot hint (multiply alpha)
slot_img = render(SLOT, None, "slot")
px = slot_img.load()
for y in range(16):
    for x in range(16):
        r, g, b, a = px[x, y]
        if a:
            px[x, y] = (r, g, b, 110)
slot_img.save(os.path.join(TEX_SMITH, "empty_slot_end_meteorite_ingot.png"))

# ---- block texture: copy animated strip + mcmeta ----
ws_root = os.path.join(ROOT, "..")
shutil.copy(os.path.join(ws_root, "end_meteorite.png"), os.path.join(TEX_BLOCK, "end_meteorite.png"))
shutil.copy(os.path.join(ws_root, "end_meteorite.png.mcmeta"), os.path.join(TEX_BLOCK, "end_meteorite.png.mcmeta"))

# ---- JSON models / blockstate ----
def dump(path, obj):
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)

dump(os.path.join(BLOCKSTATES, "end_meteorite.json"), {
    "variants": {"": {"model": "oremygod:block/end_meteorite"}}
})
dump(os.path.join(MODEL_BLOCK, "end_meteorite.json"), {
    "parent": "minecraft:block/cube_all",
    "textures": {"all": "oremygod:block/end_meteorite"}
})
dump(os.path.join(MODEL_ITEM, "end_meteorite.json"), {
    "parent": "oremygod:block/end_meteorite"
})
for name in ITEMS:
    dump(os.path.join(MODEL_ITEM, name + ".json"), {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"oremygod:item/{name}"}
    })

# ---- lang ----
NAMES = {
    "end_meteorite": "末地陨石",
    "end_meteorite_debris": "末地陨石残骸",
    "end_meteorite_scrap": "末地陨铁碎片",
    "end_meteorite_ingot": "末地陨铁锭",
    "end_meteorite_upgrade_smithing_template": "锻造模板（下界合金至末地陨铁）",
    "end_meteorite_sword": "末地陨铁剑",
    "end_meteorite_pickaxe": "末地陨铁镐",
    "end_meteorite_axe": "末地陨铁斧",
    "end_meteorite_shovel": "末地陨铁锹",
    "end_meteorite_hoe": "末地陨铁锄",
    "end_meteorite_helmet": "末地陨铁头盔",
    "end_meteorite_chestplate": "末地陨铁胸甲",
    "end_meteorite_leggings": "末地陨铁护腿",
    "end_meteorite_boots": "末地陨铁靴子",
}
EN = {
    "end_meteorite": "End Meteorite",
    "end_meteorite_debris": "End Meteorite Debris",
    "end_meteorite_scrap": "End Meteorite Iron Scrap",
    "end_meteorite_ingot": "End Meteorite Iron Ingot",
    "end_meteorite_upgrade_smithing_template": "Smithing Template (Netherite to End Meteorite Iron)",
    "end_meteorite_sword": "End Meteorite Iron Sword",
    "end_meteorite_pickaxe": "End Meteorite Iron Pickaxe",
    "end_meteorite_axe": "End Meteorite Iron Axe",
    "end_meteorite_shovel": "End Meteorite Iron Shovel",
    "end_meteorite_hoe": "End Meteorite Iron Hoe",
    "end_meteorite_helmet": "End Meteorite Iron Helmet",
    "end_meteorite_chestplate": "End Meteorite Iron Chestplate",
    "end_meteorite_leggings": "End Meteorite Iron Leggings",
    "end_meteorite_boots": "End Meteorite Iron Boots",
}
zh = {}
en = {}
for k, v in NAMES.items():
    prefix = "block" if k == "end_meteorite" else "item"
    zh[f"{prefix}.oremygod.{k}"] = v
    en[f"{prefix}.oremygod.{k}"] = EN[k]

# smithing template tooltip lines
TPL = "item.oremygod.end_meteorite_upgrade_smithing_template"
zh.update({
    f"{TPL}.upgrade_description": "末地陨铁升级",
    f"{TPL}.applies_to": "下界合金护甲、武器和工具",
    f"{TPL}.ingredients": "末地陨铁锭",
    f"{TPL}.base_slot_description": "放入下界合金护甲、武器或工具",
    f"{TPL}.additions_slot_description": "放入末地陨铁锭",
})
en.update({
    f"{TPL}.upgrade_description": "End Meteorite Iron Upgrade",
    f"{TPL}.applies_to": "Netherite Armor, Weapons, and Tools",
    f"{TPL}.ingredients": "End Meteorite Iron Ingot",
    f"{TPL}.base_slot_description": "Add Netherite armor, weapon, or tool",
    f"{TPL}.additions_slot_description": "Add End Meteorite Iron Ingot",
})
dump(os.path.join(LANG, "zh_cn.json"), dict(sorted(zh.items())))
dump(os.path.join(LANG, "en_us.json"), dict(sorted(en.items())))

# ---- spritesheet preview for QA ----
cols = 5
rows_n = (len(ITEMS) + cols - 1) // cols
sheet = Image.new("RGBA", (cols * 16, rows_n * 16), (26, 16, 40, 255))
for i, (name, rows_) in enumerate(ITEMS.items()):
    fix = _TPL_FIX if name.endswith("smithing_template") else None
    sheet.paste(render(rows_, fix, name), ((i % cols) * 16, (i // cols) * 16),
                render(rows_, fix, name))
sheet.resize((cols * 64, rows_n * 64), Image.NEAREST).save(
    os.path.join(ROOT, "..", "items_preview.png"))
print("generated", len(ITEMS), "item textures + assets")
