package efw.mixin;

import com.voltyx.mwccf.speech.client.SpeechPanelController;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(NetHandlerPlayClient.class)
public abstract class MixinSpeechTabComplete {
    @Inject(method = "handleTabComplete(Lnet/minecraft/network/play/server/SPacketTabComplete;)V", at = @At("HEAD"))
    private void speech$receiveServerSuggestions(SPacketTabComplete packet, CallbackInfo ci) {
        SpeechPanelController.acceptSuggestions(packet.getMatches());
    }
}
