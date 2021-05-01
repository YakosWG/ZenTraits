package yako.zentraits;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yako.zentraits.commands.UtilCommands;
import yako.zentraits.util.HandleTraitManager;

@Mod(modid = ZenTraits.MODID, name = ZenTraits.NAME, version = ZenTraits.VERSION, dependencies = ZenTraits.DEPENCIES)
public class ZenTraits {

	public static final String MODID = "zentraits";
	public static final String NAME = "ZenTraits";
	public static final String VERSION = "1.0";
	public static final String DEPENCIES = "required-after:crafttweaker;after:tconstruct;after:contenttweaker;after:conarm;after:tconevo";

	public static Logger logger;

	@Instance
	public static ZenTraits instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		HandleTraitManager.getDefaultTraits();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		UtilCommands.registerCommands();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		for (Runnable r : HandleTraitManager.detachTasks) {
			r.run();
		}
		for (Runnable r : HandleTraitManager.attachTasks) {
			r.run();
		}

	}

}
