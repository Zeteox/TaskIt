package ovh.zeteox.taskit;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.zeteox.taskit.util.PlayerCopyHandler;

public class TaskIt implements ModInitializer {
	public static final String MOD_ID = "taskit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerPlayerEvents.COPY_FROM.register(new PlayerCopyHandler());

		LOGGER.info("TaskIt Mod has been loaded");
	}
}