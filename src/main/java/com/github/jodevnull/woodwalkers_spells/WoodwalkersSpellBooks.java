package com.github.jodevnull.woodwalkers_spells;

import com.github.jodevnull.woodwalkers_spells.core.Config;
import com.github.jodevnull.woodwalkers_spells.core.Shapeshifting;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import tocraft.walkers.api.PlayerShape;

@Mod(WoodwalkersSpellBooks.MODID)
public class WoodwalkersSpellBooks {

    public static final String MODID = "woodwalkers_spellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WoodwalkersSpellBooks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.mSpec, "woodwalkers-spellbooks.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SpellRegistry.register(modEventBus);
        EffectRegistry.register(modEventBus);
    }

    @Mod.EventBusSubscriber
    public static class PlayerTickHandler {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
                if (event.player instanceof ServerPlayer player) {
                    if (player.tickCount % 20 != 0 || player.isCreative() || Shapeshifting.infinitySpell(player))
                        return;

                    MobEffect shapeshiftEffect = EffectRegistry.SHAPESHIFTER_EFFECT.get();
                    LivingEntity secondShape = PlayerShape.getCurrentShape(player);

                    if (!player.hasEffect(shapeshiftEffect) && secondShape != null) {
                        Shapeshifting.doShapeshift(player, 1);
                    }

                    else if (player.hasEffect(shapeshiftEffect) && secondShape == null) {
                        player.removeEffect(shapeshiftEffect);
                    }
                }
            }
        }
    }
}
