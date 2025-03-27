package ovh.zeteox.taskit.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {

    @Inject(method = "breed", at = @At("HEAD"))
    private void onBreed(CallbackInfo ci) {
        AnimalEntity entity = (AnimalEntity) (Object) this;
        if (entity.getWorld().isClient) {
            List<Task> tasks = ModClientConfig.getTasks();
            for (Task task : tasks) {
                if (task.getTaskType() == TaskTypes.BREEDING) {
                    task.addNumber(1);
                }
            }
            ModClientConfig.updateTasks(tasks);
        }
    }
}