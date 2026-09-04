import os, re, sys, json

BLOCKSTATE_SIMPLE = """{ "variants": { "normal": { "model": "mwccf:%s" } } }"""
MODEL_CUBE = """{ "parent": "block/cube_all", "textures": { "all": "mwccf:blocks/%s" } }"""
ITEM_CUBE = """{ "parent": "mwccf:block/%s" }"""

# Slab blockstates & models
BLOCKSTATE_SLAB = """{ "variants": { "half=bottom": { "model": "mwccf:%s_slab_bottom" }, "half=top": { "model": "mwccf:%s_slab_top" } } }"""
MODEL_SLAB_BOTTOM = """{ "parent": "block/half_slab", "textures": { "bottom": "mwccf:blocks/%s", "top": "mwccf:blocks/%s", "side": "mwccf:blocks/%s" } }"""
MODEL_SLAB_TOP = """{ "parent": "block/upper_slab", "textures": { "bottom": "mwccf:blocks/%s", "top": "mwccf:blocks/%s", "side": "mwccf:blocks/%s" } }"""

# Stairs
BLOCKSTATE_STAIRS = """{ "variants": { 
  "facing=east,half=bottom,shape=straight": { "model": "mwccf:%s_stairs" },
  "facing=west,half=bottom,shape=straight": { "model": "mwccf:%s_stairs", "y": 180 },
  "facing=south,half=bottom,shape=straight": { "model": "mwccf:%s_stairs", "y": 90 },
  "facing=north,half=bottom,shape=straight": { "model": "mwccf:%s_stairs", "y": 270 },
  "facing=east,half=top,shape=straight": { "model": "mwccf:%s_stairs", "x": 180 },
  "facing=west,half=top,shape=straight": { "model": "mwccf:%s_stairs", "x": 180, "y": 180 },
  "facing=south,half=top,shape=straight": { "model": "mwccf:%s_stairs", "x": 180, "y": 90 },
  "facing=north,half=top,shape=straight": { "model": "mwccf:%s_stairs", "x": 180, "y": 270 },
  "facing=east,half=bottom,shape=outer_right": { "model": "mwccf:%s_stairs_outer" },
  "facing=west,half=bottom,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "y": 180 },
  "facing=south,half=bottom,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "y": 90 },
  "facing=north,half=bottom,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "y": 270 },
  "facing=east,half=top,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "x": 180 },
  "facing=west,half=top,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "x": 180, "y": 180 },
  "facing=south,half=top,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "x": 180, "y": 90 },
  "facing=north,half=top,shape=outer_right": { "model": "mwccf:%s_stairs_outer", "x": 180, "y": 270 },
  "facing=east,half=bottom,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "y": 270 },
  "facing=west,half=bottom,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "y": 90 },
  "facing=south,half=bottom,shape=outer_left": { "model": "mwccf:%s_stairs_outer" },
  "facing=north,half=bottom,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "y": 180 },
  "facing=east,half=top,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "x": 180, "y": 270 },
  "facing=west,half=top,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "x": 180, "y": 90 },
  "facing=south,half=top,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "x": 180 },
  "facing=north,half=top,shape=outer_left": { "model": "mwccf:%s_stairs_outer", "x": 180, "y": 180 },
  "facing=east,half=bottom,shape=inner_right": { "model": "mwccf:%s_stairs_inner" },
  "facing=west,half=bottom,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "y": 180 },
  "facing=south,half=bottom,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "y": 90 },
  "facing=north,half=bottom,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "y": 270 },
  "facing=east,half=top,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "x": 180 },
  "facing=west,half=top,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "x": 180, "y": 180 },
  "facing=south,half=top,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "x": 180, "y": 90 },
  "facing=north,half=top,shape=inner_right": { "model": "mwccf:%s_stairs_inner", "x": 180, "y": 270 },
  "facing=east,half=bottom,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "y": 270 },
  "facing=west,half=bottom,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "y": 90 },
  "facing=south,half=bottom,shape=inner_left": { "model": "mwccf:%s_stairs_inner" },
  "facing=north,half=bottom,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "y": 180 },
  "facing=east,half=top,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "x": 180, "y": 270 },
  "facing=west,half=top,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "x": 180, "y": 90 },
  "facing=south,half=top,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "x": 180 },
  "facing=north,half=top,shape=inner_left": { "model": "mwccf:%s_stairs_inner", "x": 180, "y": 180 }
} }"""
MODEL_STAIRS = """{ "parent": "block/stairs", "textures": { "bottom": "mwccf:blocks/%s", "top": "mwccf:blocks/%s", "side": "mwccf:blocks/%s" } }"""
MODEL_STAIRS_INNER = """{ "parent": "block/inner_stairs", "textures": { "bottom": "mwccf:blocks/%s", "top": "mwccf:blocks/%s", "side": "mwccf:blocks/%s" } }"""
MODEL_STAIRS_OUTER = """{ "parent": "block/outer_stairs", "textures": { "bottom": "mwccf:blocks/%s", "top": "mwccf:blocks/%s", "side": "mwccf:blocks/%s" } }"""

