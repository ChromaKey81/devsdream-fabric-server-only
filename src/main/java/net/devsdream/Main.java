package net.devsdream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.devsdream.crafting.Serializers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {

    private static final Logger logger = LogManager.getLogger("devsdream");

    public static final String MODID = "devsdream";

    @Override
    public void onInitialize() {

      Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "shaped_nbt"), Serializers.CRAFTING_SHAPED_NBT);
      
    }
    
}
