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

public class TaskItItemSelectionScreen extends TaskItMainScreen {
    private final List<Item> availableItems = new ArrayList<>();
    private int page = 0;
    private static final int ITEMS_PER_PAGE = 40;
    private static final int GRID_WIDTH = 5;
    private Item selectedItem;
    private final Runnable onItemSelected;

    public TaskItItemSelectionScreen(Text title, Screen parent, Runnable onItemSelected) {
        super(title, parent, 0);
        this.onItemSelected = onItemSelected;
        populateItems();
    }

    private void populateItems() {
        Registries.BLOCK.forEach(block -> {
            Item item = block.asItem();
            if (!item.equals(Items.AIR) && !availableItems.contains(item)) {
                availableItems.add(block.asItem());
            }
        });
    }

    @Override
    protected void init() {
        ButtonWidget btnPrev = ButtonWidget.builder(Text.of("<"), button -> {
            if (page > 0) page--;
            init();
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+25, 20, 20).build();
        btnPrev.active = false;

        ButtonWidget btnNext = ButtonWidget.builder(Text.of(">"), button -> {
            if ((page + 1) * ITEMS_PER_PAGE < availableItems.size()) page++;
            init();
        }).dimensions(((width+guiWidth)/2)+1, ((height-guiHeight)/2)+5, 20, 20).build();
        btnNext.active = false;

        this.addDrawableChild(ButtonWidget.builder(Text.of("Confirm"), button -> {
            if (selectedItem != null) {
                onItemSelected.run();
                client.setScreen(parent);
            }
        }).dimensions(width / 2 - 40, this.guiHeight+34, 80, 20).build());

        if (availableItems.size()-(this.page*ITEMS_PER_PAGE) > ITEMS_PER_PAGE) {
            btnNext.active = true;
        }

        if (this.page > 0) {
            btnPrev.active = true;
        }

        this.addDrawableChild(btnPrev);
        this.addDrawableChild(btnNext);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int startX = (width - (GRID_WIDTH * 18)) / 2;
        int startY = height / 4 - 8;

        int startIndex = page * ITEMS_PER_PAGE;
        for (int i = 0; i < ITEMS_PER_PAGE && startIndex + i < availableItems.size(); i++) {
            int x = startX + (i % GRID_WIDTH) * 18;
            int y = startY + (i / GRID_WIDTH) * 18;

            Item item = availableItems.get(startIndex + i);
            ItemStack stack = new ItemStack(item);
            context.drawItem(stack, x, y);
            if (Objects.equals(this.selectedItem, item)) {
                context.drawVerticalLine(x-1, y-1, y + 16, 0xFFFFFFFF);
                context.drawVerticalLine(x+16, y-1, y + 16, 0xFFFFFFFF);
                context.drawHorizontalLine(x-1, x+16, y-1, 0xFFFFFFFF);
                context.drawHorizontalLine(x-1, x+16, y+16, 0xFFFFFFFF);
            }

            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                context.drawTooltip(textRenderer, item.getName(), mouseX, mouseY);
            }
        }
    }

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

    public Item getSelectedItem() {
        return selectedItem;
    }
}