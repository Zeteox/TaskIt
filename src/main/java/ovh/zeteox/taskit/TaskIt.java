package ovh.zeteox.taskit;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskIt implements ModInitializer {
	public static final String MOD_ID = "taskit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("TaskIt Mod has been loaded");
	}
}