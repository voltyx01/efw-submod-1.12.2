package efw.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("EFWCorePlugin")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(-1000)
public class EFWCorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        System.out.println("[EFW-MIXIN-DEBUG] EFWCorePlugin: getMixinConfigs called -> registering efw.mixins.json, mixins.shouldersurfing.json");
        return Arrays.asList("efw.mixins.json", "mixins.shouldersurfing.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        System.out.println("[EFW-MIXIN-DEBUG] EFWCorePlugin: getASMTransformerClass called -> registering WeaponMasterTransformer, ShoulderTransformer");
        return new String[]{
            "efw.core.WeaponMasterTransformer",
            "com.teamderpy.shouldersurfing.asm.ShoulderTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}