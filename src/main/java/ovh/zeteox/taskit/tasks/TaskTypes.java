package ovh.zeteox.taskit.tasks;

import net.minecraft.text.Text;

public enum TaskTypes {
    MINING,
    PLANTING,
    EATING,
    CUSTOM;

    public static Text getName(TaskTypes type) {
        return Text.of(type.name());
    }
}
