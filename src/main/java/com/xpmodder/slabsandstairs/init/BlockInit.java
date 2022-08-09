package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.block.QuarterBlock;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.block.StairBlock;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.LinkedHashSet;
import java.util.Set;

public class BlockInit {


    public static Set<Block> MY_BLOCKS = new LinkedHashSet<>();


    public BlockInit(){

        NewBlock("minecraft:oak_planks", Material.WOOD);
        NewBlock("minecraft:oak_log", Material.WOOD);
        NewBlock("minecraft:blue_concrete", Material.ROCK);
        NewBlock("minecraft:red_concrete", Material.ROCK);

    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){

        IForgeRegistry<Block> registry = event.getRegistry();
        for(Block block : MY_BLOCKS){
            registry.register(block);
        }

    }

    protected void NewBlock(String RegistryName, Material material){

        String RegistryPath = RegistryName.split(":")[1];

        Block quarter = new QuarterBlock(AbstractBlock.Properties.create(material)).setRegistryName(RegistryPath + "_quarter_sas");
        Block slab = new SlabBlock(AbstractBlock.Properties.create(material)).setRegistryName(RegistryPath + "_slab_sas");
        Block stair =  new StairBlock(AbstractBlock.Properties.create(material)).setRegistryName(RegistryPath + "_stair_sas");

        ((QuarterBlock)quarter).setReferenceBlocks(RegistryName, slab, stair);
        ((SlabBlock)slab).setReferenceBlocks(RegistryName, quarter, stair);
        ((StairBlock)stair).setReferenceBlocks(RegistryName, quarter, slab);

        MY_BLOCKS.add(quarter);
        MY_BLOCKS.add(slab);
        MY_BLOCKS.add(stair);

    }


}
