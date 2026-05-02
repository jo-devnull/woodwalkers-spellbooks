package com.github.jodevnull.woodwalkers_spells.core;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config
{
    public static final ModConfigSpec.Builder mBuilder = new ModConfigSpec.Builder();
    public static final ModConfigSpec mSpec;

    public static final ModConfigSpec.BooleanValue isXpCostEnabled;
    public static final ModConfigSpec.BooleanValue xpCostInCreative;
    public static final ModConfigSpec.BooleanValue endlessSpell;
    public static final ModConfigSpec.BooleanValue endlessSpellOnCreative;
    public static final ModConfigSpec.BooleanValue canUseSpellsTransformed;
    public static final ModConfigSpec.ConfigValue<List<Integer>> xpLevelCost;
    public static final ModConfigSpec.ConfigValue<List<Integer>> spellDuration;

    static {
        mBuilder.push("Woodwalkers Spellbooks Configuration");

        isXpCostEnabled = mBuilder
            .comment("If the shapeshifting spell needs XP levels to work (default true)")
            .define("Requires XP", true);

        xpCostInCreative = mBuilder
            .comment("If the shapeshifting spell needs XP levels to work when in creative mode (default false)")
            .define("Requires XP On Creative", false);

        endlessSpell = mBuilder
            .comment("If set to true, players will stay transformed indefinitely (default false).")
            .define("Infinity Spell", false);

        endlessSpellOnCreative = mBuilder
            .comment("If set to true, players will stay transformed indefinitely when in creative mode (default true).")
            .define("Infinity Spell On Creative", true);

        canUseSpellsTransformed = mBuilder
            .comment("If players are allowed to use spell while transformed (default false).")
            .define("Spells while Transformed", false);

        spellDuration = mBuilder
            .comment("Defines how long the transformation lasts per spell level (1-6).")
            .comment("Should be a list of 6 integers, where each element in the list")
            .comment("corresponds to the duration at that level (starting from 1)")
            .define("Spell Duration per Level", List.of(30, 45, 75, 90, 120, 240), list -> isFixedList(list, 6));

        xpLevelCost = mBuilder
            .comment("Defines how much xp the shapeshifting spell costs per level (1-6).")
            .comment("Should be a list of 6 integers, where each element in the list")
            .comment("corresponds to the xp level needed at that level (starting from 1)")
            .define("Xp Level Cost", List.of(6, 5, 4, 3, 2, 1), list -> isFixedList(list, 6));

        mBuilder.pop();
        mSpec = mBuilder.build();
    }

    private static boolean isFixedList(Object list, int size) {
        return list instanceof List && ((List<?>) list).size() == size &&
            ((List<?>) list).stream().allMatch(item -> item instanceof Integer);
    }
}
