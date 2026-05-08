package com.sg.dminus.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @Inject(at = @At("HEAD"), method = "repairPlayerGears", cancellable = true)
    private void PlayerLevelMending(ServerPlayerEntity player, int amount, CallbackInfoReturnable<Integer> cir) {
        if(player.experienceLevel < 30) {
            cir.setReturnValue(amount);
        }
    }
}
