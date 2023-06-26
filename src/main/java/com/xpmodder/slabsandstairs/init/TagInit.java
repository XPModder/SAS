package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static com.xpmodder.slabsandstairs.init.BlockInit.MY_BLOCKS;

public class TagInit {

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {

        LogHelper.info("GatherData: " + event.includeServer());

        if(event.includeServer()) {
            event.getGenerator().addProvider(
                    new ModBlockTagProvider(event.getGenerator(), Reference.MODID, event.getExistingFileHelper())
            );
        }

    }


    public static class ModBlockTagProvider extends BlockTagsProvider{

        protected List<TagKey<Block>> tags = Arrays.asList(BlockTags.WOOL, BlockTags.WOOL, BlockTags.PLANKS, BlockTags.STONE_BRICKS, BlockTags.WOODEN_BUTTONS, BlockTags.BUTTONS, BlockTags.CARPETS, BlockTags.WOODEN_DOORS, BlockTags.WOODEN_STAIRS, BlockTags.WOODEN_SLABS, BlockTags.WOODEN_FENCES, BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES, BlockTags.WOODEN_TRAPDOORS, BlockTags.DOORS, BlockTags.SAPLINGS, BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.DARK_OAK_LOGS, BlockTags.OAK_LOGS, BlockTags.BIRCH_LOGS, BlockTags.ACACIA_LOGS, BlockTags.JUNGLE_LOGS, BlockTags.SPRUCE_LOGS, BlockTags.CRIMSON_STEMS, BlockTags.WARPED_STEMS, BlockTags.BANNERS, BlockTags.SAND, BlockTags.STAIRS, BlockTags.SLABS, BlockTags.WALLS, BlockTags.ANVIL, BlockTags.RAILS, BlockTags.LEAVES, BlockTags.TRAPDOORS, BlockTags.SMALL_FLOWERS, BlockTags.BEDS, BlockTags.FENCES, BlockTags.TALL_FLOWERS, BlockTags.FLOWERS, BlockTags.PIGLIN_REPELLENTS, BlockTags.GOLD_ORES, BlockTags.IRON_ORES, BlockTags.DIAMOND_ORES, BlockTags.REDSTONE_ORES, BlockTags.LAPIS_ORES, BlockTags.COAL_ORES, BlockTags.EMERALD_ORES, BlockTags.COPPER_ORES, BlockTags.NON_FLAMMABLE_WOOD, BlockTags.CANDLES, BlockTags.DIRT, BlockTags.TERRACOTTA, BlockTags.FLOWER_POTS, BlockTags.ENDERMAN_HOLDABLE, BlockTags.ICE, BlockTags.VALID_SPAWN, BlockTags.IMPERMEABLE, BlockTags.UNDERWATER_BONEMEALS, BlockTags.CORAL_BLOCKS, BlockTags.WALL_CORALS, BlockTags.CORAL_PLANTS, BlockTags.CORALS, BlockTags.BAMBOO_PLANTABLE_ON, BlockTags.STANDING_SIGNS, BlockTags.WALL_SIGNS, BlockTags.SIGNS, BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WITHER_SUMMON_BASE_BLOCKS, BlockTags.BEEHIVES, BlockTags.CROPS, BlockTags.BEE_GROWABLES, BlockTags.PORTALS, BlockTags.FIRE, BlockTags.NYLIUM, BlockTags.WART_BLOCKS, BlockTags.BEACON_BASE_BLOCKS, BlockTags.SOUL_SPEED_BLOCKS, BlockTags.WALL_POST_OVERRIDE, BlockTags.CLIMBABLE, BlockTags.FALL_DAMAGE_RESETTING, BlockTags.SHULKER_BOXES, BlockTags.HOGLIN_REPELLENTS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.STRIDER_WARM_BLOCKS, BlockTags.CAMPFIRES, BlockTags.GUARDED_BY_PIGLINS, BlockTags.PREVENT_MOB_SPAWNING_INSIDE, BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.INFINIBURN_OVERWORLD, BlockTags.INFINIBURN_NETHER, BlockTags.INFINIBURN_END, BlockTags.BASE_STONE_OVERWORLD, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES, BlockTags.BASE_STONE_NETHER, BlockTags.CANDLE_CAKES, BlockTags.CAULDRONS, BlockTags.CRYSTAL_SOUND_BLOCKS, BlockTags.INSIDE_STEP_SOUND_BLOCKS, BlockTags.OCCLUDES_VIBRATION_SIGNALS, BlockTags.DRIPSTONE_REPLACEABLE, BlockTags.CAVE_VINES, BlockTags.MOSS_REPLACEABLE, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.AZALEA_ROOT_REPLACEABLE, BlockTags.SMALL_DRIPLEAF_PLACEABLE, BlockTags.BIG_DRIPLEAF_PLACEABLE, BlockTags.SNOW, BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_HOE, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.NEEDS_DIAMOND_TOOL, BlockTags.NEEDS_IRON_TOOL, BlockTags.NEEDS_STONE_TOOL, BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS, BlockTags.ANIMALS_SPAWNABLE_ON, BlockTags.AXOLOTLS_SPAWNABLE_ON, BlockTags.GOATS_SPAWNABLE_ON, BlockTags.MOOSHROOMS_SPAWNABLE_ON, BlockTags.PARROTS_SPAWNABLE_ON, BlockTags.POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN, BlockTags.RABBITS_SPAWNABLE_ON, BlockTags.FOXES_SPAWNABLE_ON, BlockTags.WOLVES_SPAWNABLE_ON, BlockTags.AZALEA_GROWS_ON, BlockTags.REPLACEABLE_PLANTS);
        
        public ModBlockTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {

            for(TagKey<Block> tagKey : tags){

                for(Block block : MY_BLOCKS){

                    if(block instanceof SlabBlock) {

                        Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(((SlabBlock) block).getBaseBlock()));

                        if(!base.defaultBlockState().isAir()) {

                            if (base.defaultBlockState().is(tagKey)) {
                                this.tag(tagKey).add(block);
                            }

                        }

                    }
                }

            }

        }
    }

}
