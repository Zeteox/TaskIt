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

/**
 * The main screen for the TaskIt mod.
 * This screen displays a list of tasks and allows the user to add new tasks.
 * It also provides navigation buttons to switch between pages of tasks.
 */
public class TaskItMainScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier(TaskIt.MOD_ID, "textures/gui/main_gui.png");
    private static final int NUMBER_OF_TASKS_PER_PAGE = 4;

    private int pageNumber = 0;

    protected Screen parent;
    protected int guiWidth;
    protected int guiHeight;


    /**
     * Constructor for the TaskItMainScreen.
     *
     * @param title      The title of the screen.
     * @param parent     The parent screen.
     */
    public TaskItMainScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
        this.guiWidth = 109;
        this.guiHeight = 166;
    }

    @Override
    protected void init() {

        // Adding the button to add a new task
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("+"), (btn) -> {
            MinecraftClient.getInstance().setScreen( // change the screen to the TaskItAddScreen
                    new TaskItAddScreen(Text.of("TaskItAdd"), this)
            );
        }).dimensions(((width+guiWidth)/2)+1, ((height+guiHeight)/2)-29, 20, 20).build();

        // Adding the button to go to the next page of tasks
        ButtonWidget btnNext = ButtonWidget.builder(Text.of(">"), (btn) -> {
            pageNumber ++;
            clearChildren(); // Clear the current children (buttons) from the screen
            init(); // Reinitialize the screen to update the content of the screen
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+5, 20, 20).build();
        btnNext.active = false;

        // Adding the button to go to the previous page of tasks
        ButtonWidget btnPrev = ButtonWidget.builder(Text.of("<"), (btn) -> {
            pageNumber --;
            clearChildren(); // Clear the current children (buttons) from the screen
            init(); // Reinitialize the screen to update the content of the screen
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+25, 20, 20).build();
        btnPrev.active = false;

        List<Task> tasks = ModClientConfig.getTasks();

        // Check if there are any tasks to display and if so, add them to the screen
        if (!tasks.isEmpty()) {
            int pos = 20;

            // Check if there are more tasks to display on the next page, if so,
            // enable the next button
            if ((pageNumber + 1) * NUMBER_OF_TASKS_PER_PAGE < tasks.size()) {
                btnNext.active = true;
            }
            // Check if there are tasks on the previous page, if so,
            // enable the previous button
            if (pageNumber > 0) {
                btnPrev.active = true;
            }

            // Calculate the starting index for the tasks to display on the current page
            int startIndex = pageNumber * NUMBER_OF_TASKS_PER_PAGE;

            // Loop through the tasks and add them to the screen from the starting index
            for (int i = 0; i < NUMBER_OF_TASKS_PER_PAGE && startIndex + i < tasks.size(); i++) {
                Task task = tasks.get(startIndex + i);
                TaskButtonWidget taskBtn = new TaskButtonWidget(
                        task.getTaskName(),
                        (width-guiWidth)/2+10,
                        (height-guiHeight)/2+pos,
                        textRenderer,
                        TaskItMainScreen.this,
                        task);

                // Increment the position for the next task button
                pos+=35;
                // Add the task button to the screen
                this.addDrawableChild(taskBtn);
            }
        }

        // Add all the widget to the drawing list
        this.addDrawableChild(buttonWidget);
        this.addDrawableChild(btnNext);
        this.addDrawableChild(btnPrev);
    }

    /**
     * This method is called when the screen is opened.
     *
     * @return {@code true} if the screen should pause the game, {@code false} otherwise.
     */
    @Override
    public boolean shouldPause() {
        // We don't want to pause the game when the screen is opened
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render the background
        context.drawTexture(
                BACKGROUND_TEXTURE,
                (width-guiWidth)/2,
                (height-guiHeight)/2,
                0,0,
                guiWidth,guiHeight);

        // Render the background texture and all the buttons added to the drawing list
        // Rendering after the background to ensure the buttons are on top
        super.render(context, mouseX, mouseY, delta);

        // Render the title
        context.drawText(
                this.textRenderer,
                "TaskIt",
                width/2 - 16,
                39,
                0xFFFFFFFF,
                true);

        // Render the subtitle
        if (this.title.equals(Text.of("TaskIt"))) {
            context.drawText(
                    this.textRenderer,
                    "Tasks:",
                    (width / 2) - 16,
                    59,
                    0xFFFFFFFF,
                    true);
        }
    }

    @Override
    public void close() {
        // Close the screen and return to the parent screen
        this.client.setScreen(this.parent);
    }
}
