package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;
import ovh.zeteox.taskit.util.IEntityDataSaver;
import ovh.zeteox.taskit.util.TaskData;

import java.util.Objects;

public class TaskItAddScreen extends TaskItMainScreen {

    private static final Logger log = LoggerFactory.getLogger(TaskItAddScreen.class);

    public TaskItAddScreen(Text title, Screen parent) {
        super(title, parent);
    }

    @Override
    protected void init() {
        TextFieldWidget taskNameWidget = new TextFieldWidget(
                this.textRenderer,
                (width/2)-42,
                this.height / 4 + 24 + -16,
                84,
                15,
                Text.literal("Task Name"));
        taskNameWidget.setPlaceholder(Text.of("Task name..."));

        TextFieldWidget numberToDoWidget = new TextFieldWidget(
                this.textRenderer,
                (width/2)-42,
                this.height / 4 + 64,
                84,
                15,
                Text.literal("Number"));
        numberToDoWidget.setPlaceholder(Text.of("How many time?"));

        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("add"), (btn) -> {
            try {
                if (Objects.equals(taskNameWidget.getText(), "") || Integer.parseInt(numberToDoWidget.getText()) <= 0) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Can't create the task wrong input"), false);
                } else {
                    Task task = new Task(
                            taskNameWidget.getText(),
                            TaskTypes.MINING,
                            Integer.parseInt(numberToDoWidget.getText()),
                            0,
                            false,
                            false);
                    TaskData.addTask( (IEntityDataSaver) MinecraftClient.getInstance().player, task);
                    this.parent.init(MinecraftClient.getInstance(), this.width, this.height);
                    MinecraftClient.getInstance().setScreen(this.parent);
                }
            }catch (Exception ignored) {
                log.error("e: ", ignored);
                MinecraftClient.getInstance().player.sendMessage(Text.of("Can't create the task wrong input"), false);
            }
        }).dimensions(width/2-10, ((height+guiHeight)/2)-28, 25, 20).build();


        this.addDrawableChild(taskNameWidget);
        this.addDrawableChild(numberToDoWidget);
        this.addDrawableChild(buttonWidget);
    }
}
