package net.enchant_limiter.api;

import net.enchant_limiter.EnchantLimiterMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class LimitHelper {
    public static int getLimitCount(ItemStack itemStack) {
        var limiter = itemStack.get(ItemComponentTypes.ENCHANT_LIMITER);
        if (limiter != null) {
            var limit = limiter.count();
            var config = EnchantLimiterMod.getConfig();
            var rarity = config.query_rarity_from_underlying_item
                    ? itemStack.getItem().getComponents().getOrDefault(DataComponentTypes.RARITY, Rarity.COMMON)
                    : itemStack.getRarity();
            if (rarity == Rarity.UNCOMMON) {
                limit += config.extra_limit_for_uncommon;
            } else if (rarity == Rarity.RARE) {
                limit += config.extra_limit_for_rare;
            } else if (rarity == Rarity.EPIC) {
                limit += config.extra_limit_for_epic;
            } else if (rarity.ordinal() >= Rarity.EPIC.ordinal() + 1) {
                limit += config.extra_limit_for_epic_plus;
            }
            return limit;
        }
        return 0;
    }
}
