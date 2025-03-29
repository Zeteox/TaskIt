package ovh.zeteox.taskit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import ovh.zeteox.taskit.TaskItModKeyBind.TaskItModKeybind;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.hud.TasksHudOverlay;

public class TaskItClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModClientConfig.loadConfig(); // Load the client config on startup or create it if it doesn't exist
        TaskItModKeybind.loadKeyBinds(); // Load the key binds for opening the TaskIt screen

        HudRenderCallback.EVENT.register(new TasksHudOverlay()); // Register the HUD overlay for displaying tasks
    }
}