# Fences
BLOCKSTATE_FENCE = """{ "multipart": [
  { "apply": { "model": "mwccf:%s_fence_post" } },
  { "when": { "north": "true" }, "apply": { "model": "mwccf:%s_fence_side", "uvlock": true } },
  { "when": { "east": "true" }, "apply": { "model": "mwccf:%s_fence_side", "y": 90, "uvlock": true } },
  { "when": { "south": "true" }, "apply": { "model": "mwccf:%s_fence_side", "y": 180, "uvlock": true } },
  { "when": { "west": "true" }, "apply": { "model": "mwccf:%s_fence_side", "y": 270, "uvlock": true } }
] }"""
MODEL_FENCE_POST = """{ "parent": "block/fence_post", "textures": { "texture": "mwccf:blocks/%s" } }"""
MODEL_FENCE_SIDE = """{ "parent": "block/fence_side", "textures": { "texture": "mwccf:blocks/%s" } }"""
ITEM_FENCE = """{ "parent": "block/fence_inventory", "textures": { "texture": "mwccf:blocks/%s" } }"""

# Fence Gate
BLOCKSTATE_FENCE_GATE = """{ "variants": {
  "facing=south,in_wall=false,open=false,powered=false": { "model": "mwccf:%s_fence_gate" },
  "facing=west,in_wall=false,open=false,powered=false": { "model": "mwccf:%s_fence_gate", "y": 90, "uvlock": true },
  "facing=north,in_wall=false,open=false,powered=false": { "model": "mwccf:%s_fence_gate", "y": 180, "uvlock": true },
  "facing=east,in_wall=false,open=false,powered=false": { "model": "mwccf:%s_fence_gate", "y": 270, "uvlock": true },
  "facing=south,in_wall=false,open=true,powered=false": { "model": "mwccf:%s_fence_gate_open" },
  "facing=west,in_wall=false,open=true,powered=false": { "model": "mwccf:%s_fence_gate_open", "y": 90, "uvlock": true },
  "facing=north,in_wall=false,open=true,powered=false": { "model": "mwccf:%s_fence_gate_open", "y": 180, "uvlock": true },
  "facing=east,in_wall=false,open=true,powered=false": { "model": "mwccf:%s_fence_gate_open", "y": 270, "uvlock": true },
  "facing=south,in_wall=true,open=false,powered=false": { "model": "mwccf:%s_wall_gate" },
  "facing=west,in_wall=true,open=false,powered=false": { "model": "mwccf:%s_wall_gate", "y": 90, "uvlock": true },
  "facing=north,in_wall=true,open=false,powered=false": { "model": "mwccf:%s_wall_gate", "y": 180, "uvlock": true },
  "facing=east,in_wall=true,open=false,powered=false": { "model": "mwccf:%s_wall_gate", "y": 270, "uvlock": true },
  "facing=south,in_wall=true,open=true,powered=false": { "model": "mwccf:%s_wall_gate_open" },
  "facing=west,in_wall=true,open=true,powered=false": { "model": "mwccf:%s_wall_gate_open", "y": 90, "uvlock": true },
  "facing=north,in_wall=true,open=true,powered=false": { "model": "mwccf:%s_wall_gate_open", "y": 180, "uvlock": true },
  "facing=east,in_wall=true,open=true,powered=false": { "model": "mwccf:%s_wall_gate_open", "y": 270, "uvlock": true },
  "facing=south,in_wall=false,open=false,powered=true": { "model": "mwccf:%s_fence_gate" },
  "facing=west,in_wall=false,open=false,powered=true": { "model": "mwccf:%s_fence_gate", "y": 90, "uvlock": true },
  "facing=north,in_wall=false,open=false,powered=true": { "model": "mwccf:%s_fence_gate", "y": 180, "uvlock": true },
  "facing=east,in_wall=false,open=false,powered=true": { "model": "mwccf:%s_fence_gate", "y": 270, "uvlock": true },
  "facing=south,in_wall=false,open=true,powered=true": { "model": "mwccf:%s_fence_gate_open" },
  "facing=west,in_wall=false,open=true,powered=true": { "model": "mwccf:%s_fence_gate_open", "y": 90, "uvlock": true },
  "facing=north,in_wall=false,open=true,powered=true": { "model": "mwccf:%s_fence_gate_open", "y": 180, "uvlock": true },
  "facing=east,in_wall=false,open=true,powered=true": { "model": "mwccf:%s_fence_gate_open", "y": 270, "uvlock": true },
  "facing=south,in_wall=true,open=false,powered=true": { "model": "mwccf:%s_wall_gate" },
  "facing=west,in_wall=true,open=false,powered=true": { "model": "mwccf:%s_wall_gate", "y": 90, "uvlock": true },
  "facing=north,in_wall=true,open=false,powered=true": { "model": "mwccf:%s_wall_gate", "y": 180, "uvlock": true },
  "facing=east,in_wall=true,open=false,powered=true": { "model": "mwccf:%s_wall_gate", "y": 270, "uvlock": true },
  "facing=south,in_wall=true,open=true,powered=true": { "model": "mwccf:%s_wall_gate_open" },
  "facing=west,in_wall=true,open=true,powered=true": { "model": "mwccf:%s_wall_gate_open", "y": 90, "uvlock": true },
  "facing=north,in_wall=true,open=true,powered=true": { "model": "mwccf:%s_wall_gate_open", "y": 180, "uvlock": true },
  "facing=east,in_wall=true,open=true,powered=true": { "model": "mwccf:%s_wall_gate_open", "y": 270, "uvlock": true }
} }"""
MODEL_FENCE_GATE = """{ "parent": "block/fence_gate_closed", "textures": { "texture": "mwccf:blocks/%s" } }"""
MODEL_FENCE_GATE_OPEN = """{ "parent": "block/fence_gate_open", "textures": { "texture": "mwccf:blocks/%s" } }"""
MODEL_WALL_GATE = """{ "parent": "block/wall_gate_closed", "textures": { "texture": "mwccf:blocks/%s" } }"""
MODEL_WALL_GATE_OPEN = """{ "parent": "block/wall_gate_open", "textures": { "texture": "mwccf:blocks/%s" } }"""

