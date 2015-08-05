package fox.spiteful.avaritia.compat.ticon;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import tconstruct.library.event.ToolCraftEvent;
import tconstruct.library.tools.ToolCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.LudicrousEvents;

public class TonkersEvents {

	@SubscribeEvent
    public void craftTool (ToolCraftEvent.NormalTool event)
    {
		NBTTagCompound toolTag = event.toolTag.getCompoundTag("InfiTool");
		handleInfinityMods(toolTag, event.tool);
    }
	
	private void handleInfinityMods (NBTTagCompound toolTag, ToolCore tool)
    {
		int plusmod = 5;
        int modifiers = toolTag.getInteger("Modifiers");
        if (toolTag.getInteger("Head") == Tonkers.infinityMetalId)
            modifiers += plusmod;
        if (toolTag.getInteger("Handle") == Tonkers.infinityMetalId)
            modifiers += plusmod;
        if (toolTag.getInteger("Accessory") == Tonkers.infinityMetalId)
            modifiers += plusmod;
        if (toolTag.getInteger("Extra") == Tonkers.infinityMetalId)
            modifiers += plusmod;

        // 2 part tools gain 2 modifiers for the head
        if (tool.getPartAmount() == 2 && toolTag.getInteger("Head") == Tonkers.infinityMetalId)
            modifiers += plusmod;

        toolTag.setInteger("Modifiers", modifiers);
    }
	
	@SubscribeEvent
    public void handleExtraLuck(HarvestDropsEvent event) {
		if(event.harvester == null)
            return;
        if(event.harvester.getHeldItem() == null)
            return;
        ItemStack held = event.harvester.getHeldItem();
        if (held != null && held.hasTagCompound() && held.getItem() instanceof ToolCore)
        {
        	ToolCore tool = (ToolCore)held.getItem();
        	NBTTagCompound toolTag = held.getTagCompound().getCompoundTag("InfiTool");
        	
        	if (toolTag.getInteger("Head") == Tonkers.infinityMetalId) {
        		int parts = 1;
        		if (toolTag.getInteger("Handle") == Tonkers.infinityMetalId) {
        			parts++;
        		}
                if (toolTag.getInteger("Accessory") == Tonkers.infinityMetalId) {
                	parts++;
                }
                if (toolTag.getInteger("Extra") == Tonkers.infinityMetalId) {
                	parts++;
                }
                
                int luck = Math.min(3, parts);
                if (parts == tool.getPartAmount()) {
                	luck++;
                	if (tool.getPartAmount() == 2) {
                		luck++;
                	}
                }
                
                LudicrousEvents.extraLuck(event, luck);
        	}
        }
	}
}
