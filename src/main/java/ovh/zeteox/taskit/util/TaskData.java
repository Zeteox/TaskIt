package ovh.zeteox.taskit.util;

import net.minecraft.nbt.*;
import ovh.zeteox.taskit.tasks.Task;

public class TaskData {

    public static void addTask(IEntityDataSaver player, Task task) {
        NbtCompound nbt = player.getPersistentData();
        NbtList tasks = nbt.getList("Tasks", NbtElement.COMPOUND_TYPE);
        NbtCompound nbtTask = new NbtCompound();
        nbtTask.put("name", NbtString.of(task.getTaskName()));
        nbtTask.put("type", NbtString.of(task.getTaskType().toString()));
        nbtTask.put("nbMax", NbtInt.of(task.getNumberOfTime()));
        nbtTask.put("nbAct", NbtInt.of(task.getActualNumber()));
        nbtTask.put("pinned", NbtInt.of(task.isPined() ? 1 : 0));
        nbtTask.put("completed", NbtInt.of(task.isCompleted() ? 1 : 0));

        tasks.add(nbtTask);

        System.out.println(tasks);
        nbt.put("Tasks", tasks);
    }
}
