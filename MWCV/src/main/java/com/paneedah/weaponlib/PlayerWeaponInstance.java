package com.paneedah.weaponlib;



import com.paneedah.mwc.network.TypeRegistry;

import com.paneedah.weaponlib.animation.AnimationModeProcessor;

import com.paneedah.weaponlib.animation.gui.AnimationGUI;

import com.paneedah.weaponlib.command.DebugCommand;

import com.paneedah.weaponlib.compatibility.RecoilParam;

import com.paneedah.weaponlib.config.BalancePackManager;

import com.paneedah.weaponlib.config.ModernConfigManager;

import com.paneedah.weaponlib.perspective.OpticalScopePerspective;

import com.paneedah.weaponlib.perspective.Perspective;

import com.paneedah.weaponlib.shader.DynamicShaderGroupSource;

import com.paneedah.weaponlib.shader.DynamicShaderGroupSourceProvider;

import com.paneedah.weaponlib.shader.DynamicShaderPhase;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.entity.EntityLivingBase;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.item.Item;

import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLClientHandler;

import net.minecraftforge.fml.relauncher.Side;

import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL13;



import java.util.Arrays;

import java.util.Deque;

import java.util.UUID;

import java.util.concurrent.LinkedBlockingDeque;



import static com.paneedah.mwc.proxies.ClientProxy.MC;

import static com.paneedah.mwc.utils.ModReference.ID;

import static com.paneedah.mwc.utils.ModReference.LOG;





public class PlayerWeaponInstance extends PlayerItemInstance<WeaponState> implements DynamicShaderGroupSourceProvider {





    private static final int SERIAL_VERSION = 9;



    static {

        TypeRegistry.getINSTANCE().register(PlayerWeaponInstance.class);

    }



    private static final UUID NIGHT_VISION_SOURCE_UUID = UUID.randomUUID();

    private static final UUID VIGNETTE_SOURCE_UUID = UUID.randomUUID();

    private static final UUID BLUR_SOURCE_UUID = UUID.randomUUID();



    public final DynamicShaderGroupSource BLUR_SOURCE = new DynamicShaderGroupSource(BLUR_SOURCE_UUID,

            new ResourceLocation("weaponlib:/com/paneedah/weaponlib/resources/blur.json"))

            .withUniform("Radius", context -> hasOpticScope() ? 10f : 5f)

            .withUniform("Progress", context -> getAimChangeProgress());



    public final DynamicShaderGroupSource NIGHT_VISION_SOURCE = new DynamicShaderGroupSource(NIGHT_VISION_SOURCE_UUID,

            new ResourceLocation("weaponlib:/com/paneedah/weaponlib/resources/night-vision.json"))

            .withUniform("IntensityAdjust", context -> 40f - MC.gameSettings.gammaSetting * 38)

            .withUniform("NoiseAmplification", context ->  2f + 3f * MC.gameSettings.gammaSetting);



    public final DynamicShaderGroupSource VIGNETTE_SOURCE = new DynamicShaderGroupSource(VIGNETTE_SOURCE_UUID,

            new ResourceLocation("weaponlib:/com/paneedah/weaponlib/resources/vignette.json"))

            .withUniform("Radius", context -> getOpticScopeVignetteRadius(context.getPartialTicks()))

           // .withUniform("Velocity", context -> new float[]{ClientEventHandler.scopeVelX, ClientEventHandler.scopeVelY})

