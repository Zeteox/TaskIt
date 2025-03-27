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

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
        if (world.isClient) {
            List<Task> tasks = ModClientConfig.getTasks();
            for (Task task : tasks) {
                if (task.getTaskType() == TaskTypes.BREAKING) {
                    task.addNumber(1);
                }
                ModClientConfig.updateTasks(tasks);
            }
        }
    }

}
