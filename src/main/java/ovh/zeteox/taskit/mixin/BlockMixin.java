package ovh.zeteox.taskit.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;
import java.util.Objects;

/**
 * This mixin class is responsible for tracking the breaking of blocks.
 * It injects code into the onBreak method of Block to update task counts.
 */
@Mixin(Block.class)
public class BlockMixin {

    /**
     * This method is called when a block is broken.
     *
     * @param world  The world where the block is located
     * @param pos    The position of the block
     * @param state  The state of the block
     * @param player The player who broke the block
     * @param info   The callback information
     */
    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
        if (world.isClient) {
            List<Task> tasks = ModClientConfig.getTasks();

            // For each task, check if the task is of type BREAKING
            // and if the block matches the task item
            for (Task task : tasks) {
                if (task.getTaskType() == TaskTypes.BREAKING && Objects.equals(task.getTaskItem(), state.getBlock().asItem().getName().toString())) {
                    task.addNumber(1);
                }
            }

            ModClientConfig.updateTasks(tasks);
        }
    }

}
