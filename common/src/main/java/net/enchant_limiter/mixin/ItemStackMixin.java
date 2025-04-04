package net.enchant_limiter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enchant_limiter.api.ItemComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V"))
    private void injected(ItemStack instance, ComponentType<?> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, Operation<Void> original) {
        if (componentType == DataComponentTypes.ENCHANTMENTS) {
            var limiter = instance.get(ItemComponentTypes.ENCHANT_LIMITER);
            var existingEnchantments = instance.getEnchantments();
            if (limiter != null && existingEnchantments != null) {
                var enchantmentCount = existingEnchantments.getEnchantments().size();
                if (enchantmentCount > 0) {
                    textConsumer.accept(Text.literal(""));
                    textConsumer.accept(Text.translatable("item.enchant_limiter.enchantment_limit", enchantmentCount, limiter.count()).formatted(Formatting.BLUE));
                }
            }
        }
        original.call(instance, componentType, context, textConsumer, type);
    }
}
