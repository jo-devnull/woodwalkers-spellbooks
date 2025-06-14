package com.github.jodevnull.woodwalkers_spells.spells;

import com.github.jodevnull.woodwalkers_spells.core.Config;
import com.github.jodevnull.woodwalkers_spells.WoodwalkersSpellBooks;
import com.github.jodevnull.woodwalkers_spells.core.Shapeshifting;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import tocraft.walkers.api.PlayerShapeChanger;
import tocraft.walkers.api.variant.ShapeType;

import java.util.List;
import java.util.Optional;

import static com.github.jodevnull.woodwalkers_spells.core.Shapeshifting.*;

@AutoSpellConfig
public class ShapeshiftingSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(WoodwalkersSpellBooks.MODID, "shapeshifting");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        int duration = Shapeshifting.getShapeshiftDuration(spellLevel);
        int xpNeeded = getRequiredXPlevel(spellLevel);

        return List.of(
            Component.translatable("ui.woodwalkers_spellbooks.xp_required", xpNeeded),
            Component.translatable("ui.woodwalkers_spellbooks.spell_duration", duration)
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.RARE)
        .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
        .setMaxLevel(6)
        .setCooldownSeconds(40)
        .build();

    public ShapeshiftingSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 60;
        this.baseManaCost = 40;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        boolean canCast = true;
        LivingEntity target = getTarget(level, entity, playerMagicData);

        if (entity instanceof ServerPlayer player) {
            if (target != null && !Shapeshifting.isTransformed(player)) {
                if (needsXp(player)) {
                    canCast = player.experienceLevel >= getRequiredXPlevel(spellLevel);

                    if (!canCast)
                        player.sendSystemMessage(errorMessage("ui.woodwalkers_spellbooks.not_enough_xp"), true);
                }

                if (canCast)
                    Utils.preCastTargetHelper(level, entity, playerMagicData, this, 16, .25f, false);

            } else if (!hasSecondShape(player)) {
                canCast = false;
                player.sendSystemMessage(errorMessage("ui.woodwalkers_spellbooks.cant_shapeshift"), true);
            }
        }

        return canCast;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int requiredLevel = getRequiredXPlevel(spellLevel);
        LivingEntity target = getTarget(level, entity, playerMagicData);

        if (entity instanceof ServerPlayer player) {
            if (target != null && !Shapeshifting.isTransformed(player)) {
                PlayerShapeChanger.change2ndShape(player, ShapeType.from(target));
                doShapeshift(player, spellLevel);

                if (needsXp(player))
                    player.giveExperienceLevels(-requiredLevel);
            } else {
                doShapeshift(player, spellLevel);
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private static LivingEntity getTarget(Level level, LivingEntity entity, MagicData playerMagicData) {
        LivingEntity target = null;

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            target = castTargetingData.getTarget((ServerLevel) level);
        }

        if (target == null) {
            HitResult raycast = Utils.raycastForEntity(level, entity, 32, true, .25f);
            if (raycast.getType() == HitResult.Type.ENTITY) {
                if (((EntityHitResult) raycast).getEntity() instanceof LivingEntity livingEntity)
                    target = livingEntity;
            }
        }

        return target;
    }

    static int getRequiredXPlevel(int spellLevel) {
        return Config.xpLevelCost.get().get(spellLevel - 1);
    }

    static boolean needsXp(ServerPlayer player) {
        return (player.isCreative() && Config.xpCostInCreative.get()) ||
            (!player.isCreative() && Config.isXpCostEnabled.get());
    }
}
