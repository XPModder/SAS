package com.xpmodder.slabsandstairs.utility;

public enum BlockTagTypes {

    PICKAXE("pickaxe"),
    AXE("axe"),
    SHOVEL("shovel"),
    HOE("hoe");

    private final String name;
    BlockTagTypes(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BlockTagTypes fromName(String name){
        if(PICKAXE.getName().equals(name)){
            return PICKAXE;
        }
        else if(AXE.getName().equals(name)){
            return AXE;
        }
        else if(SHOVEL.getName().equals(name)){
            return SHOVEL;
        }
        else if(HOE.getName().equals(name)){
            return HOE;
        }
        return null;
    }

}
