package com.sg.dminus.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;


import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;
import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    @Deprecated
    public static boolean stacksEqual(List<ItemStack> left, List<ItemStack> right) {
        return false;
    }

    @Inject(
            method = "onDurabilityChange",
            at = @At("TAIL")
    )
    private void PerformanceChange(int damage, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;
        ensureInit(stack);
        stack.set(
                PERFORMANCE_PENALTY_PERCENTAGE,
                PerformancePenaltyCalc(stack)
        );
    }

    @Inject(
            method = "damage*",
            at = @At("HEAD")
    )
    private void RatioInit(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;

        if (!stack.isEmpty()) {
            ensureInit(stack);
        }
    }

}
