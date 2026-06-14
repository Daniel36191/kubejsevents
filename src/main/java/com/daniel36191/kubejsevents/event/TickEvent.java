package com.daniel36191.kubejsevents.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public interface TickEvent {
    EventGroup GROUP = EventGroup.of("KubeJSEvents");
    EventTargetType<ResourceKey<Block>> TARGET = EventTargetType.registryKey(Registries.BLOCK, Block.class);
    TargetedEventHandler<ResourceKey<Block>> TICK = GROUP.server("tick", () -> BlockEntityTickEventJS.class)
            .supportsTarget(TARGET);
    EventHandler FLUID_TANK_CHANGED = GROUP.server("fluidTankChanged", () -> FluidTankChangedEventJS.class);
}
