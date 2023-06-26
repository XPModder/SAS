package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.SlabsAndStairs;
import com.xpmodder.slabsandstairs.block.CombinedBlock;
import com.xpmodder.slabsandstairs.block.QuarterBlock;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.block.StairBlock;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public static final RegistryObject<Block> combinedBlock = BLOCKS.register("combined_block", () -> new CombinedBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f), previewSlab.get().defaultBlockState()));


    public static void NewBlock(String RegistryName, Material material, float strength, int light, int power){

        String RegistryPath = RegistryName.split(":")[1];
        String RegistryNameQuarter = RegistryPath + "_quarter_sas";
        String RegistryNameSlab = RegistryPath + "_slab_sas";
        String RegistryNameStair = RegistryPath + "_stair_sas";

        BLOCKS.register(RegistryNameQuarter, () -> {
            QuarterBlock quarter = new QuarterBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state)->light/4));
            quarter.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameSlab, Reference.MODID + ":" + RegistryNameStair);
            quarter.setPower(power/4);
            MY_BLOCKS.add(quarter);
            Item newItem = new BlockItem(quarter, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameQuarter, () -> newItem);
            MY_ITEMS.add(newItem);
            return quarter;
        });
        BLOCKS.register(RegistryNameSlab, () -> {
            SlabBlock slab = new SlabBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state)->light/2));
            slab.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameQuarter, Reference.MODID + ":" + RegistryNameStair);
            slab.setPower(power/2);
            MY_BLOCKS.add(slab);
            Item newItem = new BlockItem(slab, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameSlab, () -> newItem);
            MY_ITEMS.add(newItem);
            return slab;
        });
        BLOCKS.register(RegistryNameStair, () -> {
            StairBlock stair = new StairBlock(BlockBehaviour.Properties.of(material).strength(strength).lightLevel((state)->(light*3)/4));
            stair.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameQuarter, Reference.MODID + ":" + RegistryNameSlab);
            stair.setPower((power*3)/4);
            MY_BLOCKS.add(stair);
            Item newItem = new BlockItem(stair, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS));
            ITEMS.register(RegistryNameStair, () -> newItem);
            MY_ITEMS.add(newItem);
            return stair;
        });

    }


}
