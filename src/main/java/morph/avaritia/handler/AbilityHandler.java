package morph.avaritia.handler;

import morph.avaritia.item.ItemArmorInfinity;
import morph.avaritia.util.Lumberjack;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class AbilityHandler {

    public static List<String> playersWithHat = new ArrayList<>();
    public static List<String> playersWithChest = new ArrayList<>();
    public static List<String> playersWithLeg = new ArrayList<>();
    public static List<String> playersWithFoot = new ArrayList<>();

    public static final Set<UUID> playersWithStepAssist = new HashSet<>();

    public static boolean playerHasHat(EntityPlayer player) {
        ItemStack armour = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return armour != null && armour.getItem() instanceof ItemArmorInfinity;
    }

    public static boolean playerHasChest(EntityPlayer player) {
        ItemStack armour = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        return armour != null && armour.getItem() instanceof ItemArmorInfinity;
    }

    public static boolean playerHasLeg(EntityPlayer player) {
        ItemStack armour = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        return armour != null && armour.getItem() instanceof ItemArmorInfinity;
    }

    public static boolean playerHasFoot(EntityPlayer player) {
        ItemStack armour = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        return armour != null && armour.getItem() instanceof ItemArmorInfinity;
    }

    public static String playerKey(EntityPlayer player) {
        return player.getGameProfile().getName() + ":" + player.worldObj.isRemote;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

        EntityPlayer player = event.player;

        boolean hasBoots = playerHasFoot(player);
        if (player.worldObj.isRemote) {
            boolean hasStepAssist = playersWithStepAssist.contains(player.getUniqueID());
            if (hasStepAssist && !hasBoots) {
                playersWithStepAssist.remove(player.getUniqueID());
                player.stepHeight = 0.5F;
            }
            if (!hasStepAssist && hasBoots) {
                playersWithStepAssist.add(player.getUniqueID());
                player.stepHeight = 1.0625F;//Step 17 pixels, Allows for stepping directly from a path to the top of a block next to the path.
            }
        }
    }

    @SubscribeEvent
    public void updatePlayerAbilityStatus(LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            String key = playerKey(player);

            // hat
            Boolean hasHat = playerHasHat(player);
            if (playersWithHat.contains(key)) {
                if (hasHat) {

                } else {
                    playersWithHat.remove(key);
                }
            } else if (hasHat) {
                playersWithHat.add(key);
            }

            // chest
            Boolean hasChest = playerHasChest(player);
            if (playersWithChest.contains(key)) {
                if (hasChest) {
                    player.capabilities.allowFlying = true;
                } else {
                    if (!player.capabilities.isCreativeMode) {
                        player.capabilities.allowFlying = false;
                        player.capabilities.isFlying = false;
                    }
                    playersWithChest.remove(key);
                }
            } else if (hasChest) {
                playersWithChest.add(key);
            }

            // legs
            Boolean hasLeg = playerHasLeg(player);
            if (playersWithLeg.contains(key)) {
                if (hasLeg) {

                } else {
                    playersWithLeg.remove(key);
                }
            } else if (hasLeg) {
                playersWithLeg.add(key);
            }

            // shoes
            Boolean hasFoot = playerHasFoot(player);
            if (playersWithFoot.contains(key)) {
                if (hasFoot) {
                    boolean flying = player.capabilities.isFlying;
                    boolean swimming = player.isInsideOfMaterial(Material.WATER) || player.isInWater();
                    if (player.onGround || flying || swimming) {
                        boolean sneaking = player.isSneaking();

                        float speed = 0.15f * (flying ? 1.1f : 1.0f)
                                //* (swimming ? 1.2f : 1.0f)
                                * (sneaking ? 0.1f : 1.0f);

                        if (player.moveForward > 0f) {
                            player.moveRelative(0f, 1f, speed);
                        } else if (player.moveForward < 0f) {
                            player.moveRelative(0f, 1f, -speed * 0.3f);
                        }

                        if (player.moveStrafing != 0f) {
                            player.moveRelative(1f, 0f, speed * 0.5f * Math.signum(player.moveStrafing));
                        }
                    }
                } else {
                    playersWithFoot.remove(key);
                }
            } else if (hasFoot) {
                playersWithFoot.add(key);
            }
        }
    }

    @SubscribeEvent
    public void jumpBoost(LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            String key = playerKey(player);

            if (playersWithFoot.contains(key)) {
                player.motionY += 0.4f;
            }
        }
    }
}
