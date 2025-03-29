package ovh.zeteox.taskit.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.tasks.Task;

/**
 * A button that slides to the right when pressed.
 */
public class SlideButtonWidget extends TexturedButtonWidget {
    private static final Identifier TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final Task task;

    /**
     * Creates a new SlideButtonWidget.
     *
     * @param x          The x position of the button.
     * @param y          The y position of the button.
     * @param pressAction The action to perform when the button is pressed.
     * @param task       The task associated with this button.
     */
    public SlideButtonWidget(int x, int y, PressAction pressAction, Task task) {
        super(
                x,
                y,
                23,
                14,
                200,
                23,
                15,
                TEXTURE,
                pressAction
        );

        this.task = task;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        // Set a different texture for the button based on whether the task is pinned or not
        if (task.isPinned()) {
            this.drawTexture(
                    context,
                    this.texture,
                    this.getX(),
                    this.getY(),
                    this.u,
                    this.v,
                    this.hoveredVOffset,
                    this.width,
                    this.height,
                    this.textureWidth,
                    this.textureHeight
            );
        } else {
            this.drawTexture(
                    context,
                    this.texture,
                    this.getX(),
                    this.getY(),
                    this.u+24,
                    this.v,
                    this.hoveredVOffset,
                    this.width,
                    this.height,
                    this.textureWidth,
                    this.textureHeight
            );
        }
    }

    @Override
    public void setFocused(boolean focused) {
        //left empty to prevent the button from being focused
    }
}
