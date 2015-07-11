package fox.spiteful.avaritia.compat;

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

        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.resource, 1, 1), new AspectList().add(ULTRA_DEATH, 1).add(Aspect.ENERGY, 8).add(Aspect.CRYSTAL, 32));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousItems.resource, 1, 4), new AspectList().add(ULTRA_DEATH, 2).add(Aspect.METAL, 12).add(Aspect.ENERGY, 12));
        ThaumcraftApi.registerObjectTag(new ItemStack(LudicrousBlocks.neutron_collector, 1, OreDictionary.WILDCARD_VALUE), new AspectList().add(ULTRA_DEATH, 5).add(Aspect.METAL, 64).add(Aspect.ENERGY, 64).add(Aspect.MECHANISM, 64));
    }

}
