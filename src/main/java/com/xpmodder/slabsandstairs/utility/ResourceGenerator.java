package com.xpmodder.slabsandstairs.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xpmodder.slabsandstairs.SlabsAndStairs;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ResourceGenerator {

    //Utility method that checks if the given file exists and if not attempts to create it.
    //Returns true when the file exists or has been created and is writable
    public static boolean makeFile(File fileToMake){

        try {
            if (!fileToMake.exists()) {                                 //Check if the file already exists, if not
                if (!fileToMake.getParentFile().exists()) {             //Check if the directory already exists, if not
                    if (!fileToMake.getParentFile().mkdirs()) {         //Try to create the directory
                        LogHelper.error("Could not create folder at " + fileToMake.getParentFile());
                        return false;
                    }
                }                                                       //When the directory exists, but the file does not
                if (!fileToMake.createNewFile()) {                      //Try to create the file
                    LogHelper.error("Could not create file at " + fileToMake);
                    return false;
                }
            }
            if (!fileToMake.canWrite()) {                               //Then try to write to the file
                LogHelper.error("Can not write to file at " + fileToMake);
            }
        }
        catch (Exception ex){
            LogHelper.error("Could not create or write to file at " + fileToMake);
            return false;
        }

        return true;

    }


    //Utility method that checks if a directory exists and if not attempts to create it.
    //Return true if the directory exists or has been created successfully.
    public static boolean makeDir(@NotNull File dirToMake){
        if(!dirToMake.exists()){
            if(!dirToMake.mkdirs()){
                LogHelper.error("Could not create folder at " + dirToMake);
                return true;
            }
        }
        return false;
    }

    //Gets the english name for a given block
    public static String getNameForBlock(Block block){

        String output = "";

        try {

            String namespace = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace();

            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(namespace, "lang/en_us.json")).get().open();

            JsonObject object = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);

            output = object.get(block.getDescriptionId()).getAsString();

        }
        catch (Exception ex){
            LogHelper.error("Exception trying to get name for block " + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)) + "!");
        }

        return output;

    }

    //Gets the path for the texture file for the given block and the given texture (aka "top" or "bottom" or "side")
    //Tries to get the requested texture, otherwise gets "all"
    public static String getTextureForBlock(Block block, String texture){

        String output = "";

        try{

            String namespace = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace();

            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(namespace, "models/block/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + ".json")).get().open();

            JsonObject object = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);

            if(object.getAsJsonObject("textures").has(texture)){
                output = object.getAsJsonObject("textures").get(texture).getAsString();
            }
            else if(object.getAsJsonObject("textures").has("all")){
                output = object.getAsJsonObject("textures").get("all").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("end")){
                output = object.getAsJsonObject("textures").get("end").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("pattern")){
                output = object.getAsJsonObject("textures").get("pattern").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("up") && texture.contains("top")){
                output = object.getAsJsonObject("textures").get("up").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("down") && texture.contains("bottom")){
                output = object.getAsJsonObject("textures").get("down").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("north") && texture.contains("side")){
                output = object.getAsJsonObject("textures").get("north").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("east") && texture.contains("side")){
                output = object.getAsJsonObject("textures").get("east").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("south") && texture.contains("side")){
                output = object.getAsJsonObject("textures").get("south").getAsString();
            }
            else if(object.getAsJsonObject("textures").has("west") && texture.contains("side")){
                output = object.getAsJsonObject("textures").get("west").getAsString();
            }

        }
        catch(Exception ex){
            LogHelper.error("Exception trying to get texture for block " + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)) + "!");
        }

        return output;

    }

    //Copies the input file to the output file
    public static void copyFile(InputStream input, OutputStream output){

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(new BufferedOutputStream(output));

            String line;
            while((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
            out.close();
        }
        catch (IOException ex){
            LogHelper.error("Error copying file!");
        }

    }


    //Copies all data from input to output whilst replacing target with replacement.
    public static void copyFileAndReplace(InputStream input, OutputStream output, String target, String replacement){

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(new BufferedOutputStream(output));

            String str;
            while ((str = in.readLine()) != null) {
                str = str.replace(target, replacement);
                out.println(str);
            }

            in.close();
            out.close();

        }
        catch (Exception ex){
            LogHelper.error("Exception while to copying data!");
            LogHelper.error(ex.getStackTrace());
        }

    }


    //Copies all data from input to output whilst replacing target with replacement. Accepts multiple targets and replacements
    public static void copyFileAndReplace(InputStream input, OutputStream output, String[] target, String[] replacement){

        if(target.length != replacement.length){
            LogHelper.error("Invalid Arguments! The target and replacement arrays need to be the same length");
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(new BufferedOutputStream(output));

            String str;
            while ((str = in.readLine()) != null) {

                for(int i = 0; i < target.length; i++){
                    str = str.replace(target[i], replacement[i]);
                }
                out.println(str);
            }

            in.close();
            out.close();

        }
        catch (Exception ex){
            LogHelper.error("Exception while to copying data!");
            LogHelper.error(ex.getStackTrace());
        }

    }

    public static class Mineable{
        boolean replace;
        String[] values;

        public String toString(){
            StringBuilder output = new StringBuilder("replace: " + replace + ", values: [");
            if(values == null){
                output.append("null");
            }
            else {
                for (String string : values) {
                    output.append(string).append(", ");
                }
            }
            output.append("]");
            return output.toString();
        }
    }

    public static void addToTag(String block, BlockTagTypes type){
        switch (type) {
            case PICKAXE -> addToTag(block, Reference.PICK_MINEABLE);
            case AXE -> addToTag(block, Reference.AXE_MINEABLE);
            case SHOVEL -> addToTag(block, Reference.SHOVEL_MINEABLE);
            case HOE -> addToTag(block, Reference.HOE_MINEABLE);
        }
    }

    public static void addToTag(String block, File tagFile){

        if(!tagFile.exists()){
            makeFile(tagFile);
        }
        try {
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(tagFile));
            Mineable json = gson.fromJson(reader, Mineable.class);
            reader.close();

            boolean found = false;
            if(json == null){
                json = new Mineable();
                json.replace = false;
            }

            if (json.values != null) {
                for (String value : json.values) {
                    if (Objects.equals(value, block)) {
                        found = true;
                        break;
                    }
                }
            }

            if(!found){
                List<String> vals = new ArrayList<>();
                if(json.values != null){
                    vals.addAll(List.of(json.values));
                }
                vals.add(block);
                json.values = vals.toArray(new String[0]);
                PrintWriter writer = new PrintWriter(tagFile);
                writer.write(gson.toJson(json));
                writer.close();

            }
        }
        catch (Exception ex){
            LogHelper.error("Exception trying to add block to tag file!");
            LogHelper.error(ex.fillInStackTrace());
        }
    }
    
    public static InputStream getResourceStream(String resource){
        return SlabsAndStairs.class.getResourceAsStream("/assets/" + Reference.MODID + resource);
    }


    //Generates the resource files like blockstates, block and item models as well as display text
    public static boolean generate(){

        File blockstates = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/blockstates");
        File blockModels = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/models/block");
        File itemModels = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/models/item");
        File recipes = new File(Reference.DATAPACK_FOLDER, "data/" + Reference.MODID + "/recipes");
        File langUS = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/lang/en_us.json");
        File lootTables = new File(Reference.DATAPACK_FOLDER, "data/" + Reference.MODID + "/loot_tables/blocks");

        boolean hasGenerated = false;

        if(FMLEnvironment.dist.isClient()) {

            if (!Reference.RESOURCE_FOLDER.exists() || !Reference.RESOURCE_FOLDER.isDirectory()) {
                LogHelper.error("Invalid or inexistent mod resource folder!");
                LogHelper.error("Could not find folder: " + Reference.RESOURCE_FOLDER);
                return hasGenerated;
            }

            //Check if all the directories and files exist and create them if necessary.
            if (makeDir(blockstates)) {
                return hasGenerated;
            }

            if (makeDir(blockModels)) {
                return hasGenerated;
            }

            if (makeDir(itemModels)) {
                return hasGenerated;
            }

        }

        if(!makeFile(langUS)){
            return hasGenerated;
        }

        if(makeDir(recipes)){
            return hasGenerated;
        }

        if(makeDir(lootTables)){
            return hasGenerated;
        }

        LogHelper.info(Reference.NAME + " is generating all missing resource files...");
        LogHelper.info("This may take a moment...");


        try {

            FileWriter langWrite = new FileWriter(langUS);

            if(FMLEnvironment.dist.isClient()) {

                langWrite.write("{\n\t");

            }

            boolean first = true;

            for (Block block : BlockInit.MY_BLOCKS) {

                if(FMLEnvironment.dist.isClient()) {

                    //Language file entry
                    //Get the key and the name of the base block
                    String key = block.getDescriptionId();
                    String baseBlockName = getNameForBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(((SlabBlock) block).getBaseBlock())));
                    Block baseBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(((SlabBlock) block).getBaseBlock()));
                    String entry = "";

                    //If we have multiple entries, add a comma and newline between them
                    if (!first) {
                        entry = ",\n\t";
                    }

                    //Set the name correctly based on the type of block we have
                    if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")) {
                        entry += "\"" + key + "\": \"" + baseBlockName + " Quarter\"";
                    } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")) {
                        entry += "\"" + key + "\": \"" + baseBlockName + " Stairs\"";
                    } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")) {
                        entry += "\"" + key + "\": \"" + baseBlockName + " Slab\"";
                    }

                    //Then write the new entry to the file
                    langWrite.write(entry);


                    //Blockstate

                    //Get the path for the blockstate file
                    String filename = ForgeRegistries.BLOCKS.getKey(block).getPath() + ".json";
                    File blockstate = new File(blockstates.getPath() + "/" + filename);

                    if (!blockstate.exists() || !blockstate.isFile()) {                               //Check if the file exists
                        if (!blockstate.createNewFile()) {                                            //If not attempt to create it
                            LogHelper.error("Failed to create blockstate file at " + blockstate.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockstate);                        //Then get an OutputStream to the file
                        InputStream in = new InputStream() {                                        //And an InputStream for the default blockstate file
                            @Override
                            public int read() {
                                return 0;
                            }
                        };


                        if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")) {             //Select the correct default blockstate and get it
                            in = getResourceStream("/default/blockstate_quarter.json");
                        } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")) {
                            in = getResourceStream("/default/blockstate_slab.json");
                        } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")) {
                            in = getResourceStream("/default/blockstate_stair.json");
                        }

                        //Finally copy the data from the default blockstate file to the new file and replace the placeholder with the actual registry name of the block
                        copyFileAndReplace(in, out, new String[]{"registry_namespace", "registry_path"}, new String[]{ForgeRegistries.BLOCKS.getKey(block).getNamespace(), ForgeRegistries.BLOCKS.getKey(block).getPath()});

                        hasGenerated = true;

                    }   //If the file already existed we don't need to do anything


                    //Block Models

                    if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")) {

                        File blockModel = new File(blockModels.getPath() + "/" + filename);

                        if (!blockModel.exists() || !blockModel.isFile()) {
                            if (!blockModel.createNewFile()) {
                                LogHelper.error("Failed to create block model file at " + blockModel.getPath());
                                continue;
                            }

                            OutputStream out = new FileOutputStream(blockModel);
                            InputStream in = getResourceStream("/default/blockmodel_quarter.json");
                            if(block.defaultBlockState().getMaterial() == Material.GLASS){
                                in = getResourceStream("/default/blockmodel_quarter_translucent.json");
                            }
                            copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                            hasGenerated = true;

                        }

                    } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")) {

                        File blockModel = new File(blockModels.getPath() + "/" + filename);

                        if (!blockModel.exists() || !blockModel.isFile()) {
                            if (!blockModel.createNewFile()) {
                                LogHelper.error("Failed to create block model file at " + blockModel.getPath());
                                continue;
                            }

                            OutputStream out = new FileOutputStream(blockModel);
                            InputStream in = getResourceStream("/default/blockmodel_slab.json");
                            if(block.defaultBlockState().getMaterial() == Material.GLASS){
                                in = getResourceStream("/default/blockmodel_slab_translucent.json");
                            }
                            copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                            hasGenerated = true;

                        }

                    } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")) {

                        File blockModelStraight = new File(blockModels.getPath() + "/" + filename);

                        if (!blockModelStraight.exists() || !blockModelStraight.isFile()) {
                            if (!blockModelStraight.createNewFile()) {
                                LogHelper.error("Failed to create block model file at " + blockModelStraight.getPath());
                                continue;
                            }

                            OutputStream out = new FileOutputStream(blockModelStraight);
                            InputStream in = getResourceStream("/default/blockmodel_stair.json");
                            if(block.defaultBlockState().getMaterial() == Material.GLASS){
                                in = getResourceStream("/default/blockmodel_stair_translucent.json");
                            }
                            copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                            hasGenerated = true;

                        }

                        filename = ForgeRegistries.BLOCKS.getKey(block).getPath() + "_inner.json";

                        File blockModelInner = new File(blockModels.getPath() + "/" + filename);

                        if (!blockModelInner.exists() || !blockModelInner.isFile()) {
                            if (!blockModelInner.createNewFile()) {
                                LogHelper.error("Failed to create block model file at " + blockModelInner.getPath());
                                continue;
                            }

                            OutputStream out = new FileOutputStream(blockModelInner);
                            InputStream in = getResourceStream("/default/blockmodel_stair_inner.json");
                            if(block.defaultBlockState().getMaterial() == Material.GLASS){
                                in = getResourceStream("/default/blockmodel_stair_inner_translucent.json");
                            }
                            copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                            hasGenerated = true;

                        }

                        filename = ForgeRegistries.BLOCKS.getKey(block).getPath() + "_inner_mirrored.json";

                        File blockModelInnerMirrored = new File(blockModels.getPath() + "/" + filename);

                        if (!blockModelInnerMirrored.exists() || !blockModelInnerMirrored.isFile()) {
                            if (!blockModelInnerMirrored.createNewFile()) {
                                LogHelper.error("Failed to create block model file at " + blockModelInnerMirrored.getPath());
                                continue;
                            }

                            OutputStream out = new FileOutputStream(blockModelInnerMirrored);
                            InputStream in = getResourceStream("/default/blockmodel_stair_inner_mirrored.json");
                            if(block.defaultBlockState().getMaterial() == Material.GLASS){
                                in = getResourceStream("/default/blockmodel_stair_inner_mirrored_translucent.json");
                            }
                            copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                            hasGenerated = true;

                        }

                        filename = ForgeRegistries.BLOCKS.getKey(block).getPath() + "_outer.json";

                        File blockModelOuter = new File(blockModels.getPath() + "/" + filename);

                        if (!blockModelOuter.exists() || !blockModelOuter.isFile()) {
                            if (!blockModelOuter.createNewFile()) {
                                LogHelper.error("Failed to create block model file at " + blockModelOuter.getPath());
                                continue;
                            }

                            OutputStream out = new FileOutputStream(blockModelOuter);
                            InputStream in = getResourceStream("/default/blockmodel_stair_outer.json");
                            if(block.defaultBlockState().getMaterial() == Material.GLASS){
                                in = getResourceStream("/default/blockmodel_stair_outer_translucent.json");
                            }
                            copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                            hasGenerated = true;

                        }


                    }


                    //Item Models

                    File itemModel = new File(itemModels + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + ".json");

                    if (!itemModel.exists() || !itemModel.isFile()) {
                        if (!itemModel.createNewFile()) {
                            LogHelper.error("Could not create item model file for block " + ForgeRegistries.BLOCKS.getKey(block));
                            continue;
                        }

                        FileWriter writer = new FileWriter(itemModel);
                        writer.write("{\n\t\"parent\": \"" + Reference.MODID + ":block/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "\"\n}");
                        writer.close();

                        hasGenerated = true;

                    }

                }

                //Crafting Recipes

                //Regular Crafting big recipes (crafting table)
                File recipeBig = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_big.json");

                if(!recipeBig.exists() || !recipeBig.isFile()){

                    InputStream in = null;

                    if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")) {
                        in = getResourceStream("/default/crafting_quarter_big.json");
                    } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")) {
                        in = getResourceStream("/default/crafting_slab_big.json");
                    } else if (ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")) {
                        in = getResourceStream("/default/crafting_stair_big.json");
                    }

                    if(!recipeBig.createNewFile() || in == null){
                        LogHelper.error("Could not create crafting recipe 'big' for block " + ForgeRegistries.BLOCKS.getKey(block));
                        continue;
                    }

                    OutputStream out = new FileOutputStream(recipeBig);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), ForgeRegistries.BLOCKS.getKey(block).toString()});

                    hasGenerated = true;

                }

                //Regular crafting small recipes (2x2 grid in inventory)
                File recipeSmall = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_small.json");

                if(!recipeSmall.exists() || !recipeSmall.isFile()){

                    InputStream in = null;

                    if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")){
                        in = getResourceStream("/default/crafting_quarter_small.json");
                    }
                    else if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")){
                        in = getResourceStream("/default/crafting_slab_small.json");
                    }
                    else if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")){
                        in = getResourceStream("/default/crafting_stair_small.json");
                    }

                    if(!recipeSmall.createNewFile() || in == null){
                        LogHelper.error("Could not create crafting recipe 'small' for block " + ForgeRegistries.BLOCKS.getKey(block));
                        continue;
                    }

                    OutputStream out = new FileOutputStream(recipeSmall);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), ForgeRegistries.BLOCKS.getKey(block).toString()});

                    hasGenerated = true;

                }

                //Stonecutter crafting recipes
                if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")){

                    File recipeStonecutter = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_stonecutter.json");

                    if(!recipeStonecutter.exists()) {

                        InputStream in = getResourceStream("/default/crafting_1_from_1_stonecutter.json");
                        OutputStream out = new FileOutputStream(recipeStonecutter);
                        copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{((SlabBlock) block).getBaseBlock(), ForgeRegistries.BLOCKS.getKey(block).toString()});

                        hasGenerated = true;

                    }

                }
                else if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")){

                    File recipeStonecutter = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_stonecutter.json");

                    if(!recipeStonecutter.exists()) {

                        InputStream in = getResourceStream("/default/crafting_2_from_1_stonecutter.json");
                        OutputStream out = new FileOutputStream(recipeStonecutter);
                        copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{((SlabBlock) block).getBaseBlock(), ForgeRegistries.BLOCKS.getKey(block).toString()});

                        hasGenerated = true;

                    }

                }
                else if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")){

                    File recipeStonecutter1 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_from_full_stonecutter.json");
                    File recipeStonecutter2 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_from_stair_stonecutter.json");
                    File recipeStonecutter3 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_from_slab_stonecutter.json");

                    if(!recipeStonecutter1.exists()) {

                        InputStream in1 = getResourceStream("/default/crafting_4_from_1_stonecutter.json");
                        OutputStream out1 = new FileOutputStream(recipeStonecutter1);
                        copyFileAndReplace(in1, out1, new String[]{"placeholder_input", "placeholder_output"}, new String[]{((SlabBlock) block).getBaseBlock(), ForgeRegistries.BLOCKS.getKey(block).toString()});

                        hasGenerated = true;

                    }

                    String stair = "", slab = "";
                    String regName = ForgeRegistries.BLOCKS.getKey(block).getPath();
                    regName = regName.replace("quarter_sas", "");

                    for(Block block1 : BlockInit.MY_BLOCKS){

                        if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains(regName)){

                            if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains("stair_sas")){
                                stair = ForgeRegistries.BLOCKS.getKey(block1).toString();
                            }
                            else if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains("slab_sas")){
                                slab = ForgeRegistries.BLOCKS.getKey(block1).toString();
                            }

                        }

                    }

                    if(stair.equals("") || slab.equals("")){
                        LogHelper.error("Could not create crafting recipe 'stonecutter' for block " + ForgeRegistries.BLOCKS.getKey(block));
                        LogHelper.error("Could not find corresponding slab or stair!");
                    }
                    else{

                        if(!recipeStonecutter2.exists()) {

                            InputStream in2 = getResourceStream("/default/crafting_3_from_1_stonecutter.json");
                            OutputStream out2 = new FileOutputStream(recipeStonecutter2);

                            copyFileAndReplace(in2, out2, new String[]{"placeholder_input", "placeholder_output"}, new String[]{stair, ForgeRegistries.BLOCKS.getKey(block).toString()});
                            hasGenerated = true;

                        }
                        if(!recipeStonecutter3.exists()) {

                            InputStream in3 = getResourceStream("/default/crafting_2_from_1_stonecutter.json");
                            OutputStream out3 = new FileOutputStream(recipeStonecutter3);

                            copyFileAndReplace(in3, out3, new String[]{"placeholder_input", "placeholder_output"}, new String[]{slab, ForgeRegistries.BLOCKS.getKey(block).toString()});
                            hasGenerated = true;

                        }

                    }

                }

                //Shapeless recipes
                if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("stair_sas")){

                    File recipeShapeless1 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless1.json");
                    File recipeShapeless2 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless2.json");
                    File recipeShapeless3 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless3.json");
                    File recipeShapeless4 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless4.json");


                    String quarter = "", slab = "";
                    String regName = ForgeRegistries.BLOCKS.getKey(block).getPath();
                    regName = regName.replace("stair_sas", "");

                    for(Block block1 : BlockInit.MY_BLOCKS){

                        if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains(regName)){

                            if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains("quarter_sas")){
                                quarter = ForgeRegistries.BLOCKS.getKey(block1).toString();
                            }
                            else if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains("slab_sas")){
                                slab = ForgeRegistries.BLOCKS.getKey(block1).toString();
                            }

                        }

                    }

                    if(quarter.equals("") || slab.equals("")){
                        LogHelper.error("Could not create crafting recipe 'shapeless' for block " + ForgeRegistries.BLOCKS.getKey(block));
                        LogHelper.error("Could not find corresponding slab or quarter!");
                    }
                    else {

                        if(!recipeShapeless1.exists()) {

                            InputStream in1 = getResourceStream("/default/crafting_shapeless_3_to_1.json");
                            OutputStream out1 = new FileOutputStream(recipeShapeless1);

                            copyFileAndReplace(in1, out1, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_input3", "placeholder_output"}, new String[]{quarter, quarter, quarter, ForgeRegistries.BLOCKS.getKey(block).toString()});
                            hasGenerated = true;

                        }
                        if(!recipeShapeless2.exists()) {

                            InputStream in2 = getResourceStream("/default/crafting_shapeless_2_to_1.json");
                            OutputStream out2 = new FileOutputStream(recipeShapeless2);

                            copyFileAndReplace(in2, out2, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{quarter, slab, ForgeRegistries.BLOCKS.getKey(block).toString()});
                            hasGenerated = true;

                        }
                        if(!recipeShapeless3.exists()) {

                            InputStream in3 = getResourceStream("/default/crafting_shapeless_1_to_x.json");
                            OutputStream out3 = new FileOutputStream(recipeShapeless3);

                            copyFileAndReplace(in3, out3, new String[]{"placeholder_input", "placeholder_output", "placeholder_count"}, new String[]{ForgeRegistries.BLOCKS.getKey(block).toString(), quarter, "3"});
                            hasGenerated = true;

                        }
                        if(!recipeShapeless4.exists()) {

                            InputStream in4 = getResourceStream("/default/crafting_shapeless_2_to_1.json");
                            OutputStream out4 = new FileOutputStream(recipeShapeless4);

                            copyFileAndReplace(in4, out4, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{ForgeRegistries.BLOCKS.getKey(block).toString(), quarter, ((SlabBlock) block).getBaseBlock()});
                            hasGenerated = true;

                        }

                    }

                }
                else if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("slab_sas")){

                    File recipeShapeless1 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless1.json");
                    File recipeShapeless2 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless2.json");
                    File recipeShapeless3 = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless3.json");

                    String quarter = "";
                    String regName = ForgeRegistries.BLOCKS.getKey(block).getPath();
                    regName = regName.replace("slab_sas", "");

                    for(Block block1 : BlockInit.MY_BLOCKS){

                        if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains(regName)){

                            if(ForgeRegistries.BLOCKS.getKey(block1).getPath().contains("quarter_sas")){
                                quarter = ForgeRegistries.BLOCKS.getKey(block1).toString();
                            }

                        }

                    }

                    if(quarter.equals("")){
                        LogHelper.error("Could not create crafting recipe 'shapeless' for block " + ForgeRegistries.BLOCKS.getKey(block));
                        LogHelper.error("Could not find corresponding quarter!");
                    }
                    else {

                        if(!recipeShapeless1.exists()) {

                            InputStream in1 = getResourceStream("/default/crafting_shapeless_2_to_1.json");
                            OutputStream out1 = new FileOutputStream(recipeShapeless1);

                            copyFileAndReplace(in1, out1, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{quarter, quarter, ForgeRegistries.BLOCKS.getKey(block).toString()});
                            hasGenerated = true;

                        }
                        if(!recipeShapeless2.exists()) {

                            InputStream in2 = getResourceStream("/default/crafting_shapeless_2_to_1.json");
                            OutputStream out2 = new FileOutputStream(recipeShapeless2);

                            copyFileAndReplace(in2, out2, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{ForgeRegistries.BLOCKS.getKey(block).toString(), ForgeRegistries.BLOCKS.getKey(block).toString(), ((SlabBlock) block).getBaseBlock()});
                            hasGenerated = true;

                        }
                        if(!recipeShapeless3.exists()) {

                            InputStream in3 = getResourceStream("/default/crafting_shapeless_1_to_x.json");
                            OutputStream out3 = new FileOutputStream(recipeShapeless3);

                            copyFileAndReplace(in3, out3, new String[]{"placeholder_input", "placeholder_output", "placeholder_count"}, new String[]{ForgeRegistries.BLOCKS.getKey(block).toString(), quarter, "2"});
                            hasGenerated = true;

                        }

                    }

                }
                else if(ForgeRegistries.BLOCKS.getKey(block).getPath().contains("quarter_sas")){

                    File recipeShapeless = new File(recipes + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_shapeless.json");

                    if(!recipeShapeless.exists()) {

                        InputStream in = getResourceStream("/default/crafting_shapeless_4_to_1.json");
                        OutputStream out = new FileOutputStream(recipeShapeless);
                        copyFileAndReplace(in, out, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_input3", "placeholder_input4", "placeholder_output"}, new String[]{ForgeRegistries.BLOCKS.getKey(block).toString(), ForgeRegistries.BLOCKS.getKey(block).toString(), ForgeRegistries.BLOCKS.getKey(block).toString(), ForgeRegistries.BLOCKS.getKey(block).toString(), ((SlabBlock) block).getBaseBlock()});

                        hasGenerated = true;

                    }

                }


                //Loot tables
                File lootTable = new File(lootTables + "/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + ".json");

                if(!lootTable.exists() || !lootTable.isFile()){
                    if(!lootTable.createNewFile()){
                        LogHelper.error("Could not create loot table file for block " + ForgeRegistries.BLOCKS.getKey(block));
                        continue;
                    }

                    InputStream in = getResourceStream("/default/loot_table.json");
                    OutputStream out = new FileOutputStream(lootTable);
                    copyFileAndReplace(in, out, "placeholder_block", ForgeRegistries.BLOCKS.getKey(block).toString());

                    hasGenerated = true;

                }



                first = false;

            }

            if(FMLEnvironment.dist.isClient()) {

                langWrite.write("\n}");
                langWrite.close();

            }

        }
        catch (Exception ex){
            LogHelper.error("Error creating resource files!");
            LogHelper.error(ex.fillInStackTrace());
            LogHelper.error(ex.getMessage());
        }

        return hasGenerated;

    }

}
