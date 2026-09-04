import json, os

files = [
    "C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/models/block/barrel_cactus.json",
    "C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/models/block/flowering_barrel_cactus.json",
    "C:/Users/reizv/Documents/mwccf/src/main/resources/assets/mwccf/models/block/carved_barrel_cactus.json"
]

for file in files:
    with open(file, "r", encoding="utf-8") as f:
        data = json.load(f)
    
    for element in data.get("elements", []):
        name = element.get("name", "")
        if name == "Barrel_Cactus_SpikeNorthFace":
            element["from"] = [0, 1, -0.01]
            element["to"] = [16, 16, -0.01]
        elif name == "Barrel_Cactus_SpikeSouthFace":
            element["from"] = [0, 1, 16.01]
            element["to"] = [16, 16, 16.01]
        elif name == "Barrel_Cactus_SpikeWestFace":
            element["from"] = [-0.01, 1, 0]
            element["to"] = [-0.01, 16, 16]
        elif name == "Barrel_Cactus_SpikeEastFace":
            element["from"] = [16.01, 1, 0]
            element["to"] = [16.01, 16, 16]
            
    with open(file, "w", encoding="utf-8") as f:
        json.dump(data, f, indent="\t")

print("Patched cactus models!")
