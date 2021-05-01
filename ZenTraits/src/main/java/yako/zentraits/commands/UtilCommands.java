package yako.zentraits.commands;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.getClickableCommandText;
import static crafttweaker.mc1120.commands.SpecialMessagesChat.getLinkToCraftTweakerLog;
import static crafttweaker.mc1120.commands.SpecialMessagesChat.getNormalMessage;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CTChatCommand;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTraits;

public class UtilCommands {
	
	public static void registerCommands() {
		CTChatCommand.registerCommand(dumpMats);
		CTChatCommand.registerCommand(dumpTraits);
	}

	private static CraftTweakerCommand dumpMats = new CraftTweakerCommand("ztdumpmats") {

		@Override
		protected void init() {
			setDescription(getClickableCommandText("\u00A72/ct ztdumpmats", "/ct ztdumpmats", true), getNormalMessage(
					" \u00A73Outputs a list of all Tinker's Contruct materials in the game to the crafttweaker.log"));
		}

		@Override
		public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
			Collection<Material> mats = TinkerRegistry.getAllMaterials();
			CraftTweakerAPI.logCommand("Found materials: ");
			for (Material m : mats) {
				CraftTweakerAPI.logCommand(m.getIdentifier());
			}
			CraftTweakerAPI.logCommand("------------------------------------");
			
			sender.sendMessage(getLinkToCraftTweakerLog("List of materials generated;", sender));
		}
	};

	private static CraftTweakerCommand dumpTraits = new CraftTweakerCommand("ztdumptraits") {

		@Override
		protected void init() {
			setDescription(getClickableCommandText("\u00A72/ct ztdumptraits", "/ct ztdumptraits", true),
					getNormalMessage(
							" \u00A73Outputs a list of all Tinker's Contruct traits in the game to the crafttweaker.log"));
		}

		@Override
		public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {

			try {
				Field field = TinkerRegistry.class.getDeclaredField("traits");
				field.setAccessible(true);

				Object traitMapObject = field.get(null);

				@SuppressWarnings("unchecked")
				Map<String, ITrait> modifierTraits = (Map<String, ITrait>) traitMapObject;

				CraftTweakerAPI.logCommand("Found traits: ");
				for (Entry<String, ITrait> e : modifierTraits.entrySet()) {
					CraftTweakerAPI.logCommand(e.getKey());
				}

				Field[] traitFields = TinkerTraits.class.getFields();
				
				for (Field f : traitFields) {
					AbstractTrait ftrait = (AbstractTrait) f.get(null);
					CraftTweakerAPI.logCommand(ftrait.getIdentifier());
				}
				CraftTweakerAPI.logCommand("------------------------------------");
				
				sender.sendMessage(getLinkToCraftTweakerLog("List of traits generated;", sender));

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	};

}
