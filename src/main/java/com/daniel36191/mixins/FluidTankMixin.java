package com.daniel36191.mixins;

import com.daniel36191.kubejsevents.event.FluidTankChangedEventJS;
import com.daniel36191.kubejsevents.event.TickEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.simibubi.create.content.fluids.tank.FluidTankBlockEntity")
public class FluidTankMixin {
    @Inject(method = "onFluidStackChanged(Lnet/neoforged/neoforge/fluids/FluidStack;)V", at = @At("HEAD"), require = 0)
    private void onFluidStackChanged(FluidStack stack, CallbackInfo ci) {
        BlockEntity be = (BlockEntity) (Object) this;
        if (be.hasLevel() && be.getLevel() instanceof ServerLevel serverLevel) {
            TickEvent.FLUID_TANK_CHANGED.post(
                    new FluidTankChangedEventJS(be, serverLevel, be.getBlockPos(), stack)
            );
        }
    }
}
