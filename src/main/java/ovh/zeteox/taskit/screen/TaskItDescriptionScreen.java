package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class TaskItDescriptionScreen extends TaskItMainScreen {
    private final NbtCompound task;

    public TaskItDescriptionScreen(Text title, Screen parent, NbtCompound task) {
        super(title, parent);
        this.task = task;
    }

    @Override
    protected void init() {
        ButtonWidget backBtn = ButtonWidget.builder(Text.of("Back"),(btn) -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(width/2-20, ((height+guiHeight)/2)-29, 40, 20).build();
        this.addDrawableChild(backBtn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, task.getString("name"),10,10,0xFFFFFF,true);
        context.drawText(this.textRenderer, task.getString("type"),10,20,0xFFFFFF,true);
        context.drawText(this.textRenderer,
                task.getInt("nbAct") + "/" + task.getInt("nbMax"),
                10,30,0xFFFFFF,true);
        context.drawText(this.textRenderer, "Pinned",10,40,0xFFFFFF,true);
        context.drawText(this.textRenderer, "Completed",10,50,0xFFFFFF,true);
    }
}
