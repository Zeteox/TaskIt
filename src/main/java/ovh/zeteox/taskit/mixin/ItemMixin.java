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

@Mixin(ItemStack.class)
public class ItemMixin {

    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        if (world.isClient && cir.getReturnValue() == ActionResult.SUCCESS) {
            ItemStack itemStack = (ItemStack) (Object) this;
            Item item = itemStack.getItem();

            if (item instanceof AliasedBlockItem || item instanceof BlockItem ||
                item instanceof BoneMealItem || item instanceof PlaceableOnWaterItem) {
                List<Task> tasks = ModClientConfig.getTasks();
                for (Task task : tasks) {
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