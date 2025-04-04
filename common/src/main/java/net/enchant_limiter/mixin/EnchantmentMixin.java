package net.enchant_limiter.mixin;

import net.enchant_limiter.api.ItemComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void isAcceptableItem_HEAD_EnchantLimiter(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        var limiter = stack.get(ItemComponentTypes.ENCHANT_LIMITER);
        var existingEnchantments = stack.getEnchantments();
        if (limiter != null && existingEnchantments != null) {
            if (existingEnchantments.getEnchantments().size() >= limiter.count()) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
