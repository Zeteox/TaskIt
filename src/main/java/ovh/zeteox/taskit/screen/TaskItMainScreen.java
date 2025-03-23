package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.screen.widget.TaskButtonWidget;
import ovh.zeteox.taskit.util.IEntityDataSaver;

public class TaskItMainScreen extends Screen {
    public Screen parent;
    public int guiWidth;
    public int guiHeight;

    private static final Identifier BACKGROUND_TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");

    public TaskItMainScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
        this.guiWidth = 109;
        this.guiHeight = 166;
    }

    @Override
    protected void init() {
        NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData();
        NbtList tasks = nbt.getList("Tasks", NbtElement.COMPOUND_TYPE);
        if (!tasks.isEmpty()) {
            int pos = 10;
            for (int i = 0; i < tasks.size(); i++) {
                NbtCompound task = (NbtCompound) tasks.get(i);
                String taskName = task.getString("name");
                TaskButtonWidget taskBtn = new TaskButtonWidget(
                        taskName,
                        (width-89)/2,
                        (height/2)-73+pos,
                        this.textRenderer,
                        this,
                        task);
                pos+=35;
                this.addDrawableChild(taskBtn);
            }
        }

        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("+"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new TaskItAddScreen(Text.of("TaskItAdd"),
                            MinecraftClient.getInstance().currentScreen)
            );
        }).dimensions(((width+guiWidth)/2)+1, ((height+guiHeight)/2)-29, 20, 20).build();
        this.addDrawableChild(buttonWidget);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //TEXT
        context.drawText(
                this.textRenderer,
                this.title,
                (width-this.textRenderer.getWidth(this.title))/2,
                39,
                0xFFFFFFFF,
                true);
        context.drawText(
                this.textRenderer,
                "Tasks:",
                (width/2)-16,
                59,
                0xFFFFFFFF,
                true);

        //BACKGROUND
        context.drawTexture(
                BACKGROUND_TEXTURE,
                (width-guiWidth)/2,
                (height-guiHeight)/2,
                0,0,
                guiWidth,guiHeight);

        //RENDER BUTTONS
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

}
