import sys

def clean(file):
    with open(file, "r", encoding="utf-8") as f:
        content = f.read()
    
    idx = content.find("\\n\\n# OTBWG Woods\\n")
    if idx != -1:
        content = content[:idx]
    
    with open(file, "w", encoding="utf-8") as f:
        f.write(content)

base = "C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/lang"
clean(f"{base}/en_us.lang")
clean(f"{base}/ru_ru.lang")
