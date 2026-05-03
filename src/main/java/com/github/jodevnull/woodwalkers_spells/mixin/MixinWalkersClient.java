package com.github.jodevnull.woodwalkers_spells.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tocraft.walkers.WalkersClient;

@Mixin(value = WalkersClient.class, remap = false)
public class MixinWalkersClient
{
    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Ltocraft/craftedcore/registration/KeyBindingRegistry;register(Lnet/minecraft/client/KeyMapping;)V", ordinal = 2))
    private void ww_spellbooks$removeScanKeybind(KeyMapping keyMapping, Operation<Void> original) {
        // Unlock keybind is removed, so players need to use the spell to transform into other mobs
    }
}