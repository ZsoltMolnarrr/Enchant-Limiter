package net.enchant_limiter.mixin;

import net.enchant_limiter.api.ItemComponentTypes;
import net.enchant_limiter.api.LimitHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "generateEnchantments", at = @At("RETURN"), cancellable = true)
    private static void generateEnchantments_Limited(Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        var limiter = stack.get(ItemComponentTypes.ENCHANT_LIMITER);
        if (limiter != null) {
            var selectedEnchantments = cir.getReturnValue();
            var limitCount = LimitHelper.getLimitCount(stack);
            if (!selectedEnchantments.isEmpty()
                    && selectedEnchantments.size() > limitCount) {
                var shuffled = new ArrayList<>(selectedEnchantments);
                Collections.shuffle(shuffled);
                var limit = Math.min(limitCount, shuffled.size());
                var selected = new ArrayList<>(shuffled.subList(0, limit));
                cir.setReturnValue(selected);
            }
        }
    }
}
