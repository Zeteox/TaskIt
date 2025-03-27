package ovh.zeteox.taskit.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.zeteox.taskit.TaskIt;
import ovh.zeteox.taskit.config.ModClientConfig;
import ovh.zeteox.taskit.tasks.Task;
import ovh.zeteox.taskit.tasks.TaskTypes;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract @Nullable ItemEntity dropItem(ItemStack stack, boolean retainOwnership);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "eatFood", at = @At("HEAD"))
    public void eatFood(World world, ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) {
            List<Task> tasks = ModClientConfig.getTasks();
            for (Task task : tasks) {
                if (task.getTaskType() == TaskTypes.EATING) {
                    task.addNumber(1);
                }
                ModClientConfig.updateTasks(tasks);
            }
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    private void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) {
            List<Task> tasks = ModClientConfig.getTasks();
            for (Task task : tasks) {
                if (task.getTaskType() == TaskTypes.DROPPING) {
                    task.addNumber(1);
                    TaskIt.LOGGER.info("Dropped item: {}", stack.getName().getString());
                }
                ModClientConfig.updateTasks(tasks);
            }
        }
    }

    @Inject(method = "interact", at = @At("HEAD"))
    private void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (this.getWorld().isClient) {
            ItemStack itemStack = this.getStackInHand(hand);
            if (entity instanceof AnimalEntity && ((AnimalEntity) entity).isBreedingItem(itemStack) && ((AnimalEntity) entity).canEat()) {
                List<Task> tasks = ModClientConfig.getTasks();
                for (Task task : tasks) {
                    if (task.getTaskType() == TaskTypes.FEEDING) {
                        task.addNumber(1);
                    }
                }
                ModClientConfig.updateTasks(tasks);
            }
        }
    }
}
