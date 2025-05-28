package com.github.jodevnull.woodwalkers_spells.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.patched.TComponent;
import tocraft.walkers.Walkers;
import tocraft.walkers.WalkersClient;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.api.platform.ApiLevel;
import tocraft.walkers.api.variant.ShapeType;
import tocraft.walkers.impl.tick.KeyPressHandler;
import tocraft.walkers.network.impl.SwapPackets;
import tocraft.walkers.network.impl.SwapVariantPackets;

@Mixin(KeyPressHandler.class)
public abstract class MixinKeypressHandler
{
    @Shadow
    protected abstract void handleAbilityKey(Minecraft client);

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true, remap = false)
    private void ww_spellbooks$tick(Minecraft client, CallbackInfo ci) {
        if (client.player != null) {
            if (WalkersClient.ABILITY_KEY.consumeClick()) {
                handleAbilityKey(client);
            }

            if (WalkersClient.TRANSFORM_KEY.consumeClick()) {
                if (ApiLevel.getCurrentLevel().canMorph) {
                    if (PlayerShape.getCurrentShape(client.player) != null)
                        SwapPackets.sendSwapRequest();
                } else {
                    client.player.displayClientMessage(TComponent.translatable("walkers.feature_not_available", new Object[0]), true);
                }
            }

            if (WalkersClient.VARIANTS_MENU_KEY.consumeClick() && Walkers.CONFIG.unlockEveryVariant) {
                if (ApiLevel.getCurrentLevel().allowVariantsMenu) {
                    LivingEntity shape = PlayerShape.getCurrentShape(client.player);
                    if (shape != null) {
                        ShapeType<?> shapeType = ShapeType.from(shape);
                        if (WalkersClient.isRenderingVariantsMenu) {
                            SwapVariantPackets.sendSwapRequest(shapeType.getVariantData() + WalkersClient.variantOffset);
                        }

                        WalkersClient.variantOffset = 0;
                        WalkersClient.isRenderingVariantsMenu = !WalkersClient.isRenderingVariantsMenu;
                    }
                } else {
                    client.player.displayClientMessage(TComponent.translatable("walkers.feature_not_available", new Object[0]), true);
                }
            }

            if (WalkersClient.isRenderingVariantsMenu && (client.options.hideGui || !Walkers.CONFIG.unlockEveryVariant || client.screen != null || PlayerShape.getCurrentShape(client.player) == null)) {
                WalkersClient.isRenderingVariantsMenu = false;
            }
        }

        ci.cancel();
    }
}
