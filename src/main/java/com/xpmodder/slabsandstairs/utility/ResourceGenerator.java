package com.xpmodder.slabsandstairs.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
                return false;
            }
        }
        return true;
    }

    //Gets the english name for a given block
    public static String getNameForBlock(Block block){

        String output = "";

        try {

            String namespace = block.getRegistryName().getNamespace();

            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(namespace, "lang/en_us.json")).getInputStream();

            JsonObject object = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);

            output = object.get(block.getDescriptionId()).getAsString();

        }
        catch (Exception ex){
            LogHelper.error("Exception trying to get name for block " + block.getRegistryName().toString() + "!");
        }

        return output;

    }

    //Gets the path for the texture file for the given block and the given texture (aka "top" or "bottom" or "side")
    //Tries to get the requested texture, otherwise gets "all"
    public static String getTextureForBlock(Block block, String texture){

        String output = "";

        try{

            String namespace = block.getRegistryName().getNamespace();

            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(namespace, "models/block/" + block.getRegistryName().getPath() + ".json")).getInputStream();

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

        }
        catch(Exception ex){
            LogHelper.error("Exception trying to get texture for block " + block.getRegistryName().toString() + "!");
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


    //Generates the resource files like blockstates, block and item models as well as display text
    public static void generate(){

        File blockstates = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/blockstates");
        File blockModels = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/models/block");
        File itemModels = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/models/item");
        File recipes = new File(Reference.DATAPACK_FOLDER, "data/" + Reference.MODID + "/recipes");
        File langUS = new File(Reference.RESOURCE_FOLDER, "assets/" + Reference.MODID + "/lang/en_us.json");

        if(!Reference.RESOURCE_FOLDER.exists() || !Reference.RESOURCE_FOLDER.isDirectory()){
            LogHelper.error("Invalid or inexistent mod resource folder!");
            LogHelper.error("Could not find folder: " + Reference.RESOURCE_FOLDER);
            return;
        }

        //Check if all the directories and files exist and create them if necessary.
        if(!makeDir(blockstates)){
            return;
        }

        if(!makeDir(blockModels)){
            return;
        }

        if(!makeDir(itemModels)){
            return;
        }

        if(!makeDir(recipes)){
            return;
        }

        if(!makeFile(langUS)){
            return;
        }

        LogHelper.info(Reference.NAME + " is generating all missing resource files...");
        LogHelper.info("This may take a moment...");


        try {
            FileWriter langWrite = new FileWriter(langUS);
            langWrite.write("{\n\t");

            boolean first = true;

            for (Block block : BlockInit.MY_BLOCKS) {

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
                if (block.getRegistryName().getPath().contains("quarter_sas")) {
                    entry += "\"" + key + "\": \"" + baseBlockName + " Quarter\"";
                } else if (block.getRegistryName().getPath().contains("stair_sas")) {
                    entry += "\"" + key + "\": \"" + baseBlockName + " Stairs\"";
                } else if (block.getRegistryName().getPath().contains("slab_sas")) {
                    entry += "\"" + key + "\": \"" + baseBlockName + " Slab\"";
                }

                //Then write the new entry to the file
                langWrite.write(entry);


                //Blockstate

                //Get the path for the blockstate file
                String filename = block.getRegistryName().getPath() + ".json";
                File blockstate = new File(blockstates.getPath() + "/" + filename);

                if(!blockstate.exists() || !blockstate.isFile()){                               //Check if the file exists
                    if(!blockstate.createNewFile()){                                            //If not attempt to create it
                        LogHelper.error("Failed to create blockstate file at " + blockstate.getPath());
                        continue;
                    }

                    OutputStream out = new FileOutputStream(blockstate);                        //Then get an OutputStream to the file
                    InputStream in = new InputStream() {                                        //And an InputStream for the default blockstate file
                        @Override
                        public int read() throws IOException {
                            return 0;
                        }
                    };


                    if(block.getRegistryName().getPath().contains("quarter_sas")) {             //Select the correct default blockstate and get it
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockstate_quarter.json")).getInputStream();
                    }
                    else if(block.getRegistryName().getPath().contains("slab_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockstate_slab.json")).getInputStream();
                    }
                    else if(block.getRegistryName().getPath().contains("stair_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockstate_stair.json")).getInputStream();
                    }

                    //Finally copy the data from the default blockstate file to the new file and replace the placeholder with the actual registry name of the block
                    copyFileAndReplace(in, out, new String[]{"registry_namespace", "registry_path"}, new String[]{block.getRegistryName().getNamespace(), block.getRegistryName().getPath()});

                }   //If the file already existed we don't need to do anything


                //Block Models

                if(block.getRegistryName().getPath().contains("quarter_sas")){

                    File blockModel = new File(blockModels.getPath() + "/" + filename);

                    if(!blockModel.exists() || !blockModel.isFile()){
                        if(!blockModel.createNewFile()){
                            LogHelper.error("Failed to create block model file at " + blockModel.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockModel);
                        InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockmodel_quarter.json")).getInputStream();
                        copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{ getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                    }

                }
                else if(block.getRegistryName().getPath().contains("slab_sas")){

                    File blockModel = new File(blockModels.getPath() + "/" + filename);

                    if(!blockModel.exists() || !blockModel.isFile()){
                        if(!blockModel.createNewFile()){
                            LogHelper.error("Failed to create block model file at " + blockModel.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockModel);
                        InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockmodel_slab.json")).getInputStream();
                        copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{ getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                    }

                }
                else if(block.getRegistryName().getPath().contains("stair_sas")){

                    File blockModelStraight = new File(blockModels.getPath() + "/" + filename);

                    if(!blockModelStraight.exists() || !blockModelStraight.isFile()){
                        if(!blockModelStraight.createNewFile()){
                            LogHelper.error("Failed to create block model file at " + blockModelStraight.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockModelStraight);
                        InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockmodel_stair.json")).getInputStream();
                        copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{ getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                    }

                    filename = block.getRegistryName().getPath() + "_inner.json";

                    File blockModelInner = new File(blockModels.getPath() + "/" + filename);

                    if(!blockModelInner.exists() || !blockModelInner.isFile()){
                        if(!blockModelInner.createNewFile()){
                            LogHelper.error("Failed to create block model file at " + blockModelInner.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockModelInner);
                        InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockmodel_stair_inner.json")).getInputStream();
                        copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{ getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                    }

                    filename = block.getRegistryName().getPath() + "_inner_mirrored.json";

                    File blockModelInnerMirrored = new File(blockModels.getPath() + "/" + filename);

                    if(!blockModelInnerMirrored.exists() || !blockModelInnerMirrored.isFile()){
                        if(!blockModelInnerMirrored.createNewFile()){
                            LogHelper.error("Failed to create block model file at " + blockModelInnerMirrored.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockModelInnerMirrored);
                        InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockmodel_stair_inner_mirrored.json")).getInputStream();
                        copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{ getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                    }

                    filename = block.getRegistryName().getPath() + "_outer.json";

                    File blockModelOuter = new File(blockModels.getPath() + "/" + filename);

                    if(!blockModelOuter.exists() || !blockModelOuter.isFile()){
                        if(!blockModelOuter.createNewFile()){
                            LogHelper.error("Failed to create block model file at " + blockModelOuter.getPath());
                            continue;
                        }

                        OutputStream out = new FileOutputStream(blockModelOuter);
                        InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/blockmodel_stair_outer.json")).getInputStream();
                        copyFileAndReplace(in, out, new String[]{"texture_path_top", "texture_path_bottom", "texture_path_side"}, new String[]{ getTextureForBlock(baseBlock, "top"), getTextureForBlock(baseBlock, "bottom"), getTextureForBlock(baseBlock, "side")});

                    }


                }


                //Item Models

                File itemModel = new File(itemModels + "/" + block.getRegistryName().getPath() + ".json");

                if(!itemModel.exists() || !itemModel.isFile()){
                    if(!itemModel.createNewFile()){
                        LogHelper.error("Could not create item model file for block " + block.getRegistryName());
                        continue;
                    }

                    FileWriter writer = new FileWriter(itemModel);
                    writer.write("{\n\t\"parent\": \"" + Reference.MODID + ":block/" + block.getRegistryName().getPath() + "\"\n}");
                    writer.close();

                }

                //Crafting Recipes

                //Regular Crafting big recipes (crafting table)
                File recipeBig = new File(recipes + "/" + block.getRegistryName().getPath() + "_big.json");

                if(!recipeBig.exists() || !recipeBig.isFile()){

                    InputStream in = null;

                    if(block.getRegistryName().getPath().contains("quarter_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_quarter_big.json")).getInputStream();
                    }
                    else if(block.getRegistryName().getPath().contains("slab_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_slab_big.json")).getInputStream();
                    }
                    else if(block.getRegistryName().getPath().contains("stair_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_stair_big.json")).getInputStream();
                    }

                    if(!recipeBig.createNewFile() || in == null){
                        LogHelper.error("Could not create crafting recipe 'big' for block " + block.getRegistryName());
                        continue;
                    }

                    OutputStream out = new FileOutputStream(recipeBig);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), block.getRegistryName().toString()});

                }

                //Regular crafting small recipes (2x2 grid in inventory)
                File recipeSmall = new File(recipes + "/" + block.getRegistryName().getPath() + "_small.json");

                if(!recipeSmall.exists() || !recipeSmall.isFile()){

                    InputStream in = null;

                    if(block.getRegistryName().getPath().contains("quarter_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_quarter_small.json")).getInputStream();
                    }
                    else if(block.getRegistryName().getPath().contains("slab_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_slab_small.json")).getInputStream();
                    }
                    else if(block.getRegistryName().getPath().contains("stair_sas")){
                        in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_stair_small.json")).getInputStream();
                    }

                    if(!recipeSmall.createNewFile() || in == null){
                        LogHelper.error("Could not create crafting recipe 'small' for block " + block.getRegistryName());
                        continue;
                    }

                    OutputStream out = new FileOutputStream(recipeSmall);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), block.getRegistryName().toString()});

                }

                //Stonecutter crafting recipes
                if(block.getRegistryName().getPath().contains("stair_sas")){

                    File recipeStonecutter = new File(recipes + "/" + block.getRegistryName().getPath() + "_stonecutter.json");
                    InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_1_from_1_stonecutter.json")).getInputStream();
                    OutputStream out = new FileOutputStream(recipeStonecutter);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), block.getRegistryName().toString()});

                }
                else if(block.getRegistryName().getPath().contains("slab_sas")){

                    File recipeStonecutter = new File(recipes + "/" + block.getRegistryName().getPath() + "_stonecutter.json");
                    InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_2_from_1_stonecutter.json")).getInputStream();
                    OutputStream out = new FileOutputStream(recipeStonecutter);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), block.getRegistryName().toString()});

                }
                else if(block.getRegistryName().getPath().contains("quarter_sas")){

                    File recipeStonecutter1 = new File(recipes + "/" + block.getRegistryName().getPath() + "_from_full_stonecutter.json");
                    File recipeStonecutter2 = new File(recipes + "/" + block.getRegistryName().getPath() + "_from_stair_stonecutter.json");
                    File recipeStonecutter3 = new File(recipes + "/" + block.getRegistryName().getPath() + "_from_slab_stonecutter.json");
                    InputStream in1 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_4_from_1_stonecutter.json")).getInputStream();
                    InputStream in2 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_3_from_1_stonecutter.json")).getInputStream();
                    InputStream in3 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_2_from_1_stonecutter.json")).getInputStream();
                    OutputStream out1 = new FileOutputStream(recipeStonecutter1);
                    OutputStream out2 = new FileOutputStream(recipeStonecutter2);
                    OutputStream out3 = new FileOutputStream(recipeStonecutter3);
                    copyFileAndReplace(in1, out1, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ ((SlabBlock) block).getBaseBlock(), block.getRegistryName().toString()});

                    String stair = "", slab = "";
                    String regName = block.getRegistryName().getPath();
                    regName = regName.replace("quarter_sas", "");

                    for(Block block1 : BlockInit.MY_BLOCKS){

                        if(block1.getRegistryName().getPath().contains(regName)){

                            if(block1.getRegistryName().getPath().contains("stair_sas")){
                                stair = block1.getRegistryName().toString();
                            }
                            else if(block1.getRegistryName().getPath().contains("slab_sas")){
                                slab = block1.getRegistryName().toString();
                            }

                        }

                    }

                    if(stair.equals("") || slab.equals("")){
                        LogHelper.error("Could not create crafting recipe 'stonecutter' for block " + block.getRegistryName());
                        LogHelper.error("Could not find corresponding slab or stair!");
                    }
                    else{

                        copyFileAndReplace(in2, out2, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ stair, block.getRegistryName().toString()});
                        copyFileAndReplace(in3, out3, new String[]{"placeholder_input", "placeholder_output"}, new String[]{ slab, block.getRegistryName().toString()});

                    }

                }

                //Shapeless recipes
                if(block.getRegistryName().getPath().contains("stair_sas")){

                    File recipeShapeless1 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless1.json");
                    File recipeShapeless2 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless2.json");
                    File recipeShapeless3 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless3.json");
                    File recipeShapeless4 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless4.json");
                    InputStream in1 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_3_to_1.json")).getInputStream();
                    InputStream in2 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_2_to_1.json")).getInputStream();
                    InputStream in3 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_1_to_x.json")).getInputStream();
                    InputStream in4 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_2_to_1.json")).getInputStream();
                    OutputStream out1 = new FileOutputStream(recipeShapeless1);
                    OutputStream out2 = new FileOutputStream(recipeShapeless2);
                    OutputStream out3 = new FileOutputStream(recipeShapeless3);
                    OutputStream out4 = new FileOutputStream(recipeShapeless4);

                    String quarter = "", slab = "";
                    String regName = block.getRegistryName().getPath();
                    regName = regName.replace("stair_sas", "");

                    for(Block block1 : BlockInit.MY_BLOCKS){

                        if(block1.getRegistryName().getPath().contains(regName)){

                            if(block1.getRegistryName().getPath().contains("quarter_sas")){
                                quarter = block1.getRegistryName().toString();
                            }
                            else if(block1.getRegistryName().getPath().contains("slab_sas")){
                                slab = block1.getRegistryName().toString();
                            }

                        }

                    }

                    if(quarter.equals("") || slab.equals("")){
                        LogHelper.error("Could not create crafting recipe 'shapeless' for block " + block.getRegistryName());
                        LogHelper.error("Could not find corresponding slab or quarter!");
                    }
                    else {
                        copyFileAndReplace(in1, out1, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_input3", "placeholder_output"}, new String[]{quarter, quarter, quarter, block.getRegistryName().toString()});
                        copyFileAndReplace(in2, out2, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{quarter, slab, block.getRegistryName().toString()});
                        copyFileAndReplace(in3, out3, new String[]{"placeholder_input", "placeholder_output", "placeholder_count"}, new String[]{block.getRegistryName().toString(), quarter, "3"});
                        copyFileAndReplace(in4, out4, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{block.getRegistryName().toString(), quarter, ((SlabBlock) block).getBaseBlock()});
                    }

                }
                else if(block.getRegistryName().getPath().contains("slab_sas")){

                    File recipeShapeless1 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless1.json");
                    File recipeShapeless2 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless2.json");
                    File recipeShapeless3 = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless3.json");
                    InputStream in1 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_2_to_1.json")).getInputStream();
                    InputStream in2 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_2_to_1.json")).getInputStream();
                    InputStream in3 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_1_to_x.json")).getInputStream();
                    OutputStream out1 = new FileOutputStream(recipeShapeless1);
                    OutputStream out2 = new FileOutputStream(recipeShapeless2);
                    OutputStream out3 = new FileOutputStream(recipeShapeless3);

                    String quarter = "";
                    String regName = block.getRegistryName().getPath();
                    regName = regName.replace("slab_sas", "");

                    for(Block block1 : BlockInit.MY_BLOCKS){

                        if(block1.getRegistryName().getPath().contains(regName)){

                            if(block1.getRegistryName().getPath().contains("quarter_sas")){
                                quarter = block1.getRegistryName().toString();
                            }

                        }

                    }

                    if(quarter.equals("")){
                        LogHelper.error("Could not create crafting recipe 'shapeless' for block " + block.getRegistryName());
                        LogHelper.error("Could not find corresponding quarter!");
                    }
                    else {
                        copyFileAndReplace(in1, out1, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{quarter, quarter, block.getRegistryName().toString()});
                        copyFileAndReplace(in2, out2, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_output"}, new String[]{block.getRegistryName().toString(), block.getRegistryName().toString(), ((SlabBlock)block).getBaseBlock()});
                        copyFileAndReplace(in3, out3, new String[]{"placeholder_input", "placeholder_output", "placeholder_count"}, new String[]{block.getRegistryName().toString(), quarter, "2"});
                    }

                }
                else if(block.getRegistryName().getPath().contains("quarter_sas")){

                    File recipeShapeless = new File(recipes + "/" + block.getRegistryName().getPath() + "_shapeless.json");
                    InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(Reference.MODID, "default/crafting_shapeless_4_to_1.json")).getInputStream();
                    OutputStream out = new FileOutputStream(recipeShapeless);
                    copyFileAndReplace(in, out, new String[]{"placeholder_input1", "placeholder_input2", "placeholder_input3", "placeholder_input4", "placeholder_output"}, new String[]{ block.getRegistryName().toString(), block.getRegistryName().toString(), block.getRegistryName().toString(), block.getRegistryName().toString(), ((SlabBlock) block).getBaseBlock()});

                }


                first = false;

            }

            langWrite.write("\n}");
            langWrite.close();

        }
        catch (Exception ex){
            LogHelper.error("Error creating resource files!");
            LogHelper.error(ex.fillInStackTrace());
            LogHelper.error(ex.getMessage());
        }



    }

}
