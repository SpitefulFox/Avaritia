package fox.spiteful.avaritia.compat.thaumcraft;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class Lucrum {

    public static Aspect ULTRA_DEATH;

    public static void abracadabra(){
        ULTRA_DEATH = new Aspect("terminus", 0xb90000, new Aspect[] { Aspect.GREED, Aspect.ELDRITCH }, new ResourceLocation("avaritia", "textures/misc/terminus.png"), 771);

        LudicrousItems.akashic_record = new ItemAkashicRecord();
        GameRegistry.registerItem(LudicrousItems.akashic_record, "Akashic_Record");

        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.resource, 1, 1), new AspectList().add(ULTRA_DEATH, 1).add(Aspect.ENERGY, 8).add(Aspect.CRYSTAL, 32));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.resource, 1, 4), new AspectList().add(ULTRA_DEATH, 2).add(Aspect.METAL, 12).add(Aspect.ENERGY, 12));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.resource, 1, 5), new AspectList().add(ULTRA_DEATH, 5).add(Aspect.EXCHANGE, 12));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.resource, 1, 6), new AspectList().add(ULTRA_DEATH, 16).add(Aspect.ELDRITCH, 64));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousBlocks.neutron_collector, 1, OreDictionary.WILDCARD_VALUE), new AspectList().add(ULTRA_DEATH, 5).add(Aspect.METAL, 64).add(Aspect.ENERGY, 64).add(Aspect.MECHANISM, 64));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 0), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.METAL, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 1), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.METAL, 100).add(Aspect.GREED, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 2), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.SENSES, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 3), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.MECHANISM, 100).add(Aspect.ENERGY, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 4), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.CRYSTAL, 100).add(Aspect.ORDER, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 5), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.METAL, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 6), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.METAL, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 7), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.METAL, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.singularity, 1, 8), new AspectList().add(ULTRA_DEATH, 3).add(Aspect.METAL, 100));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.infinity_sword), new AspectList().add(ULTRA_DEATH, 60).add(Aspect.WEAPON, 999).add(Aspect.DEATH, 999).add(Aspect.ELDRITCH, 100));
        ThaumcraftApi.registerComplexObjectTag(new ItemStack(LudicrousItems.skull_sword), new AspectList().add(ULTRA_DEATH, 1).add(Aspect.FIRE, 2).add(Aspect.CRYSTAL, 16).add(Aspect.DEATH, 4));

    }

}
