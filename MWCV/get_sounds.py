import re
import sys

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

for m in sorted(missing):
    print(m)
