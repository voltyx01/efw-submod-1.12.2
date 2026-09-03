package com.voltyx.mwccf.sins.network;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.sins.ActiveModifier;
import com.voltyx.mwccf.sins.SinCard;
import com.voltyx.mwccf.sins.SinType;
import com.voltyx.mwccf.sins.capability.ISinCapability;
import com.voltyx.mwccf.sins.capability.SinCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PacketLevelUpRequest implements IMessage {
    public PacketLevelUpRequest() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketLevelUpRequest, IMessage> {
        @Override
        public IMessage onMessage(PacketLevelUpRequest message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    ISinCapability cap = player.getCapability(SinCapabilityProvider.SIN_CAP, null);
                    if (cap != null && cap.getChosenSin() != null) {
                        List<SinCard> cards = generateLevelUpCards(cap.getChosenSin(), cap.getSinLevel() + 1);
                        MwccfMod.PACKET_HANDLER.sendTo(new PacketLevelUpCardsOffer(cards), player);
                    }
                });
            }
            return null;
        }

        private List<SinCard> generateLevelUpCards(SinType sin, int nextLevel) {
            List<SinCard> result = new ArrayList<>();
            Random rng = new Random();

            // Баффы
            List<ActiveModifier> possibleBuffs = new ArrayList<>();
            possibleBuffs.add(new ActiveModifier("buff_melee", "Урон холодным оружием", "Melee Damage", 15.0 + nextLevel * 2, true));
            possibleBuffs.add(new ActiveModifier("buff_speed", "Скорость спринта", "Sprint Speed", 10.0 + nextLevel, true));
            possibleBuffs.add(new ActiveModifier("buff_crit", "Критический урон", "Critical Damage", 20.0 + nextLevel * 3, true));
            possibleBuffs.add(new ActiveModifier("buff_fear_res", "Сопротивление ужасу", "Fear Resistance", 25.0 + nextLevel * 2, true));
            possibleBuffs.add(new ActiveModifier("buff_reload", "Скорость перезарядки", "Reload Speed", 15.0 + nextLevel * 2, true));
            possibleBuffs.add(new ActiveModifier("buff_loot", "Шанс редкого лута", "Rare Loot Chance", 18.0 + nextLevel * 2, true));
            possibleBuffs.add(new ActiveModifier("buff_stamina", "Запас выносливости", "Max Stamina", 30.0 + nextLevel * 5, true));

            // Дебаффы
            List<ActiveModifier> possibleDebuffs = new ArrayList<>();
            possibleDebuffs.add(new ActiveModifier("debuff_hp", "Макс. здоровье", "Max Health", -8.0 - nextLevel, false));
            possibleDebuffs.add(new ActiveModifier("debuff_hunger", "Расход сытости", "Hunger Rate", 15.0 + nextLevel * 2, false));
            possibleDebuffs.add(new ActiveModifier("debuff_armor", "Эффективность брони", "Armor Efficiency", -10.0 - nextLevel, false));
            possibleDebuffs.add(new ActiveModifier("debuff_damage_taken", "Урон от паразитов", "Parasite Damage Taken", 12.0 + nextLevel * 2, false));
            possibleDebuffs.add(new ActiveModifier("debuff_recoil", "Отдача оружия", "Weapon Recoil", 14.0 + nextLevel * 2, false));
            possibleDebuffs.add(new ActiveModifier("debuff_repair", "Стоимость ремонта", "Repair Cost", 20.0 + nextLevel * 3, false));

            Collections.shuffle(possibleBuffs, rng);
            Collections.shuffle(possibleDebuffs, rng);

            for (int i = 0; i < 3; i++) {
                ActiveModifier buff = possibleBuffs.get(i % possibleBuffs.size());
                ActiveModifier debuff = possibleDebuffs.get(i % possibleDebuffs.size());
                result.add(new SinCard(i, buff, debuff));
            }
            return result;
        }
    }
}
