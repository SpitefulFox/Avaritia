package fox.spiteful.avaritia.entity;


import fox.spiteful.avaritia.Avaritia;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class LudicrousEntities {

	public static void letLooseTheDogsOfWar() {
		EntityRegistry.registerModEntity(EntityEndestPearl.class, "EndestPearl", 1, Avaritia.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityGapingVoid.class, "GapingVoid", 2, Avaritia.instance, 256, 10, false);
		EntityRegistry.registerModEntity(EntityHeavenArrow.class, "HeavenArrow", 3, Avaritia.instance, 32, 1, true);
		EntityRegistry.registerModEntity(EntityHeavenSubArrow.class, "HeavenSubArrow", 4, Avaritia.instance, 32, 2, true);
	}
}
