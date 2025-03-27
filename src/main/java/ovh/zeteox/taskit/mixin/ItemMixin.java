package ovh.zeteox.taskit.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"))
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;
        if (context.getWorld().isClient) {
            if (itemStack.getItem() == Items.WHEAT_SEEDS
                    || itemStack.getItem() == Items.POTATO
                    || itemStack.getItem() == Items.CARROT
                    || itemStack.getItem() == Items.BEETROOT_SEEDS
                    || itemStack.getItem() == Items.PUMPKIN_SEEDS
                    || itemStack.getItem() == Items.MELON_SEEDS
                    || itemStack.getItem() == Items.SUGAR_CANE
                    || itemStack.getItem() == Items.BAMBOO
                    || itemStack.getItem() == Items.CACTUS
                    || itemStack.getItem() == Items.KELP
                    || itemStack.getItem() == Items.NETHER_WART) {
                List<Task> tasks = ModClientConfig.getTasks();
                for (Task task : tasks) {
                    if (task.getTaskType() == TaskTypes.PLANTING) {
                        task.addNumber(1);
                    }
                    ModClientConfig.updateTasks(tasks);
                }
            }
        }
    }
}