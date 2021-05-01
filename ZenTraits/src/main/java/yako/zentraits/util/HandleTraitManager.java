package yako.zentraits.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTraits;
import yako.zentraits.ZenTraits;

public class HandleTraitManager {

	public static List<Runnable> detachTasks = new ArrayList<Runnable>();
	public static List<Runnable> attachTasks = new ArrayList<Runnable>();
	public static Map<String, AbstractTrait> default_traits = new HashMap<String, AbstractTrait>();

	public static void getDefaultTraits() {

		Field[] traitFields = TinkerTraits.class.getFields();

		for (Field f : traitFields) {

			try {
				AbstractTrait ftrait = (AbstractTrait) f.get(null);
				default_traits.put(ftrait.getIdentifier(), ftrait);

			} catch (Exception e) {
				ZenTraits.logger.error("Something went wrong loading default traits", e);
			}

		}

	}

	public static void tryAttachTrait(String materialID, String traitID, String partType) {
		Material mat = TinkerRegistry.getMaterial(materialID);
		ITrait trait = TinkerRegistry.getTrait(traitID);

		// Traits are split between TinkerRegistry and TinkerTraits
		if (trait == null) {
			if (HandleTraitManager.default_traits.containsKey(traitID)) {
				trait = HandleTraitManager.default_traits.get(traitID);
			}
		}

		if (mat != null && trait != null) {

			mat.addTrait(trait, partType);
			CraftTweakerAPI.logInfo(
					"Attached trait " + traitID + " to material " + materialID + " for part " + partType);
			return;

		}
		CraftTweakerAPI.logError(
				"Could not attach trait " + traitID + " to material " + materialID + " for part " + partType);

		CraftTweakerAPI.logError("The following values were read :");

		if (mat != null)
			CraftTweakerAPI.logError("Material:" + mat.identifier);
		else
			CraftTweakerAPI.logError("Material: null");
		if (trait != null)
			CraftTweakerAPI.logError("Material:" + trait.getIdentifier());
		else
			CraftTweakerAPI.logError("Trait: null");
		if (partType != null)
			CraftTweakerAPI.logError("Part: " + partType);
		else
			CraftTweakerAPI.logError("partType: null");

	}

	public static void tryDetachTrait(String materialID, String traitID, String partType) {

		Material mat = TinkerRegistry.getMaterial(materialID);

		if (mat != null && traitID != null) {

			try {
				Field fieldTraits = mat.getClass().getDeclaredField("traits");
				fieldTraits.setAccessible(true);

				@SuppressWarnings("unchecked")
				Map<String, List<ITrait>> materialTraits = (Map<String, List<ITrait>>) fieldTraits.get(mat);

				if (partType != null) {

					List<ITrait> traits = materialTraits.get(partType);

					if (traits == null) {

						CraftTweakerAPI.logError("Detaching the trait: " + traitID + " from material: " + materialID
								+ " for parttype: " + partType + "has failed! The parttype is invalid");
						return;

					}

					traits.removeIf(t -> (t.getIdentifier().equals(traitID)));
					materialTraits.put(partType, traits);

				} else {

					for (String part : materialTraits.keySet()) {

						List<ITrait> traits = materialTraits.get(part);

						traits.removeIf(t -> (t.getIdentifier().equals(traitID)));
						materialTraits.put(part, traits);
					}
				}

				fieldTraits.set(mat, materialTraits);

				CraftTweakerAPI.logInfo("Detached the trait: " + traitID + " from material: " + materialID);
				return;

			} catch (Exception e) {
				CraftTweakerAPI.logError(
						"Detaching the trait: " + traitID + " from material: " + materialID + " has failed!", e);
				return;
			}
		}

		CraftTweakerAPI.logError("Detaching the trait: " + traitID + " from material: " + materialID
				+ " has failed! The materialID is invalid!");

	}

	public static void tryDetachAllTraits(String materialID, String partType) {

		Material mat = TinkerRegistry.getMaterial(materialID);

		if (mat != null) {

			try {

				Field fieldTraits = mat.getClass().getDeclaredField("traits");
				fieldTraits.setAccessible(true);

				@SuppressWarnings("unchecked")
				Map<String, List<ITrait>> materialTraits = (Map<String, List<ITrait>>) fieldTraits.get(mat);

				if (partType != null) {

					List<ITrait> traits = materialTraits.get(partType);

					traits.clear();
					materialTraits.put(partType, traits);

				} else {

					for (String part : materialTraits.keySet()) {

						List<ITrait> traits = materialTraits.get(part);

						traits.clear();
						materialTraits.put(part, traits);
					}
				}

				fieldTraits.set(mat, materialTraits);

				CraftTweakerAPI.logInfo("Detached the traits: from material: " + materialID);
				return;

			}

			catch (Exception e) {

				CraftTweakerAPI.logError("Detaching the traits from material: " + materialID + " has failed!", e);
				return;

			}
		}

		CraftTweakerAPI.logError(
				"Detaching the traits from material: " + materialID + " has failed! The materialID is invalid!");

	}

}
