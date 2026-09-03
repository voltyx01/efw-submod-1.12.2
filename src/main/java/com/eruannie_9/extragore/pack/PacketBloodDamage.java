/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.util.IThreadListener
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.eruannie_9.extragore.pack;

import com.eruannie_9.extragore.ExtraGore;
import com.eruannie_9.extragore.pack.BloodDamageKind;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBloodDamage
implements IMessage {
    private int entityId;
    private byte kindOrdinal;
    private float preHealth;
    private float postHealth;
    private float maxHealth;
    private float incomingDamage;
    private boolean hasDirectionalData;
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private double lookX;
    private double lookY;
    private double lookZ;
    private double motionX;
    private double motionY;
    private double motionZ;

    public PacketBloodDamage() {
    }

    public PacketBloodDamage(int entityId, @Nonnull BloodDamageKind kind, float preHealth, float postHealth, float maxHealth) {
        this(entityId, kind, preHealth, postHealth, maxHealth, Math.max(0.0f, preHealth - postHealth), null, null, null);
    }

    public PacketBloodDamage(int entityId, @Nonnull BloodDamageKind kind, float preHealth, float postHealth, float maxHealth, float incomingDamage) {
        this(entityId, kind, preHealth, postHealth, maxHealth, incomingDamage, null, null, null);
    }

    public PacketBloodDamage(int entityId, @Nonnull BloodDamageKind kind, float preHealth, float postHealth, float maxHealth, @Nullable Vec3d sourcePos, @Nullable Vec3d sourceLookDir, @Nullable Vec3d sourceMotion) {
        this(entityId, kind, preHealth, postHealth, maxHealth, Math.max(0.0f, preHealth - postHealth), sourcePos, sourceLookDir, sourceMotion);
    }

    public PacketBloodDamage(int entityId, @Nonnull BloodDamageKind kind, float preHealth, float postHealth, float maxHealth, float incomingDamage, @Nullable Vec3d sourcePos, @Nullable Vec3d sourceLookDir, @Nullable Vec3d sourceMotion) {
        this.entityId = entityId;
        this.kindOrdinal = (byte)kind.ordinal();
        this.preHealth = preHealth;
        this.postHealth = postHealth;
        this.maxHealth = maxHealth;
        this.incomingDamage = Math.max(0.0f, incomingDamage);
        boolean bl = this.hasDirectionalData = sourcePos != null && sourceLookDir != null;
        if (this.hasDirectionalData) {
            this.sourceX = sourcePos.x;
            this.sourceY = sourcePos.y;
            this.sourceZ = sourcePos.z;
            this.lookX = sourceLookDir.x;
            this.lookY = sourceLookDir.y;
            this.lookZ = sourceLookDir.z;
            Vec3d motion = sourceMotion != null ? sourceMotion : new Vec3d(0.0, 0.0, 0.0);
            this.motionX = motion.x;
            this.motionY = motion.y;
            this.motionZ = motion.z;
        }
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Nonnull
    public BloodDamageKind getKind() {
        int idx = this.kindOrdinal & 0xFF;
        BloodDamageKind[] values = BloodDamageKind.values();
        return idx < values.length ? values[idx] : BloodDamageKind.UNKNOWN;
    }

    public float getPreHealth() {
        return this.preHealth;
    }

    public float getPostHealth() {
        return this.postHealth;
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public float getIncomingDamage() {
        return this.incomingDamage;
    }

    public boolean hasDirectionalData() {
        return this.hasDirectionalData;
    }

    @Nullable
    public Vec3d getSourcePos() {
        if (!this.hasDirectionalData) {
            return null;
        }
        return new Vec3d(this.sourceX, this.sourceY, this.sourceZ);
    }

    @Nullable
    public Vec3d getSourceLookDir() {
        if (!this.hasDirectionalData) {
            return null;
        }
        return new Vec3d(this.lookX, this.lookY, this.lookZ);
    }

    @Nonnull
    public Vec3d getSourceMotion() {
        if (!this.hasDirectionalData) {
            return new Vec3d(0.0, 0.0, 0.0);
        }
        return new Vec3d(this.motionX, this.motionY, this.motionZ);
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte((int)this.kindOrdinal);
        buf.writeFloat(this.preHealth);
        buf.writeFloat(this.postHealth);
        buf.writeFloat(this.maxHealth);
        buf.writeFloat(this.incomingDamage);
        buf.writeBoolean(this.hasDirectionalData);
        if (this.hasDirectionalData) {
            buf.writeDouble(this.sourceX);
            buf.writeDouble(this.sourceY);
            buf.writeDouble(this.sourceZ);
            buf.writeDouble(this.lookX);
            buf.writeDouble(this.lookY);
            buf.writeDouble(this.lookZ);
            buf.writeDouble(this.motionX);
            buf.writeDouble(this.motionY);
            buf.writeDouble(this.motionZ);
        }
    }

    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.kindOrdinal = buf.readByte();
        this.preHealth = buf.readFloat();
        this.postHealth = buf.readFloat();
        this.maxHealth = buf.readFloat();
        this.incomingDamage = buf.readFloat();
        this.hasDirectionalData = buf.readBoolean();
        if (this.hasDirectionalData) {
            this.sourceX = buf.readDouble();
            this.sourceY = buf.readDouble();
            this.sourceZ = buf.readDouble();
            this.lookX = buf.readDouble();
            this.lookY = buf.readDouble();
            this.lookZ = buf.readDouble();
            this.motionX = buf.readDouble();
            this.motionY = buf.readDouble();
            this.motionZ = buf.readDouble();
        } else {
            this.sourceZ = 0.0;
            this.sourceY = 0.0;
            this.sourceX = 0.0;
            this.lookZ = 0.0;
            this.lookY = 0.0;
            this.lookX = 0.0;
            this.motionZ = 0.0;
            this.motionY = 0.0;
            this.motionX = 0.0;
        }
    }

    public static class Handler
    implements IMessageHandler<PacketBloodDamage, IMessage> {
        public IMessage onMessage(PacketBloodDamage message, MessageContext ctx) {
            IThreadListener thread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
            thread.addScheduledTask(() -> ExtraGore.proxy.handleBloodDamagePacket(message));
            return null;
        }
    }
}

