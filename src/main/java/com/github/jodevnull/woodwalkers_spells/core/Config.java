package com.github.jodevnull.woodwalkers_spells.core;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config
{
    public static final ForgeConfigSpec.Builder mBuilder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec mSpec;

    public static final ForgeConfigSpec.ConfigValue<Integer> mXpLevelCost;
    public static final ForgeConfigSpec.BooleanValue mXpCostEnabled;

    public static final ForgeConfigSpec.ConfigValue<List<Integer>> mSpellDuration;

    static {
        mBuilder.push("Woodwalkers Spellbooks Configuration");

        mXpCostEnabled = mBuilder
            .comment(" If the shapeshifting spell needs XP levels to work (default true)")
            .define("Requires XP Levels", true);

        mXpLevelCost = mBuilder
            .comment(" How much XP Levels the spell costs (default 4)")
            .define("Xp Level Cost", 4);

        mSpellDuration = mBuilder
            .comment(" This should be a list with 6 numbers defining the duration (in seconds) of each transformation")
            .comment(" Format: [<level-1>, <level-2>, ..., <level-6>]")
            .comment(" Default: [30, 45, 75, 90, 120, 240]")
            .define("Spell Duration per Level", List.of(30, 45, 75, 90, 120, 240), list -> isFixedList(list, 6));

        mBuilder.pop();

        mSpec = mBuilder.build();
    }

    private static boolean isFixedList(Object list, int size) {
        return list instanceof List && ((List<?>) list).size() == size &&
            ((List<?>) list).stream().allMatch(item -> item instanceof Integer);
    }

    public static List<Integer> getSpellDuration() {
        return mSpellDuration.get();
    }

    public static boolean getXpCostEnabled() {
        return mXpCostEnabled.get();
    }

    public static int getXpCost() {
        return mXpLevelCost.get();
    }
}
