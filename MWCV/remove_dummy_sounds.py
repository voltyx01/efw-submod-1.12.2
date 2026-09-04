import json

with open('get_sounds.py', 'r', encoding='utf-8') as f:
    # Just to get the missing set logic again
    pass

import re
missing = set()
try:
    with open('ошибки.log', 'r', encoding='utf-8') as f:
        for line in f:
            m = re.search(r'Missing sound for event: mwc:(.+)', line)
            if m:
                missing.add(m.group(1))
except Exception as e:
    with open('ошибки.log', 'r', encoding='utf-16') as f:
        for line in f:
            m = re.search(r'Missing sound for event: mwc:(.+)', line)
            if m:
                missing.add(m.group(1))

sounds_path = 'src/main/resources/assets/mwc/sounds.json'
with open(sounds_path, 'r', encoding='utf-8') as f:
    data = json.load(f)

removed = 0
for m in missing:
    if m in data:
        # verify it was our dummy entry
        if data[m].get("sounds") and data[m]["sounds"][0].get("name") == "mwc:gun_click":
            del data[m]
            removed += 1

with open(sounds_path, 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=2)
print(f"Removed {removed} dummy sounds from sounds.json")
