package ovh.zeteox.taskit.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;

/**
 * This mixin class is responsible for tracking the eating of food items by the player.
 * It injects code into the eatFood method of PlayerEntity to update task counts.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * This method is called when the player eats food.
     *
     * @param world The world where the player is located
     * @param item  The item being eaten
     * @param cir   The callback information
     */
    @Inject(method = "eatFood", at = @At("HEAD"))
    public void eatFood(World world, ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) {
            List<Task> tasks = ModClientConfig.getTasks();

            for (Task task : tasks) {
                // For each task, check if the task is of type EATING
                // and if the item matches the task item
                if (task.getTaskType() == TaskTypes.EATING && task.getTaskItem().equals(item.getItem().getName().toString())) {
                    task.addNumber(1);
                }
            }

            ModClientConfig.updateTasks(tasks);
        }
    }
}
