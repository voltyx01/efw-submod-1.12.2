import os
import re
import glob

# 1. Collect all literal strings from active Java code
java_files = glob.glob('src/main/java/**/*.java', recursive=True)
java_strings = set()

for jf in java_files:
    with open(jf, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        for line in lines:
            line = line.strip()
            if line.startswith('//'):
                continue
            # Extract strings
            strings = re.findall(r'"([^"]*)"', line)
            for s in strings:
                java_strings.add(s.lower())

# 2. Add extra derived strings (e.g., if code says "m4a1", it implies "m4a1.png" or "m4a1_reload")
# Actually, the code usually has explicit strings like "m4a1_reload".
# Let's collect all files in resources
resource_dir = 'src/main/resources/assets/mwc'

unused_files = []

# Scan textures
for root, _, files in os.walk(os.path.join(resource_dir, 'textures')):
    for file in files:
        if not file.endswith('.png'): continue
        basename = file[:-4].lower()
        # Is basename in java strings?
        # Sometimes textures are referenced with ".png"
        if basename not in java_strings and file.lower() not in java_strings:
            unused_files.append(os.path.join(root, file))

# Scan models (obj)
for root, _, files in os.walk(os.path.join(resource_dir, 'models')):
    for file in files:
        if not file.endswith('.obj') and not file.endswith('.json'): continue
        basename = file[:file.rfind('.')].lower()
        if basename not in java_strings and file.lower() not in java_strings:
            unused_files.append(os.path.join(root, file))

print(f"Found {len(unused_files)} potentially unused texture/model files.")
with open('unused_resources_report.txt', 'w', encoding='utf-8') as f:
    for uf in unused_files:
        f.write(uf + '\n')
