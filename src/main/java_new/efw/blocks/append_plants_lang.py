import re

with open("C:/Users/reizv/Documents/mwccf/src/main/java/efw/blocks/OtbwgBlocks.java", "r", encoding="utf-8") as f:
    content = f.read()

# Extract flowers array
match = re.search(r'String\[\] flowers = \{(.*?)\};', content, re.DOTALL)
if match:
    flowers_str = match.group(1)
    # Extract all string literals
    flowers = re.findall(r'"([^"]+)"', flowers_str)
    
    en_entries = []
    ru_entries = []
    for f in flowers:
        en_name = " ".join([word.capitalize() for word in f.split('_')])
        en_entries.append(f"tile.otbwg.{f}.name={en_name}")
        ru_entries.append(f"tile.otbwg.{f}.name={en_name}") # just fallback to english
        
    # Append missing tiny_lily_pads
    en_entries.append("tile.otbwg.tiny_lily_pads.name=Tiny Lily Pads")
    en_entries.append("tile.otbwg.flowering_tiny_lily_pads.name=Flowering Tiny Lily Pads")
    en_entries.append("tile.otbwg.water_silk.name=Water Silk")
    ru_entries.append("tile.otbwg.tiny_lily_pads.name=Крошечные кувшинки")
    ru_entries.append("tile.otbwg.flowering_tiny_lily_pads.name=Цветущие крошечные кувшинки")
    ru_entries.append("tile.otbwg.water_silk.name=Водный шелк")
        
    with open("C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/lang/en_us.lang", "a", encoding="utf-8") as f:
        f.write("\n\n# Plants\n" + "\n".join(en_entries))
        
    with open("C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/lang/ru_ru.lang", "a", encoding="utf-8") as f:
        f.write("\n\n# Plants\n" + "\n".join(ru_entries))
        
    print(f"Added {len(flowers)} plant names.")
