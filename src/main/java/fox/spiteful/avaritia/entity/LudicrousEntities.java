package fox.spiteful.avaritia.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import fox.spiteful.avaritia.Avaritia;

public class LudicrousEntities {

	public static void letLooseTheDogsOfWar() {
		EntityRegistry.registerModEntity(EntityEndestPearl.class, "EndestPearl", 1, Avaritia.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityGapingVoid.class, "GapingVoid", 2, Avaritia.instance, 64, 10, false);
	}
}
