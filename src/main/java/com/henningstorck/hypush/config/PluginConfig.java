package com.henningstorck.hypush.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class PluginConfig {
	public static final BuilderCodec<PluginConfig> CODEC = BuilderCodec
		.builder(PluginConfig.class, PluginConfig::new)
		.append(new KeyedCodec<>("Url", Codec.STRING),
			(config, value) -> config.url = value,
			config -> config.url)
		.add()
		.append(new KeyedCodec<>("Interval", Codec.INTEGER),
			(config, value) -> config.interval = value,
			config -> config.interval)
		.add()
		.build();

	private String url = "";
	private int interval = 30;

	public String getUrl() {
		return url;
	}

	public int getInterval() {
		return interval;
	}
}
