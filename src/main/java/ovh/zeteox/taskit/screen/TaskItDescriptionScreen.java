package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.screen.widget.SlideButtonWidget;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;

public class TaskItDescriptionScreen extends TaskItMainScreen {
    private static final Identifier TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final Task task;

    public TaskItDescriptionScreen(Text title, Screen parent, Task task) {
        super(title, parent, 0);
        this.task = task;
    }

    @Override
    protected void init() {
        ButtonWidget backBtn = ButtonWidget.builder(Text.of("<<"),(btn) -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(width/2-45, height/2-73, 20, 20).build();
        this.addDrawableChild(backBtn);

        SlideButtonWidget btnPin = new SlideButtonWidget(width/2-20, height/2-9, (btn) -> {
                    List<Task> allTasks = ModClientConfig.getTasks();
                    (allTasks.get(allTasks.indexOf(task))).setPined(!task.isPined());
                    task.setPined(!task.isPined());
                    ModClientConfig.updateTasks(allTasks);
                }, task);
        this.addDrawableChild(btnPin);

        if (task.getTaskType() == TaskTypes.CUSTOM) {
            ButtonWidget addCnt = ButtonWidget.builder(Text.of("Add 1"), (btn) -> {
                task.addNumber(1);
            }).dimensions(width / 2 - 40, ((height + guiHeight) / 2) - 50, 32, 20).build();
            this.addDrawableChild(addCnt);

            ButtonWidget rmvCnt = ButtonWidget.builder(Text.of("Rmv 1"), (btn) -> {
                task.removeNumber(1);
            }).dimensions(width / 2, ((height + guiHeight) / 2) - 50, 32, 20).build();
            this.addDrawableChild(rmvCnt);
        }

        ButtonWidget removeBtn = ButtonWidget.builder(Text.of("Remove Task"),(btn) -> {
            ModClientConfig.removeTask(task);
            this.parent.init(MinecraftClient.getInstance(), this.width, this.height);
            MinecraftClient.getInstance().setScreen(new TaskItMainScreen(Text.of("TaskIt"), null, 0));
        }).dimensions(width/2-40, ((height+guiHeight)/2)-30, 80, 20).build();
        this.addDrawableChild(removeBtn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int descTxtBaseX = width/2-45;
        int descTxtBaseY = 85;

        if (task.isCompleted()) {
            context.drawTexture(TEXTURE,descTxtBaseX+55,descTxtBaseY+60,209,53,8,8);
        } else {
            context.drawTexture(TEXTURE,descTxtBaseX+55,descTxtBaseY+60,200,53,8,8);
        }

        context.drawText(this.textRenderer, task.getTaskName(),descTxtBaseX,descTxtBaseY,0xFFFFFF,true);
        context.drawText(this.textRenderer, task.getTaskType().toString(),descTxtBaseX,descTxtBaseY+10,0xFFFFFF,true);
        context.drawText(this.textRenderer,
                task.getActualNumber() + "/" + task.getNumberOfTime(),
                descTxtBaseX,descTxtBaseY+20,0xFFFFFF,true);
        context.drawText(this.textRenderer, "Pin",descTxtBaseX,descTxtBaseY+40,0xFFFFFF,true);
        context.drawText(this.textRenderer, "Completed",descTxtBaseX,descTxtBaseY+60,0xFFFFFF,true);
    }
}
