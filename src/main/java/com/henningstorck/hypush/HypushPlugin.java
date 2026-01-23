package com.henningstorck.hypush;

import com.henningstorck.hypush.config.PluginConfig;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

	@Override
	protected void start() {
		if (pluginConfig.get().getUrl().isEmpty()) {
			getLogger().at(Level.WARNING).log("Set a push URL first.");
			return;
		}

		getLogger().at(Level.INFO).log("Starting pushing to %s every %d seconds.",
			pluginConfig.get().getUrl(), pluginConfig.get().getInterval());

		loop();
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

	private void loop() {
		new Thread(() -> {
			try {
				while (true) {
					push();
					Thread.sleep(1000L * pluginConfig.get().getInterval());
				}
			} catch (InterruptedException e) {
				// Do nothing
			}
		}).start();
	}

	private void push() {
		URI uri = URI.create(pluginConfig.get().getUrl());
		HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();

		try (HttpClient httpClient = HttpClient.newBuilder().build()) {
			httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
		} catch (IOException | InterruptedException e) {
			getLogger().at(Level.WARNING).log("Failed to push.");
		}
	}
}
