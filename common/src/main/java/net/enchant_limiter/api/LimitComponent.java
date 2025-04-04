package net.enchant_limiter.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LimitComponent(int count) {
    public static final int DEFAULT_COUNT = 3;
    public static final Codec<LimitComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("count", DEFAULT_COUNT).forGetter(LimitComponent::count)
    ).apply(instance, LimitComponent::new));
}
