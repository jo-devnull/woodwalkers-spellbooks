package com.github.jodevnull.woodwalkers_spells.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tocraft.walkers.WalkersClient;
import tocraft.walkers.screen.hud.VariantMenu;

@Mixin(WalkersClient.class)
public class MixinWalkersClient
{
    @Final
    @Shadow
    private VariantMenu variantMenu;

    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Ltocraft/craftedcore/registration/KeyBindingRegistry;register(Lnet/minecraft/client/KeyMapping;)V", ordinal = 2))
    private void ww_spellbooks$removeScanKeybind(KeyMapping keyMapping, Operation<Void> original) {
        // Unlock keybind is removed, so players need to use the spell to transform into other mobs
    }
}
