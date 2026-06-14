package com.daniel36191.mixins;

import com.daniel36191.kubejsevents.event.BlockEntityTickEventJS;
import com.daniel36191.kubejsevents.event.TickEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$BoundTickingBlockEntity")
public class BlockEntityMixin {
    @Shadow
    private BlockEntity blockEntity;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (blockEntity != null && blockEntity.hasLevel() && !blockEntity.isRemoved()) {
            Level level = blockEntity.getLevel();
            if (level instanceof ServerLevel serverLevel) {
                Block block = blockEntity.getBlockState().getBlock();
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                if (id != null) {
                    ResourceKey<Block> key = ResourceKey.create(
                            net.minecraft.core.registries.Registries.BLOCK, id
                    );
                    TickEvent.TICK.post(
                            new BlockEntityTickEventJS(blockEntity, serverLevel, blockEntity.getBlockPos()),
                            key
                    );
                }
            }
        }
    }
}
