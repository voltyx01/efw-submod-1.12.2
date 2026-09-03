package efw.client;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AmbientBiomeSound extends MovingSound {

    private final EntityPlayer player;

    private boolean isFadingOut = false;
    private int fadeTimer = 0;
    private static final int FADE_TIME = 40; // 2 seconds

    public AmbientBiomeSound(EntityPlayer player, SoundEvent soundEvent) {
        super(soundEvent, SoundCategory.AMBIENT);
        this.player = player;
        this.repeat = false;
        this.volume = 0.001F; // Start slightly above 0 for fade in, 0.0 causes skip
        this.pitch = 1.0F;
    }

    @Override
    public void update() {
        if (this.player.isDead) {
            this.donePlaying = true;
            return;
        }

        // Handle fading
        if (this.isFadingOut) {
            this.fadeTimer--;
            if (this.fadeTimer <= 0) {
                this.donePlaying = true;
            }
        } else {
            if (this.fadeTimer < FADE_TIME) {
                this.fadeTimer++;
            }
        }
        
        // Max volume can be 1.0F, but loop sounds might be better at 0.8F
        float targetVolume = ((float) this.fadeTimer / FADE_TIME) * 0.8F;
        this.volume = Math.max(0.001F, targetVolume);

        // Follow the player
        this.xPosF = (float) this.player.posX;
        this.yPosF = (float) this.player.posY;
        this.zPosF = (float) this.player.posZ;
    }

    public void stopPlaying() {
        if (!this.isFadingOut) {
            this.isFadingOut = true;
            // The fadeTimer is already at FADE_TIME if it was fully faded in
        }
    }
}