base = "C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf"
os.makedirs(f"{base}/blockstates", exist_ok=True)
os.makedirs(f"{base}/models/block", exist_ok=True)
os.makedirs(f"{base}/models/item", exist_ok=True)
os.makedirs(f"{base}/recipes", exist_ok=True)

def write(path, data):
    with open(path, "w") as f:
        f.write(data)

def gen_recipes(wood_name):
    # Log -> Planks
    write(f"{base}/recipes/{wood_name}_planks_from_log.json", json.dumps({
        "type": "minecraft:crafting_shapeless",
        "ingredients": [ { "item": f"mwccf:{wood_name}_log" } ],
        "result": { "item": f"mwccf:{wood_name}_planks", "count": 4 }
    }, indent=2))
    
    # Stripped Log -> Planks
    write(f"{base}/recipes/{wood_name}_planks_from_stripped.json", json.dumps({
        "type": "minecraft:crafting_shapeless",
        "ingredients": [ { "item": f"mwccf:stripped_{wood_name}_log" } ],
        "result": { "item": f"mwccf:{wood_name}_planks", "count": 4 }
    }, indent=2))
    
    # Wood -> Planks
    write(f"{base}/recipes/{wood_name}_planks_from_wood.json", json.dumps({
        "type": "minecraft:crafting_shapeless",
        "ingredients": [ { "item": f"mwccf:{wood_name}_wood" } ],
        "result": { "item": f"mwccf:{wood_name}_planks", "count": 4 }
    }, indent=2))

    # Planks -> Stairs
    write(f"{base}/recipes/{wood_name}_stairs.json", json.dumps({
        "type": "minecraft:crafting_shaped",
        "pattern": [ "P  ", "PP ", "PPP" ],
        "key": { "P": { "item": f"mwccf:{wood_name}_planks" } },
        "result": { "item": f"mwccf:{wood_name}_stairs", "count": 4 }
    }, indent=2))

    # Planks -> Slab
    write(f"{base}/recipes/{wood_name}_slab.json", json.dumps({
        "type": "minecraft:crafting_shaped",
        "pattern": [ "PPP" ],
        "key": { "P": { "item": f"mwccf:{wood_name}_planks" } },
        "result": { "item": f"mwccf:{wood_name}_slab", "count": 6 }
    }, indent=2))

    # Planks + Sticks -> Fence
    write(f"{base}/recipes/{wood_name}_fence.json", json.dumps({
        "type": "minecraft:crafting_shaped",
        "pattern": [ "P/P", "P/P" ],
        "key": { "P": { "item": f"mwccf:{wood_name}_planks" }, "/": { "item": "minecraft:stick" } },
        "result": { "item": f"mwccf:{wood_name}_fence", "count": 3 }
    }, indent=2))

    # Planks + Sticks -> Fence Gate
    write(f"{base}/recipes/{wood_name}_fence_gate.json", json.dumps({
        "type": "minecraft:crafting_shaped",
        "pattern": [ "/P/", "/P/" ],
        "key": { "P": { "item": f"mwccf:{wood_name}_planks" }, "/": { "item": "minecraft:stick" } },
        "result": { "item": f"mwccf:{wood_name}_fence_gate", "count": 1 }
    }, indent=2))

    # Wood recipe (4 logs -> 3 woods)
    write(f"{base}/recipes/{wood_name}_wood.json", json.dumps({
        "type": "minecraft:crafting_shaped",
        "pattern": [ "LL", "LL" ],
        "key": { "L": { "item": f"mwccf:{wood_name}_log" } },
        "result": { "item": f"mwccf:{wood_name}_wood", "count": 3 }
    }, indent=2))