            .withUniform("Reticle", context -> {

            	

            	GlStateManager.setActiveTexture(GL13.GL_TEXTURE0+4);

            	MC.getTextureManager().bindTexture(new ResourceLocation(ID + ":textures/hud/reticle1.png"));

            	GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);

            	

            	return 4;

            });

         



    private static final long AIM_CHANGE_DURATION = 1200;



	private int ammo;

	private float recoil;

	private int seriesShotCount;

	private long lastFireTimestamp;

	private boolean aimed;

	private int maxShots;

	private float zoom = 1f;

	private byte activeTextureIndex;

	private boolean laserOn;

	private long aimChangeTimestamp;

	private boolean nightVisionOn;

	private boolean seriesResetAllowed;

	private long lastBurstEndTimestamp;

	private boolean altModificationModeEnabled;

	public transient boolean wasAimedBeforeReload;
	public transient boolean suppressFovZoom;
	public transient long lastReloadTriggerTimestamp;
	// True when the current COMPOUND_RELOAD_EMPTY reload started with the
	// weapon's magazine at 0 ammo. Used to shorten the post-animation delay
	// before the weapon becomes READY again (see WeaponReloadAspect).
	public transient boolean wasReloadedFromEmpty;
	

	private int loadIterationCount;

	private boolean loadAfterUnloadEnabled;

	private boolean magazineRemoved;

	private boolean isDelayCompoundEnd = true;

	

	private long stateReloadUpdateTimestamp;

	private boolean isAwaitingCompoundInstructions = false;



	

	public boolean isSlideInLock = false;



	// Per-instance slide-pump value (replaces the old shared static
