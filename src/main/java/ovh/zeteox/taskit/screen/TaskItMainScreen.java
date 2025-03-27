package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.screen.widget.TaskButtonWidget;
import ovh.zeteox.taskit.tasks.Task;

import java.util.List;

public class TaskItMainScreen extends Screen {
    protected Screen parent;
    protected int guiWidth;
    protected int guiHeight;
    protected int pageNumber;

    private static final Identifier BACKGROUND_TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");

    public TaskItMainScreen(Text title, Screen parent, int pageNumber) {
        super(title);
        this.parent = parent;
        this.guiWidth = 109;
        this.guiHeight = 166;
        this.pageNumber = pageNumber;
    }

    @Override
    protected void init() {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("+"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new TaskItAddScreen(Text.of("TaskItAdd"),
                            MinecraftClient.getInstance().currentScreen)
            );
        }).dimensions(((width+guiWidth)/2)+1, ((height+guiHeight)/2)-29, 20, 20).build();
        this.addDrawableChild(buttonWidget);

        ButtonWidget btnNext = ButtonWidget.builder(Text.of(">"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new TaskItMainScreen(Text.of("TaskIt"), this.parent, pageNumber+1)
            );
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+5, 20, 20).build();
        btnNext.active = false;
        this.addDrawableChild(btnNext);

        ButtonWidget btnPrev = ButtonWidget.builder(Text.of("<"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new TaskItMainScreen(Text.of("TaskIt"), this.parent, pageNumber-1)
            );
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+25, 20, 20).build();
        btnPrev.active = false;
        this.addDrawableChild(btnPrev);

        List<Task> tasks = ModClientConfig.getTasks();
        if (!tasks.isEmpty()) {
            int pos = 20;
            if (tasks.size()-(this.pageNumber*4) > 4) {
                btnNext.active = true;
            }

            if (this.pageNumber > 0) {
                btnPrev.active = true;
            }

            int cnt = 0;
            for (int x = (this.pageNumber*4); x < tasks.size(); x++) {
                Task task = tasks.get(x);
                TaskButtonWidget taskBtn = new TaskButtonWidget(
                        task.getTaskName(),
                        (width-guiWidth)/2+10,
                        (height-guiHeight)/2+pos,
                        textRenderer,
                        TaskItMainScreen.this,
                        task);
                pos+=35;
                cnt++;
                this.addDrawableChild(taskBtn);
                if (cnt == 4) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //BACKGROUND
        context.drawTexture(
                BACKGROUND_TEXTURE,
                (width-guiWidth)/2,
                (height-guiHeight)/2,
                0,0,
                guiWidth,guiHeight);

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

        //RENDER BUTTONS
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
