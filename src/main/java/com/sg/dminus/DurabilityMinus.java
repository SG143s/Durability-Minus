package com.sg.dminus;

import com.sg.dminus.config.ConfigManager;
import com.sg.dminus.degrade_data.DegradeRegistry;
import com.sg.dminus.degrade_data.data_loader.GroupDataLoader;
import com.sg.dminus.degrade_data.data_loader.SingleDataLoader;
import com.sg.dminus.degrade_components.DegradeDataComponentApply;
import com.sg.dminus.degrade_components.DegradeDataComponents;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurabilityMinus implements ModInitializer {
	public static final String MOD_ID = "durability-minus";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		DegradeDataComponents.init();
		DegradeDataComponentApply.apply();
		ResourceLoader.get(PackType.SERVER_DATA)
				.registerReloader(
						Identifier.fromNamespaceAndPath(MOD_ID, "clear_registry"),
						(ResourceManagerReloadListener) manager -> DegradeRegistry.clear()
				);
		ResourceLoader.get(PackType.SERVER_DATA)
						.registerReloader(
								Identifier.fromNamespaceAndPath(MOD_ID, "degrade_single_loader"),
								new SingleDataLoader()
						);
		ResourceLoader.get(PackType.SERVER_DATA)
						.registerReloader(
								Identifier.fromNamespaceAndPath(MOD_ID, "degrade_group_loader"),
								new GroupDataLoader()
						);
		ConfigManager.load();

		LOGGER.info("Hello Fabric world!");
	}
}