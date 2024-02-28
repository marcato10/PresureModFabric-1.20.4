package com.pressurerisk.core.data;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class PlayerBonus {
    private final Object2IntOpenHashMap<UUID> playersScoreExtra;

    public PlayerBonus(){
        this.playersScoreExtra = new Object2IntOpenHashMap<>();
    }

    public int getPlayerScoreByUUID(UUID uuid){
        return this.playersScoreExtra.containsKey(uuid) ? this.playersScoreExtra.getInt(uuid) : 0;
    }



    public Object2IntOpenHashMap<UUID> getPlayersScoreExtra() {
        return playersScoreExtra;
    }

    public void resetAllScores(){
        this.playersScoreExtra.clear();
    }

    public void resetPlayerScore(UUID uuid){
        this.playersScoreExtra.put(uuid,0);
    }

    public NbtCompound toNbt(NbtCompound nbt){
        this.playersScoreExtra.forEach((uuid, integer) -> nbt.putInt(uuid.toString(),integer));
        return nbt;
    }

    public PlayerBonus createFromNbt(NbtCompound nbtCompound){
        PlayerBonus playerBonus = new PlayerBonus();
        nbtCompound.getKeys().forEach(key->{
            playerBonus.playersScoreExtra.put(UUID.fromString(key),nbtCompound.getInt(key));
        });
        return playerBonus;
    }
}
