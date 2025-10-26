package net.enchant_limiter.config;

public class Config {
    public int default_limit = 3;
    public boolean apply_to_enchanted_books = true;
    public boolean apply_to_damageable_items = true;
    public boolean query_rarity_from_underlying_item = true;
    public int extra_limit_for_uncommon = 0;
    public int extra_limit_for_rare = 1;
    public int extra_limit_for_epic = 1;
    public int extra_limit_for_epic_plus = 1;
}
