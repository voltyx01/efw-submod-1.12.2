package techguns.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TGSpawnerTileEnt extends TileEntity implements ITickable {
    protected Random rand = new Random();
    protected int delay = 100;
    protected int spawndelay = 100;
    protected int mobsLeft;
    protected int maxActive;
    protected int wavesLeft;
    private boolean wasActive = false;
    
    /** 
     * true  = SOLDIER_SPAWN: pick ONE random mob type from config each wave 
     * false = HOLE (monster): spawn all mob types from config
     */
    protected boolean soldierMode = true;
    
    protected double spawnrange = 2d;
    protected List<EntityLiving> activeMobs = new ArrayList<>();
    
    // The chosen mob for soldier mode (chosen once per wave)
    protected String chosenMobForWave = null;

    public TGSpawnerTileEnt() {
        super();
        this.maxActive = efw.biomeinfo.MwccfConfig.techguns.spawners.maxMobsAlive;
        this.delay = efw.biomeinfo.MwccfConfig.techguns.spawners.delay;
        this.spawndelay = delay;
        this.wavesLeft = efw.biomeinfo.MwccfConfig.techguns.spawners.waves;
        this.mobsLeft = this.maxActive;
    }

    public void setParams(int mobsleft, int maxActive, int delay, int spawnrange) {
        this.mobsLeft = mobsleft;
        this.maxActive = maxActive;
        this.delay = delay;
        this.spawndelay = delay;
        this.spawnrange = spawnrange;
    }

    public void setWeaponOverride(ItemStack weapon) {
        // stub
    }

    public void addMobType(Class clazz, int weight) {
        // stub
    }
    
    public TGSpawnerTileEnt setSoldierMode(boolean soldier) {
        this.soldierMode = soldier;
        return this;
    }

    @Override
    public void update() {
        if (world.isRemote) return;

        double checkRange = 6.0;
        boolean playerNearby = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, checkRange, false) != null;

        if (!playerNearby) {
            wasActive = false; // Игрок ушел, сбрасываем состояние
            return;
        }

        // Если игрок только что вошел в зону, форсируем спавн быстрее
        if (!wasActive) {
            spawndelay = 20; // Спавн начнется через 1 секунду вместо 5
            wasActive = true;
        }

    // --- ДОБАВЛЕННЫЙ КОД ---
    // Проверяем наличие игрока в радиусе 32 блоков (можно настроить)
    if (world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, checkRange, false) == null) {
        return; // Игрока нет рядом, спавнер "засыпает"
    }
    // -----------------------

    // Clean up dead/unloaded mobs
    Iterator<EntityLiving> it = activeMobs.iterator();
        if (world.isRemote) return;

        // Clean up dead/unloaded mobs

        while (it.hasNext()) {
            EntityLiving mob = it.next();
            if (mob == null || mob.isDead || !world.loadedEntityList.contains(mob)) {
                it.remove();
            }
        }

        // Wave progression: start next wave when all active mobs dead and more waves left
        if (mobsLeft <= 0 && activeMobs.isEmpty() && wavesLeft > 0) {
            wavesLeft--;
            if (wavesLeft > 0) {
                mobsLeft = maxActive;
                spawndelay = delay;
                chosenMobForWave = null; // reset chosen mob for next wave
            }
        }

        // All waves done, all mobs dead → destroy block
        if (mobsLeft <= 0 && activeMobs.isEmpty() && wavesLeft <= 0) {
            world.destroyBlock(pos, false);
            return;
        }

        // Spawn more mobs if slots available
        if (mobsLeft > 0 && activeMobs.size() < maxActive) {
            if (spawndelay > 0) {
                spawndelay--;
            } else {
                if (soldierMode) {
                    spawnAllMobTypes(); // Раньше тут был Soldier, теперь Monster
                } else {
                    spawnSoldierMob();  // Раньше тут был Monster, теперь Soldier
                }
                spawndelay = delay;
            }
        }
    }
    
    /** Soldier mode: pick ONE random mob type (chosen once per wave) and spawn it */
    protected void spawnSoldierMob() {
        String[] configMobs = efw.biomeinfo.MwccfConfig.techguns.spawners.spawnerMobs;
        if (configMobs == null || configMobs.length == 0) return;
        
        // Pick mob type once per wave
        if (chosenMobForWave == null) {
            chosenMobForWave = configMobs[rand.nextInt(configMobs.length)];
        }
        
        if (trySpawnMob(chosenMobForWave)) {
            mobsLeft--;
        }
    }
    
    /** Monster spawner mode: cycle through all mob types, spawn each */
    protected void spawnAllMobTypes() {
        String[] configMobs = efw.biomeinfo.MwccfConfig.techguns.spawners.spawnerMobs;
        if (configMobs == null || configMobs.length == 0) return;
        
        for (String mobIdStr : configMobs) {
            if (mobsLeft <= 0 || activeMobs.size() >= maxActive) break;
            if (trySpawnMob(mobIdStr)) {
                mobsLeft--;
            }
        }
    }

    protected boolean trySpawnMob(String mobIdStr) {
        ResourceLocation mobId = new ResourceLocation(mobIdStr);
        Entity entity = EntityList.createEntityByIDFromName(mobId, world);

        if (entity instanceof EntityLiving) {
            EntityLiving mob = (EntityLiving) entity;
            double rx = (rand.nextDouble() * spawnrange * 2) - spawnrange;
            double rz = (rand.nextDouble() * spawnrange * 2) - spawnrange;
            
            double x = pos.getX() + 0.5 + rx;
            double y = pos.getY();
            double z = pos.getZ() + 0.5 + rz;
            
            mob.setLocationAndAngles(x, y, z, rand.nextFloat() * 360F, 0.0F);
            
            if (world.getCollisionBoxes(mob, mob.getEntityBoundingBox()).isEmpty()) {
                mob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(mob)), null);
                world.spawnEntity(mob);
                activeMobs.add(mob);

                // --- ДОБАВЛЕННЫЙ КОД ---
                // Создаем эффект частиц в месте спавна
                // Параметры: EnumParticleTypes, longDistance, x, y, z, xSpeed, ySpeed, zSpeed, параметры...
                for (int i = 0; i < 10; i++) { // Создаем 10 частиц
                    world.spawnParticle(
                        net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, 
                        x, y + 0.5, z, 
                        (rand.nextDouble() - 0.5) * 0.2, // небольшое случайное движение
                        0.1, 
                        (rand.nextDouble() - 0.5) * 0.2
                    );
                }
                // -----------------------

                return true;
            }
        }
        return false;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("soldierMode", soldierMode);
        compound.setInteger("mobsLeft", mobsLeft);
        compound.setInteger("wavesLeft", wavesLeft);
        compound.setInteger("spawndelay", spawndelay);
        return compound;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.soldierMode = compound.getBoolean("soldierMode");
        if (compound.hasKey("mobsLeft")) this.mobsLeft = compound.getInteger("mobsLeft");
        if (compound.hasKey("wavesLeft")) this.wavesLeft = compound.getInteger("wavesLeft");
        if (compound.hasKey("spawndelay")) this.spawndelay = compound.getInteger("spawndelay");
    }
}
