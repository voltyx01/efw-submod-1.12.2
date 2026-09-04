import re

path = r"c:\Users\reizv\Documents\mwccf\src\main\java\efw\AnimationTickHandler.java"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

# Replace local player check in onRenderPlayerPre
content = content.replace("if (player != Minecraft.getMinecraft().player)\n            return;", "if (!player.world.isRemote)\n            return;")

# Replace local player check in onPlayerTick
content = content.replace("if (event.phase != TickEvent.Phase.END || event.player != net.minecraft.client.Minecraft.getMinecraft().player)\n            return;", "if (event.phase != TickEvent.Phase.END || !event.player.world.isRemote)\n            return;")
content = content.replace("if (event.phase != TickEvent.Phase.END || event.player != net.minecraft.client.Minecraft.getMinecraft().player)", "if (event.phase != TickEvent.Phase.END || !event.player.world.isRemote)")

# Render overrides logic
content = content.replace("prevWeaponHoldWeight +", "prevWeaponHoldWeightMap.getOrDefault(player, 0.0f) +")
content = content.replace("(weaponHoldWeight - prevWeaponHoldWeight)", "(weaponHoldWeightMap.getOrDefault(player, 0.0f) - prevWeaponHoldWeightMap.getOrDefault(player, 0.0f))")
content = content.replace("storedRenderYawOffset =", "storedRenderYawOffsetMap.put(player, ")
content = content.replace("storedPrevRenderYawOffset =", "storedPrevRenderYawOffsetMap.put(player, ")
content = content.replace("storedRotationYawHead =", "storedRotationYawHeadMap.put(player, ")
content = content.replace("storedPrevRotationYawHead =", "storedPrevRotationYawHeadMap.put(player, ")

content = content.replace("player.renderYawOffset;", "player.renderYawOffset);")
content = content.replace("player.prevRenderYawOffset;", "player.prevRenderYawOffset);")
content = content.replace("player.rotationYawHead;", "player.rotationYawHead);")
content = content.replace("player.prevRotationYawHead;", "player.prevRotationYawHead);")

content = content.replace("didOverrideYaw = true;", "overridenYawPlayers.add(player);")
content = content.replace("if (didOverrideYaw && player == Minecraft.getMinecraft().player) {", "if (overridenYawPlayers.contains(player)) {")
content = content.replace("player.renderYawOffset = storedRenderYawOffset;", "player.renderYawOffset = storedRenderYawOffsetMap.getOrDefault(player, player.renderYawOffset);")
content = content.replace("player.prevRenderYawOffset = storedPrevRenderYawOffset;", "player.prevRenderYawOffset = storedPrevRenderYawOffsetMap.getOrDefault(player, player.prevRenderYawOffset);")
content = content.replace("player.rotationYawHead = storedRotationYawHead;", "player.rotationYawHead = storedRotationYawHeadMap.getOrDefault(player, player.rotationYawHead);")
content = content.replace("player.prevRotationYawHead = storedPrevRotationYawHead;", "player.prevRotationYawHead = storedPrevRotationYawHeadMap.getOrDefault(player, player.prevRotationYawHead);")
content = content.replace("didOverrideYaw = false;", "overridenYawPlayers.remove(player);")

# Tick variables
content = re.sub(r'lastHeldItem([^M])', r'lastHeldItemMap.getOrDefault(player, null)\1', content)
content = re.sub(r'lastHeldItemMap\.getOrDefault\(player, null\) =', r'lastHeldItemMap.put(player,', content)
content = content.replace("lastHeldItemMap.put(player, currentItem;", "lastHeldItemMap.put(player, currentItem);")

content = re.sub(r'wasReloadingAnimPlaying([^M])', r'wasReloadingAnimPlayingMap.getOrDefault(player, false)\1', content)
content = re.sub(r'wasReloadingAnimPlayingMap\.getOrDefault\(player, false\) =', r'wasReloadingAnimPlayingMap.put(player,', content)
content = content.replace("wasReloadingAnimPlayingMap.put(player, false;", "wasReloadingAnimPlayingMap.put(player, false);")
content = content.replace("wasReloadingAnimPlayingMap.put(player, currentlyReloadingAnim;", "wasReloadingAnimPlayingMap.put(player, currentlyReloadingAnim);")

content = re.sub(r'postReloadTimer([^M])', r'postReloadTimerMap.getOrDefault(player, 0)\1', content)
content = re.sub(r'postReloadTimerMap\.getOrDefault\(player, 0\) =', r'postReloadTimerMap.put(player,', content)
content = content.replace("postReloadTimerMap.put(player, 0;", "postReloadTimerMap.put(player, 0);")
content = content.replace("postReloadTimerMap.put(player, 10;", "postReloadTimerMap.put(player, 10);")
content = content.replace("postReloadTimerMap.getOrDefault(player, 0)--;", "postReloadTimerMap.put(player, postReloadTimerMap.getOrDefault(player, 0) - 1);")

content = re.sub(r'ignoreReloadState([^M])', r'ignoreReloadStateMap.getOrDefault(player, false)\1', content)
content = re.sub(r'ignoreReloadStateMap\.getOrDefault\(player, false\) =', r'ignoreReloadStateMap.put(player,', content)
content = content.replace("ignoreReloadStateMap.put(player, false;", "ignoreReloadStateMap.put(player, false);")
content = content.replace("ignoreReloadStateMap.put(player, true;", "ignoreReloadStateMap.put(player, true);")

content = re.sub(r'lieFireTicks([^M])', r'lieFireTicksMap.getOrDefault(player, 0)\1', content)
content = re.sub(r'lieFireTicksMap\.getOrDefault\(player, 0\) =', r'lieFireTicksMap.put(player,', content)
content = content.replace("lieFireTicksMap.put(player, 20;", "lieFireTicksMap.put(player, 20);")
content = content.replace("lieFireTicksMap.getOrDefault(player, 0)--;", "lieFireTicksMap.put(player, lieFireTicksMap.getOrDefault(player, 0) - 1);")

