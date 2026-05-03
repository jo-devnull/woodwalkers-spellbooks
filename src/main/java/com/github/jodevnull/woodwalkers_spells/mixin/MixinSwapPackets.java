package com.github.jodevnull.woodwalkers_spells.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.network.impl.SwapPackets;

@Mixin(value = SwapPackets.class, remap = false)
public class MixinSwapPackets
{
    @WrapOperation(
        method = "lambda$registerWalkersRequestPacketHandler$0",
        at = @At(
            value = "INVOKE",
            target = "Ltocraft/walkers/api/PlayerShape;updateShapes(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/LivingEntity;)Z"
        )
    )
    private static boolean ww_spellbooks$disableMorph(ServerPlayer player, LivingEntity entity, Operation<Boolean> original) {
        if (PlayerShape.getCurrentShape(player) != null) {
            return original.call(player, entity);
        }

        return false;
    }
}