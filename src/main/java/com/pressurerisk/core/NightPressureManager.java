package com.pressurerisk.core;

import com.pressurerisk.core.data.PlayerBonus;
import com.pressurerisk.core.data.TotemData;
import com.pressurerisk.utils.ModConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

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
        NightPressureManager nightPressureManager = new NightPressureManager();
        nightPressureManager.playerBonus = nightPressureManager.playerBonus.createFromNbt(compound.getCompound("players_data"));
        Optional<BlockPos>blockPos = Optional.empty();

        if(compound.contains("totem_pos")){
            int[] positions = compound.getIntArray("totem_pos");
            blockPos = Optional.of(new BlockPos(positions[0],positions[1],positions[2]));
        }
        nightPressureManager.totemData = new TotemData(compound.getInt("pressure_level"),blockPos);
        nightPressureManager.nightState = compound.getString("night_state").toUpperCase().equals(ModConstants.NIGHT_STATE.IDLE.toString()) ? ModConstants.NIGHT_STATE.IDLE : ModConstants.NIGHT_STATE.RUNNING;
        return nightPressureManager;
    }
    


    public ModConstants.NIGHT_STATE getNightState() {
        return nightState;
    }

    public PlayerBonus getPlayerBonus() {
        return playerBonus;
    }

    public void setPlayerBonus(PlayerBonus playerBonus) {
        this.playerBonus = playerBonus;
    }

    public TotemData getTotemData() {return totemData;}

    public void setTotemData(TotemData totemData) {
        this.totemData = totemData;
    }

    public void setNightState(ModConstants.NIGHT_STATE nightState) {
        this.nightState = nightState;
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
        return serverWorld.getPersistentStateManager().getOrCreate(type,ModConstants.MOD_ID).getTotemData().pressureLevel();
    }

}
