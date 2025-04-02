package ovh.zeteox.taskit.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A screen for selecting an item from a list of available items.
 * The items are displayed in a grid format, and the user can navigate through pages of items.
 * The class extends {@link TaskItMainScreen}
 *
 * @see TaskItMainScreen
 */
public class TaskItItemSelectionScreen extends TaskItMainScreen {
    private final List<Item> availableItems = new ArrayList<>();
    private int page = 0;
    private static final int GRID_WIDTH = 5;
    private static final int ITEMS_PER_PAGE = 40;
    private Item selectedItem;
    private final Runnable onItemSelected;

    /**
     * Constructor for the TaskItItemSelectionScreen.
     *
     * @param title        The title of the screen.
     * @param parent       The parent screen
     * @param onItemSelected A runnable to execute when an item is selected.
     */
    public TaskItItemSelectionScreen(Text title, Screen parent, Runnable onItemSelected) {
        super(title, parent);
        this.onItemSelected = onItemSelected;
        populateItems();
    }

    /**
     * Populates the available items list with all items from the Minecraft ITEM registry.
     * This method is called in the constructor to ensure the list is populated when the screen is created.
     */
    private void populateItems() {
        Registries.ITEM.forEach(item -> {
            if (!item.equals(Items.AIR) && !availableItems.contains(item)) {
                availableItems.add(item);
            }
        });
    }

    @Override
    protected void init() {

        // Adding a button to go back to the previous screen
        ButtonWidget btnPrev = ButtonWidget.builder(Text.of("<"), button -> {
            page--; // Decrease the page number
            init(); // Reinitialize the screen to update the buttons state
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+25, 20, 20).build();
        btnPrev.active = false; // Disable the button

        // Adding a button to go to the next page
        ButtonWidget btnNext = ButtonWidget.builder(Text.of(">"), button -> {
            page++; // Increase the page number
            init(); // Reinitialize the screen to update the buttons state
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+5, 20, 20).build();
        btnNext.active = false; // Disable the button

        // Adding a button to confirm the selected item
        ButtonWidget confirmBtn = ButtonWidget.builder(Text.of("Confirm"), button -> {
            if (selectedItem != null) {
                onItemSelected.run();
                client.setScreen(parent);
            }
        }).dimensions(width / 2 - 40, this.guiHeight+34, 80, 20).build();

        // Set the button states based on the current page
        if ((page + 1) * ITEMS_PER_PAGE < availableItems.size()) { //if there are more items to show
            btnNext.active = true;
        }
        if (this.page > 0) { //if there are previous items to show
            btnPrev.active = true;
        }

        // Add all the widget to the drawing list
        this.addDrawableChild(btnPrev);
        this.addDrawableChild(btnNext);
        this.addDrawableChild(confirmBtn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render the background texture and all the buttons added to the drawing list
        super.render(context, mouseX, mouseY, delta);

        int startX = (width - (GRID_WIDTH * 18)) / 2;
        int startY = height / 4 - 8;
        int startIndex = page * ITEMS_PER_PAGE;

        // Draw the items in a grid format from the start index
        for (int i = 0; i < ITEMS_PER_PAGE && startIndex + i < availableItems.size(); i++) {
            int x = startX + (i % GRID_WIDTH) * 18;
            int y = startY + (i / GRID_WIDTH) * 18;

            Item item = availableItems.get(startIndex + i);
            ItemStack stack = new ItemStack(item);

            // Draw the item icon
            context.drawItem(stack, x, y);

            // Draw a border around the selected item
            if (Objects.equals(this.selectedItem, item)) {
                context.drawVerticalLine(x-1, y-1, y + 16, 0xFFFFFFFF); // ║
                context.drawVerticalLine(x+16, y-1, y + 16, 0xFFFFFFFF); // ║ ║
                context.drawHorizontalLine(x-1, x+16, y-1, 0xFFFFFFFF); // ╔═╗
                context.drawHorizontalLine(x-1, x+16, y+16, 0xFFFFFFFF); //╚═╝
            }

            // Draw the item name as a tooltip when hovered
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                context.drawTooltip(textRenderer, item.getName(), mouseX, mouseY);
            }
        }
    }

    /**
     * Handles mouse clicks on the screen.
     * If an item is clicked, it sets the selected item and reinitializes the screen.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param button The button that was clicked.
     * @return true if an item was clicked, false otherwise.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (width - (GRID_WIDTH * 18)) / 2;
        int startY = height / 4 - 8;

        int itemX = (int) ((mouseX - startX) / 18);
        int itemY = (int) ((mouseY - startY) / 18);

        if (itemX >= 0 && itemX < GRID_WIDTH && itemY >= 0 && itemY < 8) {
            int index = page * ITEMS_PER_PAGE + itemY * GRID_WIDTH + itemX;
            if (index < availableItems.size()) {
                selectedItem = availableItems.get(index);
                init();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Returns the selected item.
     *
     * @return The selected item.
     */
    public Item getSelectedItem() {
        return selectedItem;
    }
}