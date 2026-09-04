import os
import glob
import re

disabled_factories = [
    'AS50', 'BrenMkII', 'BrowningAuto5', 'BrowningHiPower', 'Chainsaw', 'ChiappaRhino', 'DP28', 'DSR1', 'G2Contender', 'G43Gewehr', 'GL06', 'HS12', 'Kar98K', 'Kedr', 'KragJorgensen', 'KS23', 'L96A1', 'M1014', 'M134', 'M1873', 'M1897', 'M1911', 'M1928Thompson', 'M1941Johnson', 'M1941JohnsonRifle', 'M1Carbine', 'M1Garand', 'M200Intervention', 'M202', 'M32MGL', 'M60E4', 'M712', 'M79', 'MaresLeg', 'MAS21', 'MG34', 'MG42', 'MP40', 'MP43E', 'Remington700', 'Remington870', 'RPG7', 'Saiga12', 'Spas12', 'Springfield', 'SSG08', 'STG44', 'StonerA1', 'Supernova', 'SV98', 'SW500Magnum',
    'AUG',
    'TacticalTomahawk', 'BaseballBat', 'BaseballBatNails', 'NightStick'
]

disabled_files = set()
for df in disabled_factories:
    # They could be in items/guns or items/melee
    matches = glob.glob(f'src/main/java/com/paneedah/mwc/items/**/{df}Factory.java', recursive=True)
    for m in matches:
        disabled_files.add(os.path.abspath(m))

all_java_files = [os.path.abspath(p) for p in glob.glob('src/main/java/**/*.java', recursive=True)]
active_files = [f for f in all_java_files if f not in disabled_files]

print(f"Disabled files: {len(disabled_files)}")
print(f"Active files: {len(active_files)}")

# Build a massive blob of active text to search quickly
active_text = ""
for f in active_files:
    with open(f, 'r', encoding='utf-8') as file:
        active_text += file.read().lower() + "\n"

# Extract all string literals from disabled files
disabled_strings = set()
for df in disabled_files:
    with open(df, 'r', encoding='utf-8') as file:
        content = file.read()
        strings = re.findall(r'"([^"]+)"', content)
        for s in strings:
            s_lower = s.lower()
            if len(s_lower) > 2: # Ignore very short strings
                # Strip path parts if present (e.g. mwc:textures/models/gun.png -> gun)
                basename = s_lower
                if '/' in basename:
                    basename = basename.split('/')[-1]
                if basename.endswith('.png'):
                    basename = basename[:-4]
                disabled_strings.add(basename)

print(f"Total unique strings in disabled files: {len(disabled_strings)}")

safe_to_delete_strings = set()
for s in disabled_strings:
    # If the string is NOT in the active text AT ALL
    if s not in active_text:
        safe_to_delete_strings.add(s)

print(f"Strings uniquely used by disabled files: {len(safe_to_delete_strings)}")

# Now find corresponding files
resource_dir = 'src/main/resources/assets/mwc'
to_delete = []

for root, _, files in os.walk(resource_dir):
    for file in files:
        if file.endswith('.png') or file.endswith('.obj') or file.endswith('.json') or file.endswith('.ogg'):
            file_lower = file.lower()
            stem = file_lower
            if '.' in stem:
                stem = stem[:stem.rfind('.')]
            
            # Check if this file matches any safe_to_delete_string
            if stem in safe_to_delete_strings:
                to_delete.append(os.path.join(root, file))
            elif file_lower in safe_to_delete_strings:
                to_delete.append(os.path.join(root, file))

print(f"Found {len(to_delete)} files to delete.")
with open('files_to_delete.txt', 'w', encoding='utf-8') as f:
    for fp in to_delete:
        f.write(fp + '\n')
