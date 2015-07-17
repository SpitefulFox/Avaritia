package fox.spiteful.avaritia;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class LudicrousText {

	private static final EnumChatFormatting[] fabulousness = new EnumChatFormatting[] {EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE};
	
	public static String makeFabulous(String input) {
		StringBuilder sb = new StringBuilder(input.length()*3);
		
		int offset = (int) Math.floor(Minecraft.getSystemTime() / 80.0) % fabulousness.length;
		
		for (int i=0; i<input.length(); i++) {
			char c = input.charAt(i);
			
			int col = (i + fabulousness.length - offset) % fabulousness.length;
			
			sb.append(fabulousness[col].toString());
			sb.append(c);
		}
		
		return sb.toString();
	}
}
