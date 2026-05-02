package com.github.jodevnull.woodwalkers_spells;

import com.github.jodevnull.woodwalkers_spells.spells.ShapeshiftingSpell;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class SpellRegistry
{
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, WoodwalkersSpellBooks.MODID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static DeferredHolder<AbstractSpell, ShapeshiftingSpell> registerSpell(ShapeshiftingSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final DeferredHolder<AbstractSpell, ShapeshiftingSpell> SHAPESHIFTING_SPELL = registerSpell(new ShapeshiftingSpell());
}