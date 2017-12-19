package morph.avaritia.recipe.extreme;

import codechicken.lib.reflect.ObfMapping;
import codechicken.lib.reflect.ReflectionManager;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ExtremeCraftingManager {

    public static final RegistryNamespaced<ResourceLocation, IExtremeRecipe> REGISTRY = new RegistryNamespaced<>();

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static Map<ResourceLocation, BiFunction<JsonContext, JsonObject, IExtremeRecipe>> recipeFactories = new HashMap<>();

    private static final ObfMapping mapping = new ObfMapping(//
            "net/minecraftforge/common/crafting/JsonContext",//
            "loadConstants",//
            "([Lcom/google/gson/JsonObject;)V"//
    );
    private static final BiConsumer<JsonContext, JsonObject[]> callLoadContext = (ctx, json) -> ReflectionManager.callMethod(mapping, null, ctx, new Object[] { json });

    public static void init() {
        recipeFactories.put(new ResourceLocation("avaritia:shaped"), ExtremeShapedRecipe::fromJson);
        recipeFactories.put(new ResourceLocation("avaritia:shapeless"), ExtremeShapelessRecipe::fromJson);

        ModContainer me = Loader.instance().activeModContainer();
        Loader.instance().setActiveModContainer(null);
        Loader.instance().getActiveModList().forEach(ExtremeCraftingManager::loadRecipes);
        Loader.instance().setActiveModContainer(me);
    }

    private static void loadRecipes(ModContainer mod) {
        JsonContext ctx = new JsonContext(mod.getModId());

        CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/recipes", root -> {
            Path fPath = root.resolve("_constants.json");
            if (fPath != null && Files.exists(fPath)) {
                BufferedReader reader = null;
                try {
                    reader = Files.newBufferedReader(fPath);
                    JsonObject[] json = JsonUtils.fromJson(GSON, reader, JsonObject[].class);
                    callLoadContext.accept(ctx, json);
                } catch (IOException e) {
                    FMLLog.log.error("Error loading _constants.json: ", e);
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
                IExtremeRecipe recipe = getExtremeRecipe(json, ctx);
                REGISTRY.putObject(key, recipe.setRegistryName(key));
            } catch (JsonParseException e) {
                FMLLog.log.error("Parsing error loading recipe {}", key, e);
                return false;
            } catch (IOException e) {
                FMLLog.log.error("Couldn't read recipe {} from {}", key, file, e);
                return false;
            } finally {
                IOUtils.closeQuietly(reader);
            }
            return true;
        });
    }

    public static IExtremeRecipe getExtremeRecipe(JsonObject obj, JsonContext ctx) {
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

        BiFunction<JsonContext, JsonObject, IExtremeRecipe> factory = recipeFactories.get(new ResourceLocation(type));
        if (factory == null) {
            throw new JsonSyntaxException("Unknown recipe type: " + type);
        }

        return factory.apply(ctx, obj);
    }
}
