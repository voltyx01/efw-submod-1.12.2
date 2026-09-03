package com.paneedah.weaponlib;

import com.paneedah.mwc.capabilities.EquipmentCapability;
import com.paneedah.mwc.equipment.inventory.EquipmentInventory;
import com.paneedah.mwc.equipment.inventory.carryable.backpack.BackpackInventory;
import com.paneedah.mwc.network.NetworkPermitManager;
import com.paneedah.mwc.network.TypeRegistry;
import com.paneedah.mwc.utils.MWCUtil;
import com.paneedah.weaponlib.animation.AnimationModeProcessor;
import com.paneedah.weaponlib.state.Aspect;
import com.paneedah.weaponlib.state.Permit;
import com.paneedah.weaponlib.state.Permit.Status;
import com.paneedah.weaponlib.state.StateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.paneedah.mwc.equipment.inventory.EquipmentInventory.BELT_SLOT;
import static com.paneedah.mwc.utils.ModReference.LOG;

public class WeaponReloadAspect implements Aspect<WeaponState, PlayerWeaponInstance> {

    private static final long ALERT_TIMEOUT = 500;
    private static final long INSPECT_TIMEOUT = 500;
    private static final long UNLOAD_TIMEOUT = 1000;

    static {
        TypeRegistry.getINSTANCE().register(CompoundPermit.class);
        TypeRegistry.getINSTANCE().register(UnloadPermit.class);
        TypeRegistry.getINSTANCE().register(LoadPermit.class);
        TypeRegistry.getINSTANCE().register(PlayerWeaponInstance.class); // TODO: move it out
    }

    private static final Set<WeaponState> ALLOWED_UPDATE_FROM_STATES = new HashSet<>(Arrays.asList(
            WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS,
            WeaponState.COMPOUND_REQUESTED,
            WeaponState.COMPOUND_RELOAD,
            WeaponState.COMPOUND_RELOAD_EMPTY,
            WeaponState.COMPOUND_RELOAD_FINISH,
            WeaponState.COMPOUND_RELOAD_FINISHED,
            WeaponState.TACTICAL_RELOAD,
            WeaponState.LOAD_REQUESTED,
            WeaponState.LOAD,
            WeaponState.LOAD_ITERATION,
            WeaponState.LOAD_ITERATION_COMPLETED,
            WeaponState.ALL_LOAD_ITERATIONS_COMPLETED,
            WeaponState.UNLOAD_PREPARING,
            WeaponState.UNLOAD_REQUESTED,
            WeaponState.UNLOAD,
            WeaponState.ALERT,
            WeaponState.INSPECTING,
            WeaponState.DRAWING));

    public static class CompoundPermit extends Permit<WeaponState> {

        public CompoundPermit() {
        }

        public CompoundPermit(WeaponState state) {
            super(state);
        }

    }

    public static class UnloadPermit extends Permit<WeaponState> {

        public UnloadPermit() {
        }

        public UnloadPermit(WeaponState state) {
            super(state);
        }
    }

    public static class LoadPermit extends Permit<WeaponState> {

        public LoadPermit() {
        }

        public LoadPermit(WeaponState state) {
            super(state);
        }
    }

    private static Predicate<PlayerWeaponInstance> hasNextLoadIteration = weaponInstance -> weaponInstance.getWeapon()
            .hasIteratedLoad() && weaponInstance.getLoadIterationCount() > 0;

    private static Predicate<PlayerWeaponInstance> supportsDirectBulletLoad = weaponInstance -> weaponInstance
            .getWeapon().getAmmoCapacity() > 0;

