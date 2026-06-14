package com.daniel36191.adpotherkubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.fml.common.Mod;

@Mod(AdPotherKubeJS.MOD_ID)
public class AdPotherKubeJS implements KubeJSPlugin {
    public static final String MOD_ID = "adpotherkubejs";

    @Override
    public void registerBindings(BindingRegistry event) {
        event.add("AdPother", AdPotherBindings.class);
    }
}