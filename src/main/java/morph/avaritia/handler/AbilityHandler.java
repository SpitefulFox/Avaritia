package morph.avaritia.handler;

import morph.avaritia.item.ItemArmorInfinity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static net.minecraft.inventory.EntityEquipmentSlot.*;

/**
 * Handles all abilities for ANY EntityLivingBase.
 * Some abilities are player specific, but just don't give a zombie your boots..
 */
public class AbilityHandler {

    //@formatter:off
    public static final Set<String> entitiesWithHelmets =     new HashSet<>();
    public static final Set<String> entitiesWithChestplates = new HashSet<>();
    public static final Set<String> entitiesWithLeggings =    new HashSet<>();
    public static final Set<String> entitiesWithBoots =       new HashSet<>();
    public static final Set<String> entitiesWithFlight =      new HashSet<>();
    //@formatter:on

    public static boolean isPlayerWearing(EntityLivingBase entity, EntityEquipmentSlot slot, Predicate<Item> predicate) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        return !stack.isEmpty() && predicate.test(stack.getItem());
    }

    @SubscribeEvent
    //Updates all ability states for an entity, Handles firing updates and state changes.
    public void updateAbilities(LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }

        EntityLivingBase entity = event.getEntityLiving();
        String key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;

        boolean hasHelmet = isPlayerWearing(event.getEntityLiving(), HEAD, item -> item instanceof ItemArmorInfinity);
        boolean hasChestplate = isPlayerWearing(event.getEntityLiving(), CHEST, item -> item instanceof ItemArmorInfinity);
        boolean hasLeggings = isPlayerWearing(event.getEntityLiving(), LEGS, item -> item instanceof ItemArmorInfinity);
        boolean hasBoots = isPlayerWearing(event.getEntityLiving(), FEET, item -> item instanceof ItemArmorInfinity);

        //Helmet toggle.
        if (hasHelmet) {
            entitiesWithHelmets.add(key);
            handleHelmetStateChange(entity, true);
        }
        if (!hasHelmet) {
            entitiesWithHelmets.remove(key);
            handleHelmetStateChange(entity, false);
        }

        //Chestplate toggle.
        if (hasChestplate) {
            entitiesWithChestplates.add(key);
            handleChestplateStateChange(entity, true);
        }
        if (!hasChestplate) {
            entitiesWithChestplates.remove(key);
            handleChestplateStateChange(entity, false);
        }

        //Leggings toggle.
        if (hasLeggings) {
            entitiesWithLeggings.add(key);
            handleLeggingsStateChange(entity, true);
        }
        if (!hasLeggings) {
            entitiesWithLeggings.remove(key);
            handleLeggingsStateChange(entity, false);
        }

        //Boots toggle.
        if (hasBoots) {
            handleBootsStateChange(entity);
            entitiesWithBoots.add(key);
        }
        if (!hasBoots) {
            handleBootsStateChange(entity);
            entitiesWithBoots.remove(key);
        }

        //Active ability ticking.
        if (entitiesWithHelmets.contains(key)) {
            tickHelmetAbilities(entity);
        }
        if (entitiesWithChestplates.contains(key)) {
            tickChestplateAbilities(entity);
        }
        if (entitiesWithLeggings.contains(key)) {
            tickLeggingsAbilities(entity);
        }
        if (entitiesWithBoots.contains(key)) {
            tickBootsAbilities(entity);
        }
    }

    /**
     * Strips all Abilities from an entity if the entity had any special abilities.
     *
     * @param entity EntityLivingBase we speak of.
     */
    private static void stripAbilities(EntityLivingBase entity) {
        String key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;

        if (entitiesWithHelmets.remove(key)) {
            handleHelmetStateChange(entity, false);
        }

        if (entitiesWithChestplates.remove(key)) {
            handleChestplateStateChange(entity, false);
        }

        if (entitiesWithLeggings.remove(key)) {
            handleLeggingsStateChange(entity, false);
        }

        if (entitiesWithBoots.remove(key)) {
            handleBootsStateChange(entity);
        }
    }

    //region StateChanging
    private static void handleHelmetStateChange(EntityLivingBase entity, boolean isNew) {
        //TODO, Helmet abilities? Water breathing, NightVision, Auto Eat or No Hunger, No bad effects.
    }

    private static void handleChestplateStateChange(EntityLivingBase entity, boolean isNew) {
        String key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) entity);
            if (isNew) {
                player.capabilities.allowFlying = true;
                entitiesWithFlight.add(key);
            } else {
                if (!player.capabilities.isCreativeMode && entitiesWithFlight.contains(key)) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    entitiesWithFlight.remove(key);
                }
            }
        }
    }

    private static void handleLeggingsStateChange(EntityLivingBase entity, boolean isNew) {

    }

    private static void handleBootsStateChange(EntityLivingBase entity) {
        String temp_key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;
        boolean hasBoots = isPlayerWearing(entity, FEET, item -> item instanceof ItemArmorInfinity);
        if (hasBoots) {
            entity.stepHeight = 1.0625F;//Step 17 pixels, Allows for stepping directly from a path to the top of a block next to the path.
            if (!entitiesWithBoots.contains(temp_key)) {
                entitiesWithBoots.add(temp_key);
            }
        } else {
            if (entitiesWithBoots.contains(temp_key)) {
                entity.stepHeight = 0.5F;
                entitiesWithBoots.remove(temp_key);
            }
        }
    }
    //endregion

    //region Ability Ticking
    private static void tickHelmetAbilities(EntityLivingBase entity) {

    }

    private static void tickChestplateAbilities(EntityLivingBase entity) {

    }

    private static void tickLeggingsAbilities(EntityLivingBase entity) {

    }

    private static void tickBootsAbilities(EntityLivingBase entity) {
        boolean flying = entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying;
        boolean swimming = entity.isInsideOfMaterial(Material.WATER) || entity.isInWater();
        if (entity.onGround || flying || swimming) {
            boolean sneaking = entity.isSneaking();

            float speed = 0.15f * (flying ? 1.1f : 1.0f)
                    //* (swimming ? 1.2f : 1.0f)
                    * (sneaking ? 0.1f : 1.0f);

            if (entity.moveForward > 0f) {
                entity.moveRelative(0f, 0f, 1f, speed);
            } else if (entity.moveForward < 0f) {
                entity.moveRelative(0f, 0f, 1f, -speed * 0.3f);
            }

            if (entity.moveStrafing != 0f) {
                entity.moveRelative(1f, 0f, 0f, speed * 0.5f * Math.signum(entity.moveStrafing));
            }
        }
    }
    //endregion

    //region Ability Specific Events
    @SubscribeEvent
    public void jumpBoost(LivingJumpEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entitiesWithBoots.contains( entity.getCachedUniqueIdString() + "|" + entity.world.isRemote)) {
            entity.motionY += 0.4f;
        }
    }
    //endregion

    //region Ability Striping Events
    //These are anything that should strip all abilities from an entity, Anything that creates an entity.
    @SubscribeEvent
    public void onPlayerDemensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        stripAbilities(event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        stripAbilities(event.player);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        stripAbilities(event.player);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        stripAbilities(event.player);
    }

    @SubscribeEvent
    public void entityContstructedEvent(EntityConstructing event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            //stripAbilities((EntityLivingBase) event.getEntity());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        stripAbilities(event.getEntityLiving());
    }
    //endregion
}
