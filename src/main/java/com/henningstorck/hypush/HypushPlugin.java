package com.henningstorck.hypush;

import com.henningstorck.hypush.config.PluginConfig;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class HypushPlugin extends JavaPlugin {
	private final Config<PluginConfig> pluginConfig = withConfig(PluginConfig.CODEC);

	public HypushPlugin(@NonNullDecl JavaPluginInit init) {
		super(init);
	}

	@Override
	protected void setup() {
		initConfig();
	}

	private void initConfig() {
		try {
			Path dataDir = getDataDirectory();

			if (!Files.exists(dataDir)) {
				Files.createDirectories(dataDir);
			}

			Path configFile = dataDir.resolve("config.json");

			if (!Files.exists(configFile)) {
				try (InputStream defaultConfig = getClass().getResourceAsStream("/config.json")) {
					if (defaultConfig != null) {
						Files.copy(defaultConfig, configFile);
					}
				}
			}
		} catch (IOException e) {
			getLogger().at(Level.WARNING).log("Cannot initialize config file.");
		}
	}
}
