package com.github.jodevnull.woodwalkers_spells.spells;

import com.github.jodevnull.woodwalkers_spells.WoodwalkersSpellBooks;
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
import tocraft.craftedcore.patched.CEntity;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.api.PlayerShapeChanger;
import tocraft.walkers.api.variant.ShapeType;
import tocraft.walkers.impl.PlayerDataProvider;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class ShapeshiftingSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(WoodwalkersSpellBooks.MODID, "shapeshifting");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable("ui.woodwalkers_spellbooks.xp_required", getRequiredXPlevel(spellLevel))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.RARE)
        .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
        .setMaxLevel(6)
        .setCooldownSeconds(100)
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

        if (target != null && entity instanceof ServerPlayer player) {
            canCast = player.experienceLevel >= getRequiredXPlevel(spellLevel);

            if (!canCast)
                player.sendSystemMessage(Component.translatable("ui.woodwalkers_spellbooks.not_enough_xp"), true);
            else
                Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, .25f, false);
        }

        return canCast;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int requiredLevel = getRequiredXPlevel(spellLevel);
        LivingEntity target = getTarget(level, entity, playerMagicData);

        if (entity instanceof ServerPlayer player) {
            if (target != null) {
                PlayerShapeChanger.change2ndShape(player, ShapeType.from(target));
                shapeshift(player);
                player.giveExperienceLevels(-requiredLevel);
            } else {
                shapeshift(player);
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
        if (spellLevel >= 5) return 3;
        if (spellLevel >= 3) return 4;

        return 5;
    }

    public static void shapeshift(ServerPlayer player) {
        ShapeType<LivingEntity> type = null;

        if (PlayerShape.getCurrentShape(player) == null)
            type = (ShapeType<LivingEntity>) ((PlayerDataProvider) player).walkers$get2ndShape();

        if (type == null)
            PlayerShape.updateShapes(player, null);
        else
            PlayerShape.updateShapes(player, type.create(CEntity.level(player), player));
    }
}
