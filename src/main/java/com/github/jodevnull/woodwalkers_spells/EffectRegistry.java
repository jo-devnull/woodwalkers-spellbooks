package com.github.jodevnull.woodwalkers_spells;

import com.github.jodevnull.woodwalkers_spells.effects.ShapeshifterEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectRegistry
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
        DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, WoodwalkersSpellBooks.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> SHAPESHIFTER_EFFECT = MOB_EFFECTS.register("shapeshifter", () ->
        new ShapeshifterEffect(MobEffectCategory.NEUTRAL, 0x4e26ff)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
