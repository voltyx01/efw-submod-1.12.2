package efw.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

public class EFWLateMixinLoader implements ILateMixinLoader {

    static {
        System.out.println("[EFW-MIXIN-LOAD] EFWLateMixinLoader class loaded!");
    }

    @Override
    public List<String> getMixinConfigs() {
        System.out.println("[EFW-MIXIN-DEBUG] EFWLateMixinLoader: getMixinConfigs called -> registering efw.late.mixins.json");
        return Collections.singletonList("efw.late.mixins.json");
    }
}