    private static Predicate<PlayerWeaponInstance> magazineAttached = weaponInstance -> WeaponAttachmentAspect
            .getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance) != null && !weaponInstance.isMagazineRemoved();

    private static Predicate<PlayerWeaponInstance> hasAmmo = (i) -> i.getAmmo() != 0;

    private static Predicate<PlayerWeaponInstance> loadIterationCompleted = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp()
                    + Math.max(weaponInstance.getWeapon().builder.loadIterationTimeout,
                            weaponInstance.getWeapon().getTotalLoadIterationDuration() + 250);

    private static Predicate<PlayerWeaponInstance> allLoadIterationsCompleted = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp()
                    + weaponInstance.getWeapon().getAllLoadIterationAnimationsCompletedDuration();

    private static Predicate<PlayerWeaponInstance> reloadAnimationCompleted = weaponInstance -> {
    private static double getEffectiveReloadSpeed(PlayerWeaponInstance weaponInstance) {
        if (weaponInstance == null) return com.paneedah.weaponlib.config.ModernConfigManager.reloadSpeedMultiplier;
        double speed = com.paneedah.weaponlib.config.ModernConfigManager.reloadSpeedMultiplier;
        com.paneedah.weaponlib.stats.AttachmentStatsManager.EffectiveWeaponStats effStats =
                com.paneedah.weaponlib.stats.AttachmentStatsManager.getEffectiveStats(weaponInstance);
        if (effStats != null) {
            speed *= effStats.reloadSpeedMultiplier;
        }
        return speed <= 0.01 ? 1.0 : speed;
    }

    private static Predicate<PlayerWeaponInstance> reloadAnimationCompleted = weaponInstance -> {
        long maxTime = weaponInstance.getAnimationDuration();

        long targetDuration = (long)(Math.max(weaponInstance.getWeapon().builder.reloadingTimeout, maxTime)
                    / getEffectiveReloadSpeed(weaponInstance));

        return System.currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp() + targetDuration;
    };

    // В WeaponReloadAspect, после reloadAnimationCompleted:
    private static Predicate<PlayerWeaponInstance> reloadAlmostCompleted = weaponInstance -> {
        long maxTime2 = Math.max(weaponInstance.getWeapon().builder.reloadingTimeout,
                weaponInstance.getAnimationDuration());
        return System.currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp()
                + (long)(maxTime2 / getEffectiveReloadSpeed(weaponInstance)) - 500;
    };

    // True while the COMPOUND_RELOAD_EMPTY state hasn't received its new ammo
    // yet (the CompoundPermit round-trip that performs the actual magazine
    // swap / setAmmo hasn't completed). Used to let the COMPOUND_RELOAD_FINISH
    // transition (and thus the CompoundPermit request that credits ammo) fire
    // slightly earlier for weapons that were reloaded from an empty magazine,
    // removing the extra delay before the player can shoot/aim afterwards.
    private static Predicate<PlayerWeaponInstance> emptyMagazineReloadPending = weaponInstance -> weaponInstance
            .getState() == WeaponState.COMPOUND_RELOAD_EMPTY && weaponInstance.getAmmo() == 0;

    private static Predicate<PlayerWeaponInstance> magSwapCompleted = weaponInstance -> weaponInstance.isMagSwapDone();

    private static Predicate<PlayerWeaponInstance> reloadMidpoint = weaponInstance -> Math
            .abs((System.currentTimeMillis() - (weaponInstance.getReloadTimestamp()))
                    / ((double) weaponInstance.getWeapon().getTotalReloadingDuration() * 0.5) - 0.5) < 0.01;

    private static Predicate<PlayerWeaponInstance> unloadTimeoutExpired = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp() + UNLOAD_TIMEOUT;

    private static Predicate<PlayerWeaponInstance> awaitFurtherLoadInstructionCompleted = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp() + 295;

    private static Predicate<PlayerWeaponInstance> loadAfterUnloadEnabled = PlayerWeaponInstance::isLoadAfterUnloadEnabled;

    private static Predicate<PlayerWeaponInstance> unloadAnimationCompleted = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp()
                    + (long)(weaponInstance.getWeapon().getTotalUnloadingDuration() * 1.1 / getEffectiveReloadSpeed(weaponInstance));

    private static Predicate<PlayerWeaponInstance> prepareFirstLoadIterationAnimationCompleted = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp()
                    + (long)(weaponInstance.getWeapon().getPrepareFirstLoadIterationAnimationDuration() * 1.1 / getEffectiveReloadSpeed(weaponInstance));

    private static Predicate<PlayerWeaponInstance> shouldFinishCompoundReload = weaponInstance -> {
        if (weaponInstance.isDelayCompoundEnd())
            return true;
        else {
            long maxTime = (long) (weaponInstance.getAnimationDuration() / getEffectiveReloadSpeed(weaponInstance));

            return System.currentTimeMillis() >= weaponInstance.getReloadTimestamp() + maxTime;
        }
    };

    private static Predicate<PlayerWeaponInstance> alertTimeoutExpired = instance -> System
            .currentTimeMillis() >= ALERT_TIMEOUT + instance.getStateUpdateTimestamp();

    private static Predicate<PlayerWeaponInstance> inspectTimeoutExpired = instance -> System
            .currentTimeMillis() >= INSPECT_TIMEOUT + instance.getStateUpdateTimestamp();

    private static Predicate<PlayerWeaponInstance> drawingAnimationCompleted = weaponInstance -> System
            .currentTimeMillis() >= weaponInstance.getStateUpdateTimestamp()
                    + (long)(weaponInstance.getWeapon().getTotalDrawingDuration() * 1.0 / com.paneedah.weaponlib.config.ModernConfigManager.drawSpeedMultiplier);

    private ModContext modContext;

    private NetworkPermitManager permitManager;

    private StateManager<WeaponState, ? super PlayerWeaponInstance> stateManager;

    public WeaponReloadAspect(ModContext modContext) {
        this.modContext = modContext;
    }

    @Override
    public void setStateManager(StateManager<WeaponState, ? super PlayerWeaponInstance> stateManager) {
        if (permitManager == null)
            throw new IllegalStateException("Permit manager not initialized");

        this.stateManager = stateManager
                .in(this)
                .change(WeaponState.READY).to(WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS)
                .manual()

                .in(this)
                .change(WeaponState.READY).to(WeaponState.COMPOUND_REQUESTED)
                .manual()



                .in(this)

                .change(WeaponState.READY).to(WeaponState.COMPOUND_RELOAD)

                // .withAction(this::clientCompoundReload)

                /*
                 * .withPermit((s, es) -> new LoadPermit(s),
                 * modContext.getPlayerItemInstanceRegistry()::update, permitManager)
                 * .withAction((c, f, t, p) -> completeClientLoad(c, (LoadPermit)p))
                 */
                .manual()

                .in(this)
                .change(WeaponState.READY).to(WeaponState.COMPOUND_RELOAD_EMPTY)
                .manual()

                .in(this)
                .change(WeaponState.READY).to(WeaponState.TACTICAL_RELOAD)
                .manual()

                /*
                 * .in(this)
                 * .change(WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS).to(WeaponState.LOAD)
                 * .when(hasAmmo.and(magazineAttached))
                 * .withAction(this::noFurtherLoadInstructionsReceived)
                 * .automatic()
                 */

                .in(this)
                .change(WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS).to(WeaponState.READY)
                .when(awaitFurtherLoadInstructionCompleted)
                .withAction(this::noFurtherLoadInstructionsReceived)
                .automatic()

                .in(this)
                .change(WeaponState.AWAIT_FURTHER_LOAD_INSTRUCTIONS).to(WeaponState.READY)
                .withAction(this::furtherLoadInstructionsReceived)
                .manual()

                .in(this)
                .change(WeaponState.COMPOUND_REQUESTED).to(WeaponState.READY)
                .when(awaitFurtherLoadInstructionCompleted)
                .withAction(this::noCompoundInstructionsReceived)
                .automatic()

                .in(this)
                .change(WeaponState.COMPOUND_REQUESTED).to(WeaponState.READY)
                .withAction(this::compoundInstructionsReceived)
                .manual()

                .in(this)
                .change(WeaponState.READY).to(WeaponState.LOAD)
                .when(supportsDirectBulletLoad.or(magazineAttached.negate()))
                .withPermit((s, es) -> new LoadPermit(s), modContext.getPlayerItemInstanceRegistry()::update,
                        permitManager)
                .withAction((c, f, t, p) -> completeClientLoad(c, (LoadPermit) p))
                .manual()

                .in(this)
                .change(WeaponState.UNLOAD).to(WeaponState.LOAD)
                .when(loadAfterUnloadEnabled.and(supportsDirectBulletLoad.or(magazineAttached.negate())))
                .withPermit((s, es) -> new LoadPermit(s), modContext.getPlayerItemInstanceRegistry()::update,
                        permitManager)
                .withAction((c, f, t, p) -> completeClientLoad(c, (LoadPermit) p))
                .manual()

                .in(this)
                .change(WeaponState.LOAD).to(WeaponState.COMPOUND_RELOAD_FINISHED)
                .when(reloadAnimationCompleted.and(hasNextLoadIteration.negate()))
                .automatic()

                .in(this)
                .change(WeaponState.TACTICAL_RELOAD).to(WeaponState.COMPOUND_RELOAD_FINISH)
                .when(reloadAnimationCompleted.and(hasNextLoadIteration.negate()))
                .withAction((c, f, t, p) -> obamaCorporation(c))
                .automatic()

                .in(this)

                .change(WeaponState.COMPOUND_RELOAD).to(WeaponState.COMPOUND_RELOAD_FINISH)
                .when(reloadAnimationCompleted.and(hasNextLoadIteration.negate()))
                .withAction((c, f, t, p) -> obamaCorporation(c))
                .automatic()

                .in(this)
                .change(WeaponState.COMPOUND_RELOAD_EMPTY).to(WeaponState.COMPOUND_RELOAD_FINISH)
                .when(reloadAnimationCompleted.and(hasNextLoadIteration.negate()))
                .withAction((c, f, t, p) -> obamaCorporation(c))
                .automatic()

                .in(this)
                .change(WeaponState.COMPOUND_RELOAD_FINISH).to(WeaponState.COMPOUND_RELOAD_FINISHED)
                .withPermit((s, es) -> new CompoundPermit(s), modContext.getPlayerItemInstanceRegistry()::update,
                        permitManager)
                .withAction((c, f, t, p) -> completeClientLoad(c, null))
                .manual()

                .in(this)
                .change(WeaponState.COMPOUND_RELOAD_FINISHED).to(WeaponState.READY)
                .when(shouldFinishCompoundReload)
                .withAction(this::afterReloadFinished)
                .automatic()

                .in(this)
                .change(WeaponState.LOAD).to(WeaponState.LOAD_ITERATION)
                .when(hasNextLoadIteration.and(prepareFirstLoadIterationAnimationCompleted))
                .withAction(this::startLoadIteration)
                .automatic()

                .in(this)
                .change(WeaponState.LOAD_ITERATION).to(WeaponState.LOAD_ITERATION_COMPLETED)
                .when(loadIterationCompleted)
                .withAction(this::completeLoadIteration)
                .automatic()

                .in(this)
                .change(WeaponState.LOAD_ITERATION_COMPLETED).to(WeaponState.LOAD_ITERATION)
                .when(hasNextLoadIteration)
                .withAction(this::startLoadIteration)
                .automatic()

                .in(this)
                .change(WeaponState.LOAD_ITERATION_COMPLETED).to(WeaponState.ALL_LOAD_ITERATIONS_COMPLETED)
                .when(hasNextLoadIteration.negate())
                .automatic()

                .in(this)
                .change(WeaponState.ALL_LOAD_ITERATIONS_COMPLETED).to(WeaponState.READY)
                .when(allLoadIterationsCompleted)
                .withAction(this::afterAllLoadIterationsCompleted)
                .automatic()

                .in(this)
                .prepare((c, f, t) -> {
                    prepareUnload(c);
                }, unloadAnimationCompleted)
                .change(WeaponState.READY).to(WeaponState.UNLOAD)
                .when(magazineAttached)
                .withPermit((s, c) -> new UnloadPermit(s), modContext.getPlayerItemInstanceRegistry()::update,
                        permitManager)
                .withAction((c, f, t, p) -> completeClientUnload(c, (UnloadPermit) p))
                .manual()

                .in(this)
                .change(WeaponState.UNLOAD).to(WeaponState.READY)
                .when(loadAfterUnloadEnabled.negate().or(unloadTimeoutExpired))
                .withAction(this::afterReloadFinished)
                .automatic()

                .in(this).change(WeaponState.ALERT).to(WeaponState.READY)
                .when(alertTimeoutExpired)
                .automatic()

                .in(this)
                .change(WeaponState.READY).to(WeaponState.INSPECTING)
                .withAction(this::inspect)
                .manual()

                .in(this)
                .change(WeaponState.INSPECTING).to(WeaponState.READY)
                .when(inspectTimeoutExpired)
                .automatic()

                .in(this)
                .change(WeaponState.READY).to(WeaponState.DRAWING)

                .withAction(this::draw)
                // .automatic()
                .manual()

                .in(this)
                .change(WeaponState.DRAWING).to(WeaponState.READY)
                .when(drawingAnimationCompleted)
                .automatic();
    }

    public ItemAttachment<Weapon> previousMagazine;

    @Override
    public void setPermitManager(NetworkPermitManager permitManager) {
        this.permitManager = permitManager;

        permitManager.registerEvaluator(LoadPermit.class, PlayerWeaponInstance.class, this::processLoadPermit);
        permitManager.registerEvaluator(UnloadPermit.class, PlayerWeaponInstance.class, this::processUnloadPermit);
        permitManager.registerEvaluator(CompoundPermit.class, PlayerWeaponInstance.class, this::processCompoundPermit);
    }

    public void processCompoundPermit(CompoundPermit p, PlayerWeaponInstance pwi) {
        processActualCompoundPermit(p, pwi);

        // processUnloadPermit(new UnloadPermit(p.getState()), pwi);
        // p.setStatus(Status.GRANTED);

        previousMagazine = null;
    }

    public void obamaCorporation(PlayerWeaponInstance instance) {
        stateManager.changeState(this, instance, WeaponState.COMPOUND_RELOAD_FINISHED);
    }

    public void clientCompoundReload(PlayerWeaponInstance instance) {
        if (instance == null)
            return;

        instance.completeMagSwap();

        instance.getWeapon().getRenderer().setMagicMagPermit(true);

        // processUnloadPermit(new UnloadPermit(instance.getState()), instance);

        // Send request to the server
        // permitManager.request(new CompoundPermit(instance.getState()), instance, (a,
        // b) -> {});

        // processUnloadPermit(new UnloadPermit(instance.getState()), instance);
        // processLoadPermit(new LoadPermit(instance.getState()), instance);

        previousMagazine = null;
    }

    
    private boolean hasAmmo(net.minecraft.entity.player.EntityPlayer player, PlayerWeaponInstance weaponInstance) {
        if (player.isCreative()) return true;

        Weapon weapon = (Weapon) weaponInstance.getItem();
        ItemAttachment<Weapon> attachment = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
        java.util.List<? extends net.minecraft.item.Item> comp = null;
        
        if (attachment instanceof ItemMagazine) {
            comp = ((ItemMagazine) attachment).getCompatibleBullets();
        } else {
            comp = weapon.getCompatibleAttachments(ItemBullet.class);
        }
        
        if (comp == null || comp.isEmpty()) {
            if (weapon.builder.ammo != null) {
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    net.minecraft.item.ItemStack stack = player.inventory.getStackInSlot(i);
                    if (stack != null && stack.getItem() == weapon.builder.ammo) {
                        return true;
                    }
                }
            }
            return false;
        }

        com.paneedah.mwc.items.equipment.ItemAmmoPack ammoPackItem = com.paneedah.mwc.init.MWCItems.ammoPack;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            net.minecraft.item.ItemStack invStack = player.inventory.getStackInSlot(i);
            if (invStack != null && invStack.getItem() == ammoPackItem) {
                ItemBullet packBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(invStack);
                if (comp.contains(packBullet)) {
                    if (com.paneedah.mwc.items.equipment.ItemAmmoPack.getAmmo(invStack) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void reloadMainHeldItem(EntityPlayer player) {
        PlayerWeaponInstance instance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player,
                PlayerWeaponInstance.class);

        if (instance != null) {
            if (instance.getState() != WeaponState.READY && instance.getState() != WeaponState.DRAWING) {
                return;
            }
            if (System.currentTimeMillis() - instance.lastReloadTriggerTimestamp < 500) {
                return;
            }

            boolean canReload = false;
            if (AnimationModeProcessor.getInstance().isLegacyMode()) {
                canReload = hasAmmo((net.minecraft.entity.player.EntityPlayer)instance.getPlayer(), instance);
            } else {
                canReload = hasAmmo((net.minecraft.entity.player.EntityPlayer)instance.getPlayer(), instance);
            }

            if (!canReload) {
                return;
            }

            instance.lastReloadTriggerTimestamp = System.currentTimeMillis();
            instance.wasAimedBeforeReload = instance.isAimed();
            instance.setAimed(false);
            instance.suppressFovZoom = false;
            if (AnimationModeProcessor.getInstance().isLegacyMode()) {
                furtherLoadInstructionsReceived(instance);
                stateManager.changeState(this, instance, WeaponState.READY);
            } else {
                if (WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, instance) == null) {
                    ItemStack nextAttachment = getNextBestMagazineStack(instance);

                    if (nextAttachment == null)
                        return; // ← нет магазина в инвентаре — стоп

                    if (instance.getWeapon().getRenderer().getBuilder().isHasLoadEmpty()
                            && Tags.getAmmo(nextAttachment) == 0)
                        instance.getWeapon().getRenderer().setShouldDoEmptyVariant(true);

                    noFurtherLoadInstructionsReceived(instance);
                } else {
                    ItemAttachment<Weapon> nextAttachment = getNextMagazine(instance);

                    instance.markReloadDirt();
                    instance.markMagSwapReady();
                    if (instance.getAmmo() == 0) {
                        instance.wasReloadedFromEmpty = true;
                        if (instance.isMagazineRemoved()) {
                            stateManager.changeState(this, instance, WeaponState.LOAD);
                        } else {
                            if (nextAttachment != null)
                                instance.getWeapon().getRenderer().setMagicMag(instance, nextAttachment,
                                        WeaponState.COMPOUND_RELOAD_EMPTY);
                            else
                                return;

                            if (instance.getAmmo() == 0 && instance.getWeapon().getRenderer().getBuilder()
                                    .isHasCompoundReloadEmpty()) {
                                stateManager.changeState(this, instance, WeaponState.COMPOUND_RELOAD_EMPTY);
                            } else {
                                stateManager.changeState(this, instance, WeaponState.COMPOUND_RELOAD);
                            }
                        }
                    } else {
                        instance.wasReloadedFromEmpty = false;
                        if (nextAttachment != null)
                            instance.getWeapon().getRenderer().setMagicMag(instance, nextAttachment,
                                    WeaponState.COMPOUND_RELOAD);
                        else
                            return;
                        compoundInstructionsReceived(instance);
                    }
                }
            }
        }
    }

    public void unloadMainHeldItem(EntityPlayer player) {
        PlayerWeaponInstance instance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player,
                PlayerWeaponInstance.class);

        if (instance != null) {
            if (instance.getState() != WeaponState.READY) {
                return;
            }

            boolean hasMagazine = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE,
                    instance) != null;
            if (!hasMagazine) {
                return;
            }

            if (System.currentTimeMillis() - instance.lastReloadTriggerTimestamp < 500) {
                return;
            }
            instance.lastReloadTriggerTimestamp = System.currentTimeMillis();

            instance.wasAimedBeforeReload = instance.isAimed();
            instance.setAimed(false);
            instance.getWeapon().getRenderer().compoundReloadEmpty = false;
            instance.getWeapon().getRenderer().compoundReload = false;
            instance.setLoadAfterUnloadEnabled(false);

            ItemAttachment<Weapon> currentMagazine = modContext.getAttachmentAspect().getActiveAttachment(instance,
                    AttachmentCategory.MAGAZINE);
            if (instance.getWeapon().getRenderer().getBuilder().isHasUnloadEmpty() && currentMagazine != null
                    && instance.getAmmo() == 0)
                instance.getWeapon().getRenderer().setShouldDoEmptyVariant(true);

            stateManager.changeState(this, instance, WeaponState.UNLOAD, WeaponState.ALERT);
        }
    }

    void updateMainHeldItem(EntityPlayer player) {
        PlayerWeaponInstance instance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player,
                PlayerWeaponInstance.class);

        if (instance != null) {
            if (instance.getPlayer() != null && instance.getPlayer().world != null && instance.getPlayer().world.isRemote) {
                // Midpoint mag swap check on tick without triggering state machine self-loops
                if (!instance.isMagSwapDone() && (instance.getState() == WeaponState.COMPOUND_RELOAD || instance.getState() == WeaponState.COMPOUND_RELOAD_EMPTY)) {
                    if (reloadMidpoint.test(instance)) {
                        clientCompoundReload(instance);
                    }
                }
            }
            stateManager.changeStateFromAnyOf(this, instance, ALLOWED_UPDATE_FROM_STATES);
        }
    }

    public void inspectMainHeldItem(EntityPlayer player) {
        PlayerWeaponInstance instance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player,
                PlayerWeaponInstance.class);

        if (instance != null)
            stateManager.changeState(this, instance, WeaponState.INSPECTING);
    }

    public void drawMainHeldItem(EntityPlayer player) {
        PlayerWeaponInstance instance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player,
                PlayerWeaponInstance.class);

        if (instance != null) {
            stateManager.changeState(this, instance, WeaponState.DRAWING);
            if (player.world.isRemote && player == com.paneedah.mwc.proxies.ClientProxy.MC.player) {
                if (com.paneedah.weaponlib.config.ModernConfigManager.useDirectionalCameraSway) {
                    com.paneedah.weaponlib.compatibility.CameraOverhaulCompat.directionalSwayCamera(
                        (float) com.paneedah.weaponlib.config.ModernConfigManager.drawCameraSwayTrauma * 15.0f,
                        0.0f,
                        0.0f,
                        (float) com.paneedah.weaponlib.config.ModernConfigManager.drawCameraSwayLength * 1.5f
                    );
                } else {
                    com.paneedah.weaponlib.compatibility.CameraOverhaulCompat.smoothSwayCamera(
                        (float) com.paneedah.weaponlib.config.ModernConfigManager.drawCameraSwayTrauma,
                        (float) com.paneedah.weaponlib.config.ModernConfigManager.drawCameraSwayFrequency,
                        (float) com.paneedah.weaponlib.config.ModernConfigManager.drawCameraSwayLength
                    );
                }
            }
        }
    }

    private ItemAttachment<Weapon> getNextMagazine(PlayerWeaponInstance weaponInstance) {
        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
        if (activeMag != null && hasAmmo((net.minecraft.entity.player.EntityPlayer)weaponInstance.getPlayer(), weaponInstance)) return activeMag;
        return null;
    }

    private ItemStack getNextBestMagazineStack(PlayerWeaponInstance weaponInstance) {
        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
        if (activeMag != null && hasAmmo((net.minecraft.entity.player.EntityPlayer)weaponInstance.getPlayer(), weaponInstance)) return new net.minecraft.item.ItemStack((net.minecraft.item.Item)activeMag);
        return null;
    }

    private void processActualCompoundPermit(CompoundPermit p, PlayerWeaponInstance instance) {
        processLoadPermit(new LoadPermit(p.getState()), instance);
        p.setStatus(Status.GRANTED);
    }

    private void processLoadPermit(LoadPermit p, PlayerWeaponInstance weaponInstance) {
        LOG.debug("Processing load permit on server for {}", weaponInstance);

        ItemStack weaponItemStack = weaponInstance.getItemStack();

        if (weaponItemStack == null || !(weaponInstance.getPlayer() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) weaponInstance.getPlayer();
        Status status = Status.GRANTED;
        weaponInstance.setLoadIterationCount(0);
        Weapon weapon = (Weapon) weaponInstance.getItem();

        if (weaponItemStack.getTagCompound() == null)
            weaponItemStack.setTagCompound(new NBTTagCompound());

        List<ItemMagazine> compatibleMagazines = weapon.getCompatibleMagazines().stream().filter(
                compatibleMagazine -> WeaponAttachmentAspect.hasRequiredAttachments(compatibleMagazine, weaponInstance))
                .collect(Collectors.toList());
        List<ItemAttachment<Weapon>> compatibleBullets = weapon.getCompatibleAttachments(ItemBullet.class);
        
        boolean consumed = false;
        if (weapon.builder.ammo != null) {
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (itemstack != null && itemstack.getItem() == weapon.builder.ammo) {
                    itemstack.shrink(1);
                    consumed = true;
                    break;
                }
            }
        }

        if (!compatibleMagazines.isEmpty()) {
            ItemAttachment<Weapon> existingMagazine = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
            if (existingMagazine instanceof ItemMagazine) {
                int capacity = ((ItemMagazine) existingMagazine).getCapacity();
                int ammo = Tags.getAmmo(weaponItemStack);
                int needed = capacity - ammo;
                
                if (needed > 0 && !player.isCreative()) {
                    List<ItemBullet> comp = ((ItemMagazine) existingMagazine).getCompatibleBullets();
                    if (comp != null && !comp.isEmpty()) {
                        com.paneedah.mwc.items.equipment.ItemAmmoPack ammoPackItem = com.paneedah.mwc.init.MWCItems.ammoPack;
                        int consumedAmmo = 0;
                        
                        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                            ItemStack invStack = player.inventory.getStackInSlot(i);
                            if (invStack != null && invStack.getItem() == ammoPackItem) {
                                ItemBullet packBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(invStack);
                                if (comp.contains(packBullet)) {
                                    int packAmmo = com.paneedah.mwc.items.equipment.ItemAmmoPack.getAmmo(invStack);
                                    int toTake = Math.min(needed, packAmmo);
                                    if (toTake > 0) {
                                        consumedAmmo += toTake;
                                        needed -= toTake;
                                        
                                        if (packAmmo - toTake <= 0) {
                                            invStack.shrink(1);
                                            if (invStack.getCount() <= 0) {
                                                player.inventory.setInventorySlotContents(i, net.minecraft.item.ItemStack.EMPTY);
                                            }
                                        } else {
                                            com.paneedah.mwc.items.equipment.ItemAmmoPack.setAmmo(invStack, packAmmo - toTake);
                                        }
                                        
                                        if (needed <= 0) break;
                                    }
                                }
                            }
                        }
                        
                        if (consumedAmmo > 0) {
                            int newAmmo = ammo + consumedAmmo;
                            Tags.setAmmo(weaponItemStack, newAmmo);
                            weaponInstance.setAmmo(newAmmo);
                            player.world.playSound(null, player.posX, player.posY, player.posZ, weapon.getReloadSound(), player.getSoundCategory(), 1.0f, 1.0F);
                            weaponInstance.setMagazineRemoved(false);
            Tags.setMagazineRemoved(weaponItemStack, false);
                            status = Status.GRANTED;
                        } else {
                            status = Status.DENIED;
                        }
                    } else {
                        status = Status.DENIED;
                    }
                } else if (needed > 0 && player.isCreative()) {
                    Tags.setAmmo(weaponItemStack, capacity);
                    weaponInstance.setAmmo(capacity);
                    player.world.playSound(null, player.posX, player.posY, player.posZ, weapon.getReloadSound(), player.getSoundCategory(), 1.0f, 1.0F);
                    weaponInstance.setMagazineRemoved(false);
            Tags.setMagazineRemoved(weaponItemStack, false);
                            status = Status.GRANTED;
                } else {
                    status = Status.DENIED; // already full
                }
            } else {
                status = Status.DENIED; // no magazine attached
            }
        } else if (!compatibleBullets.isEmpty()) {
            int needed = Math.min(weapon.getMaxBulletsPerReload(), weapon.getAmmoCapacity() - weaponInstance.getAmmo());
            if (needed > 0 && !player.isCreative()) {
                com.paneedah.mwc.items.equipment.ItemAmmoPack ammoPackItem = com.paneedah.mwc.init.MWCItems.ammoPack;
                int consumedAmmo = 0;
                
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack invStack = player.inventory.getStackInSlot(i);
                    if (invStack != null && invStack.getItem() == ammoPackItem) {
                        ItemBullet packBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(invStack);
                        if (compatibleBullets.contains(packBullet)) {
                            int packAmmo = com.paneedah.mwc.items.equipment.ItemAmmoPack.getAmmo(invStack);
                            int toTake = Math.min(needed, packAmmo);
                            if (toTake > 0) {
                                consumedAmmo += toTake;
                                needed -= toTake;
                                
                                if (packAmmo - toTake <= 0) {
                                    invStack.shrink(1);
                                    if (invStack.getCount() <= 0) {
                                        player.inventory.setInventorySlotContents(i, net.minecraft.item.ItemStack.EMPTY);
                                    }
                                } else {
                                    com.paneedah.mwc.items.equipment.ItemAmmoPack.setAmmo(invStack, packAmmo - toTake);
                                }
                                
                                if (needed <= 0) break;
                            }
                        }
                    }
                }
                
                if (consumedAmmo > 0) {
                    int newAmmo = weaponInstance.getAmmo() + consumedAmmo;
                    Tags.setAmmo(weaponItemStack, newAmmo);
                    weaponInstance.setAmmo(newAmmo);
                    if (weapon.hasIteratedLoad())
                        weaponInstance.setLoadIterationCount(consumedAmmo);
                    player.world.playSound(null, player.posX, player.posY, player.posZ, weapon.getReloadSound(), player.getSoundCategory(), 1.0F, 1.0F);
                    weaponInstance.setMagazineRemoved(false);
            Tags.setMagazineRemoved(weaponItemStack, false);
                            status = Status.GRANTED;
                } else {
                    status = Status.DENIED;
                }
            } else if (needed > 0 && player.isCreative()) {
                int newAmmo = weaponInstance.getAmmo() + needed;
                Tags.setAmmo(weaponItemStack, newAmmo);
                weaponInstance.setAmmo(newAmmo);
                if (weapon.hasIteratedLoad())
                    weaponInstance.setLoadIterationCount(needed);
                player.world.playSound(null, player.posX, player.posY, player.posZ, weapon.getReloadSound(), player.getSoundCategory(), 1.0F, 1.0F);
                weaponInstance.setMagazineRemoved(false);
            Tags.setMagazineRemoved(weaponItemStack, false);
                            status = Status.GRANTED;
            } else {
                status = Status.DENIED;
            }
        } else if (consumed || player.isCreative()) {
            Tags.setAmmo(weaponItemStack, weapon.builder.ammoCapacity);
            weaponInstance.setAmmo(weapon.builder.ammoCapacity);
            player.world.playSound(null, player.posX, player.posY, player.posZ, weapon.getReloadSound(), player.getSoundCategory(), 1.0F, 1.0F);
        } else {
            LOG.debug("No suitable ammo found for {}. Permit denied.", weaponInstance);
            status = Status.DENIED;
        }

        p.setStatus(status);
    }

    private void prepareUnload(PlayerWeaponInstance weaponInstance) {
        weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getUnloadSound(), 1.0F, 1.0F);
    }

    private void processUnloadPermit(UnloadPermit p, PlayerWeaponInstance weaponInstance) {
        LOG.debug("Processing unload permit on server for {}", weaponInstance);

        if (!(weaponInstance.getPlayer() instanceof EntityPlayer))
            return;

        ItemStack weaponItemStack = weaponInstance.getItemStack();
        EntityPlayer player = (EntityPlayer) weaponInstance.getPlayer();
        Weapon weapon = (Weapon) weaponItemStack.getItem();

        if (weaponItemStack.getTagCompound() != null) {
            int ammo = Tags.getAmmo(weaponItemStack);
            if (ammo > 0 && !player.isCreative()) {
                ItemAttachment<Weapon> attachment = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
                List<? extends ItemAttachment<Weapon>> comp = null;
                
                if (attachment instanceof ItemMagazine) {
                    comp = ((ItemMagazine) attachment).getCompatibleBullets();
                } else {
                    comp = weapon.getCompatibleAttachments(ItemBullet.class);
                }
                
                if (comp != null && !comp.isEmpty()) {
                    ItemBullet bullet = (ItemBullet) comp.get(0);
                    com.paneedah.mwc.items.equipment.ItemAmmoPack ammoPackItem = com.paneedah.mwc.init.MWCItems.ammoPack;
                    
                    // Fill existing ammo packs
                    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                        ItemStack invStack = player.inventory.getStackInSlot(i);
                        if (invStack != null && invStack.getItem() == ammoPackItem) {
                            ItemBullet packBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(invStack);
                            if (packBullet == bullet) {
                                int packAmmo = com.paneedah.mwc.items.equipment.ItemAmmoPack.getAmmo(invStack);
                                if (packAmmo < 50) {
                                    int space = 50 - packAmmo;
                                    int toGive = Math.min(ammo, space);
                                    com.paneedah.mwc.items.equipment.ItemAmmoPack.setAmmo(invStack, packAmmo + toGive);
                                    ammo -= toGive;
                                    if (ammo <= 0) break;
                                }
                            }
                        }
                    }
                    
                    // Create new ammo packs
                    while (ammo > 0) {
                        int toGive = Math.min(ammo, 50);
                        ItemStack newPack = new ItemStack(ammoPackItem);
                        com.paneedah.mwc.items.equipment.ItemAmmoPack.setBullet(newPack, bullet);
                        com.paneedah.mwc.items.equipment.ItemAmmoPack.setAmmo(newPack, toGive);
                        if (!player.inventory.addItemStackToInventory(newPack)) {
                            player.dropItem(newPack, false);
                        }
                        ammo -= toGive;
                    }
                } else if (weapon.builder.ammo != null) {
                    // For grenade launchers etc.
                    while (ammo > 0) {
                        ItemStack newAmmo = new ItemStack(weapon.builder.ammo);
                        if (!player.inventory.addItemStackToInventory(newAmmo)) {
                            player.dropItem(newAmmo, false);
                        }
                        ammo--;
                    }
                }
            }

            Tags.setAmmo(weaponItemStack, 0);
            weaponInstance.setAmmo(0);
            weaponInstance.setMagazineRemoved(true);
            Tags.setMagazineRemoved(weaponItemStack, true);
            player.world.playSound(null, player.posX, player.posY, player.posZ, weapon.getUnloadSound(), player.getSoundCategory(), 1.0F, 1.0F);

            p.setStatus(Status.GRANTED);
        } else {
            p.setStatus(Status.DENIED);
        }
    }

    private void completeClientLoad(PlayerWeaponInstance weaponInstance, LoadPermit permit) {
        weaponInstance.setLoadAfterUnloadEnabled(false);
        if (permit == null) {
            LOG.error("Permit is null, something went wrong");
            return;
        }

        if (permit.getStatus() == Status.GRANTED)
            weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getReloadSound(), 1, 1);
    }

    private void completeClientUnload(PlayerWeaponInstance weaponInstance, UnloadPermit p) {
        if (weaponInstance.isLoadAfterUnloadEnabled()) {
            stateManager.changeState(this, weaponInstance, WeaponState.LOAD, WeaponState.ALERT);
            weaponInstance.setLoadAfterUnloadEnabled(false);
        }
    }

    public void inspect(PlayerWeaponInstance weaponInstance) {
        weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getInspectSound(), 1, 1);
    }

    public void draw(PlayerWeaponInstance weaponInstance) {
        // Draw sound is now handled entirely by WeaponRenderer's drawSoundDelay
    }

    public void startLoadIteration(PlayerWeaponInstance weaponInstance) {
        weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getReloadIterationSound(), 1, 1);
    }

    public void completeLoadIteration(PlayerWeaponInstance weaponInstance) {
        weaponInstance.setLoadIterationCount(weaponInstance.getLoadIterationCount() - 1);
    }

    public void completeAllLoadIterations(PlayerWeaponInstance weaponInstance) {
        weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getAllReloadIterationsCompletedSound(), 1, 1);
    }

    public void noCompoundInstructionsReceived(PlayerWeaponInstance weaponInstance) {
        weaponInstance.setDelayCompoundEnd(false);
        weaponInstance.setIsAwaitingCompoundInstructions(false);
        stateManager.changeState(this, weaponInstance, WeaponState.COMPOUND_RELOAD);
    }

    public void compoundInstructionsReceived(PlayerWeaponInstance weaponInstance) {
        weaponInstance.setIsAwaitingCompoundInstructions(false);
        // If magazine capacity is 31 or greater, or it doesn't have a tactical reload, use normal compound reload
        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
        int capacity = (activeMag instanceof ItemMagazine) ? ((ItemMagazine) activeMag).getCapacity() : 0;
        if (capacity >= 31 || !weaponInstance.getWeapon().getRenderer().getBuilder().isHasTacticalReload()) {
            stateManager.changeState(this, weaponInstance, WeaponState.COMPOUND_RELOAD);
            return;
        }

        weaponInstance.getWeapon().getRenderer().setMagicMag(weaponInstance,
                weaponInstance.getWeapon().getRenderer().magicMagReplacement, WeaponState.TACTICAL_RELOAD);

        stateManager.changeState(this, weaponInstance, WeaponState.TACTICAL_RELOAD);
    }

    public void noFurtherLoadInstructionsReceived(PlayerWeaponInstance weaponInstance) {
        stateManager.changeState(this, weaponInstance, WeaponState.LOAD, WeaponState.ALERT);
    }

    public void furtherLoadInstructionsReceived(PlayerWeaponInstance weaponInstance) {
        weaponInstance.setLoadAfterUnloadEnabled(true);
        stateManager.changeState(this, weaponInstance, WeaponState.UNLOAD, WeaponState.LOAD, WeaponState.ALERT);
    }

    private void afterReloadFinished(PlayerWeaponInstance instance) {
        boolean wasAimed = instance.wasAimedBeforeReload;
        instance.wasAimedBeforeReload = false;
        instance.suppressFovZoom = false;
        instance.wasReloadedFromEmpty = false;
        if (instance.getPlayer() != null && instance.getPlayer().world != null && instance.getPlayer().world.isRemote && !instance.getPlayer().isSprinting() && com.paneedah.weaponlib.config.ModernConfigManager.holdToAim && wasAimed) {
            checkAimKeyAndSetAim(instance);
        }
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void checkAimKeyAndSetAim(PlayerWeaponInstance instance) {
        if (instance.getPlayer() == null || instance.getPlayer().isSprinting()) {
            return;
        }

        boolean aimKeyHeld = false;
        int keyCode = net.minecraft.client.Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode();
        if (keyCode < 0) {
            aimKeyHeld = org.lwjgl.input.Mouse.isButtonDown(keyCode + 100);
        } else {
            aimKeyHeld = org.lwjgl.input.Keyboard.isKeyDown(keyCode);
        }

        if (!com.paneedah.weaponlib.config.ModernConfigManager.holdToAim || aimKeyHeld) {
            instance.setAimed(true);
        }
    }

    private void afterAllLoadIterationsCompleted(PlayerWeaponInstance instance) {
        completeAllLoadIterations(instance);
        afterReloadFinished(instance);
    }
}