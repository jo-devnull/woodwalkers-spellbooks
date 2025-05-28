package com.github.jodevnull.woodwalkers_spells.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;
import tocraft.craftedcore.registration.KeyBindingRegistry;
import tocraft.walkers.WalkersClient;
import tocraft.walkers.ability.AbilityOverlayRenderer;
import tocraft.walkers.api.model.EntityArms;
import tocraft.walkers.api.model.EntityUpdaters;
import tocraft.walkers.api.platform.ApiLevel;
import tocraft.walkers.eventhandler.ClientRespawnHandler;
import tocraft.walkers.impl.tick.KeyPressHandler;
import tocraft.walkers.network.ClientNetworking;
import tocraft.walkers.screen.hud.OverlayEventHandler;
import tocraft.walkers.screen.hud.VariantMenu;

@Mixin(WalkersClient.class)
public class MixinWalkersClient
{
    @Final
    @Shadow
    private VariantMenu variantMenu;

    @Inject(at = @At("HEAD"), method = "initialize", cancellable = true, remap = false)
    private void ww_spellbooks$initialize(CallbackInfo ci) {
        KeyBindingRegistry.register(WalkersClient.ABILITY_KEY);
        KeyBindingRegistry.register(WalkersClient.TRANSFORM_KEY);
        KeyBindingRegistry.register(WalkersClient.VARIANTS_MENU_KEY);
        EntityUpdaters.init();
        AbilityOverlayRenderer.register();
        EntityArms.init();
        ClientTickEvents.CLIENT_PRE.register(new KeyPressHandler());
        RenderEvents.HUD_RENDERING.register((guiGraphics, tickDelta) -> variantMenu.render(guiGraphics));
        ClientNetworking.registerPacketHandlers();
        OverlayEventHandler.initialize();

        ClientPlayerEvents.CLIENT_PLAYER_RESPAWN.register(new ClientRespawnHandler());
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.register((player) -> {
            if (player != null && ApiLevel.getClientLevel() != null) {
                ApiLevel.ON_API_LEVEL_CHANGE_EVENT.invoke().onApiLevelChange(ApiLevel.getClientLevel());
            }
        });

        ci.cancel();
    }
}
