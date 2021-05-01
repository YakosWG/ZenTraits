package yako.zentraits.compat;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yako.zentraits.util.HandleTraitManager;

@ZenClass("zentraits.TraitManager")
@ZenRegister
@ModOnly("tconstruct")
public class TraitManager {

	// partTypes { "head", "projectile", "handle", "extra", "bow", "shaft",
	// "bowstring", "fletching", conarm: "core", "plate", "trim" }

	@ZenMethod
	@ZenDoc("Add trait to a material or (optionally) only one part of the material")
	public static void attachTrait(String materialID, String traitID, @Optional String partType) {

		HandleTraitManager.attachTasks.add(new Runnable() {

			@Override
			public void run() {
				HandleTraitManager.tryAttachTrait(materialID, traitID, partType);
			}
		});

	}

	@ZenMethod
	@ZenDoc("Will schedule a trait to be detached from a material. Because TiC registers the materials during init the task is run during postinit.")
	public static void detachTrait(String materialID, String traitID, @Optional String partType) {

		HandleTraitManager.detachTasks.add(new Runnable() {
			@Override
			public void run() {
				HandleTraitManager.tryDetachTrait(materialID, traitID, partType);
			}
		});

	}

	@ZenMethod
	@ZenDoc("Will schedule all traits to be detached from a material")
	public static void detachAllTraits(String materialID, @Optional String partType) {

		HandleTraitManager.detachTasks.add(new Runnable() {
			@Override
			public void run() {
				HandleTraitManager.tryDetachAllTraits(materialID, partType);
			}
		});
	}

}
