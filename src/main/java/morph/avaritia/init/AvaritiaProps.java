package morph.avaritia.init;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

/**
 * Created by covers1624 on 18/04/2017.
 */
public class AvaritiaProps {

    public static final PropertyEnum<EnumFacing> HORIZONTAL_FACING = PropertyEnum.create("facing", EnumFacing.class, facing -> facing.getAxis() != Axis.Y);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

}
