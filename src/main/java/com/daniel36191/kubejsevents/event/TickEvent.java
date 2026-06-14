package com.daniel36191.kubejsevents.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface TickEvent {
    EventGroup GROUP = EventGroup.of("KubeJSEvents");
    EventHandler TICK = GROUP.server("tick", () -> BlockEntityTickEventJS.class);
}
