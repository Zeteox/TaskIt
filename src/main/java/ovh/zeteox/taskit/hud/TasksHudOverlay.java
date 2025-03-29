package ovh.zeteox.taskit.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;

import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for rendering the tasks overlay on the HUD.
 * It implements the {@link HudRenderCallback} interface to draw the tasks on the screen.
 *
 * @see HudRenderCallback
 */
public class TasksHudOverlay implements HudRenderCallback {
    public static final Identifier TASKS_HUD_TEXTURE = new Identifier("taskit", "textures/gui/main_gui.png");
    public static Item taskItem = null;

    @Override
    public void onHudRender(DrawContext drawContext, float v) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();

        // Check if the client is not null and get the window dimensions
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width - 100;
            y = height / 4;
        }

        List<Task> tasks = ModClientConfig.getTasks();

        // Draw all tasks that are pinned
        for (Task task : tasks) {
            if (task.isPinned()) {
                drawContext.drawTexture(TASKS_HUD_TEXTURE, x, y, 110, 1, 89, 32);

                // Draw specific task texture based on completion status
                if (task.isCompleted()) {
                    drawContext.drawTexture(
                            TASKS_HUD_TEXTURE, x+41, y+1, 219, 6, 8, 4
                    );
                } else {
                    drawContext.drawTexture(
                            TASKS_HUD_TEXTURE, x+41, y+1, 219, 1, 8, 4
                    );
                }

                // Get the item associated with the task
                Registries.ITEM.forEach((item) -> {
                    if (Objects.equals(item.getName().toString(), task.getTaskItem())) {
                        taskItem = item;
                    }
                });

                // Draw the item icon
                drawContext.drawItem(new ItemStack(taskItem), x + 7, y + 8, 0xFFFFFF);

                // Draw the task name
                drawContext.drawText(
                        MinecraftClient.getInstance().textRenderer,
                        Text.of(task.getTaskName()),
                        x + 27,
                        y + 7,
                        0xFFFFFF,
                        true);

                // Draw the task progress
                drawContext.drawText(
                        MinecraftClient.getInstance().textRenderer,
                        Text.of(task.getActualNumber() + "/" + task.getNumberOfTime()),
                        x + 27,
                        y + 17,
                        0xFFFFFF,
                        true);

                //increment y position for the next task
                y+= 34;
            }
        }
    }
}
