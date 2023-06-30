package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class ItemInit {

    public static Set<Item> MY_ITEMS = new LinkedHashSet<>();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<Item> combinedBlock = ITEMS.register("combined_block", () -> new BlockItem(BlockInit.combinedBlock.get(), new Item.Properties()));


}