with open('C:/Users/reizv/Documents/mwccf/src/main/java/efw/blocks/OtbwgBlocks.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Extract planks (to know the wood types)
planks_list = re.findall(r'makePlanks\("([a-z0-9_]+)_planks"\)', content)
for w in planks_list:
    gen_recipes(w)
    planks_tex = f"{w}/planks"
    
    # Slab
    write(f"{base}/blockstates/{w}_slab.json", BLOCKSTATE_SLAB % (w, w))
    write(f"{base}/models/block/{w}_slab_bottom.json", MODEL_SLAB_BOTTOM % (planks_tex, planks_tex, planks_tex))
    write(f"{base}/models/block/{w}_slab_top.json", MODEL_SLAB_TOP % (planks_tex, planks_tex, planks_tex))
    write(f"{base}/models/item/{w}_slab.json", ITEM_CUBE % f"{w}_slab_bottom")
    
    # Stairs
    v = (w,)*40
    write(f"{base}/blockstates/{w}_stairs.json", BLOCKSTATE_STAIRS % v)
    write(f"{base}/models/block/{w}_stairs.json", MODEL_STAIRS % (planks_tex, planks_tex, planks_tex))
    write(f"{base}/models/block/{w}_stairs_inner.json", MODEL_STAIRS_INNER % (planks_tex, planks_tex, planks_tex))
    write(f"{base}/models/block/{w}_stairs_outer.json", MODEL_STAIRS_OUTER % (planks_tex, planks_tex, planks_tex))
    write(f"{base}/models/item/{w}_stairs.json", ITEM_CUBE % f"{w}_stairs")

    # Fence
    write(f"{base}/blockstates/{w}_fence.json", BLOCKSTATE_FENCE % (w, w, w, w, w))
    write(f"{base}/models/block/{w}_fence_post.json", MODEL_FENCE_POST % planks_tex)
    write(f"{base}/models/block/{w}_fence_side.json", MODEL_FENCE_SIDE % planks_tex)
    write(f"{base}/models/item/{w}_fence.json", ITEM_FENCE % planks_tex)

    # Fence Gate
    gv = (w,)*32
    write(f"{base}/blockstates/{w}_fence_gate.json", BLOCKSTATE_FENCE_GATE % gv)
    write(f"{base}/models/block/{w}_fence_gate.json", MODEL_FENCE_GATE % planks_tex)
    write(f"{base}/models/block/{w}_fence_gate_open.json", MODEL_FENCE_GATE_OPEN % planks_tex)
    write(f"{base}/models/block/{w}_wall_gate.json", MODEL_WALL_GATE % planks_tex)
    write(f"{base}/models/block/{w}_wall_gate_open.json", MODEL_WALL_GATE_OPEN % planks_tex)
    write(f"{base}/models/item/{w}_fence_gate.json", ITEM_CUBE % f"{w}_fence_gate")

print("Generated models and recipes!")
