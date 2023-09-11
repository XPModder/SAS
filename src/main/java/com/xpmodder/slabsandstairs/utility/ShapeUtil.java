package com.xpmodder.slabsandstairs.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ShapeUtil {

    public static class Part{
        public String name;
        public VoxelShape shape;

        public Part(String n, VoxelShape s){
            name = n;
            shape = s;
        }
    }

    protected static final VoxelShape NWD_CORNER = Block.box(0, 0, 0, 8, 8, 8);
    protected static final VoxelShape SWD_CORNER = Block.box(0, 0, 8, 8, 8, 16);
    protected static final VoxelShape NWU_CORNER = Block.box(0, 8, 0, 8, 16, 8);
    protected static final VoxelShape SWU_CORNER = Block.box(0, 8, 8, 8, 16, 16);
    protected static final VoxelShape NED_CORNER = Block.box(8, 0, 0, 16, 8, 8);
    protected static final VoxelShape SED_CORNER = Block.box(8, 0, 8, 16, 8, 16);
    protected static final VoxelShape NEU_CORNER = Block.box(8, 8, 0, 16, 16, 8);
    protected static final VoxelShape SEU_CORNER = Block.box(8, 8, 8, 16, 16, 16);


    public static final VoxelShape fullBlock = Block.box(0, 0, 0, 16, 16, 16);
    public static final VoxelShape SlabSouth = Block.box(0, 0, 0, 16, 16, 8);
    public static final VoxelShape SlabEast = Block.box(0, 0, 0, 8, 16, 16);
    public static final VoxelShape SlabNorth = Block.box(0, 0, 8, 16, 16, 16);
    public static final VoxelShape SlabWest = Block.box(8, 0, 0, 16, 16, 16);
    public static final VoxelShape SlabUp = Block.box(0, 0, 0, 16, 8, 16);
    public static final VoxelShape SlabDown = Block.box(0, 8, 0, 16, 16, 16);

    public static final VoxelShape StairNorthInv = Shapes.or(NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER, NWD_CORNER, NED_CORNER);
    public static final VoxelShape StairNorth = Shapes.or(NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER, NWU_CORNER, NEU_CORNER);
    public static final VoxelShape StairEastInv = Shapes.or(NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER, NED_CORNER, SED_CORNER);
    public static final VoxelShape StairEast = Shapes.or(NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER, NEU_CORNER, SEU_CORNER);
    public static final VoxelShape StairSouthInv = Shapes.or(NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER, SED_CORNER, SWD_CORNER);
    public static final VoxelShape StairSouth = Shapes.or(NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER, SEU_CORNER, SWU_CORNER);
    public static final VoxelShape StairWestInv = Shapes.or(NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER, SWD_CORNER, NWD_CORNER);
    public static final VoxelShape StairWest = Shapes.or(NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER, SWU_CORNER, NWU_CORNER);
    public static final VoxelShape StairUpInv = Shapes.or(NWU_CORNER, NEU_CORNER, NWD_CORNER, NED_CORNER, SWU_CORNER, SWD_CORNER);
    public static final VoxelShape StairUp = Shapes.or(NWU_CORNER, NEU_CORNER, NWD_CORNER, NED_CORNER, SEU_CORNER, SED_CORNER);
    public static final VoxelShape StairDownInv = Shapes.or(SWU_CORNER, SEU_CORNER, SWD_CORNER, SED_CORNER, NWU_CORNER, NWD_CORNER);
    public static final VoxelShape StairDown = Shapes.or(SWU_CORNER, SEU_CORNER, SWD_CORNER, SED_CORNER, NEU_CORNER, NED_CORNER);

    public static final VoxelShape Complex1 = Shapes.or(NED_CORNER, NWU_CORNER, NWD_CORNER, SEU_CORNER, SED_CORNER, SWU_CORNER);
    public static final VoxelShape Complex2 = Shapes.or(NEU_CORNER, NWU_CORNER, NWD_CORNER, SEU_CORNER, SED_CORNER, SWD_CORNER);
    public static final VoxelShape Complex3 = Shapes.or(NEU_CORNER, NED_CORNER, NWD_CORNER, SEU_CORNER, SWU_CORNER, SWD_CORNER);
    public static final VoxelShape Complex4 = Shapes.or(NEU_CORNER, NED_CORNER, NWU_CORNER, SED_CORNER, SWU_CORNER, SWD_CORNER);


    public static final List<VoxelShape> SlabShapes = Arrays.asList(SlabNorth, SlabEast, SlabSouth, SlabWest, SlabUp, SlabDown);
    public static final List<VoxelShape> StairShapes = Arrays.asList(StairNorthInv, StairEastInv, StairSouthInv, StairWestInv, StairUpInv, StairDownInv, StairNorth, StairEast, StairSouth, StairWest, StairUp, StairDown);
    public static final List<VoxelShape> ComplexShapes = Arrays.asList(Complex1, Complex2, Complex3, Complex4);


    public static final List<Part> parts = Arrays.asList(new Part("NEU", NEU_CORNER), new Part("NED", NED_CORNER),
            new Part("NWU", NWU_CORNER), new Part("NWD", NWD_CORNER), new Part("SEU", SEU_CORNER), new Part("SED", SED_CORNER),
            new Part("SWU", SWU_CORNER), new Part("SWD", SWD_CORNER));

    public static Part getPart(String name){
        for(Part p : parts){
            if(Objects.equals(p.name, name)){
                return p;
            }
        }
        return null;
    }



    public static boolean occupiesPart(@Nullable VoxelShape part, BlockState state, BlockGetter level, BlockPos pos){

        if(part == null){
            return false;
        }

        VoxelShape block = state.getShape(level, pos);
        return block.toAabbs().equals(Shapes.or(part, block).toAabbs());
    }


    public static boolean isSlabShape(VoxelShape shape){

        for(VoxelShape s : SlabShapes){
            if(s.toAabbs().equals(shape.toAabbs())){
                return true;
            }
        }

        return false;

    }

    public static boolean isStairShape(VoxelShape shape){

        for(VoxelShape s : StairShapes){
            if(s.toAabbs().equals(shape.toAabbs())){
                return true;
            }
        }

        return false;

    }

    public static boolean isComplexShape(VoxelShape shape){

        for(VoxelShape s : ComplexShapes){
            if(s.toAabbs().equals(shape.toAabbs())){
                return true;
            }
        }

        return false;

    }


}
