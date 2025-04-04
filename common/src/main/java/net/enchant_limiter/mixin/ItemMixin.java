package net.enchant_limiter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enchant_limiter.EnchantLimiterMod;
import net.enchant_limiter.api.ItemComponentTypes;
import net.enchant_limiter.api.LimitComponent;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;getValidatedComponents()Lnet/minecraft/component/ComponentMap;")
    )
    private ComponentMap init_Wrap_getValidatedComponents_EnchantLimiter(
            // Mixin
            Item.Settings instance, Operation<ComponentMap> original
    ) {
        var config = EnchantLimiterMod.getConfig();
         var componentMap = original.call(instance);

        var isEnchantedBook = ((Object)this) instanceof EnchantedBookItem;
        var maxDamage = componentMap.get(DataComponentTypes.MAX_DAMAGE);
        var isDamageable = maxDamage != null && maxDamage > 0;

        if ( (isEnchantedBook && config.apply_to_enchanted_books)
            || (isDamageable && config.apply_to_damageable_items) ) {
            instance.component(ItemComponentTypes.ENCHANT_LIMITER, new LimitComponent(config.default_limit));
        }
        instance.component(ItemComponentTypes.ENCHANT_LIMITER, new LimitComponent(config.default_limit));

        return original.call(instance);
    }
}
