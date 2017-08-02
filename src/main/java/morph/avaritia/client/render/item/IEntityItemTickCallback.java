package morph.avaritia.client.render.item;

import net.minecraft.entity.item.EntityItem;

public interface IEntityItemTickCallback {

    void onEntityTick(EntityItem item);
}
