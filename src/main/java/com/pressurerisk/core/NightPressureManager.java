package com.pressurerisk.core;

import com.pressurerisk.core.data.PlayerBonus;
import com.pressurerisk.core.data.TotemData;
import com.pressurerisk.utils.ModConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.Optional;
import java.util.UUID;

public class NightPressureManager extends PersistentState {
    private PlayerBonus playerBonus;
    private TotemData totemData;

    private ModConstants.NIGHT_STATE nightState;

    public NightPressureManager(){
        this.totemData = new TotemData(0,Optional.empty());
        this.playerBonus = new PlayerBonus();
        this.nightState = ModConstants.NIGHT_STATE.IDLE;
    }

    public NightPressureManager(TotemData totemData, PlayerBonus playerBonus, ModConstants.NIGHT_STATE nightState){
        this.totemData = totemData;
        this.playerBonus = playerBonus;
        this.nightState = nightState;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playerCompound = new NbtCompound();
        nbt.put("players_data",playerBonus.toNbt(playerCompound));

        if(this.totemData.blockPos().isPresent()){
            nbt.putIntArray("totem_pos",totemData.blockPosToIntArray(totemData.blockPos().get()));
        }
        nbt.putInt("pressure_level",totemData.pressureLevel());
        nbt.putString("night_state",this.nightState.toString());
        return nbt;
    }

    public static NightPressureManager createFromNbt(NbtCompound compound){
        Optional<BlockPos>blockPos = Optional.empty();

        if(compound.contains("totem_pos")){
            int[] positions = compound.getIntArray("totem_pos");
            blockPos = Optional.of(new BlockPos(positions[0],positions[1],positions[2]));
        }

        return new NightPressureManager(
                new TotemData(compound.getInt("pressure_level"),blockPos),
                PlayerBonus.createFromNbt(compound.getCompound("players_data")),
                compound.getString("night_state").toUpperCase().equals(ModConstants.NIGHT_STATE.IDLE.toString()) ? ModConstants.NIGHT_STATE.IDLE : ModConstants.NIGHT_STATE.RUNNING
        );
    }

    public void addPlayerScore(UUID uuid, int score){
        this.getPlayerBonus().getPlayersScoreExtra().addTo(uuid, score);
        this.markDirty();
    }

    public void resetNight(){
        this.setNightState(ModConstants.NIGHT_STATE.IDLE);
        this.setTotemData(new TotemData(0,Optional.empty()));
        this.getPlayerBonus().resetAllScores();
        this.markDirty();
    }

    public void resetPlayerScore(UUID uuid){
        this.getPlayerBonus().resetPlayerScore(uuid);
        this.markDirty();
    }

    public void claimPlayersScore(ServerWorld serverWorld,int multiplier){
        for(ServerPlayerEntity serverPlayer : serverWorld.getPlayers()){
            serverPlayer.addExperience(this.getPlayerBonus().getPlayerScoreByUUID(serverPlayer.getUuid()) * multiplier);
        }
        this.getPlayerBonus().resetAllScores();
        this.markDirty();
    }

    public ModConstants.NIGHT_STATE getNightState() {
        return nightState;
    }

    public PlayerBonus getPlayerBonus() {
        return playerBonus;
    }

    public TotemData getTotemData() {return totemData;}

    public void setTotemData(TotemData totemData) {
        this.totemData = totemData;
        this.markDirty();
    }

    public void setNightState(ModConstants.NIGHT_STATE nightState) {
        this.nightState = nightState;
        this.markDirty();
    }

    private static Type<NightPressureManager>type = new Type<>(
        NightPressureManager::new,
            NightPressureManager::createFromNbt,
            null
    );
    public static NightPressureManager getServerWorldState(ServerWorld serverWorld){
        return serverWorld.getPersistentStateManager().getOrCreate(type,ModConstants.MOD_ID);
    }

    public static int getPressureLevel(ServerWorld serverWorld){
        return getServerWorldState(serverWorld).getTotemData().pressureLevel();
    }

}
