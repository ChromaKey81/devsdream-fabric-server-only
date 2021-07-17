package net.devsdream.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.client.realms.util.JsonUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;

import net.minecraft.recipe.RecipeSerializer;

import net.devsdream.crafting.Serializers;
import net.devsdream.util.ChromaJsonHelper;

public class ShapedNBTRecipe extends ShapedRecipe {

    private final NbtCompound nbt;

    public ShapedNBTRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input,
            ItemStack output, NbtCompound nbt) {
        super(id, group, width, height, input, output);
        this.nbt = nbt;
    }

    public RecipeSerializer<?> getSerializer() {
        return Serializers.CRAFTING_SHAPED_NBT;
    }

    @Override
    public ItemStack getOutput() {
       ItemStack out = super.getOutput().copy();
       out.setNbt(this.nbt);
       return out;
    }

    public static class Serializer implements RecipeSerializer<ShapedNBTRecipe> {
        public ShapedNBTRecipe read(Identifier identifier, JsonObject jsonObject) {
           String string = JsonHelper.getString(jsonObject, "group", "");
           Map<String, Ingredient> map = ShapedRecipe.readSymbols(JsonHelper.getObject(jsonObject, "key"));
           String[] strings = ShapedRecipe.removePadding(ShapedRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
           int i = strings[0].length();
           int j = strings.length;
           DefaultedList<Ingredient> defaultedList = ShapedRecipe.createPatternMatrix(strings, map, i, j);
           ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
           NbtCompound nbt = ChromaJsonHelper.getNbt(jsonObject, "nbt");
           return new ShapedNBTRecipe(identifier, string, i, j, defaultedList, itemStack, nbt);
        }
  
        public ShapedNBTRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
           int i = packetByteBuf.readVarInt();
           int j = packetByteBuf.readVarInt();
           String string = packetByteBuf.readString();
           DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
           for(int k = 0; k < defaultedList.size(); ++k) {
              defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
           }
  
           ItemStack itemStack = packetByteBuf.readItemStack();
           return new ShapedNBTRecipe(identifier, string, i, j, defaultedList, itemStack, itemStack.getNbt());
        }
  
        public void write(PacketByteBuf packetByteBuf, ShapedNBTRecipe shapedRecipe) {
           packetByteBuf.writeVarInt(shapedRecipe.getWidth());
           packetByteBuf.writeVarInt(shapedRecipe.getHeight());
           packetByteBuf.writeString(shapedRecipe.getGroup());
           Iterator<Ingredient> ingredients = shapedRecipe.getIngredients().iterator();
  
           while(ingredients.hasNext()) {
              Ingredient ingredient = ingredients.next();
              ingredient.write(packetByteBuf);
           }
  
           packetByteBuf.writeItemStack(shapedRecipe.getOutput());
        }
     }
    
}