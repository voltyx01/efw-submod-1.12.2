package com.voltyx.gender.main.networking;

import com.voltyx.gender.main.Breasts;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.GenderPlayer.Gender;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

// В 1.12.2 пакет ОБЯЗАН реализовывать интерфейс IMessage
public abstract class PacketGenderInfo implements IMessage {

    // Убираем final, так как fromBytes будет заполнять их после создания пустого объекта
    protected UUID uuid;
    private Gender gender;
    private float bust_size;

    // Переменные физики
    private boolean breast_physics;
    private boolean breast_physics_armor;
    private boolean show_in_armor;
    private float bounceMultiplier;
    private float floppyMultiplier;

    private float xOffset, yOffset, zOffset;
    private boolean uniboob;
    private float cleavage;

    private boolean hurtSounds;

    // Обязательный пустой конструктор для рефлексии 1.12.2
    public PacketGenderInfo() {
    }

    protected PacketGenderInfo(GenderPlayer plr) {
        this.uuid = plr.uuid;
        this.gender = plr.getGender();
        this.bust_size = plr.getBustSize();
        this.hurtSounds = plr.hasHurtSounds();

        // Физика
        this.breast_physics = plr.hasBreastPhysics();
        this.breast_physics_armor = plr.hasArmorBreastPhysics();
        this.show_in_armor = plr.showBreastsInArmor();
        this.bounceMultiplier = plr.getBounceMultiplierRaw();
        this.floppyMultiplier = plr.getFloppiness();

        Breasts breasts = plr.getBreasts();
        this.xOffset = breasts.getXOffset();
        this.yOffset = breasts.getYOffset();
        this.zOffset = breasts.getZOffset();

        this.uniboob = breasts.isUniboob();
        this.cleavage = breasts.getCleavage();
    }

    // Заменяем конструктор с буфером на метод интерфейса IMessage
    @Override
    public void fromBytes(ByteBuf buf) {
        // В 1.12.2 нет readUUID(), поэтому читаем два числа Long
        long msb = buf.readLong();
        long lsb = buf.readLong();
        this.uuid = new UUID(msb, lsb);

        // В 1.12.2 нет readEnum(), читаем по порядковому номеру (ordinal)
        this.gender = Gender.values()[buf.readInt()];

        this.bust_size = buf.readFloat();
        this.hurtSounds = buf.readBoolean();

        this.breast_physics = buf.readBoolean();
        this.breast_physics_armor = buf.readBoolean();
        this.show_in_armor = buf.readBoolean();
        this.bounceMultiplier = buf.readFloat();
        this.floppyMultiplier = buf.readFloat();

        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.uniboob = buf.readBoolean();
        this.cleavage = buf.readFloat();
    }

    // Заменяем метод encode на метод интерфейса IMessage
    @Override
    public void toBytes(ByteBuf buf) {
        // Пишем UUID как два Long
        buf.writeLong(this.uuid.getMostSignificantBits());
        buf.writeLong(this.uuid.getLeastSignificantBits());

        // Пишем Enum как Int
        buf.writeInt(this.gender.ordinal());

        buf.writeFloat(this.bust_size);
        buf.writeBoolean(this.hurtSounds);

        buf.writeBoolean(this.breast_physics);
        buf.writeBoolean(this.breast_physics_armor);
        buf.writeBoolean(this.show_in_armor);
        buf.writeFloat(this.bounceMultiplier);
        buf.writeFloat(this.floppyMultiplier);

        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeBoolean(this.uniboob);
        buf.writeFloat(this.cleavage);
    }

    protected void updatePlayerFromPacket(GenderPlayer plr) {
        plr.updateGender(gender);
        plr.updateBustSize(bust_size);
        plr.updateHurtSounds(hurtSounds);

        // Физика
        plr.updateBreastPhysics(breast_physics);
        plr.updateArmorBreastPhysics(breast_physics_armor);
        plr.updateShowBreastsInArmor(show_in_armor);
        plr.updateBounceMultiplier(bounceMultiplier);
        plr.updateFloppiness(floppyMultiplier);

        Breasts breasts = plr.getBreasts();
        breasts.updateXOffset(xOffset);
        breasts.updateYOffset(yOffset);
        breasts.updateZOffset(zOffset);
        breasts.updateUniboob(uniboob);
        breasts.updateCleavage(cleavage);
    }
}