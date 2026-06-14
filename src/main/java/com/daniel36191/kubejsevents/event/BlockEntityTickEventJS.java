package com.daniel36191.kubejsevents.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityTickEventJS implements KubeEvent {
    private final BlockEntity blockEntity;
    private final ServerLevel level;
    private final BlockPos pos;

    public BlockEntityTickEventJS(BlockEntity blockEntity, ServerLevel level, BlockPos pos) {
        this.blockEntity = blockEntity;
        this.level = level;
        this.pos = pos;
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
}
