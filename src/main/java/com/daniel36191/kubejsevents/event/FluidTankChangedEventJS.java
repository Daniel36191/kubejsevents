package com.daniel36191.kubejsevents.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidTankChangedEventJS implements KubeEvent {
    private final BlockEntity blockEntity;
    private final ServerLevel level;
    private final BlockPos pos;
    private final Object fluidStack;

    public FluidTankChangedEventJS(BlockEntity blockEntity, ServerLevel level, BlockPos pos, Object fluidStack) {
        this.blockEntity = blockEntity;
        this.level = level;
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public BlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Object getFluid() {
        return this.fluidStack;
    }
}
