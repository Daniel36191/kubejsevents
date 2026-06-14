package com.daniel36191.kubejsevents;

import com.daniel36191.kubejsevents.event.TickEvent;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.fml.common.Mod;

@Mod(KubeJSEvents.MOD_ID)
public class KubeJSEvents implements KubeJSPlugin {
    public static final String MOD_ID = "kubejsevents";

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(TickEvent.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry event) {
        event.add("Events", KubeJSEventsBindings.class);
    }
}