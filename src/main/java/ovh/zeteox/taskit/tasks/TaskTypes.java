package ovh.zeteox.taskit.tasks;

import net.minecraft.text.Text;

/**
 * This enum represents the different types of tasks in the TaskIt mod.
 * It includes types such as BREAKING, PLANTING, EATING, and CUSTOM.
 */
public enum TaskTypes {
    BREAKING,
    PLANTING,
    EATING,
    CUSTOM;

    public static Text getName(TaskTypes type) {
        return Text.of(type.name());
    }
}
