package com.github.jodevnull.woodwalkers_spells;

import com.github.jodevnull.woodwalkers_spells.effects.ShapeshifterEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WoodwalkersSpellBooks.MODID);

    public static final RegistryObject<MobEffect> SHAPESHIFTER_EFFECT = MOB_EFFECTS.register("shapeshifter", () ->
        new ShapeshifterEffect(MobEffectCategory.NEUTRAL, 0x4e26ff)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
