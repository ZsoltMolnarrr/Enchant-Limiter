package net.enchant_limiter;

import net.enchant_limiter.config.Config;
import net.tiny_config.ConfigManager;

public final class EnchantLimiterMod {
    public static final String ID = "enchant_limiter";

    private static ConfigManager<Config> config = new ConfigManager<>
            ("enchant_limiter", new Config())
            .builder()
            .sanitize(true)
            .build();

    private static boolean initialized = false;
    public static Config getConfig() {
        if (!initialized) {
            config.refresh();
            initialized = true;
        }
        return config.value;
    }

    public static void init() {
    }
}
