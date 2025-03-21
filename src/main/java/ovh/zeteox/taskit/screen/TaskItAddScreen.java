package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TaskItAddScreen extends TaskItMainScreen {

    public TaskItAddScreen(Text title, Screen parent) {
        super(title, parent);
    }

    @Override
    protected void init() {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("add"), (btn) -> {
            MinecraftClient.getInstance().setScreen(this.parent);
            MinecraftClient.getInstance().player.sendMessage(Text.of("Added Task"));
        }).dimensions(width/2-10, ((height+guiHeight)/2)-28, 25, 20).build();
        this.addDrawableChild(buttonWidget);
    }
}
