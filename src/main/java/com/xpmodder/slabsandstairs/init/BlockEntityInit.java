package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.block.CombinedBlockEntity;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Reference.MODID);
    public static final RegistryObject<BlockEntityType<CombinedBlockEntity>> COMBINED_BLOCK =
            BLOCK_ENTITIES.register("combined_block", () -> BlockEntityType.Builder.of(CombinedBlockEntity::new, BlockInit.combinedBlock.get()).build(null));

}
