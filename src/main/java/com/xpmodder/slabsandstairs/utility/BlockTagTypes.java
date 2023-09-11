package com.xpmodder.slabsandstairs.utility;

@SuppressWarnings("unused")
public enum BlockTagTypes {

    PICKAXE("pickaxe"),
    AXE("axe"),
    SHOVEL("shovel"),
    HOE("hoe"),
    FENCE("fence"),
    WALL("wall");

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
        else if(FENCE.getName().equals(name)){
            return FENCE;
        }
        else if(WALL.getName().equals(name)){
            return WALL;
        }
        return null;
    }

}
