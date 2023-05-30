package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.block.QuarterBlock;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.block.StairBlock;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class BlockInit {


    public static Set<Block> MY_BLOCKS = new LinkedHashSet<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);

    public static final RegistryObject<Block> previewQuarter = BLOCKS.register("preview_quarter", () -> new QuarterBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> previewSlab = BLOCKS.register("preview_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> previewStair = BLOCKS.register("preview_stair", () -> new StairBlock(BlockBehaviour.Properties.of(Material.STONE)));

    public static void NewBlock(String RegistryName, Material material){

        String RegistryPath = RegistryName.split(":")[1];
        String RegistryNameQuarter = RegistryPath + "_quarter_sas";
        String RegistryNameSlab = RegistryPath + "_slab_sas";
        String RegistryNameStair = RegistryPath + "_stair_sas";

        BLOCKS.register(RegistryNameQuarter, () -> {
            QuarterBlock quarter = new QuarterBlock(BlockBehaviour.Properties.of(material));
            quarter.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameSlab, Reference.MODID + ":" + RegistryNameStair);
            MY_BLOCKS.add(quarter);
            return quarter;
        });
        BLOCKS.register(RegistryNameSlab, () -> {
            SlabBlock slab = new SlabBlock(BlockBehaviour.Properties.of(material));
            slab.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameQuarter, Reference.MODID + ":" + RegistryNameStair);
            MY_BLOCKS.add(slab);
            return slab;
        });
        BLOCKS.register(RegistryNameStair, () -> {
            StairBlock stair = new StairBlock(BlockBehaviour.Properties.of(material));
            stair.setReferenceBlocks(RegistryName, Reference.MODID + ":" + RegistryNameQuarter, Reference.MODID + ":" + RegistryNameSlab);
            MY_BLOCKS.add(stair);
            return stair;
        });

    }


}
