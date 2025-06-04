package com.github.jodevnull.woodwalkers_spells.core;

import com.github.jodevnull.woodwalkers_spells.EffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import tocraft.craftedcore.patched.CEntity;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.api.variant.ShapeType;
import tocraft.walkers.impl.PlayerDataProvider;

import java.util.List;

public class Shapeshifting
{
    public static ShapeType<LivingEntity> getSecondShape(ServerPlayer player) {
        ShapeType<LivingEntity> type = null;

        if (PlayerShape.getCurrentShape(player) == null)
            type = (ShapeType<LivingEntity>) ((PlayerDataProvider) player).walkers$get2ndShape();

        return type;
    }

    public static boolean hasSecondShape(ServerPlayer player) {
        return ((PlayerDataProvider) player).walkers$get2ndShape() != null;
    }

    public static void doShapeshift(ServerPlayer player, int spellLevel) {
        var type = getSecondShape(player);

        if (type == null)
            PlayerShape.updateShapes(player, null);
        else {
            MobEffect effect = EffectRegistry.SHAPESHIFTER_EFFECT.get();
            int duration = 20 * getShapeshiftDuration(spellLevel);

            PlayerShape.updateShapes(player, type.create(CEntity.level(player), player));

            if (!infinitySpell(player))
                player.addEffect(new MobEffectInstance(effect, duration, 1, false, false));
        }
    }

    public static int getShapeshiftDuration(int spellLevel) {
        List<Integer> durations = Config.getSpellDuration();
        return durations.get(Math.max(0, Math.min(spellLevel - 1, durations.size())));
    }

    public static boolean infinitySpell(ServerPlayer player) {
        return (player.isCreative() && Config.getInfSpellCreative()) || (!player.isCreative() && Config.getInfSpell());
    }

    public static MutableComponent errorMessage(String name) {
        return Component.translatable(name).withStyle(style -> style.withColor(0xFF3333));
    }
}
