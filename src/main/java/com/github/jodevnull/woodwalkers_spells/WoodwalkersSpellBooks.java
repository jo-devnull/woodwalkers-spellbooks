package com.github.jodevnull.woodwalkers_spells;

import com.github.jodevnull.woodwalkers_spells.core.Config;
import com.github.jodevnull.woodwalkers_spells.core.Shapeshifting;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;
import tocraft.walkers.api.PlayerShape;

@Mod(WoodwalkersSpellBooks.MODID)
public class WoodwalkersSpellBooks
{
    public static final String MODID = "woodwalkers_spellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WoodwalkersSpellBooks(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.mSpec, "woodwalkers-spellbooks.toml");

        SpellRegistry.register(modEventBus);
        EffectRegistry.register(modEventBus);
    }

    public static void playsound(ServerPlayer player, SoundEvent sound, SoundSource source) {
        player.serverLevel().playSound(null, player.blockPosition(), sound, source);
    }

    @EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onSpellPreCast(SpellPreCastEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (!Config.canUseSpellsTransformed.get() && Shapeshifting.isTransformed(player))
                    event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (player.tickCount % 20 != 0 || player.isCreative() || Shapeshifting.infinitySpell(player))
                    return;

                final var shapeshiftEffect = EffectRegistry.SHAPESHIFTER_EFFECT;
                final var secondShape = PlayerShape.getCurrentShape(player);

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
