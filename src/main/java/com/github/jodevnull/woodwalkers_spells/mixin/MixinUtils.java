package com.github.jodevnull.woodwalkers_spells.mixin;

import com.github.jodevnull.woodwalkers_spells.core.Config;
import com.github.jodevnull.woodwalkers_spells.core.Shapeshifting;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Utils.class)
public class MixinUtils
{
    @Inject(
        at = @At("HEAD"),
        remap = false,
        cancellable = true,
        method = "preCastTargetHelper(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lio/redspace/ironsspellbooks/api/magic/MagicData;Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;IFZLjava/util/function/Predicate;)Z"
    )
    private static void ww_spellbooks$preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist, boolean sendFailureMessage, Predicate<LivingEntity> filter, CallbackInfoReturnable<Boolean> cir) {
        if (caster instanceof ServerPlayer player) {
            if (Shapeshifting.isTransformed(player) && !Config.canUseSpellsTransformed.get()) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
