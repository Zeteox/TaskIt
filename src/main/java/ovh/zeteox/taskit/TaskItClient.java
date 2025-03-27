package ovh.zeteox.taskit;

import net.fabricmc.api.ClientModInitializer;
import ovh.zeteox.taskit.TaskItModKeyBind.TaskItModKeybind;
import ovh.zeteox.taskit.config.ModClientConfig;

public class TaskItClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModClientConfig.loadConfig();
        TaskItModKeybind.loadKeyBinds();
    }
}
