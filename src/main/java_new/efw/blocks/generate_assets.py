import os, re, sys

BLOCKSTATE = """{ "variants": { "normal": { "model": "mwccf:%s" } } }"""
MODEL_CUBE = """{ "parent": "block/cube_all", "textures": { "all": "mwccf:blocks/%s" } }"""
MODEL_CROSS = """{ "parent": "block/cross", "textures": { "cross": "mwccf:blocks/%s" } }"""
MODEL_LILYPAD = """{ "parent": "block/lily_pad", "textures": { "texture": "mwccf:blocks/%s" } }"""
MODEL_GRASS = """{ "parent": "block/grass", "textures": { "particle": "mwccf:blocks/%s", "bottom": "mwccf:blocks/%s_dirt", "top": "mwccf:blocks/%s_top", "side": "mwccf:blocks/%s_side", "overlay": "mwccf:blocks/%s_side_overlay" } }"""
ITEM_CUBE = """{ "parent": "mwccf:block/%s" }"""
ITEM_FLAT = """{ "parent": "item/generated", "textures": { "layer0": "mwccf:blocks/%s" } }"""

def main():
    if not os.path.exists('OtbwgBlocks.java'):
        print("Положите скрипт рядом с OtbwgBlocks.java!")
        return

    with open('OtbwgBlocks.java', 'r', encoding='utf-8') as f:
        content = f.read()

    # Вытаскиваем все зарегистрированные имена (теперь скрипт видит makeCube и makeIce!)
    names = re.findall(r'setRegistryName\("mwccf", "([a-z0-9_]+)"\)', content)
    names += re.findall(r'BlockOtbwgFlower\("([a-z0-9_]+)"\)', content)
    names += re.findall(r'BlockOtbwgLeaves\("([a-z0-9_]+)"\)', content)
    names += re.findall(r'BlockCloverPatch\("([a-z0-9_]+)"\)', content)
    names += re.findall(r'BlockOtbwgLilyPad\("([a-z0-9_]+)"\)', content)
    names += re.findall(r'makeGrass\("([a-z0-9_]+)"\)', content)
    names += re.findall(r'makeCube\("([a-z0-9_]+)"', content)
    names += re.findall(r'makeIce\("([a-z0-9_]+)"', content)
    
    blocks = list(set(names))

    base = "resources/assets/mwccf"
    os.makedirs(f"{base}/blockstates", exist_ok=True)
    os.makedirs(f"{base}/models/block", exist_ok=True)
    os.makedirs(f"{base}/models/item", exist_ok=True)

    for name in blocks:
        # Blockstate
        bs_path = f"{base}/blockstates/{name}.json"
        with open(bs_path, "w") as f: f.write(BLOCKSTATE % name)
        
        # Разбираемся, какая нужна модель
        model_content = MODEL_CUBE % name
        item_content = ITEM_CUBE % name
        
        # Если это лепестки или грибные блоки - это точно 3D КУБЫ, игнорируем их в проверках на растения
        if "petal_block" in name or "mushroom_block" in name or "mushroom_stem" in name:
            model_content = MODEL_CUBE % name
            item_content = ITEM_CUBE % name

        elif "grass" in name and "glass" not in name:
            # Трава
            clean_name = name.replace("_grass", "")
            model_content = MODEL_GRASS % (clean_name + "_dirt", clean_name, name, name, name)
            
        elif "overgrown" in name:
            # Overgrown blocks (e.g. overgrown_dacite)
            model_content = """{ "parent": "block/grass", "textures": { "particle": "mwccf:blocks/%s_top", "bottom": "mwccf:blocks/%s_bottom", "top": "mwccf:blocks/%s_top", "side": "mwccf:blocks/%s_side", "overlay": "mwccf:blocks/%s_side_overlay" } }""" % (name, name, name, name, name)

        elif any(k in name for k in ['flower', 'patch', 'bush', 'tulip', 'sage', 'amaranth', 'daffodil', 'anemone', 'bellflower', 'orchid', 'crocus', 'ivy', 'vine', 'fungi', 'rose', 'milkcap', 'blewit', 'succulent', 'snowdrops', 'cyclamen', 'scilla', 'cattail', 'puffball', 'leaves', 'cactus', 'aloe', 'fruit', 'shrub']):
            # Растения
            model_content = MODEL_CROSS % name
            item_content = ITEM_FLAT % name
            
        elif 'lily_pad' in name or 'silk' in name:
            # Кувшинки
            model_content = MODEL_LILYPAD % name
            item_content = ITEM_FLAT % name

        bm_path = f"{base}/models/block/{name}.json"
        with open(bm_path, "w") as f: f.write(model_content)
            
        im_path = f"{base}/models/item/{name}.json"
        with open(im_path, "w") as f: f.write(item_content)

    print(f"Успешно пересобраны модели для {len(blocks)} блоков!")

if __name__ == "__main__":
    main()