content = re.sub(r'recentlyHitBlockTicks([^M])', r'recentlyHitBlockTicksMap.getOrDefault(player, 0)\1', content)
content = re.sub(r'recentlyHitBlockTicksMap\.getOrDefault\(player, 0\) =', r'recentlyHitBlockTicksMap.put(player,', content)
content = content.replace("recentlyHitBlockTicksMap.put(player, 15;", "recentlyHitBlockTicksMap.put(player, 15);")
content = content.replace("recentlyHitBlockTicksMap.getOrDefault(player, 0)--;", "recentlyHitBlockTicksMap.put(player, recentlyHitBlockTicksMap.getOrDefault(player, 0) - 1);")

content = re.sub(r'recentlyMinedTicks([^M])', r'recentlyMinedTicksMap.getOrDefault(player, 0)\1', content)
content = re.sub(r'recentlyMinedTicksMap\.getOrDefault\(player, 0\) =', r'recentlyMinedTicksMap.put(player,', content)
content = content.replace("recentlyMinedTicksMap.put(player, 8;", "recentlyMinedTicksMap.put(player, 8);")
content = content.replace("recentlyMinedTicksMap.getOrDefault(player, 0)--;", "recentlyMinedTicksMap.put(player, recentlyMinedTicksMap.getOrDefault(player, 0) - 1);")

content = re.sub(r'ticksSinceLastSwing([^M])', r'ticksSinceLastSwingMap.getOrDefault(player, 100)\1', content)
content = re.sub(r'ticksSinceLastSwingMap\.getOrDefault\(player, 100\) =', r'ticksSinceLastSwingMap.put(player,', content)
content = content.replace("ticksSinceLastSwingMap.put(player, 0;", "ticksSinceLastSwingMap.put(player, 0);")
content = content.replace("ticksSinceLastSwingMap.getOrDefault(player, 100)++;", "ticksSinceLastSwingMap.put(player, ticksSinceLastSwingMap.getOrDefault(player, 100) + 1);")

content = re.sub(r'alternateSwordAnim([^M])', r'alternateSwordAnimMap.getOrDefault(player, false)\1', content)
content = re.sub(r'alternateSwordAnimMap\.getOrDefault\(player, false\) =', r'alternateSwordAnimMap.put(player,', content)
content = content.replace("alternateSwordAnimMap.put(player, !alternateSwordAnimMap.getOrDefault(player, false);", "alternateSwordAnimMap.put(player, !alternateSwordAnimMap.getOrDefault(player, false));")

content = re.sub(r'lastMiningToolAnim([^M])', r'lastMiningToolAnimMap.getOrDefault(player, null)\1', content)
content = re.sub(r'lastMiningToolAnimMap\.getOrDefault\(player, null\) =', r'lastMiningToolAnimMap.put(player,', content)
content = content.replace("lastMiningToolAnimMap.put(player, actionAnim;", "lastMiningToolAnimMap.put(player, actionAnim);")

content = re.sub(r'airTicks([^M])', r'airTicksMap.getOrDefault(player, 0)\1', content)
content = re.sub(r'airTicksMap\.getOrDefault\(player, 0\) =', r'airTicksMap.put(player,', content)
content = content.replace("airTicksMap.put(player, 0;", "airTicksMap.put(player, 0);")
content = content.replace("airTicksMap.getOrDefault(player, 0)++;", "airTicksMap.put(player, airTicksMap.getOrDefault(player, 0) + 1);")

content = re.sub(r'outOfWaterTicks([^M])', r'outOfWaterTicksMap.getOrDefault(player, 100)\1', content)
content = re.sub(r'outOfWaterTicksMap\.getOrDefault\(player, 100\) =', r'outOfWaterTicksMap.put(player,', content)
content = content.replace("outOfWaterTicksMap.put(player, 0;", "outOfWaterTicksMap.put(player, 0);")
content = content.replace("outOfWaterTicksMap.getOrDefault(player, 100)++;", "outOfWaterTicksMap.put(player, outOfWaterTicksMap.getOrDefault(player, 100) + 1);")

content = re.sub(r'prevWeaponHoldWeight([^M])', r'prevWeaponHoldWeightMap.getOrDefault(player, 0.0f)\1', content)
content = re.sub(r'prevWeaponHoldWeightMap\.getOrDefault\(player, 0.0f\) =', r'prevWeaponHoldWeightMap.put(player,', content)
# It uses weaponHoldWeight map inside the assignment so we have to replace weaponHoldWeight first or just be careful
content = re.sub(r'weaponHoldWeight([^M])', r'weaponHoldWeightMap.getOrDefault(player, 0.0f)\1', content)
content = re.sub(r'weaponHoldWeightMap\.getOrDefault\(player, 0.0f\) =', r'weaponHoldWeightMap.put(player,', content)

# Fix up the assignments to weaponHoldWeight/prev
content = content.replace("prevWeaponHoldWeightMap.put(player, weaponHoldWeightMap.getOrDefault(player, 0.0f);", "prevWeaponHoldWeightMap.put(player, weaponHoldWeightMap.getOrDefault(player, 0.0f));")
content = content.replace("weaponHoldWeightMap.put(player, Math.min(1.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) + 0.1f);", "weaponHoldWeightMap.put(player, Math.min(1.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) + 0.1f));")
content = content.replace("weaponHoldWeightMap.put(player, Math.max(0.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) - 0.1f);", "weaponHoldWeightMap.put(player, Math.max(0.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) - 0.1f));")

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
