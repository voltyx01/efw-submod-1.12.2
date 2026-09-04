import os
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

def is_missing(snd):
    if snd in missing:
        return True
    if snd.endswith('$'):
        base = snd[:-1]
        for i in range(1, 10):
            if f"{base}{i}" in missing:
                return True
    return False

def replacer(match):
    snd = match.group(1)
    if is_missing(snd):
        return ""
    return match.group(0)

modified_files = 0
for root, dirs, files in os.walk('src/main/java/com/paneedah/mwc'):
    for file in files:
        if file.endswith('.java'):
            path = os.path.join(root, file)
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # Match .withSomethingSound("sound") or .withSomethingSounds("sound")
            # Note: handle spaces and newlines if they are chained, but usually they are on separate lines or same line
            # It replaces the method call with an empty string
            new_content = re.sub(r'\.with[A-Za-z0-9_]*Sound[s]?\s*\(\s*"([^"]+)"\s*\)', replacer, content)
            
            if new_content != content:
                # cleanup empty lines that might have been left
                lines = new_content.split('\n')
                cleaned_lines = []
                for line in lines:
                    if line.strip() == '':
                        continue
                    cleaned_lines.append(line)
                
                with open(path, 'w', encoding='utf-8') as f:
                    f.write('\n'.join(cleaned_lines) + '\n')
                modified_files += 1

print(f"Removed missing sounds from {modified_files} Java files.")
