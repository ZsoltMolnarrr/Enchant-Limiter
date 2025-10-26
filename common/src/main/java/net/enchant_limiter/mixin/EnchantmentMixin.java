package net.enchant_limiter.mixin;

import net.enchant_limiter.api.ItemComponentTypes;
import net.enchant_limiter.api.LimitHelper;
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
            var enchantment = (Enchantment) (Object) this;
            boolean existingEnchantment = existingEnchantments.getEnchantments().stream()
                    .anyMatch(entry -> entry.value().equals(enchantment));
            var limitCount = LimitHelper.getLimitCount(stack);
            boolean atLimit = existingEnchantments.getEnchantments().size() >= limitCount;
            if (atLimit && !existingEnchantment) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
