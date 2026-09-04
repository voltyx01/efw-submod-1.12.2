import sys

path = 'src/main/java/com/paneedah/weaponlib/Tags.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

methods = """    public static void setMagazineRemoved(net.minecraft.item.ItemStack itemStack, boolean removed) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new net.minecraft.nbt.NBTTagCompound());
        }
        itemStack.getTagCompound().setBoolean("MagRemoved", removed);
    }

    public static boolean isMagazineRemoved(net.minecraft.item.ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            return false;
        }
        return itemStack.getTagCompound().getBoolean("MagRemoved");
    }"""

if methods in content:
    content = content.replace(methods, '')

# find the last }
last_brace = content.rfind('}')
if last_brace != -1:
    content = content[:last_brace] + methods + '\n}\n'

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

