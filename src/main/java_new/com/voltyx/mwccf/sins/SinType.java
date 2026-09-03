package com.voltyx.mwccf.sins;

public enum SinType {
    WRATH(
        "wrath",
        "Гнев",
        "Wrath",
        "01",
        "Ближний бой, агрессия",
        "Больше входящего урона",
        0xA8341F
    ),
    PRIDE(
        "pride",
        "Гордыня",
        "Pride",
        "02",
        "Харизма, торговля, найм",
        "Слабее в одиночку",
        0xE8A53D
    ),
    LUST(
        "lust",
        "Похоть",
        "Lust",
        "03",
        "Скорость, уклонение",
        "Меньше макс. здоровья",
        0xD14BA8
    ),
    ENVY(
        "envy",
        "Зависть",
        "Envy",
        "04",
        "Кража, крафт из чужого",
        "Штраф к своим ресурсам",
        0x4BA36A
    ),
    GLUTTONY(
        "gluttony",
        "Чревоугодие",
        "Gluttony",
        "05",
        "Живучесть, реген",
        "Медленнее передвижение",
        0xC97F36
    ),
    GREED(
        "greed",
        "Алчность",
        "Greed",
        "06",
        "Лут, экономика",
        "Дороже ремонт/торговля",
        0xD4AF37
    ),
    SLOTH(
        "sloth",
        "Лень",
        "Sloth",
        "07",
        "Пассивный доход, стелс",
        "Слабый урон в бою",
        0x57614A
    );

    private final String id;
    private final String nameRu;
    private final String nameEn;
    private final String number;
    private final String focusRu;
    private final String priceRu;
    private final int color;

    SinType(String id, String nameRu, String nameEn, String number, String focusRu, String priceRu, int color) {
        this.id = id;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.number = number;
        this.focusRu = focusRu;
        this.priceRu = priceRu;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNumber() {
        return number;
    }

    public String getFocusRu() {
        return focusRu;
    }

    public String getPriceRu() {
        return priceRu;
    }

    public int getColor() {
        return color;
    }

    public static SinType byId(String id) {
        if (id == null) return null;
        for (SinType sin : values()) {
            if (sin.id.equalsIgnoreCase(id) || sin.name().equalsIgnoreCase(id)) {
                return sin;
            }
        }
        return null;
    }
}
