package yako.zentraits.compat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTraits;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yako.zentraits.util.UtilMethods;

@ZenClass("zentraits.TraitManager")
@ZenRegister
@ModOnly("tconstruct")
public class TraitManager {

	// partTypes { "head", "projectile", "handle", "extra", "bow", "shaft",
	// "bowstring",
	// "fletching" }

	@ZenMethod
	@ZenDoc("Add trait to a material or (optionally) only one part of the material")
	public static void attachTrait(String materialID, String traitID, @Optional String partType) {

		Material mat = TinkerRegistry.getMaterial(materialID);
		ITrait trait = TinkerRegistry.getTrait(traitID);

		// Traits are split between TinkerRegistry and TinkerTraits
		if (trait == null) {

			if (UtilMethods.default_traits.containsKey(traitID)) {

				trait = UtilMethods.default_traits.get(traitID);

			}

		}

		if (mat != null && trait != null) {

			mat.addTrait(trait, partType);
			CraftTweakerAPI
					.logInfo("Attached trait " + traitID + " to material " + materialID + " for part " + partType);
			return;

		}

		CraftTweakerAPI
				.logError("Could not attach trait " + traitID + " to material " + materialID + " for part " + partType);
		CraftTweakerAPI.logError("Either the traitID or the materialID are invalid!");

	}

	@ZenMethod
	@ZenDoc("Will schedule a trait to be detached from a material. Because TiC registers the materials during init the task is run during postinit.")
	public static void detachTrait(String materialID, String traitID, @Optional String partType) {

		UtilMethods.detachTasks.add(new Runnable() {

			@Override
			public void run() {

				UtilMethods.tryDetachTrait(materialID, traitID, partType);

			}
		});

	}

	@ZenMethod
	@ZenDoc("Will schedule all traits to be detached from a material")
	public static void detachAllTraits(String materialID, @Optional String partType) {

		UtilMethods.detachTasks.add(new Runnable() {

			@Override
			public void run() {

				UtilMethods.tryDetachAllTraits(materialID, partType);

			}
		});

	}

	@ZenMethod
	@ZenDoc("Will dump all possible materials to the log. If a material is not listed here it has not been registered at this point in time")
	public static void dumpMats() {

		Collection<Material> mats = TinkerRegistry.getAllMaterials();

		for (Material m : mats) {

			CraftTweakerAPI.logInfo(m.getIdentifier());

		}

	}

	@ZenMethod
	@ZenDoc("Dumps all possible traits to the log. If a trait isn't listed here, it can't be attached using ZenTraits. If you believe that a trait should be listed here please contact the author of ZenTraits")
	public static void dumpTraits() {

		try {
			Field field = TinkerRegistry.class.getDeclaredField("traits");
			field.setAccessible(true);

			Object traitMapObject = field.get(null);

			@SuppressWarnings("unchecked")
			Map<String, ITrait> modifierTraits = (Map<String, ITrait>) traitMapObject;

			for (Entry<String, ITrait> e : modifierTraits.entrySet()) {

				CraftTweakerAPI.logInfo("Found trait: " + e.getKey());

			}

			Field[] traitFields = TinkerTraits.class.getFields();

			for (Field f : traitFields) {

				AbstractTrait ftrait = (AbstractTrait) f.get(null);
				CraftTweakerAPI.logInfo("Found trait: " + ftrait.getIdentifier());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
