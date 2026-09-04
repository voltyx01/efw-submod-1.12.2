import sys

path = 'src/main/java/com/paneedah/weaponlib/Tags.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content += "\n\n    public static void setMagazineRemoved(net.minecraft.item.ItemStack itemStack, boolean removed) {\n        if (itemStack.getTagCompound() == null) {\n            itemStack.setTagCompound(new net.minecraft.nbt.NBTTagCompound());\n        }\n        itemStack.getTagCompound().setBoolean(\"MagRemoved\", removed);\n    }\n\n    public static boolean isMagazineRemoved(net.minecraft.item.ItemStack itemStack) {\n        if (itemStack.getTagCompound() == null) {\n            return false;\n        }\n        return itemStack.getTagCompound().getBoolean(\"MagRemoved\");\n    }\n"

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

