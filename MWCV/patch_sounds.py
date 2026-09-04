import re
import json

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

for m in missing:
    if m not in data:
        data[m] = {
            "category": "master",
            "sounds": [
                {
                    "name": "mwc:gun_click",
                    "stream": False
                }
            ]
        }

with open(sounds_path, 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=2)
print(f"Added {len(missing)} missing sounds to sounds.json")
