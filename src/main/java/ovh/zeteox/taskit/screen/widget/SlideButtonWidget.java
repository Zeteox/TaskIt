package ovh.zeteox.taskit.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.tasks.Task;

public class SlideButtonWidget extends TexturedButtonWidget {
    private static final Identifier TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final Task task;

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
        if (task.isPined()) {
            this.drawTexture(
                    context, this.texture, this.getX(), this.getY(), this.u, this.v, this.hoveredVOffset, this.width, this.height, this.textureWidth, this.textureHeight
            );
        } else {
            this.drawTexture(
                    context, this.texture, this.getX(), this.getY(), this.u+24, this.v, this.hoveredVOffset, this.width, this.height, this.textureWidth, this.textureHeight
            );
        }
    }

    @Override
    public void setFocused(boolean focused) {
        //stop the button from being focused
    }
}
