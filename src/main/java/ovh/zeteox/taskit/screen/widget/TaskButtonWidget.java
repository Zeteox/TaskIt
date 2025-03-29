package ovh.zeteox.taskit.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.screen.TaskItDescriptionScreen;
import ovh.zeteox.taskit.tasks.Task;

/**
 * A button widget that represents a task in the GUI.
 * It displays the task's completion status, pin status and name.
 * This class extends {@link TexturedButtonWidget} to provide a custom button with textures.
 *
 * @see TexturedButtonWidget
 */
public class TaskButtonWidget extends TexturedButtonWidget {
    private static final Identifier TEXTURES = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final TextRenderer textRenderer;
    private final boolean taskCompleted;
    private final boolean taskPinned;

    /**
     * Creates a new TaskButtonWidget.
     *
     * @param text         The text to display on the button.
     * @param x            The x position of the button.
     * @param y            The y position of the button.
     * @param textRenderer  The text renderer to use for rendering the button text.
     * @param screen       The parent screen.
     * @param task         The task associated with this button.
     */
    public TaskButtonWidget(String text, int x, int y, TextRenderer textRenderer, Screen screen, Task task) {
        super(
                x,
                y,
                89,
                32,
                110,
                1,
                33,
                TEXTURES,
                256,
                256,
                (btn) -> {
                    MinecraftClient.getInstance().setScreen(new TaskItDescriptionScreen(Text.of("TaskIt Desc"), screen, task));
                },
                Text.of(text));

        this.taskCompleted = task.isCompleted();
        this.textRenderer = textRenderer;
        this.taskPinned = task.isPinned();
    }

    /**
     * Draws the button with the task's name, completion status and pinned status.
     *
     * @param context The draw context.
     * @param mouseX  The mouse X position.
     * @param mouseY  The mouse Y position.
     * @param delta   The delta time.
     */
    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);

        // Draw an additional object with a different texture based on the completion status
        if (this.taskCompleted) {
          context.drawTexture(
                  TEXTURES, this.getX()+42, this.getY()+1, 219, 6, 8, 4
          );
        } else {
            context.drawTexture(
                    TEXTURES, this.getX()+42, this.getY()+1, 219, 1, 8, 4
            );
        }

        // Draw an additional object if the task is pinned
        if (this.taskPinned) {
            context.drawTexture(
                    TEXTURES, this.getX()+77, this.getY()+2, 231, 1, 10, 9
            );
        }

        // Draw the task name
        this.drawMessage(context, this.textRenderer, 0xFFFFFF);
    }
}
