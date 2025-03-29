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

/**
 * {@code TaskItAddScreen} is a screen that allows the user to add a new task.
 * It extends the {@link TaskItMainScreen} class.
 * It contains fields for the task name, task type, item selection, and number of times to do the task.
 *
 * @see TaskItMainScreen
 */
public class TaskItAddScreen extends TaskItMainScreen {
    private TaskTypes selectedTaskType = TaskTypes.CUSTOM;
    private Item selectedItem = Items.BARRIER; // Default item to avoid null pointer exception
    private TaskItItemSelectionScreen selectionScreen;
    private static final Logger log = LoggerFactory.getLogger(TaskItAddScreen.class);

    /**
     * Constructor for TaskItAddScreen.
     *
     * @param title  The title of the screen.
     * @param parent The parent screen.
     */
    public TaskItAddScreen(Text title, Screen parent) {
        super(title, parent);
    }

    @Override
    protected void init() {
        // Add the text field for the task name
        TextFieldWidget taskNameWidget = new TextFieldWidget(
                this.textRenderer,
                (width / 2) - 42,
                this.height / 4 + 24 + -16,
                84,
                15,
                Text.literal("Task Name"));
        taskNameWidget.setMaxLength(12); // Set max length to 12 for design reasons
        taskNameWidget.setPlaceholder(Text.of("Task name..."));

        // Add the selection button for the task type
        CyclingButtonWidget<TaskTypes> taskTypeWidget = CyclingButtonWidget.builder(TaskTypes::getName)
                .values(TaskTypes.values())
                .initially(TaskTypes.CUSTOM)
                .omitKeyText()
                .build((width / 2) - 43, this.height / 4 + 34, 60, 20, Text.literal(""), (button, taskType) -> {
                    this.selectedTaskType = taskType;
                });

        // Add the button for the task item selection
        ButtonWidget selectItemBtn = ButtonWidget.builder(Text.of("Select Item"), (btn) -> {
            this.selectionScreen = new TaskItItemSelectionScreen(Text.of("Select Item"), this,() -> {
                this.selectedItem = this.selectionScreen.getSelectedItem();
            });
            MinecraftClient.getInstance().setScreen(selectionScreen);
        }).dimensions(width / 2 - 43, this.height / 4 + 63, 86, 20).build();

        // Add the text field for the number of times to do the task
        TextFieldWidget numberToDoWidget = new TextFieldWidget(
                this.textRenderer,
                (width / 2) - 42,
                this.height / 4 + 94,
                84,
                15,
                Text.literal("Number"));
        numberToDoWidget.setPlaceholder(Text.of("How many time?"));
        numberToDoWidget.setMaxLength(9); // Set max length to 9 because the max minecraft number is 999 999 999

        // Add the button to create the task
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("add"), (btn) -> {
            try {
                // Check if the task name is empty or the number of times to do the task is less than or equal to 0
                if (Objects.equals(taskNameWidget.getText(), "") || Integer.parseInt(numberToDoWidget.getText()) <= 0) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Can't create the task wrong input"), false);

                } else {
                    // Create the task and add it to the config if all inputs are valid
                    Task task = new Task(
                            taskNameWidget.getText(),
                            this.selectedTaskType,
                            Integer.parseInt(numberToDoWidget.getText()),
                            0,
                            false,
                            false,
                            this.selectedItem.getName().toString());

                    ModClientConfig.addTask(task);
                    this.parent.init(MinecraftClient.getInstance(), this.width, this.height); // Refresh the parent screen
                    MinecraftClient.getInstance().setScreen(this.parent); // Go back to the parent screen
                }
            } catch (Exception e) {
                // Catch any exception that occurs if the number input is not a number
                log.error("e: ", e); // Log the error without stopping the program
                MinecraftClient.getInstance().player.sendMessage(Text.of("Can't create the task wrong input"), false);
            }
        }).dimensions(width / 2 - 10, ((height + guiHeight) / 2) - 28, 25, 20).build();

        // Add all the widget to the drawing list
        this.addDrawableChild(taskNameWidget);
        this.addDrawableChild(taskTypeWidget);
        this.addDrawableChild(selectItemBtn);
        this.addDrawableChild(numberToDoWidget);
        this.addDrawableChild(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw the background and all the widgets of the drawing list
        super.render(context, mouseX, mouseY, delta);

        // Draw the selected item icon
        ItemStack stack = new ItemStack(this.selectedItem);
        context.drawItem(stack, (width+guiWidth)/2 - 30, this.guiHeight/2 + 17);
    }
}