package com.xpmodder.slabsandstairs.reference;

import net.minecraftforge.fml.loading.FMLPaths;
import org.lwjgl.system.CallbackI;

import java.io.File;

public class Reference {

    public static final String MODID = "slabsandstairs";
    public static final String NAME = "SAS - The Slabs and Stairs Mod";
    public static final String VERSION = "0.8.0b";
    public static final File RESOURCE_FOLDER = new File(FMLPaths.GAMEDIR.get().toFile(), MODID + "/modResources");
    public static final File DATAPACK_FOLDER = new File(FMLPaths.GAMEDIR.get().toFile(), MODID + "/modDatapack");
    public static final File BLOCK_LIST = new File(FMLPaths.GAMEDIR.get().toFile(), MODID + "/blocks.csv");
    public static final File BLOCK_TAGS_FOLDER = new File(DATAPACK_FOLDER, "data/minecraft/tags/blocks");
    public static final File PICK_MINEABLE = new File(BLOCK_TAGS_FOLDER, "mineable/pickaxe.json");
    public static final File AXE_MINEABLE = new File(BLOCK_TAGS_FOLDER, "mineable/axe.json");
    public static final File SHOVEL_MINEABLE = new File(BLOCK_TAGS_FOLDER, "mineable/shovel.json");
    public static final File HOE_MINEABLE = new File(BLOCK_TAGS_FOLDER, "mineable/hoe.json");

    public static final String KEY_CATEGORY = "key.categories." + MODID;

}
