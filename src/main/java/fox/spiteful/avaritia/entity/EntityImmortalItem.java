package fox.spiteful.avaritia.entity;

import java.util.Iterator;
import java.util.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class EntityImmortalItem extends EntityItem {

	public EntityImmortalItem(World world, Entity original, ItemStack stack) {
		this(world, original.posX, original.posY, original.posZ, stack);
		this.setPickupDelay(20);
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
        this.setEntityItemStack(stack);
	}
	
	public EntityImmortalItem(World world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z);
		this.setEntityItemStack(stack);
	}

	public EntityImmortalItem(World world, double x, double y, double z) {
		super(world, x, y, z);
		this.isImmuneToFire = true;
	}

	public EntityImmortalItem(World world) {
		super(world);
		this.isImmuneToFire = true;
	}
	
	@Override
	protected void dealFireDamage(int damage) {}

	@Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
		if (source.getDamageType().equals("outOfWorld")) {
			return true;
		}
        return false;
    }
	
	@Override
	public void onUpdate()
    {
        DataParameter<Optional<ItemStack>> arrowItem = ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, this, "ITEM", "field_184533_c");
        ItemStack stack = this.getDataManager().get(arrowItem).orNull();
        if (stack != null && stack.getItem() != null)
        {
            if (stack.getItem().onEntityItemUpdate(this))
            {
                return;
            }
        }

        if (this.getEntityItem() == null)
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();

            if (cannotPickup())
            {
                int delay = ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, this, "delayBeforeCanPickup", "field_145804_b");
                setPickupDelay(delay-1);
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            boolean flag = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;

            if (flag || this.ticksExisted % 25 == 0)
            {
                if (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))).getMaterial() == Material.LAVA)
                {
                    this.motionY = 0.20000000298023224D;
                    this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    //this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }

                if (!this.worldObj.isRemote)
                {
                    this.searchForOtherItemsNearby2();
                }
            }

            float f = 0.98F;

            if (this.onGround)
            {
                f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
            }

            this.motionX *= (double)f;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double)f;

            if (this.onGround)
            {
                this.motionY *= -0.5D;
            }

            ObfuscationReflectionHelper.setPrivateValue(EntityItem.class, this, getAge()+1, "age", "field_70292_b");

            DataParameter<Optional<ItemStack>> arrowItem2 = ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, this, "ITEM", "field_184533_c");
            ItemStack item = this.getDataManager().get(arrowItem2).orNull();
    
            if (!this.worldObj.isRemote && this.age >= lifespan)
            {
                if (item != null)
                {   
                    /*ItemExpireEvent event = new ItemExpireEvent(this, (item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, worldObj)));
                    if (MinecraftForge.EVENT_BUS.post(event))
                    {
                        lifespan += event.extraLife;
                    }
                    else
                    {
                        this.setDead();
                    }*/
                }
                else
                {
                    this.setDead();
                }
            }
    
            if (item != null && item.stackSize <= 0)
            {
                this.setDead();
            }
        }
    }
	
	// GODDAMNIT MOJANG
	@SuppressWarnings("rawtypes")
	private void searchForOtherItemsNearby2()
    {
        Iterator iterator = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();

        while (iterator.hasNext())
        {
            EntityItem entityitem = (EntityItem)iterator.next();
            this.combineItems(entityitem);
        }
    }
}
