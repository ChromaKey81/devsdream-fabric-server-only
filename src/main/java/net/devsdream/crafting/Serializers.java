package net.devsdream.crafting;

import net.minecraft.recipe.RecipeSerializer;

public class Serializers {
    public static RecipeSerializer<ShapedNBTRecipe> CRAFTING_SHAPED_NBT = new ShapedNBTRecipe.Serializer();
}
