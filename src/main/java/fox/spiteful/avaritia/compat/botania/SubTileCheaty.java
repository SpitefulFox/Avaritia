package fox.spiteful.avaritia.compat.botania;

import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;

public class SubTileCheaty extends SubTileGenerating {
    public static LexiconEntry lexicon;

    @Override
    public int getMaxMana() {
        return 1000000;
    }
    @Override
    public int getColor() {
        return 0x11FF00;
    }
    @Override
    public LexiconEntry getEntry() {
        return lexicon;
    }
    @Override
    public boolean canGeneratePassively() {
        return true;
    }
    @Override
    public int getDelayBetweenPassiveGeneration() {
        return 1;
    }
    @Override
    public int getValueForPassiveGeneration() {
        return 1000000;
    }
    @Override
    public IIcon getIcon(){
        return BotaniaAPI.getSignatureForName("asgardandelion").getIconForStack(null);
    }
}