package fox.spiteful.avaritia.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

@Deprecated // maybe someday... but not today
public class EntityCollationItem extends EntityItem {
	public static final String ITEMTAG = "item";

	public EntityCollationItem(World world, double x, double y, double z, Collection<ItemStack> items) {
		super(world, x,y,z);
		ItemStack watchstack = new ItemStack(Blocks.bedrock);
		
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList itemlist = new NBTTagList();
		
		for (ItemStack stack : items) {
			NBTTagCompound itemtag = stack.writeToNBT(new NBTTagCompound());
			itemlist.appendTag(itemtag);
		}
		
		tag.setTag(ITEMTAG, itemlist);
		watchstack.setTagCompound(tag);
		
		this.setEntityItemStack(watchstack);
		
		this.lifespan = 18000;
		this.delayBeforeCanPickup = 20;
		this.isImmuneToFire = true;
	}
	
	// default constructors
	public EntityCollationItem(World world, double x,	double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
	}

	public EntityCollationItem(World world, double x,	double y, double z) {
		super(world, x, y, z);
	}

	public EntityCollationItem(World world) {
		super(world);
	}
	// end that junk
	
	@Override
	public void onUpdate()
    {
		super.onUpdate();
		ItemStack stack = this.getEntityItem();
		NBTTagList list = stack.getTagCompound().getTagList(ITEMTAG, 10);
		if (list.tagCount() == 0) {
			this.setDead();
		}
    }
	
	@Override
	public boolean combineItems(EntityItem other) {
		return false;
	}
	
	// and the big one!
	@Override
	public void onCollideWithPlayer(EntityPlayer player)
    {
        if (!this.worldObj.isRemote)
        {
            if (this.delayBeforeCanPickup > 0)
            {
                return;
            }

            EntityItemPickupEvent event = new EntityItemPickupEvent(player, this);

            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return;
            }

            String owner = this.func_145798_i();
            
            ItemStack basestack = this.getEntityItem();
            NBTTagList list = basestack.getTagCompound().getTagList(ITEMTAG, 10);
            List<Integer> removed = new ArrayList<Integer>();

            for (int i=0; i<list.tagCount(); i++) {
            	
            	ItemStack itemstack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
            	
	            if (this.delayBeforeCanPickup <= 0 
	            		&& (owner == null || lifespan - this.age <= 200 || owner.equals(player.getCommandSenderName())) 
	            		&& (event.getResult() == Result.ALLOW || player.inventory.addItemStackToInventory(itemstack)))
	            {
	                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log))
	                {
	                    player.triggerAchievement(AchievementList.mineWood);
	                }
	
	                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2))
	                {
	                    player.triggerAchievement(AchievementList.mineWood);
	                }
	
	                if (itemstack.getItem() == Items.leather)
	                {
	                    player.triggerAchievement(AchievementList.killCow);
	                }
	
	                if (itemstack.getItem() == Items.diamond)
	                {
	                    player.triggerAchievement(AchievementList.diamonds);
	                }
	
	                if (itemstack.getItem() == Items.blaze_rod)
	                {
	                    player.triggerAchievement(AchievementList.blazeRod);
	                }
	
	                if (itemstack.getItem() == Items.diamond && this.func_145800_j() != null)
	                {
	                    EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(this.func_145800_j());
	
	                    if (entityplayer1 != null && entityplayer1 != player)
	                    {
	                        entityplayer1.triggerAchievement(AchievementList.field_150966_x);
	                    }
	                }
	
	                FMLCommonHandler.instance().firePlayerItemPickupEvent(player, this);
	
	                this.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
	                //player.onItemPickup(this, itemstack.stackSize);
	
	                if (itemstack.stackSize <= 0)
	                {
	                    removed.add(0, i);
	                }
	            }
        	}
            
            for (int i=0; i<removed.size(); i++) {
            	int index = removed.get(i);
            	list.removeTag(index);
            }
            
            if (list.tagCount() == 0) {
            	this.setDead();
            }
            
            this.setEntityItemStack(basestack);
        }
    }
}
