package fox.spiteful.avaritia.compat.botania;

import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;

public class SubTileChicken extends SubTileGenerating {
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
        return 20;
    }
    @Override
    public int getValueForPassiveGeneration() {
        return 0;
    }
    @Override
    public IIcon getIcon(){
        return BotaniaAPI.getSignatureForName("soarleander").getIconForStack(null);
    }
}
