package com.paneedah.mwc.network.messages;

import com.paneedah.weaponlib.melee.PlayerMeleeInstance;
import com.paneedah.mwc.network.TypeRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class MeleeAttackMessage implements IMessage {

    private PlayerMeleeInstance instance;
    private int entityId;
    private boolean isHeavyAttack;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        instance = TypeRegistry.getINSTANCE().fromBytes(byteBuf);
        entityId = byteBuf.readInt();
        isHeavyAttack = byteBuf.readBoolean();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        TypeRegistry.getINSTANCE().toBytes(instance, byteBuf);
        byteBuf.writeInt(entityId);
        byteBuf.writeBoolean(isHeavyAttack);
    }

    public MeleeAttackMessage() {}

    public MeleeAttackMessage(PlayerMeleeInstance instance, int entityId, boolean isHeavyAttack) {
        this.instance = instance;
        this.entityId = entityId;
        this.isHeavyAttack = isHeavyAttack;
    }

    public PlayerMeleeInstance getInstance() {
        return this.instance;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean isHeavyAttack() {
        return this.isHeavyAttack;
    }

}