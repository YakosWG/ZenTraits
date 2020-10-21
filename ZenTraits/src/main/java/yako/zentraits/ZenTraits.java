package yako.zentraits;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yako.zentraits.proxy.CommonProxy;
import yako.zentraits.util.UtilMethods;

@Mod(modid = ZenTraits.MODID, name = ZenTraits.NAME, version = ZenTraits.VERSION, dependencies = ZenTraits.DEPENCIES)
public class ZenTraits {

	public static final String MODID = "zentraits";
	public static final String NAME = "ZenTraits";
	public static final String VERSION = "1.0-beta";
	public static final String DEPENCIES = "required-after:crafttweaker;after:tconstruct;after:contenttweaker";

	public static Logger logger;

	@Instance
	public static ZenTraits instance;

	@SidedProxy(clientSide = "yako.zentraits.proxy.ClientProxy", serverSide = "yako.zentraits.proxy.ServerProxy")
	private static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		UtilMethods.getDefaultTraits();

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		for (Runnable r : UtilMethods.detachTasks) {

			r.run();

		}

		proxy.postInit(event);
	}

}
