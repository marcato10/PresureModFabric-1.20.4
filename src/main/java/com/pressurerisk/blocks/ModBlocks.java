package com.pressurerisk.blocks;

import com.pressurerisk.PressureRisk;
import com.pressurerisk.blocks.entity.TotemFractalBlockEntity;
import com.pressurerisk.utils.ModConstants;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static com.pressurerisk.blocks.TotemFractalBeacon.registerBlock;

public class ModBlocks {
    public static final Block TOTEM_FRACTAL_BEACON = registerBlock("totem_beacon_block",new TotemFractalBeacon(FabricBlockSettings.copyOf(Blocks.GLOWSTONE).hardness(1.5f).sounds(BlockSoundGroup.AMETHYST_BLOCK).mapColor(MapColor.LIGHT_BLUE)));

    private static void addBlockToTab(FabricItemGroupEntries fabricItemGroupEntries){
        fabricItemGroupEntries.add(TOTEM_FRACTAL_BEACON);
    }

    public static void blocksToGame(){
        PressureRisk.LOGGER.info("Registering Blocks "+ ModConstants.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModBlocks::addBlockToTab);
    }

    public static final BlockEntityType<TotemFractalBlockEntity> TOTEM_FRACTAL_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,new Identifier(ModConstants.MOD_ID,"totem_fractal_block_entity"),
            FabricBlockEntityTypeBuilder.create(TotemFractalBlockEntity::new,TOTEM_FRACTAL_BEACON).build()
    );

}
