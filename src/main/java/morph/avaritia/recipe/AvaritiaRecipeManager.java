package morph.avaritia.recipe;

import codechicken.lib.reflect.ObfMapping;
import codechicken.lib.reflect.ReflectionManager;
import com.google.gson.*;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import morph.avaritia.util.Lumberjack;
import morph.avaritia.util.TriConsumer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by covers1624 on 10/10/2017.
 */
public class AvaritiaRecipeManager {

    public static final Map<ResourceLocation, IExtremeRecipe> EXTREME_RECIPES = new HashMap<>();
    public static final Map<ResourceLocation, ICompressorRecipe> COMPRESSOR_RECIPES = new HashMap<>();
    private static final Map<ResourceLocation, IRecipeFactory<IExtremeRecipe>> extremeRecipeFactories = new HashMap<>();
    private static final Map<ResourceLocation, IRecipeFactory<ICompressorRecipe>> compressorRecipeFactories = new HashMap<>();

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final ObfMapping mapping = new ObfMapping(//
            "net/minecraftforge/common/crafting/JsonContext",//
            "loadConstants",//
            "([Lcom/google/gson/JsonObject;)V"//
    );
    private static final BiConsumer<JsonContext, JsonObject[]> callLoadContext =//
            (ctx, json) -> ReflectionManager.callMethod(mapping, null, ctx, new Object[] { json });

    public static void init() {
        extremeRecipeFactories.put(new ResourceLocation("avaritia:shaped"), ExtremeShapedRecipe::fromJson);
        extremeRecipeFactories.put(new ResourceLocation("avaritia:shapeless"), ExtremeShapelessRecipe::fromJson);

        compressorRecipeFactories.put(new ResourceLocation("avaritia:compressor"), CompressorRecipe::fromJson);

        Loader loader = Loader.instance();
        ModContainer me = loader.activeModContainer();
        loader.setActiveModContainer(null);
        loader.getActiveModList().forEach(AvaritiaRecipeManager::loadFactories);
        loader.getActiveModList().forEach(mod ->//
                loadRecipes(mod, "extreme", (json, ctx, key) -> {
                    IExtremeRecipe recipe = getRecipe(json, ctx, extremeRecipeFactories::get);
                    EXTREME_RECIPES.put(key, recipe.setRegistryName(key));
                })//
        );
        loader.getActiveModList().forEach(mod ->//
                loadRecipes(mod, "compressor", (json, ctx, key) -> {
                    ICompressorRecipe recipe = getRecipe(json, ctx, compressorRecipeFactories::get);
                    if (recipe != null) {
                        recipe.setRegistryName(key);
                        COMPRESSOR_RECIPES.put(key, recipe);
                    }
                })//
        );
        loader.setActiveModContainer(me);
    }

    @SuppressWarnings ("unchecked")
    private static void loadFactories(ModContainer mod) {
        FileSystem fs = null;
        BufferedReader reader = null;
        try {
            JsonContext ctx = new JsonContext(mod.getModId());
            Path fPath = null;
            if (mod.getSource().isFile()) {
                fs = FileSystems.newFileSystem(mod.getSource().toPath(), null);
                fPath = fs.getPath("/assets/" + ctx.getModId() + "/avaritia_recipes/_factories.json");
            } else if (mod.getSource().isDirectory()) {
                fPath = mod.getSource().toPath().resolve("assets/" + ctx.getModId() + "/avaritia_recipes/_factories.json");
            }
            if (fPath != null && Files.exists(fPath)) {
                reader = Files.newBufferedReader(fPath);
                JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                {
                    if (json.has("extreme")) {
                        for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "extreme").entrySet()) {
                            ResourceLocation key = new ResourceLocation(ctx.getModId(), entry.getKey());
                            String clazzName = JsonUtils.getString(entry.getValue(), "extreme[" + entry.getValue() + "]");
                            extremeRecipeFactories.put(key, newClass(clazzName, IRecipeFactory.class));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fs, reader);
        }
    }

    private static void loadRecipes(ModContainer mod, String type, TriConsumer<JsonObject, JsonContext, ResourceLocation> loadRecipe) {
        JsonContext ctx = new JsonContext(mod.getModId());
        CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/avaritia_recipes/" + type, root -> {
            Path fPath = root.resolve("_constants.json");
            if (fPath != null && Files.exists(fPath)) {
                BufferedReader reader = null;
                try {
                    reader = Files.newBufferedReader(fPath);
                    JsonObject[] json = JsonUtils.fromJson(GSON, reader, JsonObject[].class);
                    callLoadContext.accept(ctx, json);
                } catch (IOException e) {
                    Lumberjack.log(Level.ERROR, e, "Error loading _constants.json: ");
                    return false;
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
            return true;
        }, (root, file) -> {
            Loader.instance().setActiveModContainer(mod);

            String relative = root.relativize(file).toString();
            if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_")) {
                return true;
            }

            String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
            ResourceLocation key = new ResourceLocation(ctx.getModId(), name);

            BufferedReader reader = null;
            try {
                reader = Files.newBufferedReader(file);
                JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                if (json.has("conditions") && !CraftingHelper.processConditions(JsonUtils.getJsonArray(json, "conditions"), ctx)) {
                    return true;
                }
                loadRecipe.accept(json, ctx, key);
            } catch (JsonParseException e) {
                Lumberjack.log(Level.ERROR, e, "Parsing error loading recipe %s", key);
                return false;
            } catch (IOException e) {
                Lumberjack.log(Level.ERROR, e, "Couldn't read recipe %s from %s", key, file);
                return false;
            } finally {
                IOUtils.closeQuietly(reader);
            }
            return true;
        });
    }

