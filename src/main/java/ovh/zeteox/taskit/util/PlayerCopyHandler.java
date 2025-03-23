package ovh.zeteox.taskit.util;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCopyHandler implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if (oldPlayer != null && newPlayer != null) {
            ((IEntityDataSaver) newPlayer).getPersistentData().put("Tasks",
                    ((IEntityDataSaver) oldPlayer).getPersistentData().getList("Tasks", NbtElement.COMPOUND_TYPE));
        }
    }
}