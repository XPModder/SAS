package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.SlabsAndStairs;
import com.xpmodder.slabsandstairs.block.*;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.BlockTagTypes;
import com.xpmodder.slabsandstairs.utility.ResourceGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.xpmodder.slabsandstairs.init.ItemInit.ITEMS;
import static com.xpmodder.slabsandstairs.init.ItemInit.MY_ITEMS;

public class BlockInit {


    public static Set<Block> MY_BLOCKS = new LinkedHashSet<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);

    public static final RegistryObject<Block> previewQuarter = BLOCKS.register("preview_quarter", () -> new QuarterBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> previewSlab = BLOCKS.register("preview_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> previewStair = BLOCKS.register("preview_stair", () -> new StairBlock(BlockBehaviour.Properties.of(Material.STONE)));

    public static final RegistryObject<Block> combinedBlock = BLOCKS.register("combined_block", () -> new CombinedBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f).lightLevel((state) -> state.getValue(BlockStateProperties.LEVEL)).noOcclusion(), previewSlab.get().defaultBlockState()));


    public static void NewBlock(String RegistryName, Material material, float strength, int light, int power, BlockTagTypes tool){

        String RegistryPath = RegistryName.split(":")[1];
        String RegistryNameQuarter = RegistryPath + "_quarter_sas";
        String RegistryNameSlab = RegistryPath + "_slab_sas";
        String RegistryNameStair = RegistryPath + "_stair_sas";
        String RegistryNameFence = RegistryPath + "_fence_sas";
        String RegistryNameWall = RegistryPath + "_wall_sas";

        BLOCKS.register(RegistryNameQuarter, () -> {
            QuarterBlock quarter;
            if(material == Material.GLASS){
                quarter = new QuarterBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state)->light/4).noOcclusion());
            }
            else{
                quarter = new QuarterBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state)->light/4));
            }
            quarter.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameSlab, Reference.MODID + ":" + RegistryNameStair);
            quarter.setPower(power/4);
            MY_BLOCKS.add(quarter);
            Item newItem = new BlockItem(quarter, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameQuarter, () -> newItem);
            MY_ITEMS.add(newItem);
            if(tool != null) {
                ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameQuarter).toString(), tool);
            }
            return quarter;
        });
        BLOCKS.register(RegistryNameSlab, () -> {
            SlabBlock slab;
            if(material == Material.GLASS) {
                slab = new SlabBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> light / 2).noOcclusion());
            }
            else{
                slab = new SlabBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> light / 2));
            }
            slab.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameQuarter, Reference.MODID + ":" + RegistryNameStair);
            slab.setPower(power/2);
            MY_BLOCKS.add(slab);
            Item newItem = new BlockItem(slab, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameSlab, () -> newItem);
            MY_ITEMS.add(newItem);
            if(tool != null) {
                ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameSlab).toString(), tool);
            }
            return slab;
        });
        BLOCKS.register(RegistryNameStair, () -> {
            StairBlock stair;
            if(material == Material.GLASS) {
                stair = new StairBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> (light * 3) / 4).noOcclusion());
            }
            else{
                stair = new StairBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> (light * 3) / 4));
            }
            stair.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameQuarter, Reference.MODID + ":" + RegistryNameSlab);
            stair.setPower((power*3)/4);
            MY_BLOCKS.add(stair);
            Item newItem = new BlockItem(stair, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameStair, () -> newItem);
            MY_ITEMS.add(newItem);
            if(tool != null) {
                ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameStair).toString(), tool);
            }
            return stair;
        });
        BLOCKS.register(RegistryNameFence, () -> {
            FenceBlock fence;
            if(material == Material.GLASS) {
                fence = new FenceBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> (light * 3) / 4).noOcclusion());
            }
            else{
                fence = new FenceBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> (light * 3) / 4));
            }
            fence.setReferenceBlocks(RegistryName);
            fence.setPower((power*3)/4);
            MY_BLOCKS.add(fence);
            Item newItem = new BlockItem(fence, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameFence, () -> newItem);
            MY_ITEMS.add(newItem);
            ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameFence).toString(), BlockTagTypes.FENCE);
            if(tool != null) {
                ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameFence).toString(), tool);
            }
            return fence;
        });
        BLOCKS.register(RegistryNameWall, () -> {
            WallBlock wall;
            if(material == Material.GLASS) {
                wall = new WallBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> (light * 3) / 4).noOcclusion());
            }
            else{
                wall = new WallBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state) -> (light * 3) / 4));
            }
            wall.setReferenceBlocks(RegistryName);
            wall.setPower((power*3)/4);
            MY_BLOCKS.add(wall);
            Item newItem = new BlockItem(wall, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameWall, () -> newItem);
            MY_ITEMS.add(newItem);
            ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameWall).toString(), BlockTagTypes.WALL);
            if(tool != null) {
                ResourceGenerator.addToTag(new ResourceLocation(Reference.MODID, RegistryNameWall).toString(), tool);
            }
            return wall;
        });


    }


}
