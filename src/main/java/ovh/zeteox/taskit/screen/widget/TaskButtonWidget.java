package ovh.zeteox.taskit.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Font;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.screen.TaskItDescriptionScreen;

public class TaskButtonWidget extends TexturedButtonWidget {
    private static final Identifier BUTTON_TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final TextRenderer textRenderer;

    public TaskButtonWidget(String text, int x, int y, TextRenderer textRenderer, Screen screen) {
        super(
                x,
                y,
                89,
                32,
                110,
                1,
                33,
                BUTTON_TEXTURE,
                256,
                256,
                (btn) -> {
                    MinecraftClient.getInstance().setScreen(new TaskItDescriptionScreen(Text.of("TaskIt Desc"), screen));
                },
                Text.of(text));

        this.textRenderer = textRenderer;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
        this.drawMessage(context, this.textRenderer, 0xFFFFFF);
    }
}