// ClientValueRepo.slidePumpValue, which only tracked the locally held
// weapon and caused the slide on other players' weapons, or weapons
// rendered in third person, to desync or not move at all).
// Time-based decay so it doesn't depend on a per-tick update call —
// correct regardless of render path (1st or 3rd person) or how many
// PlayerWeaponInstances exist.
	// Стало:
	private transient float slidePumpValue = 0f;
	private transient float slidePumpValueCached = 0f;
	private transient long lastSlidePumpUpdateTime = System.currentTimeMillis();
	private transient long lastSlidePumpReadFrame = -1L;

	private transient float thirdPersonSlidePumpValue = 0f;
	private transient long lastThirdPersonSlidePumpUpdateTime = System.currentTimeMillis();

	public transient float visualRecoilStartIntensity = 0f;
	public transient float lastVisualRecoilIntensity = 0f;
	public transient long lastVisualRecoilShotTimestamp = -1L;

	// Decay rate: 0.30 per tick (50ms) — same as old ClientValueRepo
	private static final double SLIDE_PUMP_DECAY_PER_TICK = 0.30;
	private static final long SLIDE_PUMP_TICK_MS = 50L;

	/**
	 * Возвращает текущее значение слайда для первого лица.
	 */
	public float getSlidePumpValue() {
		return getSlidePumpValue(this.player);
	}

	public float getSlidePumpValue(EntityLivingBase holder) {
		int entityId = holder != null ? holder.getEntityId() : (this.player != null ? this.player.getEntityId() : -1);
		long lastShot = -1L;
		if (entityId != -1 && ClientEventHandler.lastShotTimeByEntity.containsKey(entityId)) {
			lastShot = ClientEventHandler.lastShotTimeByEntity.get(entityId);
		} else if (lastFireTimestamp > 0) {
			lastShot = lastFireTimestamp;
		}

		float kick = 0.0f;
		if (lastShot > 0) {
			long elapsed = System.currentTimeMillis() - lastShot;
			if (elapsed >= 0 && elapsed < 110L) {
				float progress = elapsed / 110.0f;
				if (progress < 0.20f) {
					kick = 1.0f * (progress / 0.20f);
				} else {
					kick = 1.0f * ((1.0f - progress) / 0.80f);
				}
			}
		}

		return kick;
	}

	public float getThirdPersonSlidePumpValue() {
		return getThirdPersonSlidePumpValue(this.player);
	}

	public float getThirdPersonSlidePumpValue(EntityLivingBase holder) {
		float target = (getAmmo() == 0 && isSlideLocked() && !isSuppressSlideLock()) ? 0.6f : 0.0f;
		
		int entityId = holder != null ? holder.getEntityId() : (this.player != null ? this.player.getEntityId() : -1);
		long lastShot = -1L;
		if (entityId != -1 && ClientEventHandler.lastShotTimeByEntity.containsKey(entityId)) {
			lastShot = ClientEventHandler.lastShotTimeByEntity.get(entityId);
		} else if (lastFireTimestamp > 0) {
			lastShot = lastFireTimestamp;
		}

		float kick = 0.0f;
		if (lastShot > 0) {
			long elapsed = System.currentTimeMillis() - lastShot;
			if (elapsed >= 0 && elapsed < 110L) {
				float progress = elapsed / 110.0f;
				if (progress < 0.20f) {
					kick = 1.0f * (progress / 0.20f);
				} else {
					kick = 1.0f * ((1.0f - progress) / 0.80f);
				}
			}
		}

		return target + kick;
	}

	public void bumpSlidePump() {
		this.lastFireTimestamp = System.currentTimeMillis();
		if (player != null) {
			ClientEventHandler.lastShotTimeByEntity.put(player.getEntityId(), this.lastFireTimestamp);
		}
	}

	/*

	 * Upon adding an element to the head of the queue, all existing elements with lower priority are removed

	 * from the queue. Elements with the same priority are not removed.

	 * This ensures the queue is always sorted by priority, lowest (head) to highest (tail).

	 */

	private Deque<AsyncWeaponState> filteredStateQueue = new LinkedBlockingDeque<>();

	private int[] activeAttachmentIds = new int[0];

	private byte[] selectedAttachmentIndexes = new byte[0];



	public PlayerWeaponInstance() {

		super();

	}

	

    public PlayerWeaponInstance(int itemInventoryIndex, EntityLivingBase player, ItemStack itemStack) {

		super(itemInventoryIndex, player, itemStack);

	}



	public PlayerWeaponInstance(int itemInventoryIndex, EntityLivingBase player) {

		super(itemInventoryIndex, player);

	}



	@Override

	protected int getSerialVersion() {

		return SERIAL_VERSION;

	}

	

	public RecoilParam getRecoilParameters() {

		if(AnimationModeProcessor.getInstance().getFPSMode()) {

			return AnimationGUI.getInstance().getRecoilParams();

		}

		return getWeapon().builder.recoilParam;

	}

	

	public Pair<Double, Double> getScreenShakeParameters() {

		if(DebugCommand.isWorkingOnScreenShake()) {

			return DebugCommand.screenShakeParam;

		}

		return getWeapon().getModernScreenShakeParameters();

	}



	private void addStateToHistory(WeaponState state) {

		AsyncWeaponState t;

		// Remove existing items from lower priorities from the top of the stack; stop when same or higher priority item is found

		while((t = filteredStateQueue.peekFirst()) != null) {

			if(t.getState().getPriority() < state.getPriority()) {

				filteredStateQueue.pollFirst();

			} else {

				break;

			}

		}



		long expirationTimeout;



		if(state == WeaponState.FIRING || state == WeaponState.RECOILED || state == WeaponState.PAUSED) {

			if(isAutomaticModeEnabled() && !getWeapon().hasRecoilPositioning()) {

				expirationTimeout = (long) (50f / getFireRate());

			} else {

				expirationTimeout = 500;

			}

			

		} else {

			expirationTimeout = Integer.MAX_VALUE;

		}

		filteredStateQueue.addFirst(new AsyncWeaponState(state, this.stateUpdateTimestamp, expirationTimeout));

	}

	

	

	public long getAnimationDuration() {

		

		// Give the old animations

		if(!getWeapon().builder.isUsingNewSystem()) {

			LOG.debug("Weapon is using the old system, returning standard value");

			return getWeapon().getTotalReloadingDuration();

		}

		

		return getAnimationDuration(getState());



		

	}

	

	public long getAnimationDuration(WeaponState state) {

		

		//System.out.println(getWeapon().getRenderer().getWeaponRendererBuilder().getTacticalReloadDuration());

		

		switch(state) {

		

		case LOAD:

			return getWeapon().getTotalReloadingDuration();

		case UNLOAD:

			return getWeapon().getTotalUnloadingDuration();

		case DRAWING:

			return getWeapon().getTotalDrawingDuration();

		case COMPOUND_RELOAD:

			return getWeapon().getRenderer().getWeaponRendererBuilder().getCompoundReloadDuration();

		case COMPOUND_RELOAD_EMPTY:

			//System.out.println(getWeapon().getRenderer().getWeaponRendererBuilder().getCompoundReloadEmptyDuration());

			//return getWeapon().getRenderer().getWeaponRendererBuilder().getCompoundReloadEmptyDuration();

			return getWeapon().getRenderer().getWeaponRendererBuilder().getCompoundReloadEmptyDuration();

		case TACTICAL_RELOAD:

			

			return getWeapon().getRenderer().getWeaponRendererBuilder().getTacticalReloadDuration();

		case COMPOUND_RELOAD_FINISHED:
			WeaponRenderer renderer = getWeapon().getRenderer();
			if (renderer != null) {
				if (renderer.getMagicState() == WeaponState.COMPOUND_RELOAD_EMPTY) {
					return renderer.getWeaponRendererBuilder().getCompoundReloadEmptyDuration();
				} else if (renderer.getMagicState() == WeaponState.TACTICAL_RELOAD) {
					return renderer.getWeaponRendererBuilder().getTacticalReloadDuration();
				}
			}
			return getWeapon().getRenderer().getWeaponRendererBuilder().getCompoundReloadDuration();

			

		}

		

	return 100L;

	}



	private long lastReloadFinishTimestamp = 0;

	private long lastReloadStartTimestamp = 0;

	private long lastUnloadFinishTimestamp = 0;

	private boolean suppressSlideLock = false;



	public long getLastReloadFinishTimestamp() {

		return lastReloadFinishTimestamp;

	}



	public long getLastReloadStartTimestamp() {

		return lastReloadStartTimestamp;

	}



	public long getLastUnloadFinishTimestamp() {

		return lastUnloadFinishTimestamp;

	}



	public boolean isSuppressSlideLock() {

		return suppressSlideLock;

	}



	public void setSuppressSlideLock(boolean suppressSlideLock) {

		this.suppressSlideLock = suppressSlideLock;

	}



	@Override

	public boolean setState(WeaponState state) {

		if ((state == WeaponState.COMPOUND_RELOAD_FINISHED || state == WeaponState.COMPOUND_RELOAD_FINISH) && 

		    getState() != WeaponState.COMPOUND_RELOAD_FINISHED && getState() != WeaponState.COMPOUND_RELOAD_FINISH) {

			lastReloadFinishTimestamp = System.currentTimeMillis();

		}

		if ((state == WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS || state == WeaponState.COMPOUND_RELOAD 
				|| state == WeaponState.COMPOUND_RELOAD_EMPTY || state == WeaponState.TACTICAL_RELOAD || state == WeaponState.LOAD) 
				&& getState() != WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS && getState() != WeaponState.COMPOUND_RELOAD 
				&& getState() != WeaponState.COMPOUND_RELOAD_EMPTY && getState() != WeaponState.TACTICAL_RELOAD && getState() != WeaponState.LOAD) {

			lastReloadStartTimestamp = System.currentTimeMillis();

		}

		if (state == WeaponState.READY && getState() == WeaponState.UNLOAD) {

			lastUnloadFinishTimestamp = System.currentTimeMillis();

		}

	

		boolean result = super.setState(state);

		addStateToHistory(state);

		return result;

	}



	public AsyncWeaponState nextHistoryState() {

		AsyncWeaponState result = filteredStateQueue.pollLast();

		if(result == null) {

		    result = new AsyncWeaponState(getState(), stateUpdateTimestamp);

		}

		return result;

	}



	public boolean isSlideLockSuppressed() {

		return suppressSlideLock;

	}



	public int getAmmo() {

		return ammo;

	}

	

	public boolean isSlideLocked() {
		return this.isSlideInLock || (this.ammo == 0 && !isSuppressSlideLock());
	}

	public void setSlideLock(boolean state) {
		this.isSlideInLock = state;
	}



	public void setAmmo(int ammo) {

		if(ammo != this.ammo) {

			this.ammo = ammo;

			markDirty();

			if (ammo > 0) {

				this.markMagSwapReady();

			}

		}

	}



	@Override

	public void read(ByteBuf byteBuf) {

		super.read(byteBuf);

		activeAttachmentIds = initIntArray(byteBuf);

		selectedAttachmentIndexes = initByteArray(byteBuf);

		ammo = byteBuf.readInt();

		aimed = byteBuf.readBoolean();

		recoil = byteBuf.readFloat();

		maxShots = byteBuf.readInt();

		zoom = byteBuf.readFloat();

		activeTextureIndex = byteBuf.readByte();

		laserOn = byteBuf.readBoolean();

		nightVisionOn = byteBuf.readBoolean();

		loadIterationCount = byteBuf.readInt();

		loadAfterUnloadEnabled = byteBuf.readBoolean();

		magazineRemoved = byteBuf.readBoolean();

		altModificationModeEnabled = byteBuf.readBoolean();

	}



	@Override

	public void write(ByteBuf byteBuf) {

		super.write(byteBuf);

		serializeIntArray(byteBuf, activeAttachmentIds);

		serializeByteArray(byteBuf, selectedAttachmentIndexes);

		byteBuf.writeInt(ammo);

		byteBuf.writeBoolean(aimed);

		byteBuf.writeFloat(recoil);

		byteBuf.writeInt(maxShots);

		byteBuf.writeFloat(zoom);

		byteBuf.writeByte(activeTextureIndex);

		byteBuf.writeBoolean(laserOn);

		byteBuf.writeBoolean(nightVisionOn);

		byteBuf.writeInt(loadIterationCount);

		byteBuf.writeBoolean(loadAfterUnloadEnabled);

		byteBuf.writeBoolean(magazineRemoved);

		byteBuf.writeBoolean(altModificationModeEnabled);

	}



	private static void serializeIntArray(ByteBuf buf, int a[]) {

		buf.writeByte(a.length);

		for (int j : a) {

			buf.writeInt(j);

		}

	}



	private static void serializeByteArray(ByteBuf buf, byte a[]) {

		buf.writeByte(a.length);

		for(int i = 0; i < a.length; i++) {

			buf.writeByte(a[i]);

		}

	}



	private static int[] initIntArray(ByteBuf buf) {

		int length = buf.readByte();

		int a[] = new int[length];

		for(int i = 0; i < length; i++) {

			a[i] = buf.readInt();

		}

		return a;

	}



	private static byte[] initByteArray(ByteBuf buf) {

		int length = buf.readByte();

		byte a[] = new byte[length];

		for(int i = 0; i < length; i++) {

			a[i] = buf.readByte();

		}

		return a;

	}



	@Override

	protected void updateWith(PlayerItemInstance<WeaponState> otherItemInstance, boolean updateManagedState) {

		super.updateWith(otherItemInstance, updateManagedState);

		PlayerWeaponInstance otherWeaponInstance = (PlayerWeaponInstance) otherItemInstance;



		setAmmo(otherWeaponInstance.ammo);

		setZoom(otherWeaponInstance.zoom);

		setRecoil(otherWeaponInstance.recoil);

		setSelectedAttachmentIndexes(otherWeaponInstance.selectedAttachmentIndexes);

		setActiveAttachmentIds(otherWeaponInstance.activeAttachmentIds);

		setActiveTextureIndex(otherWeaponInstance.activeTextureIndex);

		//setSlideLock(otherWeaponInstance.isSlideInLock);

		setLaserOn(otherWeaponInstance.laserOn);

		setMaxShots(otherWeaponInstance.maxShots);

		setLoadIterationCount(otherWeaponInstance.loadIterationCount);

		setLoadAfterUnloadEnabled(otherWeaponInstance.loadAfterUnloadEnabled);

		setMagazineRemoved(otherWeaponInstance.magazineRemoved);

	}



	public Weapon getWeapon() {

		return (Weapon)item;

	}



	public float getRecoil() {

		return recoil;

	}



	public void setRecoil(float recoil) {

		if(recoil != this.recoil) {

			this.recoil = recoil;

			markDirty();

		}

	}

	

	public boolean isDelayCompoundEnd() {

		return isDelayCompoundEnd;

	}

	

	public void setDelayCompoundEnd(boolean bool) {

		if(!bool) {

			stateReloadUpdateTimestamp = System.currentTimeMillis();

		}

		this.isDelayCompoundEnd = bool;

	}

	

	public boolean isMagazineRemoved() {

        return magazineRemoved;

    }

	

	public void setMagazineRemoved(boolean magazineRemoved) {

        this.magazineRemoved = magazineRemoved;

    }



	public boolean isLoadAfterUnloadEnabled() {

        return loadAfterUnloadEnabled;

    }

	

	public void setLoadAfterUnloadEnabled(boolean loadAfterUnloadEnabled) {

        this.loadAfterUnloadEnabled = loadAfterUnloadEnabled;

    }



	public int getMaxShots() {

		return maxShots;

	}



	void setMaxShots(int maxShots) {

		if(this.maxShots != maxShots) {

			this.maxShots = maxShots;

			markDirty();

		}

	}



	public int getSeriesShotCount() {

	    //System.out.println("Series shot count: " + seriesShotCount);

		return seriesShotCount;

	}



	public void setSeriesShotCount(int seriesShotCount) {

		this.seriesShotCount = seriesShotCount;

	}



	public long getLastFireTimestamp() {

		return lastFireTimestamp;

	}



	public void setLastFireTimestamp(long lastFireTimestamp) {

		this.lastFireTimestamp = lastFireTimestamp;

	}



	public void resetCurrentSeries() {

		seriesShotCount = 0;

		seriesResetAllowed = false;

	}

	

	public void setLastBurstEndTimestamp(long lastBurstEndTimestamp) {

        this.lastBurstEndTimestamp = lastBurstEndTimestamp;

    }

	

	public long getLastBurstEndTimestamp() {

	    return lastBurstEndTimestamp;

	}

	

	public void setSeriesResetAllowed(boolean seriesResetAllowed) {

	    this.seriesResetAllowed = seriesResetAllowed;

	}

	

	public boolean isSeriesResetAllowed() {

	    return seriesResetAllowed;

	}

	

