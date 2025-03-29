package ovh.zeteox.taskit.TaskItModKeyBind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ovh.zeteox.taskit.screen.TaskItMainScreen;

/**
 * This class handles the key binding for opening the TaskIt screen.
 */
public class TaskItModKeybind {
    private static final KeyBinding openScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open Tasks", // Key binding name
            InputUtil.Type.KEYSYM, // Input type
            GLFW.GLFW_KEY_G, // Key to open the screen (G)
            "TaskIt" // Category name
    ));

    /**
     * Registers the key binding and sets up the event listener to open the TaskIt screen.
     */
    public static void loadKeyBinds() {
        // Register the key binding
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Check if the key is pressed
            while (openScreenKey.wasPressed()) {
                // Open the TaskIt screen
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new TaskItMainScreen(Text.of("TaskIt"), currentScreen)
                );
            }
        });
    }
}
