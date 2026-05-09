package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
    @Inject(at = @At("HEAD"), method = "repairPlayerItems", cancellable = true)
    private void PlayerLevelMending(ServerPlayer player, int amount, CallbackInfoReturnable<Integer> cir) {
        if(!ConfigManager.get().enableHarderMending) {
            return;
        }
        if(player.experienceLevel < 30) {
            cir.setReturnValue(amount);
        }
    }
}
