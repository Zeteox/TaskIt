package ovh.zeteox.taskit.mixin;

import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;
import java.util.Objects;

/**
 * This mixin class is responsible for tracking the usage of items on blocks.
 * It injects code into the useOnBlock method of ItemStack to update task counts.
 */
@Mixin(ItemStack.class)
public class ItemMixin {

    /**
     * This method is called when an item is used on a block.
     *
     * @param context The context of the item usage
     * @param cir     The callback information
     */
    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        // Check if the world is client-side and the action result is SUCCESS
        if (world.isClient && cir.getReturnValue() == ActionResult.SUCCESS) {
            ItemStack itemStack = (ItemStack) (Object) this;
            Item item = itemStack.getItem();

            // Check if the item is an instance of BlockItem, BoneMealItem, or PlaceableOnWaterItem
            if (item instanceof BlockItem || item instanceof BoneMealItem || item instanceof PlaceableOnWaterItem) {
                List<Task> tasks = ModClientConfig.getTasks();

                for (Task task : tasks) {
                    // For each task, check if the task is of type PLANTING
                    // and if the item matches the task item
                    if (task.getTaskType() == TaskTypes.PLANTING &&
                        Objects.equals(task.getTaskItem(), item.getName().toString())) {
                        task.addNumber(1);
                    }
                }

                ModClientConfig.updateTasks(tasks);
            }
        }
    }
}