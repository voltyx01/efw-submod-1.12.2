package com.voltyx.mwccf.furniture.client.audio;

import com.voltyx.mwccf.furniture.FurnitureSounds;
import com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GeneratorSound extends MovingSound {

    private final TileEntityElectricityGenerator generator;

    public GeneratorSound(TileEntityElectricityGenerator generator) {
        super(FurnitureSounds.BLOCK_ELECTRICITY_GENERATOR_ENGINE, SoundCategory.BLOCKS);
        this.generator = generator;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.6F;
        this.pitch = 1.0F;
        this.xPosF = generator.getPos().getX() + 0.5F;
        this.yPosF = generator.getPos().getY() + 0.5F;
        this.zPosF = generator.getPos().getZ() + 0.5F;
    }

    @Override
    public void update() {
        if (this.generator == null || this.generator.isInvalid() || !this.generator.isGeneratingPower()) {
            this.donePlaying = true;
            return;
        }

        this.xPosF = this.generator.getPos().getX() + 0.5F;
        this.yPosF = this.generator.getPos().getY() + 0.5F;
        this.zPosF = this.generator.getPos().getZ() + 0.5F;
    }
}
