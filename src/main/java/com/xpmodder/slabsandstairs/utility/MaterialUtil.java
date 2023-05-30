package com.xpmodder.slabsandstairs.utility;

import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialUtil {

    private static class Mat{
        public String name;
        public Material material;

        Mat(String name, Material mat){
            this.name = name;
            this.material = mat;
        }
    }

    protected static final List<Mat> materials = new ArrayList<>();


    static{

        materials.add(new Mat("Air", Material.AIR));
        materials.add(new Mat("StructuralAir", Material.STRUCTURAL_AIR));
        materials.add(new Mat("Portal", Material.PORTAL));
        materials.add(new Mat("ClothDecoration", Material.CLOTH_DECORATION));
        materials.add(new Mat("Plant", Material.PLANT));
        materials.add(new Mat("WaterPlant", Material.WATER_PLANT));
        materials.add(new Mat("ReplaceablePlant", Material.REPLACEABLE_PLANT));
        materials.add(new Mat("ReplaceableFireproofPlant", Material.REPLACEABLE_FIREPROOF_PLANT));
        materials.add(new Mat("ReplaceableWaterPlant", Material.REPLACEABLE_WATER_PLANT));
        materials.add(new Mat("Water", Material.WATER));
        materials.add(new Mat("BubbleColumn", Material.BUBBLE_COLUMN));
        materials.add(new Mat("Lava", Material.LAVA));
        materials.add(new Mat("TopSnow", Material.TOP_SNOW));
        materials.add(new Mat("Fire", Material.FIRE));
        materials.add(new Mat("Decoration", Material.DECORATION));
        materials.add(new Mat("Web", Material.WEB));
        materials.add(new Mat("Sculk", Material.SCULK));
        materials.add(new Mat("BuildableGlass", Material.BUILDABLE_GLASS));
        materials.add(new Mat("Clay", Material.CLAY));
        materials.add(new Mat("Dirt", Material.DIRT));
        materials.add(new Mat("Grass", Material.GRASS));
        materials.add(new Mat("IceSolid", Material.ICE_SOLID));
        materials.add(new Mat("Sand", Material.SAND));
        materials.add(new Mat("Sponge", Material.SPONGE));
        materials.add(new Mat("ShulkerShell", Material.SHULKER_SHELL));
        materials.add(new Mat("Wood", Material.WOOD));
        materials.add(new Mat("NetherWood", Material.NETHER_WOOD));
        materials.add(new Mat("BambooSapling", Material.BAMBOO_SAPLING));
        materials.add(new Mat("Bamboo", Material.BAMBOO));
        materials.add(new Mat("Wool", Material.WOOL));
        materials.add(new Mat("Explosive", Material.EXPLOSIVE));
        materials.add(new Mat("Leaves", Material.LEAVES));
        materials.add(new Mat("Glass", Material.GLASS));
        materials.add(new Mat("Ice", Material.ICE));
        materials.add(new Mat("Cactus", Material.CACTUS));
        materials.add(new Mat("Stone", Material.STONE));
        materials.add(new Mat("Metal", Material.METAL));
        materials.add(new Mat("Snow", Material.SNOW));
        materials.add(new Mat("HeavyMetal", Material.HEAVY_METAL));
        materials.add(new Mat("Barrier", Material.BARRIER));
        materials.add(new Mat("Piston", Material.PISTON));
        materials.add(new Mat("Moss", Material.MOSS));
        materials.add(new Mat("Vegetable", Material.VEGETABLE));
        materials.add(new Mat("Egg", Material.EGG));
        materials.add(new Mat("Cake", Material.CAKE));
        materials.add(new Mat("Amethyst", Material.AMETHYST));
        materials.add(new Mat("PowderSnow", Material.POWDER_SNOW));

    }


    public static Material getMaterialFromString(String mat){

        mat = mat.trim();

        for(Mat material : materials){
            if(material.name.equalsIgnoreCase(mat)){
                return material.material;
            }
        }

        return null;
    }

    public static String getStringFromMaterial(Material mat){

        for(Mat material : materials){
            if(material.material == mat){
                return material.name;
            }
        }

        return null;
    }

}
