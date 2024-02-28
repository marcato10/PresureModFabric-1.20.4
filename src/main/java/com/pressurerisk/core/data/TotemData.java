package com.pressurerisk.core.data;

import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public record TotemData(int pressureLevel, Optional<BlockPos> blockPos) {
    public TotemData(int pressureLevel,BlockPos blockPos){
        this(pressureLevel,Optional.ofNullable(blockPos));
    }
    public int[] blockPosToIntArray(BlockPos pos){
        return new int[]{pos.getX(),pos.getY(),pos.getZ()};
    }
}
