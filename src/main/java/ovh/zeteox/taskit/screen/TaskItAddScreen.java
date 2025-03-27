package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.Objects;

public class TaskItAddScreen extends TaskItMainScreen {
    private TaskTypes selectedTaskType = TaskTypes.CUSTOM;
    private Item selectedItem = Items.BARRIER;
    private TaskItItemSelectionScreen selectionScreen;
    private static final Logger log = LoggerFactory.getLogger(TaskItAddScreen.class);

    public TaskItAddScreen(Text title, Screen parent) {
        super(title, parent, 0);
    }

    @Override
    protected void init() {
        TextFieldWidget taskNameWidget = new TextFieldWidget(
                this.textRenderer,
                (width / 2) - 42,
                this.height / 4 + 24 + -16,
                84,
                15,
                Text.literal("Task Name"));
        taskNameWidget.setPlaceholder(Text.of("Task name..."));

        CyclingButtonWidget<TaskTypes> taskTypeWidget = CyclingButtonWidget.builder(TaskTypes::getName)
                .values(TaskTypes.values())
                .initially(TaskTypes.CUSTOM)
                .omitKeyText()
                .build((width / 2) - 43, this.height / 4 + 34, 60, 20, Text.literal(""), (button, taskType) -> {
                    this.selectedTaskType = taskType;
                });
        this.addDrawableChild(taskTypeWidget);

        ButtonWidget selectItemBtn = ButtonWidget.builder(Text.of("Select Item"), (btn) -> {
            this.selectionScreen = new TaskItItemSelectionScreen(Text.of("Select Item"), this, this.selectedTaskType,() -> {
                this.selectedItem = this.selectionScreen.getSelectedItem();
            });
            MinecraftClient.getInstance().setScreen(selectionScreen);
        }).dimensions(width / 2 - 43, this.height / 4 + 63, 86, 20).build();
        this.addDrawableChild(selectItemBtn);

        TextFieldWidget numberToDoWidget = new TextFieldWidget(
                this.textRenderer,
                (width / 2) - 42,
                this.height / 4 + 94,
                84,
                15,
                Text.literal("Number"));
        numberToDoWidget.setPlaceholder(Text.of("How many time?"));
        numberToDoWidget.setMaxLength(9);

        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("add"), (btn) -> {
            try {
                if (Objects.equals(taskNameWidget.getText(), "") || Integer.parseInt(numberToDoWidget.getText()) <= 0) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Can't create the task wrong input"), false);
                } else {
                    Task task = new Task(
                            taskNameWidget.getText(),
                            this.selectedTaskType,
                            Integer.parseInt(numberToDoWidget.getText()),
                            0,
                            false,
                            false);
                    ModClientConfig.addTask(task);
                    this.parent.init(MinecraftClient.getInstance(), this.width, this.height);
                    MinecraftClient.getInstance().setScreen(this.parent);
                }
            } catch (Exception ignored) {
                log.error("e: ", ignored);
                MinecraftClient.getInstance().player.sendMessage(Text.of("Can't create the task wrong input"), false);
            }
        }).dimensions(width / 2 - 10, ((height + guiHeight) / 2) - 28, 25, 20).build();

        this.addDrawableChild(taskNameWidget);
        this.addDrawableChild(numberToDoWidget);
        this.addDrawableChild(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        ItemStack stack = new ItemStack(this.selectedItem);
        context.drawItem(stack, (width+guiWidth)/2 - 30, this.guiHeight/2 + 17);
    }
}