//	public void resetCurrentSeriesEventually() {

//	    if(isOneClickBurstAllowed()) {

//	        seriesResetAllowed = true;

//	    } else {

//	        seriesShotCount = 0;

//	    }

//    }



	public float getFireRate() {

		return BalancePackManager.getFirerate(getWeapon());

		//return getWeapon().builder.fireRate;

	}



	public float getInaccuracy() {

		return BalancePackManager.getInaccuracy(getWeapon());

	}

	

    public boolean isOneClickBurstAllowed() {

        //System.out.println("One click burst allowed: " + getWeapon().builder.isOneClickBurstAllowed);

        return getWeapon().builder.isOneClickBurstAllowed;

    }



	public boolean isAutomaticModeEnabled() {

		return maxShots > 1;

	}



	public boolean isAimed() {

		return aimed;

	}



	public void setAimed(boolean aimed) {

		if(aimed != this.aimed) {

			this.aimed = aimed;

			markDirty();

			aimChangeTimestamp = System.currentTimeMillis();

		}

	}



	public int[] getActiveAttachmentIds() {

		if(activeAttachmentIds == null || activeAttachmentIds.length != AttachmentCategory.values.length) {

			activeAttachmentIds = new int[AttachmentCategory.values.length];

			for(CompatibleAttachment<Weapon> attachment: getWeapon().getCompatibleAttachments().values()) {

				if(attachment.isDefault()) {

					activeAttachmentIds[attachment.getAttachment().getCategory().ordinal()] = Item.getIdFromItem(attachment.getAttachment());

				}

			}

		}

		return activeAttachmentIds;

	}



	void setActiveAttachmentIds(int[] activeAttachmentIds) {

		if(!Arrays.equals(this.activeAttachmentIds, activeAttachmentIds)) {

			this.activeAttachmentIds = activeAttachmentIds;

			markDirty();

		}

	}



	public byte[] getSelectedAttachmentIds() {

		return selectedAttachmentIndexes;

	}



	void setSelectedAttachmentIndexes(byte[] selectedAttachmentIndexes) {

		if(!Arrays.equals(this.selectedAttachmentIndexes, selectedAttachmentIndexes)) {

			this.selectedAttachmentIndexes = selectedAttachmentIndexes;

			markDirty();

		}

	}



	public boolean isAttachmentZoomEnabled() {

		Item scopeItem = getAttachmentItemWithCategory(AttachmentCategory.SCOPE);

		return scopeItem instanceof ItemScope;

	}



	

	public ItemAttachment<Weapon> getAttachmentItemWithCategory(AttachmentCategory category) {

		if(activeAttachmentIds == null || activeAttachmentIds.length <= category.ordinal()) {

			return null;

		}

		Item activeAttachment = Item.getItemById(activeAttachmentIds[category.ordinal()]);

		if(activeAttachment instanceof ItemAttachment) {

		    return (ItemAttachment<Weapon>) activeAttachment;

		}

		return null;

	}

	

	public boolean isAwaitingCompoundInstructions() {

		return this.isAwaitingCompoundInstructions;

	}

	

	public void setIsAwaitingCompoundInstructions(boolean state) {

		this.isAwaitingCompoundInstructions = state;

	}



	public float getZoom() {

		return zoom;

	}



	public void setZoom(float zoom) {

		if(this.zoom != zoom && zoom > 0) {

			this.zoom = zoom;

			markDirty();

		}

	}



	public boolean isLaserOn() {

		return laserOn;

	}



	public void setLaserOn(boolean laserOn) {

		if(this.laserOn != laserOn) {

			this.laserOn = laserOn;

			markDirty();

		}

	}



	public boolean isNightVisionOn() {

        return nightVisionOn;

    }



    public void setNightVisionOn(boolean nightVisionOn) {

        if(this.nightVisionOn != nightVisionOn) {

            this.nightVisionOn = nightVisionOn;

            markDirty();

        }

    }



	public int getActiveTextureIndex() {

		return activeTextureIndex;

	}



	public void setActiveTextureIndex(int activeTextureIndex) {

		if(this.activeTextureIndex != activeTextureIndex) {

			if(activeTextureIndex > Byte.MAX_VALUE) {

				throw new IllegalArgumentException("activeTextureIndex must be less than " + Byte.MAX_VALUE);

			}

			this.activeTextureIndex = (byte)activeTextureIndex;

			markDirty();

		}

	}



	@Override

	public Class<? extends Perspective<?>> getRequiredPerspectiveType() {

	    Class<? extends Perspective<?>> result = null;

	    if(isAimed() || !isAimed()) {

	        ItemAttachment<Weapon> scope = getAttachmentItemWithCategory(AttachmentCategory.SCOPE);

	        if(scope instanceof ItemScope && ((ItemScope) scope).isOptical()) {

	            result = OpticalScopePerspective.class;

	        }

	    }

	    return result;

	}



    private boolean hasOpticScope() {

        ItemAttachment<Weapon> scope = getAttachmentItemWithCategory(AttachmentCategory.SCOPE);

        return scope instanceof ItemScope && ((ItemScope) scope).isOptical();

    }



    public ItemScope getScope() {

        ItemAttachment<Weapon> scope = getAttachmentItemWithCategory(AttachmentCategory.SCOPE);

        return scope instanceof ItemScope ? (ItemScope) scope : null;

    }



	@SideOnly(Side.CLIENT)

    private float getOpticScopeVignetteRadius(float partialTicks) {

        //ItemAttachment<Weapon> scope = getAttachmentItemWithCategory(AttachmentCategory.SCOPE);

        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

//        float f = player.distanceWalkedModified - player.prevDistanceWalkedModified;

//        float f1 = -(player.distanceWalkedModified + f * partialTicks);

        float f2 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;

       // return -2f * f2 + 0.55f;

        return 0.55f;

    }



    private float getAimChangeProgress() {

        float p = MiscUtils.clamp((float)(System.currentTimeMillis() - aimChangeTimestamp) / AIM_CHANGE_DURATION, 0f, 1f);

        if(!isAimed()) {

            p = 1f - p;

        }



        return p;

    }



   	@Override

    public DynamicShaderGroupSource getShaderSource(DynamicShaderPhase phase) {

	    if(isAimed() && phase == DynamicShaderPhase.POST_WORLD_OPTICAL_SCOPE_RENDER) {

	        ItemScope scope = getScope();

	        if(scope.isOptical())

	            return scope.hasNightVision() && nightVisionOn ? NIGHT_VISION_SOURCE : (ModernConfigManager.enableScopeVignette ? VIGNETTE_SOURCE : null);

	    }



	    float progress = getAimChangeProgress();

        return ModernConfigManager.enableBlurOnAim && phase == DynamicShaderPhase.PRE_ITEM_RENDER && (isAimed() || (progress > 0f && progress < 1f)) ? BLUR_SOURCE : null;

    }

   	

   	public void setLoadIterationCount(int loadIterationCount) {

        this.loadIterationCount = loadIterationCount;

    }

   	

   	public int getLoadIterationCount() {

        return loadIterationCount;

    }

   	

   	@Override

   	protected void reconcile() {

		if (!player.world.getGameRules().getBoolean("reconcileAmmunition") && !player.world.getGameRules().getBoolean("reconcileAttachment"))

			return;



        final ItemStack itemStack = getItemStack();



        if(itemStack != null) {

	        if (player.world.getGameRules().getBoolean("reconcileAmmunition")) {

		        final int expectedStackAmmo = Tags.getAmmo(itemStack);



		        if(ammo != expectedStackAmmo) {

		            LOG.debug("Reconciling ammunition. Expected ammunition: {}, Current ammunition: {}", expectedStackAmmo, ammo);



		            ammo = expectedStackAmmo;



			        updateTimestamp = System.currentTimeMillis();

		        }

	        }



	        if (player.world.getGameRules().getBoolean("reconcileAttachments")) {

		        final int[] expectedAttachmentIds = Tags.getAttachmentIds(itemStack);



		        if(!Arrays.equals(expectedAttachmentIds, activeAttachmentIds)) {

			        LOG.debug("Reconciling attachments. Expected attachments: {}, Current attachments: {}", Arrays.toString(expectedAttachmentIds), Arrays.toString(activeAttachmentIds));



		            activeAttachmentIds = expectedAttachmentIds;



			        updateTimestamp = System.currentTimeMillis();

		        }

	        }

        }

   	}



	@Override

	public String toString() {

		return getWeapon().builder.name + "[" + getUuid() + "]";

	}





    public boolean isAltMofificationModeEnabled() {

        return altModificationModeEnabled;

    }

    

    public void setAltModificationModeEnabled(boolean altModificationModeEnabled) {

        this.altModificationModeEnabled = altModificationModeEnabled;

    }

}