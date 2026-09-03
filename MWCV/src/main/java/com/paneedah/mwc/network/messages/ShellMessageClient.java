package com.paneedah.mwc.network.messages;

import com.paneedah.weaponlib.render.shells.ShellParticleSimulator.Shell;
import io.netty.buffer.ByteBuf;
import io.redstudioragnarok.redcore.vectors.Vector3D;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class ShellMessageClient implements IMessage {

	private int shooter;
	private Shell.Type type;
	private Vector3D position = new Vector3D();
	private Vector3D velocity = new Vector3D();

	@Override
	public void fromBytes(final ByteBuf byteBuf) {
		shooter = byteBuf.readInt();
		type = Shell.Type.valueOf(ByteBufUtils.readUTF8String(byteBuf));
		position.read(byteBuf);
		velocity.read(byteBuf);
	}

	@Override
	public void toBytes(final ByteBuf byteBuf) {
		byteBuf.writeInt(shooter);
		ByteBufUtils.writeUTF8String(byteBuf, type.toString());
		position.write(byteBuf);
		velocity.write(byteBuf);
	}

    public ShellMessageClient() {}

    public ShellMessageClient(int shooter, Shell.Type type, Vector3D position, Vector3D velocity) {
        this.shooter = shooter;
        this.type = type;
        this.position = position;
        this.velocity = velocity;
    }

    public int getShooter() {
        return this.shooter;
    }

    public Shell.Type getType() {
        return this.type;
    }

    public Vector3D getPosition() {
        return this.position;
    }

    public Vector3D getVelocity() {
        return this.velocity;
    }

}