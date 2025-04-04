package net.enchant_limiter.api;

import net.enchant_limiter.EnchantLimiterMod;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ItemComponentTypes {
    public static final ComponentType<LimitComponent> ENCHANT_LIMITER = register(Identifier.of(EnchantLimiterMod.ID, "limit"),
            builder -> builder.codec(LimitComponent.CODEC)
    );

    private static <T> ComponentType<T> register(Identifier id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
}
