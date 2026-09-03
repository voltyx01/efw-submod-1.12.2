package techguns;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "mwccf")
public class TGSounds {

	public static SoundEvent BUNKER_DOOR_OPEN = createSoundEvent("blocks.metaldooropen");
	public static SoundEvent TECHDOOR_OPEN = createSoundEvent("blocks.techdoor.open");
	public static SoundEvent TECHDOOR_CLOSE = createSoundEvent("blocks.techdoor.close");
	public static SoundEvent TECHDOOR_STATE_FINISHED = createSoundEvent("blocks.techdoor.statefinished");

	private static SoundEvent createSoundEvent(String soundName) {
		ResourceLocation name = new ResourceLocation("techguns", soundName);
		return new SoundEvent(name).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(
			BUNKER_DOOR_OPEN,
			TECHDOOR_OPEN,
			TECHDOOR_CLOSE,
			TECHDOOR_STATE_FINISHED
		);
	}
}
