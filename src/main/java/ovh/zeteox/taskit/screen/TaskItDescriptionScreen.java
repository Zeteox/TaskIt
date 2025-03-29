package ovh.zeteox.taskit.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.screen.widget.SlideButtonWidget;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;

/**
 * This class is used to display the description of a task.
 * It extends the {@link TaskItMainScreen} class.
 *
 * @see TaskItMainScreen
 */
public class TaskItDescriptionScreen extends TaskItMainScreen {
    private static final Identifier TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private final Task task;
    private int nbOfPinnedTasks;

    /**
     * Constructor for the {@code TaskItDescriptionScreen} class.
     *
     * @param title The title of the screen.
     * @param parent The parent screen.
     * @param task The task to display.
     */
    public TaskItDescriptionScreen(Text title, Screen parent, Task task) {
        super(title, parent);
        this.task = task;
    }

    @Override
    protected void init() {
        this.nbOfPinnedTasks = 0;

        // Adding a button to go back to the main screen
        ButtonWidget backBtn = ButtonWidget.builder(Text.of("X"),(btn) -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(width/2-45, height/2-73, 20, 20).build();

        // Counting the number of pinned tasks
        ModClientConfig.getTasks().forEach(task -> {
            if (task.isPinned()) {
                this.nbOfPinnedTasks++;
            }
        });

        // Adding a button to pin the task
        SlideButtonWidget btnPin = new SlideButtonWidget(
                width/2-20,
                height/2-9,
                (btn) -> {
                    // If the task is pinned, unpin it
                    // If the task is not pinned and there is less than 3 pinned tasks, pin it
                    if (this.nbOfPinnedTasks < 3 || task.isPinned()) {
                        List<Task> allTasks = ModClientConfig.getTasks();

                        (allTasks.get(allTasks.indexOf(task))).setPinned(!task.isPinned()); // Update the task in the config list
                        task.setPinned(!task.isPinned()); // Update the task visualy

                        ModClientConfig.updateTasks(allTasks);

                    } else {
                        MinecraftClient.getInstance().player.sendMessage(Text.of("You can only pin 3 tasks"), false);
                    }
                },
                task);

        // Adding a button to increase/decrease the task count if the task type is CUSTOM
        if (task.getTaskType() == TaskTypes.CUSTOM) {

            ButtonWidget addCnt = ButtonWidget.builder(Text.of("Add 1"), (btn) -> {
                task.addNumber(1);
            }).dimensions(width / 2 - 40, ((height + guiHeight) / 2) - 51, 32, 20).build();

            ButtonWidget rmvCnt = ButtonWidget.builder(Text.of("Remove 1"), (btn) -> {
                task.removeNumber(1);
            }).dimensions(width / 2 - 7, ((height + guiHeight) / 2) - 51, 47, 20).build();

            // Adding buttons to the drawing list
            this.addDrawableChild(addCnt);
            this.addDrawableChild(rmvCnt);
        }

        ButtonWidget removeBtn = ButtonWidget.builder(Text.of("Remove Task"),(btn) -> {
            ModClientConfig.removeTask(task);
            this.parent.init(MinecraftClient.getInstance(), this.width, this.height);
            MinecraftClient.getInstance().setScreen(new TaskItMainScreen(Text.of("TaskIt"), null));
        }).dimensions(width/2-40, ((height+guiHeight)/2)-30, 80, 20).build();

        // Adding buttons to the drawing list
        this.addDrawableChild(backBtn);
        this.addDrawableChild(btnPin);
        this.addDrawableChild(removeBtn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render the background texture and all the buttons added to the drawing list
        super.render(context, mouseX, mouseY, delta);

        int descTxtBaseX = width/2-45;
        int descTxtBaseY = 85;

        // Render the task description and item icon
        context.drawText(this.textRenderer, task.getTaskName(),descTxtBaseX,descTxtBaseY-3,0xFFFFFF,true);
        context.drawText(this.textRenderer, task.getTaskType().toString(),descTxtBaseX,descTxtBaseY+10,0xFFFFFF,true);
        context.drawText(this.textRenderer,
                task.getActualNumber() + "/" + task.getNumberOfTime(),
                descTxtBaseX,descTxtBaseY+23,0xFFFFFF,true);
        context.drawText(this.textRenderer, "Pin",descTxtBaseX,descTxtBaseY+40,0xFFFFFF,true);
        context.drawText(this.textRenderer, "Completed",descTxtBaseX,descTxtBaseY+60,0xFFFFFF,true);

        Registries.ITEM.forEach(item -> {
            if (item.getName().toString().equals(task.getTaskItem())) {
                context.drawItem(new ItemStack(item),descTxtBaseX+40,descTxtBaseY+5,0xFFFFFF);
            }
        });

        // Render the task completed status
        if (task.isCompleted()) {
            context.drawTexture(TEXTURE,descTxtBaseX+55,descTxtBaseY+60,209,53,8,8);
        } else {
            context.drawTexture(TEXTURE,descTxtBaseX+55,descTxtBaseY+60,200,53,8,8);
        }

    }
}
