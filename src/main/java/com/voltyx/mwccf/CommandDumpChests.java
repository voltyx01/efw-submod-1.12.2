package com.voltyx.mwccf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommandDumpChests extends CommandBase {

    @Override
    public String getName() {
        return "dumpchests";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("dumpchest", "exportchests");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/dumpchests [filename.txt]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String filename = args.length > 0 ? args[0].trim() : "chest_items_dump.txt";
        if (!filename.toLowerCase(Locale.ROOT).endsWith(".txt")) {
            filename += ".txt";
        }

        Entity entity = sender.getCommandSenderEntity();
        BlockPos center = entity != null ? new BlockPos(entity.posX, entity.posY, entity.posZ) : sender.getPosition();
        World world = sender.getEntityWorld();

        // In integrated server, ensure we read from WorldServer if available
        if (world.isRemote && FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
            WorldServer ws = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(world.provider.getDimension());
            if (ws != null) {
                world = ws;
            }
        }

        int radius = 10;
        int rSq = radius * radius;
        Set<TileEntity> scannedTEs = new HashSet<>();
        List<ItemStack> foundStacks = new ArrayList<>();
        int chestCount = 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz > rSq) {
                        continue;
                    }
                    BlockPos pos = center.add(dx, dy, dz);
                    TileEntity te = world.getTileEntity(pos);
                    if (te == null || scannedTEs.contains(te)) {
                        continue;
                    }

                    boolean isContainer = false;
                    if (te instanceof IInventory) {
                        isContainer = true;
                        scannedTEs.add(te);
                        IInventory inv = (IInventory) te;
                        for (int i = 0; i < inv.getSizeInventory(); i++) {
                            ItemStack s = inv.getStackInSlot(i);
                            if (s != null && !s.isEmpty()) {
                                foundStacks.add(s.copy());
                            }
                        }
                    } else if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                        if (handler != null) {
                            isContainer = true;
                            scannedTEs.add(te);
                            for (int i = 0; i < handler.getSlots(); i++) {
                                ItemStack s = handler.getStackInSlot(i);
                                if (s != null && !s.isEmpty()) {
                                    foundStacks.add(s.copy());
                                }
                            }
                        }
                    }

                    if (isContainer) {
                        chestCount++;
                    }
                }
            }
        }

        if (foundStacks.isEmpty()) {
            sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "[MWCCF] В радиусе 10 блоков не найдено предметов в сундуках (найдено контейнеров: " + chestCount + ")."));
            return;
        }

        // Load RU and EN translations from all available resources
        Map<String, String> langRU = loadLanguageMap(Arrays.asList("ru_ru", "ru_RU"));
        Map<String, String> langEN = loadLanguageMap(Arrays.asList("en_us", "en_US"));

        // Deduplicate items by ID
        Map<String, ItemEntry> uniqueMap = new LinkedHashMap<>();

        for (ItemStack stack : foundStacks) {
            Item item = stack.getItem();
            if (item == null) continue;
            ResourceLocation reg = item.getRegistryName();
            if (reg == null) continue;

            String id = reg.toString();
            if (!stack.isItemStackDamageable() && stack.getMetadata() != 0) {
                id += ":" + stack.getMetadata();
            }

            if (!uniqueMap.containsKey(id)) {
                String unlocalized = stack.getTranslationKey();
                String keyName = unlocalized + ".name";
                String regKey = "item." + reg.toString() + ".name";
                String tileKey = "tile." + reg.toString() + ".name";
                String regPathKey = "item." + reg.getPath() + ".name";
                String tilePathKey = "tile." + reg.getPath() + ".name";

                String ru = findTranslation(langRU, keyName, unlocalized, regKey, tileKey, regPathKey, tilePathKey);
                String en = findTranslation(langEN, keyName, unlocalized, regKey, tileKey, regPathKey, tilePathKey);

                if ("none".equals(en)) {
                    if (net.minecraft.util.text.translation.I18n.canTranslate(keyName)) {
                        en = net.minecraft.util.text.translation.I18n.translateToLocal(keyName).trim();
                    } else if (net.minecraft.util.text.translation.I18n.canTranslate(unlocalized)) {
                        en = net.minecraft.util.text.translation.I18n.translateToLocal(unlocalized).trim();
                    }
                }

                // Tooltips extraction
                String tipRU = findTooltipFromLang(langRU, stack);
                String tipEN = findTooltipFromLang(langEN, stack);

                // In-game live tooltip lines (e.g. lore, enchantments, stats, charge, custom lines)
                List<String> inGameLines = new ArrayList<>();
                try {
                    net.minecraft.entity.player.EntityPlayer player = null;
                    if (FMLCommonHandler.instance().getSide().isClient()) {
                        player = Minecraft.getMinecraft().player;
                    }
                    if (player == null && sender.getCommandSenderEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
                        player = (net.minecraft.entity.player.EntityPlayer) sender.getCommandSenderEntity();
                    }
                    List<String> raw = stack.getTooltip(player, net.minecraft.client.util.ITooltipFlag.TooltipFlags.NORMAL);
                    for (int i = 1; i < raw.size(); i++) {
                        String clean = TextFormatting.getTextWithoutFormattingCodes(raw.get(i)).trim();
                        if (!clean.isEmpty()) {
                            inGameLines.add(clean);
                        }
                    }
                } catch (Throwable ignored) {}

                if (!inGameLines.isEmpty()) {
                    String inGameStr = String.join(" | ", inGameLines);
                    boolean isClientRU = true;
                    try {
                        if (FMLCommonHandler.instance().getSide().isClient()) {
                            String code = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
                            isClientRU = code.toLowerCase(Locale.ROOT).startsWith("ru");
                        }
                    } catch (Throwable ignored) {}

                    if (isClientRU) {
                        if ("none".equals(tipRU) || inGameStr.length() > tipRU.length()) {
                            tipRU = inGameStr;
                        }
                    } else {
                        if ("none".equals(tipEN) || inGameStr.length() > tipEN.length()) {
                            tipEN = inGameStr;
                        }
                    }
                }

                // Protect column splitting by replacing inner " - " with " — "
                if (tipRU != null) tipRU = tipRU.replace(" - ", " — ");
                if (tipEN != null) tipEN = tipEN.replace(" - ", " — ");

                uniqueMap.put(id, new ItemEntry(id, ru, en, tipRU, tipEN));
            }
        }

        File outFile = new File(".", filename);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8))) {
            for (ItemEntry entry : uniqueMap.values()) {
                writer.write(entry.id + " - " + entry.nameRU + " - " + entry.nameEN + " - " + entry.tooltipRU + " - " + entry.tooltipEN);
                writer.newLine();
            }
        } catch (IOException e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "[MWCCF] Ошибка при сохранении файла: " + e.getMessage()));
            return;
        }

        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "[MWCCF] Найдено сундуков: " + chestCount + ", уникальных предметов: " + uniqueMap.size()));
        try {
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "[MWCCF] Файл создан: " + outFile.getCanonicalPath()));
        } catch (IOException e) {
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "[MWCCF] Файл создан: " + outFile.getAbsolutePath()));
        }
    }

    private static String findTooltipFromLang(Map<String, String> langMap, ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation reg = item.getRegistryName();
        String unlocalized = stack.getTranslationKey();

        List<String> foundLines = new ArrayList<>();
        List<String> baseKeys = new ArrayList<>();
        baseKeys.add(unlocalized);
        baseKeys.add(unlocalized + ".desc");
        baseKeys.add(unlocalized + ".description");
        baseKeys.add(unlocalized + ".tooltip");
        baseKeys.add(unlocalized + ".tip");
        baseKeys.add(unlocalized + ".info");

        if (reg != null) {
            baseKeys.add("tooltip." + reg.toString() + ".desc");
            baseKeys.add("tooltip." + reg.getPath() + ".desc");
            baseKeys.add("tooltip." + reg.toString());
            baseKeys.add("tooltip." + reg.getPath());
            baseKeys.add("item." + reg.toString() + ".desc");
            baseKeys.add("item." + reg.getPath() + ".desc");
            baseKeys.add("tile." + reg.toString() + ".desc");
            baseKeys.add("tile." + reg.getPath() + ".desc");
        }

        if (unlocalized.startsWith("item.")) {
            baseKeys.add("tooltip." + unlocalized.substring(5) + ".desc");
            baseKeys.add("tooltip." + unlocalized.substring(5));
        }

        for (String bk : baseKeys) {
            if (!bk.equals(unlocalized)) {
                if (langMap.containsKey(bk)) {
                    String val = langMap.get(bk).trim();
                    if (!val.isEmpty() && !val.equals(bk) && !foundLines.contains(val)) {
                        foundLines.add(val);
                    }
                }
            }

            for (int i = 0; i <= 10; i++) {
                String[] suffixes = { "." + i, "_" + i, ".desc." + i, ".desc" + i, ".line" + i };
                for (String sfx : suffixes) {
                    String subKey = bk + sfx;
                    if (langMap.containsKey(subKey)) {
                        String val = langMap.get(subKey).trim();
                        if (!val.isEmpty() && !val.equals(subKey) && !foundLines.contains(val)) {
                            foundLines.add(val);
                        }
                    }
                }
            }
        }

        if (foundLines.isEmpty()) {
            return "none";
        }
        return String.join(" | ", foundLines);
    }

    private static String findTranslation(Map<String, String> langMap, String... keys) {
        for (String k : keys) {
            if (k == null) continue;
            String val = langMap.get(k);
            if (val != null) {
                val = val.trim();
                if (!val.isEmpty() && !val.equals(k)) {
                    return val;
                }
            }
        }
        return "none";
    }

    private static Map<String, String> loadLanguageMap(List<String> langCodes) {
        Map<String, String> map = new HashMap<>();

        // 1. Try loading from Minecraft client ResourceManager if running on client
        try {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
                if (rm != null) {
                    for (String domain : rm.getResourceDomains()) {
                        for (String code : langCodes) {
                            // .lang
                            try {
                                List<IResource> resources = rm.getAllResources(new ResourceLocation(domain, "lang/" + code + ".lang"));
                                for (IResource res : resources) {
                                    parseLangStream(res.getInputStream(), map);
                                }
                            } catch (Exception ignored) {}

                            // .json
                            try {
                                List<IResource> resources = rm.getAllResources(new ResourceLocation(domain, "lang/" + code + ".json"));
                                for (IResource res : resources) {
                                    parseJsonStream(res.getInputStream(), map);
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}

        // 2. Scan active ModContainers sources as fallback/addition
        try {
            for (ModContainer mod : Loader.instance().getModList()) {
                File src = mod.getSource();
                if (src == null || !src.exists()) continue;

                if (src.isFile() && (src.getName().endsWith(".jar") || src.getName().endsWith(".zip"))) {
                    try (ZipFile zip = new ZipFile(src)) {
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry ze = entries.nextElement();
                            String name = ze.getName();
                            for (String code : langCodes) {
                                if (name.startsWith("assets/") && (name.endsWith("/lang/" + code + ".lang") || name.endsWith("/lang/" + code.toLowerCase(Locale.ROOT) + ".lang"))) {
                                    try (InputStream is = zip.getInputStream(ze)) {
                                        parseLangStream(is, map);
                                    } catch (Exception ignored) {}
                                } else if (name.startsWith("assets/") && (name.endsWith("/lang/" + code + ".json") || name.endsWith("/lang/" + code.toLowerCase(Locale.ROOT) + ".json"))) {
                                    try (InputStream is = zip.getInputStream(ze)) {
                                        parseJsonStream(is, map);
                                    } catch (Exception ignored) {}
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                } else if (src.isDirectory()) {
                    File assetsDir = new File(src, "assets");
                    if (assetsDir.isDirectory()) {
                        File[] domains = assetsDir.listFiles();
                        if (domains != null) {
                            for (File d : domains) {
                                if (!d.isDirectory()) continue;
                                File lDir = new File(d, "lang");
                                if (!lDir.isDirectory()) continue;
                                for (String code : langCodes) {
                                    File lf = new File(lDir, code + ".lang");
                                    if (lf.isFile()) {
                                        try (InputStream is = new FileInputStream(lf)) {
                                            parseLangStream(is, map);
                                        } catch (Exception ignored) {}
                                    }
                                    File jf = new File(lDir, code + ".json");
                                    if (jf.isFile()) {
                                        try (InputStream is = new FileInputStream(jf)) {
                                            parseJsonStream(is, map);
                                        } catch (Exception ignored) {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}

        return map;
    }

    private static void parseLangStream(InputStream is, Map<String, String> out) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int eq = line.indexOf('=');
                if (eq > 0) {
                    String k = line.substring(0, eq).trim();
                    String v = line.substring(eq + 1).trim();
                    if (!k.isEmpty() && !v.isEmpty()) {
                        out.put(k, v);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private static void parseJsonStream(InputStream is, Map<String, String> out) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) {
                    out.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        } catch (Exception ignored) {}
    }

    private static class ItemEntry {
        final String id;
        final String nameRU;
        final String nameEN;
        final String tooltipRU;
        final String tooltipEN;

        ItemEntry(String id, String nameRU, String nameEN, String tooltipRU, String tooltipEN) {
            this.id = id;
            this.nameRU = nameRU;
            this.nameEN = nameEN;
            this.tooltipRU = tooltipRU != null && !tooltipRU.isEmpty() ? tooltipRU : "none";
            this.tooltipEN = tooltipEN != null && !tooltipEN.isEmpty() ? tooltipEN : "none";
        }
    }
}
