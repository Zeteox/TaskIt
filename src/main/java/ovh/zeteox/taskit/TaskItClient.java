package ovh.zeteox.taskit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ovh.zeteox.taskit.screen.TaskItMainScreen;

public class TaskItClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open Tasks",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "TaskIt"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new TaskItMainScreen(Text.of("TaskIt"), currentScreen)
                );
            }
        });
    }
}
