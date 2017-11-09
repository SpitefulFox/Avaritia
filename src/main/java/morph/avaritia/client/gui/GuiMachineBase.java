package morph.avaritia.client.gui;

import morph.avaritia.container.ContainerMachineBase;
import morph.avaritia.tile.TileMachineBase;
import net.covers1624.lib.gui.GuiAnimBase;

/**
 * Created by covers1624 on 21/05/2017.
 */
public abstract class GuiMachineBase<T extends TileMachineBase, C extends ContainerMachineBase<T>> extends GuiAnimBase {

    protected final T machineTile;
    protected final C container;

    public GuiMachineBase(C container) {
        super(container);
        this.container = container;
        machineTile = container.getTile();
    }

    protected static float scaleF(float num, float max, float pixels) {
        return num * pixels / max;
    }

    protected static int scale(int num, int max, int pixels) {
        return num * pixels / max;
    }
}
