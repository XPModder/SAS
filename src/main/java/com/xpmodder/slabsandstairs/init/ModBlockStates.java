package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.client.rendering.CombinedBlockModelLoader;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStates extends BlockStateProvider {

    public ModBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerCombinedBlock();
    }


    private void registerCombinedBlock(){

        BlockModelBuilder builder = models().getBuilder(BlockInit.combinedBlock.getId().getPath())
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((blockModelBuilder, helper) -> new CustomLoaderBuilder<BlockModelBuilder>(CombinedBlockModelLoader.COMBINED_BLOCK_LOADER, blockModelBuilder, helper){}).end();
        directionalBlock(BlockInit.combinedBlock.get(), builder);

    }


}