    private static <T> T getRecipe(JsonObject obj, JsonContext ctx, Function<ResourceLocation, IRecipeFactory<T>> getter) {
        if (obj == null || obj.isJsonNull()) {
            throw new JsonSyntaxException("Json cannot be null");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("getRecipe Context cannot be null");
        }

        String type = ctx.appendModId(JsonUtils.getString(obj, "type"));
        if (type.isEmpty()) {
            throw new JsonSyntaxException("Recipe type can not be an empty string");
        }

        IRecipeFactory<T> factory = getter.apply(new ResourceLocation(type));
        if (factory == null) {
            throw new JsonSyntaxException("Unknown recipe type: " + type);
        }

        return factory.load(ctx, obj);
    }

    private static <T> T newClass(String name, Class<T> clazz) {
        try {
            ObfMapping mapping = new ObfMapping(name.replace(".", "/"), "", "()V");
            return ReflectionManager.newInstance_Unsafe(mapping, clazz);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new JsonSyntaxException("Could not instantiate: " + name, e);
        }
    }

    public static ICompressorRecipe getCompressorRecipeFromInput(ItemStack input) {
        for (ICompressorRecipe recipe : COMPRESSOR_RECIPES.values()) {
            if (recipe.matches(input)) {
                return recipe;
            }
        }
        return null;
    }

    public static ICompressorRecipe getCompressorRecipeFromResult(ItemStack result) {
        for (ICompressorRecipe recipe : COMPRESSOR_RECIPES.values()) {
            if (recipe.getResult().isItemEqual(result)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean hasCompressorRecipe(ItemStack input) {
        return getCompressorRecipeFromInput(input) != null;
    }

    public static boolean hasCompressorRecipe(ItemStack input, ItemStack output) {
        if (!input.isEmpty()) {
            for (ICompressorRecipe recipe : COMPRESSOR_RECIPES.values()) {
                if (recipe.matches(input)) {
                    if (!output.isEmpty()) {
                        if (!recipe.getResult().isItemEqual(output)) {
                            continue;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack getExtremeCraftingResult(InventoryCrafting matrix, World world) {
        for (IExtremeRecipe recipe : EXTREME_RECIPES.values()) {
            if (recipe.matches(matrix, world)) {
                return recipe.getCraftingResult(matrix);
            }
        }
        return ItemStack.EMPTY;
    }

    public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting matrix, World world) {
        for (IExtremeRecipe recipe : EXTREME_RECIPES.values()) {
            if (recipe.matches(matrix, world)) {
                return recipe.getRemainingItems(matrix);
            }
        }
        NonNullList<ItemStack> stacks = NonNullList.withSize(matrix.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            stacks.set(i, matrix.getStackInSlot(i));
        }
        return stacks;
    }
}
