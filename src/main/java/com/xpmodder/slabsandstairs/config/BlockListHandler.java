package com.xpmodder.slabsandstairs.config;

import com.xpmodder.slabsandstairs.SlabsAndStairs;
import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import com.xpmodder.slabsandstairs.utility.MaterialUtil;
import com.xpmodder.slabsandstairs.utility.ResourceGenerator;

import java.io.*;

public class BlockListHandler {

    public static void read() {

        if(!Reference.BLOCK_LIST.exists() || !Reference.BLOCK_LIST.isFile()){
            generate();
        }

        try {

            InputStream stream = new FileInputStream(Reference.BLOCK_LIST);

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;

            while((line = reader.readLine()) != null){

                String name, material;
                float strength = 1.0f;
                int light = 0, power = 0;

                String[] data = line.split(";");

                if(data.length < 2){
                    LogHelper.error("Incorrect line in block list! Cannot read: " + line);
                    continue;
                }

                name = data[0].trim();
                material = data[1].trim();

                if(data.length >= 3){
                    strength = Float.parseFloat(data[2].trim());
                    if(strength < 0.01f){
                        strength = 1.0f;
                    }
                }

                if(data.length >= 4){
                    light = Integer.parseInt(data[3].trim());
                }

                if(data.length >= 5){
                    power = Integer.parseInt(data[4].trim());
                }

                BlockInit.NewBlock(name, MaterialUtil.getMaterialFromString(material), strength, light, power);

            }

        }
        catch (FileNotFoundException ex){
            LogHelper.error("Block list file not found! Cannot register blocks!");
        }
        catch (IOException ex){
            LogHelper.error("IO Exception when reading Block list! Cannot register blocks!");
        }

    }

    public static void generate(){

        if(!Reference.BLOCK_LIST.exists() || !Reference.BLOCK_LIST.isFile()){

            if(ResourceGenerator.makeFile(Reference.BLOCK_LIST)){

                try {

                    LogHelper.info("Copying block list file...");

                    InputStream in = SlabsAndStairs.class.getClassLoader().getResourceAsStream("assets/" + Reference.MODID + "/default/blocklist.csv");
                    OutputStream out = new FileOutputStream(Reference.BLOCK_LIST);

                    ResourceGenerator.copyFile(in, out);

                }
                catch (Exception ex){
                    LogHelper.error("Could not generate BLOCK_LIST file!");
                    ex.printStackTrace();
                }

            }

        }

    }

}
