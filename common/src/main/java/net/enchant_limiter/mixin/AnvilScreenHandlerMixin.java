package net.enchant_limiter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enchant_limiter.api.ItemComponentTypes;
import net.enchant_limiter.api.LimitHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @WrapOperation(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/AnvilScreenHandler;sendContentUpdates()V"
            )
    )
    private void updateResult_EnchantLimiter(AnvilScreenHandler instance, Operation<Void> original) {
        var outputStack = output.getStack(0);
        var limiter = outputStack.get(ItemComponentTypes.ENCHANT_LIMITER);
        if (limiter != null) {
            var limitCount = LimitHelper.getLimitCount(outputStack);
            var enchantments = outputStack.get(DataComponentTypes.ENCHANTMENTS);
            if (enchantments != null && enchantments.getEnchantments().size() > limitCount) {
                output.setStack(0, ItemStack.EMPTY);
                levelCost.set(0);
            }
        }

        original.call(instance);
    }
}
