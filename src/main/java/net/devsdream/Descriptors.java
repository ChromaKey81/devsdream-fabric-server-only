package net.devsdream;

import java.lang.reflect.Method;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;

public class Descriptors {
    
    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        System.out.println(getMethodDescriptor(Descriptors.class.getMethod("removePadding", String[].class)));
        System.out.println(getMethodDescriptor(Descriptors.class.getMethod("getPattern", JsonArray.class)));
        System.out.println(getMethodDescriptor(Descriptors.class.getMethod("createPatternMatrix", String[].class, Map.class, int.class, int.class)));
    }

    public static String[] removePadding(String... pattern) {
        return new String[1];
    }

    public static String[] getPattern(JsonArray json) {
        return new String[1];
    }

    public static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        return DefaultedList.ofSize(1, Ingredient.EMPTY);
    }

    static String getMethodDescriptor(Method m) {
        String s="(";
        for(final Class<?> c:(m.getParameterTypes()))
            s += c.descriptorString();
        s+=')';
        return s + m.getReturnType().descriptorString();
    }

}
