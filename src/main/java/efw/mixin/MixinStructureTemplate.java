package efw.mixin;

import com.voltyx.mwccf.zone.StructureZoneHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Template.class)
public abstract class MixinStructureTemplate {

    @Unique
    private NBTTagList mwccf$questZones = new NBTTagList();

    @Inject(method = "takeBlocksFromWorld", at = @At("TAIL"))
    private void onTakeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos size, boolean takeEntities,
                                       @Nullable Block toIgnore, CallbackInfo ci) {
        if (!worldIn.isRemote) {
            this.mwccf$questZones = StructureZoneHelper.captureZones(worldIn, startPos, size);
        }
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    private void onWriteToNBT(NBTTagCompound nbt, CallbackInfoReturnable<NBTTagCompound> cir) {
        if (this.mwccf$questZones != null && this.mwccf$questZones.tagCount() > 0) {
            nbt.setTag(StructureZoneHelper.NBT_KEY, this.mwccf$questZones.copy());
        }
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void onRead(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey(StructureZoneHelper.NBT_KEY, Constants.NBT.TAG_LIST)) {
            this.mwccf$questZones = compound.getTagList(StructureZoneHelper.NBT_KEY, Constants.NBT.TAG_COMPOUND).copy();
        } else {
            this.mwccf$questZones = new NBTTagList();
        }
    }

    @Inject(method = "addBlocksToWorld(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/structure/template/ITemplateProcessor;Lnet/minecraft/world/gen/structure/template/PlacementSettings;I)V", at = @At("TAIL"))
    private void onAddBlocksToWorld(World worldIn, BlockPos pos, @Nullable ITemplateProcessor templateProcessor,
                                    PlacementSettings placementIn, int flags, CallbackInfo ci) {
        if (!worldIn.isRemote && this.mwccf$questZones != null && this.mwccf$questZones.tagCount() > 0) {
            StructureZoneHelper.applyZonesToWorld(worldIn, pos, placementIn, this.mwccf$questZones);
        }
    }
}