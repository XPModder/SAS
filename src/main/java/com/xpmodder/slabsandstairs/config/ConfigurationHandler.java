package com.xpmodder.slabsandstairs.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigurationHandler {

    public static class Common
    {
        private static final boolean defaultGetCombinedBlock = true;
        public final ForgeConfigSpec.ConfigValue<Boolean> canGetCombinedBlock;


        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("general");
            this.canGetCombinedBlock = builder.comment("When set to true, you can get a combined block in item form by holding down the placement mode key while breaking the block. The block can then be placed elsewhere. Default: true")
                    .define("possible to get Combined Block as Item", defaultGetCombinedBlock);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static //constructor
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }

}
