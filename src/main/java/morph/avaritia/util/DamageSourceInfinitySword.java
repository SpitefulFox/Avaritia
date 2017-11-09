package morph.avaritia.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class DamageSourceInfinitySword extends EntityDamageSource {

    public DamageSourceInfinitySword(Entity source) {
        super("infinity", source);
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        ItemStack itemstack = damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) damageSourceEntity).getHeldItem(EnumHand.MAIN_HAND) : null;
        String s = "death.attack.infinity";
        int rando = entity.getEntityWorld().rand.nextInt(5);
        if (rando != 0) {
            s = s + "." + rando;
        }
        return new TextComponentTranslation(s, entity.getDisplayName(), itemstack.getDisplayName());
    }

    @Override
    public boolean isDifficultyScaled() {
        return false;
    }

}
