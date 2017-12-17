package morph.avaritia.proxy;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ItemNBTUtils;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.AvaritiaClientEventHandler;
import morph.avaritia.client.render.entity.RenderGapingVoid;
import morph.avaritia.client.render.entity.RenderHeavenArrow;
import morph.avaritia.client.render.entity.WrappedEntityItemRenderer;
import morph.avaritia.client.render.shader.ShaderHelper;
import morph.avaritia.entity.EntityEndestPearl;
import morph.avaritia.entity.EntityGapingVoid;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.entity.EntityHeavenSubArrow;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemMatterCluster;
import morph.avaritia.network.ClientPacketHandler;
import morph.avaritia.network.NetworkDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashSet;
import java.util.Set;

public class ProxyClient extends Proxy {

    private Set<IModelRegister> modelRegisters = new HashSet<>();

    //@formatter:off
    public static final int[] SINGULARITY_COLOURS_FOREGROUND = new int[] {
            0xE6E7E8, 0xE8EF23, 0x5a82e2, 0xDF0000,
            0xffffff, 0xE47200, 0xA5C7DE, 0x444072,
            0xC0C0C0, 0xDEE187, 0x45ACA5, 0x5CBE34,
            0xD62306, 0x00BFFF, 0xE6E6FA
    };

    public static final int[] SINGULARITY_COLOURS_BACKGROUND = new int[] {
            0x7F7F7F, 0xdba213, 0x224baf, 0x900000,
            0x94867d, 0x89511A, 0x9BA9B2, 0x3E3D4E,
            0xD5D5D5, 0xC4C698, 0x8fcdc9, 0x8cd170,
            0xfffc95, 0x5a82e2, 0xE6E6FA
    };

    public static final int[][] SINGULARITY_COLOURS = new int[][] {
            SINGULARITY_COLOURS_FOREGROUND,
            SINGULARITY_COLOURS_BACKGROUND
    };
    //@formatter:on

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        super.preInit(event);
        TextureUtils.addIconRegister(new AvaritiaTextures());
        MinecraftForge.EVENT_BUS.register(new AvaritiaClientEventHandler());

        for (IModelRegister register : modelRegisters) {
            register.registerModels();
        }
        ShaderHelper.initShaders();
        ResourceLocation tools = new ResourceLocation("avaritia:tools");
        ResourceLocation resource = new ResourceLocation("avaritia:resource");

        {
            ModelResourceLocation pickaxe = new ModelResourceLocation(tools, "infinity_pickaxe=pickaxe");
            ModelResourceLocation hammer = new ModelResourceLocation(tools, "infinity_pickaxe=hammer");
            ModelLoader.registerItemVariants(ModItems.infinity_pickaxe, pickaxe, hammer);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_pickaxe, stack -> {
                if (stack.hasTagCompound()) {
                    if (ItemNBTUtils.getBoolean(stack, "hammer")) {
                        return hammer;
                    }
                }
                return pickaxe;
            });
        }

        {
            ModelResourceLocation shovel = new ModelResourceLocation(tools, "infinity_shovel=shovel");
            ModelResourceLocation destroyer = new ModelResourceLocation(tools, "infinity_shovel=destroyer");
            ModelLoader.registerItemVariants(ModItems.infinity_shovel, shovel, destroyer);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_shovel, stack -> {
                if (stack.hasTagCompound()) {
                    if (ItemNBTUtils.getBoolean(stack, "destroyer")) {
                        return destroyer;
                    }
                }
                return shovel;
            });
        }

        {
            ModelResourceLocation axe = new ModelResourceLocation(tools, "type=infinity_axe");
            ModelLoader.registerItemVariants(ModItems.infinity_axe, axe);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_axe, (ItemStack stack) -> axe);
        }

        {
            ModelResourceLocation hoe = new ModelResourceLocation(tools, "type=infinity_hoe");
            ModelLoader.registerItemVariants(ModItems.infinity_axe, hoe);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_hoe, (ItemStack stack) -> hoe);
        }

        {
            ModelResourceLocation helmet = new ModelResourceLocation(tools, "armor=helmet");
            ModelLoader.registerItemVariants(ModItems.infinity_helmet, helmet);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_helmet, (ItemStack stack) -> helmet);
        }

        {
            ModelResourceLocation chestplate = new ModelResourceLocation(tools, "armor=chestplate");
            ModelLoader.registerItemVariants(ModItems.infinity_chestplate, chestplate);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_chestplate, (ItemStack stack) -> chestplate);
        }

        {
            ModelResourceLocation legs = new ModelResourceLocation(tools, "armor=legs");
            ModelLoader.registerItemVariants(ModItems.infinity_pants, legs);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_pants, (ItemStack stack) -> legs);
        }

        {
            ModelResourceLocation boots = new ModelResourceLocation(tools, "armor=boots");
            ModelLoader.registerItemVariants(ModItems.infinity_boots, boots);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_boots, (ItemStack stack) -> boots);
        }

        {
            ModelResourceLocation sword = new ModelResourceLocation(tools, "type=skull_sword");
            ModelLoader.registerItemVariants(ModItems.skull_sword, sword);
            ModelLoader.setCustomMeshDefinition(ModItems.skull_sword, (ItemStack stack) -> sword);
        }

        {
            ModelResourceLocation stew = new ModelResourceLocation(resource, "type=ultimate_stew");
            ModelLoader.registerItemVariants(ModItems.ultimate_stew, stew);
            ModelLoader.setCustomMeshDefinition(ModItems.ultimate_stew, (ItemStack stack) -> stew);
        }

        {
            ModelResourceLocation meatballs = new ModelResourceLocation(resource, "type=cosmic_meatballs");
            ModelLoader.registerItemVariants(ModItems.cosmic_meatballs, meatballs);
            ModelLoader.setCustomMeshDefinition(ModItems.cosmic_meatballs, (ItemStack stack) -> meatballs);
        }

        {
            ModelResourceLocation empty = new ModelResourceLocation(resource, "matter_cluster=empty");
            ModelResourceLocation full = new ModelResourceLocation(resource, "matter_cluster=full");
            ModelLoader.registerItemVariants(ModItems.matter_cluster, empty, full);
            ModelLoader.setCustomMeshDefinition(ModItems.matter_cluster, (ItemStack stack) -> {
                if (ItemMatterCluster.getClusterSize(stack) == ItemMatterCluster.CAPACITY) {
                    return full;
                }
                return empty;
            });
        }
        registerRenderers();
        PacketCustom.assignHandler(NetworkDispatcher.NET_CHANNEL, new ClientPacketHandler());
    }

    @SuppressWarnings ("unchecked")
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        itemColors.registerItemColorHandler((stack, tintIndex) -> SINGULARITY_COLOURS[tintIndex ^ 1][stack.getItemDamage()], ModItems.singularity);

        RenderManager manager = Minecraft.getMinecraft().getRenderManager();

        Render<EntityItem> render = (Render<EntityItem>) manager.entityRenderMap.get(EntityItem.class);
        if (render == null) {
            throw new RuntimeException("EntityItem does not have a Render bound... This is likely a bug..");
        }
        manager.entityRenderMap.put(EntityItem.class, new WrappedEntityItemRenderer(manager, render));
    }

    @Override
    public void addModelRegister(IModelRegister register) {
        modelRegisters.add(register);
    }

    private void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityEndestPearl.class, manager -> new RenderSnowball<>(manager, ModItems.endest_pearl, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityGapingVoid.class, RenderGapingVoid::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHeavenArrow.class, RenderHeavenArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHeavenSubArrow.class, RenderHeavenArrow::new);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }
}
