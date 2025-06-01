package com.github.jodevnull.woodwalkers_spells.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ShapeshifterEffect extends MobEffect
{
    public ShapeshifterEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);
    }
}
