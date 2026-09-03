package com.voltyx.mwccf.client.loading;

/**
 * Один предмет из JSON конфига.
 * Формат JSON:
 * [
 *   {
 *     "item": "minecraft:golden_sword",
 *     "meta": 0,
 *     "description": "Найден рядом с телом солдата.",
 *     "lore": "Судя по отметинам на рукояти — не первый хозяин."
 *   }
 * ]
 */
public class LoadingScreenEntry {
    public String item;
    public int meta = 0;
    public String description;
    public String lore;
}
