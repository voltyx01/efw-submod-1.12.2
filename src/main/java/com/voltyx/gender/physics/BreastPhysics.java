package com.voltyx.gender.physics;

import com.voltyx.gender.api.IGenderArmor;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireHelper;
import com.paneedah.weaponlib.Tags;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BreastPhysics {

	private float bounceVel = 0, targetBounce = 0, velocity = 0, wfg_femaleBreast, wfg_preBounce;
	private float bounceRotVel = 0, targetRotVel = 0, rotVelocity = 0, wfg_bounceRotation, wfg_preBounceRotation;
	private float bounceVelX = 0, targetBounceX = 0, velocityX = 0, wfg_femaleBreastX, wfg_preBounceX;
	private float randomMultiplier = 1.0f;
	private boolean justSneaking = false, alreadySleeping = false;
	private float breastSize = 0, preBreastSize = 0;

	private Vec3d prePos;
	private GenderPlayer genderPlayer;

	public BreastPhysics(GenderPlayer genderPlayer) {
		this.genderPlayer = genderPlayer;
	}
	private int lastAmmo = -1;
	private int randomB = 1;
	private boolean alreadyFalling = false;

	public void update(EntityPlayer plr, IGenderArmor armor) {
		this.wfg_preBounce = this.wfg_femaleBreast;
		this.wfg_preBounceX = this.wfg_femaleBreastX;
		this.wfg_preBounceRotation = this.wfg_bounceRotation;
		this.preBreastSize = this.breastSize;

		if (this.prePos == null) {
			this.prePos = plr.getPositionVector();
			return;
		}

		float breastWeight = genderPlayer.getBustSize() * 1.25f;
		float targetBreastSize = genderPlayer.getGender().canHaveBreasts() ? genderPlayer.getBustSize() : 0;

		if (breastSize < targetBreastSize) {
			breastSize += Math.abs(breastSize - targetBreastSize) / 2f;
		} else {
			breastSize -= Math.abs(breastSize - targetBreastSize) / 2f;
		}

		Vec3d motion = plr.getPositionVector().subtract(this.prePos);
		this.prePos = plr.getPositionVector();

		float bounceIntensity = (targetBreastSize * 3f) * genderPlayer.getBounceMultiplier();
		float resistance = MathHelper.clamp(armor.physicsResistance(), 0, 1);
		bounceIntensity *= 1 - resistance;
		if (plr.getHeldItemMainhand() != null) {
			int currentAmmo = com.paneedah.weaponlib.Tags.getAmmo(plr.getHeldItemMainhand());
			if (lastAmmo != -1 && currentAmmo < lastAmmo) {
				// патроны убыли — был выстрел
				this.targetBounce += 0.4f * bounceIntensity;
				this.targetRotVel += (plr.world.rand.nextBoolean() ? 1 : -1) * 4f * bounceIntensity;
			}
			lastAmmo = currentAmmo;
		}
		if (plr.isAirBorne && !alreadyFalling) {
			randomB = plr.world.rand.nextBoolean() ? -1 : 1;
			randomMultiplier = WildfireHelper.randFloat(0.3f, 1.7f); // больший диапазон
			alreadyFalling = true;
		}
		if (plr.onGround && alreadyFalling) {
			randomMultiplier = WildfireHelper.randFloat(0.3f, 1.7f);
			alreadyFalling = false;
		}

		if (!genderPlayer.getBreasts().isUniboob()) {
			bounceIntensity *= randomMultiplier;
		}

		this.targetBounce = MathHelper.clamp((float) motion.y * bounceIntensity * 0.5f, -1.0f, 1.0f);
		this.targetBounce += breastWeight;

		float f = (float) (motion.x * motion.x + motion.y * motion.y + motion.z * motion.z);
		f = f / 0.2F;
		f = f * f * f;
		if (f < 1.0F) f = 1.0F;

		if (!plr.isAirBorne) {
			this.targetBounce += MathHelper.cos(plr.limbSwing * 0.6662F + (float) Math.PI)
					* 0.5F * plr.limbSwingAmount * 0.5F / f;
		}

		float yawDelta = MathHelper.clamp(plr.renderYawOffset - plr.prevRenderYawOffset, -5f, 5f);
		this.targetRotVel = -(yawDelta / 350f) * bounceIntensity;


		if (plr.isSneaking() && !this.justSneaking) {
			this.justSneaking = true;
			this.targetBounce += bounceIntensity;
		}
		if (!plr.isSneaking() && this.justSneaking) {
			this.justSneaking = false;
			this.targetBounce += bounceIntensity;
		}

		if (plr.getRidingEntity() != null) {
			if (plr.getRidingEntity() instanceof EntityBoat) {
				// логика лодки не портируется
			}
			if (plr.getRidingEntity() instanceof EntityMinecart) {
				EntityMinecart cart = (EntityMinecart) plr.getRidingEntity();
				float speed = (float)(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
				if (Math.random() * speed < 0.5f && speed > 0.2f) {
					if (Math.random() > 0.5) {
						this.targetBounce = -bounceIntensity / 6f;
					} else {
						this.targetBounce = bounceIntensity / 6f;
					}
				}
			}
			if (plr.getRidingEntity() instanceof AbstractHorse) {
				AbstractHorse horse = (AbstractHorse) plr.getRidingEntity();
				float movement = (float) Math.sqrt(horse.motionX * horse.motionX + horse.motionZ * horse.motionZ);
				if (((net.minecraft.entity.Entity) horse).ticksExisted % clampMovement(movement) == 5 && movement > 0.1f) {
					this.targetBounce = bounceIntensity / 4f;
				}
			}
			if (plr.getRidingEntity() instanceof EntityPig) {
				EntityPig pig = (EntityPig) plr.getRidingEntity();
				float movement = (float) Math.sqrt(pig.motionX * pig.motionX + pig.motionZ * pig.motionZ);
				if (((net.minecraft.entity.Entity) pig).ticksExisted % clampMovement(movement) == 5 && movement > 0.08f) {
					this.targetBounce = bounceIntensity / 4f;
				}
			}
		}

		if (plr.swingProgress > 0 && ((net.minecraft.entity.Entity) plr).ticksExisted % 5 == 0 && !plr.isPlayerSleeping()) {
			if (Math.random() > 0.5) {
				this.targetBounce += -0.25f * bounceIntensity;
			} else {
				this.targetBounce += 0.25f * bounceIntensity;
			}
		}

		if (plr.isPlayerSleeping() && !this.alreadySleeping) {
			this.targetBounce = bounceIntensity;
			this.alreadySleeping = true;
		}
		if (!plr.isPlayerSleeping() && this.alreadySleeping) {
			this.targetBounce = bounceIntensity;
			this.alreadySleeping = false;
		}

		float percent = genderPlayer.getFloppiness();
		float bounceAmount = 0.45f * (1f - percent) + 0.15f;
		bounceAmount = MathHelper.clamp(bounceAmount, 0.15f, 0.6f);
		float delta = 2.25f - bounceAmount;

		float distanceFromMin = Math.abs(bounceVel + 0.5f) * 0.5f;
		float distanceFromMax = Math.abs(bounceVel - 2.65f) * 0.5f;
		if (bounceVel < -0.5f) targetBounce += distanceFromMin;
		if (bounceVel > 2.5f)  targetBounce -= distanceFromMax;

		if (targetBounce < -1.5f) targetBounce = -1.5f;
		if (targetBounce > 2.5f)  targetBounce = 2.5f;
		if (targetRotVel < -25f)  targetRotVel = -25f;
		if (targetRotVel > 25f)   targetRotVel = 25f;

		this.velocity = lerp(bounceAmount, this.velocity, (this.targetBounce - this.bounceVel) * delta);
		this.bounceVel += this.velocity * percent * 1.1625f;
		this.bounceVel *= 0.9f;
		this.bounceVel = MathHelper.clamp(this.bounceVel, -1.5f, 2.5f); // жёсткий лимит

		this.velocityX = lerp(bounceAmount, this.velocityX, (this.targetBounceX - this.bounceVelX) * delta);
		this.bounceVelX += this.velocityX * percent;

		this.rotVelocity = lerp(bounceAmount, this.rotVelocity, (this.targetRotVel - this.bounceRotVel) * delta);
		this.bounceRotVel += this.rotVelocity * percent;

		this.wfg_bounceRotation = this.bounceRotVel;
		this.wfg_femaleBreastX = this.bounceVelX;
		this.wfg_femaleBreast = this.bounceVel;
		// Добавь поле:


// В update после вычисления bounceIntensity:

	}

	private int clampMovement(float movement) {
		int val = (int) (10 - movement * 2f);
		if (val < 1) val = 1;
		return val;
	}

	public float getBreastSize(float partialTicks) { return lerp(partialTicks, preBreastSize, breastSize); }
	public float getPreBounceY() { return this.wfg_preBounce; }
	public float getBounceY() { return this.wfg_femaleBreast; }
	public float getPreBounceX() { return this.wfg_preBounceX; }
	public float getBounceX() { return this.wfg_femaleBreastX; }
	public float getBounceRotation() { return this.wfg_bounceRotation; }
	public float getPreBounceRotation() { return this.wfg_preBounceRotation; }

	private float lerp(float pct, float start, float end) { return start + pct * (end - start); }
}