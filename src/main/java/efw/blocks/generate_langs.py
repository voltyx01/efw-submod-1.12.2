import os, re

base = "C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/lang"
en_us_path = f"{base}/en_us.lang"
ru_ru_path = f"{base}/ru_ru.lang"

with open('C:/Users/reizv/Documents/mwccf/src/main/java/efw/blocks/OtbwgBlocks.java', 'r', encoding='utf-8') as f:
    content = f.read()

planks_list = re.findall(r'makePlanks\("([a-z0-9_]+)_planks"\)', content)

def format_en(w):
    return " ".join([word.capitalize() for word in w.split('_')])

# Русский словарь
RU_DICT = {
    "aspen": "Осина",
    "baobab": "Баобаб",
    "blue_enchanted": "Синее Зачарованное Дерево",
    "cika": "Цика",
    "cypress": "Кипарис",
    "ebony": "Эбеновое Дерево",
    "fir": "Пихта",
    "green_enchanted": "Зеленое Зачарованное Дерево",
    "holly": "Падуб",
    "ironwood": "Железное Дерево",
    "jacaranda": "Жакаранда",
    "mahogany": "Махагони",
    "maple": "Клен",
    "palm": "Пальма",
    "pine": "Сосна",
    "rainbow_eucalyptus": "Радужный Эвкалипт",
    "redwood": "Секвойя",
    "sakura": "Сакура",
    "skyris": "Скайрис",
    "willow": "Ива",
    "witch_hazel": "Гамамелис",
    "zelkova": "Дзельква",
    "bulbis": "Бульбис",
    "imparius": "Импариус",
    "nightshade": "Паслен",
    "sythian": "Ситиан",
    "ether": "Эфир"
}

def format_ru(w):
    return RU_DICT.get(w, format_en(w))

en_entries = []
ru_entries = []

for w in planks_list:
    en_name = format_en(w)
    ru_name = format_ru(w)
    
    en_entries.append(f"tile.otbwg.{w}_log.name={en_name} Log")
    en_entries.append(f"tile.otbwg.stripped_{w}_log.name=Stripped {en_name} Log")
    en_entries.append(f"tile.otbwg.{w}_wood.name={en_name} Wood")
    en_entries.append(f"tile.otbwg.{w}_planks.name={en_name} Planks")
    en_entries.append(f"tile.otbwg.{w}_leaves.name={en_name} Leaves")
    en_entries.append(f"tile.otbwg.{w}_stairs.name={en_name} Stairs")
    en_entries.append(f"tile.otbwg.{w}_slab.name={en_name} Slab")
    en_entries.append(f"tile.otbwg.{w}_fence.name={en_name} Fence")
    en_entries.append(f"tile.otbwg.{w}_fence_gate.name={en_name} Fence Gate")
    
    ru_entries.append(f"tile.otbwg.{w}_log.name=Бревно: {ru_name}")
    ru_entries.append(f"tile.otbwg.stripped_{w}_log.name=Обтесанное бревно: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_wood.name=Древесина: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_planks.name=Доски: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_leaves.name=Листва: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_stairs.name=Ступеньки: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_slab.name=Плита: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_fence.name=Забор: {ru_name}")
    ru_entries.append(f"tile.otbwg.{w}_fence_gate.name=Калитка: {ru_name}")

with open(en_us_path, "a", encoding="utf-8") as f:
    f.write("\n\n# OTBWG Woods\n" + "\n".join(en_entries))

with open(ru_ru_path, "a", encoding="utf-8") as f:
    f.write("\n\n# OTBWG Woods\n" + "\n".join(ru_entries))

print("Lang files updated!")
