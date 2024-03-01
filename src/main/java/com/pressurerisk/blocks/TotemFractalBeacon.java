package com.pressurerisk.blocks;

import com.mojang.serialization.MapCodec;
import com.pressurerisk.blocks.entity.TotemFractalBlockEntity;
import com.pressurerisk.core.NightPressureManager;
import com.pressurerisk.core.data.TotemData;
import com.pressurerisk.utils.ModConstants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class TotemFractalBeacon extends BlockWithEntity implements BlockEntityProvider {
    public static final IntProperty LEVEL_TOTEM = IntProperty.of("level",0,1);

    private int level = 0;

    public TotemFractalBeacon(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(LEVEL_TOTEM,this.level));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL_TOTEM);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, new Identifier(ModConstants.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    public static Block registerBlock(String name,Block block){
        registerBlockItem(name,block);
        return Registry.register(Registries.BLOCK,new Identifier(ModConstants.MOD_ID,name),block);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient()){
            NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState((ServerWorld) world);
            if(nightPressureManager.getTotemData().blockPos().isEmpty() && isPivotTotemValid(world,pos)){
                world.setBlockState(pos,state.with(LEVEL_TOTEM,this.level+1));
                nightPressureManager.setTotemData(new TotemData(this.level+2, pos.toImmutable()));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if(!world.isClient()){
            NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState((ServerWorld) world);
            if(nightPressureManager.getTotemData().blockPos().isPresent() && nightPressureManager.getTotemData().blockPos().get().equals(pos)){
                nightPressureManager.resetNight();
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.playSound(placer,pos,SoundEvents.ENTITY_BLAZE_AMBIENT,SoundCategory.BLOCKS,1f,1f);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TotemFractalBlockEntity(pos,state);
    }

    private boolean isPivotTotemValid(World world, BlockPos pivotPos) {
        long number = checkAreaOutline(world,pivotPos);
        return number == 24 && world.getDimension().bedWorks();
    }

    private long checkAreaOutline(World world, BlockPos pivotPos){
        return StreamSupport.stream(BlockPos.iterateInSquare(pivotPos, 3, Direction.WEST, Direction.SOUTH).spliterator(), false)
                .filter(blockPos -> world.getBlockState(blockPos).isIn(BlockTags.BASE_STONE_NETHER))
                .count();
    }

}
