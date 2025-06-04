package com.github.jodevnull.woodwalkers_spells.core;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config
{
    public static final ForgeConfigSpec.Builder mBuilder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec mSpec;

    public static final ForgeConfigSpec.BooleanValue mXpCostEnabled;
    public static final ForgeConfigSpec.BooleanValue mXpCostCreative;
    public static final ForgeConfigSpec.BooleanValue mInfinitySpell;
    public static final ForgeConfigSpec.BooleanValue mInfinitySpellCreative;
    public static final ForgeConfigSpec.ConfigValue<Integer> mXpLevelCost;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> mSpellDuration;

    static {
        mBuilder.push("Woodwalkers Spellbooks Configuration");

        mXpCostEnabled = mBuilder
            .comment(" If the shapeshifting spell needs XP levels to work (default true)")
            .define("Requires XP", true);

        mXpCostCreative = mBuilder
            .comment(" If the shapeshifting spell needs XP levels to work when in creative mode (default false)")
            .define("Requires XP On Creative", false);

        mXpLevelCost = mBuilder
            .comment(" How much XP Levels the spell costs (default 4)")
            .define("Xp Level Cost", 4);

        mInfinitySpell = mBuilder
            .comment(" If set to true, players will stay transformed indefinitely (default false).")
            .define("Infinity Spell", false);

        mInfinitySpellCreative = mBuilder
            .comment(" If set to true, players will stay transformed indefinitely when in creative mode (default true).")
            .define("Infinity Spell On Creative", true);

        mSpellDuration = mBuilder
            .comment(" This should be a list with 6 numbers defining how long (in seconds) the transformation lasts.")
            .comment(" Each element position on this list corresponds to the spell level. For example, the first")
            .comment(" element determines the spell duration at level 1.")
            .comment(" Default: [30, 45, 75, 90, 120, 240]")
            .define("Spell Duration per Level", List.of(30, 45, 75, 90, 120, 240), list -> isFixedList(list, 6));

        mBuilder.pop();

        mSpec = mBuilder.build();
    }

    private static boolean isFixedList(Object list, int size) {
        return list instanceof List && ((List<?>) list).size() == size &&
            ((List<?>) list).stream().allMatch(item -> item instanceof Integer);
    }

    public static boolean getInfSpell() { return mInfinitySpell.get(); }

    public static boolean getInfSpellCreative() { return mInfinitySpellCreative.get(); }

    public static List<Integer> getSpellDuration() {
        return mSpellDuration.get();
    }

    public static boolean getXpCostEnabled() {
        return mXpCostEnabled.get();
    }

    public static boolean getXpCostCreative() {
        return mXpCostCreative.get();
    }

    public static int getXpCost() {
        return mXpLevelCost.get();
    }
}
