package com.github.jodevnull.woodwalkers_spells;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WoodwalkersSpellBooks.MODID)
public class WoodwalkersSpellBooks {

    public static final String MODID = "woodwalkers_spellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WoodwalkersSpellBooks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModSpellRegistry.register(modEventBus);
    }
}
