package ovh.zeteox.taskit.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.screen.TaskItDescriptionScreen;
import ovh.zeteox.taskit.tasks.Task;

public class TaskButtonWidget extends TexturedButtonWidget {
    private static final Identifier TEXTURES = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final TextRenderer textRenderer;
    private final boolean taskCompleted;

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
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
        if (this.taskCompleted) {
          context.drawTexture(
                  TEXTURES, this.getX()+42, this.getY()+1, 219, 6, 8, 4
          );
        } else {
            context.drawTexture(
                    TEXTURES, this.getX()+42, this.getY()+1, 219, 1, 8, 4
            );
        }
        this.drawMessage(context, this.textRenderer, 0xFFFFFF);
    }
}
