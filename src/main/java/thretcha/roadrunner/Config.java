package thretcha.roadrunner;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import thretcha.roadrunner.proxy.CommonProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Config {
    private static List<String> blockIDs = new ArrayList<>();
    private static List<Double> amounts = new ArrayList<>();
    private static List<Integer> operations = new ArrayList<>();
    private static List<UUID> uuids = new ArrayList<>();

    private static String[] baseUUIDs = {
            "8753f773-a718-4ea8-98db-4318be0159fe",
            "b8b8b5b0-a92a-4721-a9ec-5b83b1950331",
            "8d560517-f314-47e5-8420-1b13b4740034",
            "6de83cb8-883b-4d6b-915c-b90e2fdfe154",
            "48cf6bac-5b95-44b7-b8b0-8f3e491b2c2f",
    };

    private static String[] baseIDs = {
            "minecraft:stonebrick",
            "minecraft:grass_path",
            "minecraft:cobblestone",
            "minecraft:stone_stairs",
            "minecraft:concrete",
    };

    public static double[] baseAmounts = {
            0.2,
            0.1,
            0.1,
            0.1,
            0.3,
    };

    public static int[] baseOperations = {
            2,
            2,
            2,
            2,
            2,
    };

    private static final String CATEGORY_ROAD_BLOCKS = "RoadBlocks";
    private static final String CATEGORY_SHOES = "shoes";

    //creates config if it doesn't exist yet and reads the values if it does
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initRoadBlockConfig(cfg);
            initShoeConfig(cfg);
        } catch (Exception e1) {
            RoadRunnerMod.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }
    //currently Road Blocks are hard coded to be 5 at most
    //hopefully I can find a more dynamic way later.
    private static void initRoadBlockConfig(Configuration cfg) {
        int i = 1;

        //TODO: fix this, needed atm to secure index in array == number in config (starts at 1)
        blockIDs.add(null);
        amounts.add(null);
        operations.add(null);
        uuids.add(null);

        while (cfg.hasCategory("RoadBlocks.Block"+i)) {
            blockIDs.add(i, cfg.getString("Block ID", "RoadBlocks.Block" + i, "", "ID of Road Block"));
            amounts.add(i, (double) cfg.getFloat("Modifier Amount", "RoadBlocks.Block" + i, 0.0f, -1000.0f, 1000.0f, "Amound of Speed Modification"));
            operations.add(i, cfg.getInt("Modifier Operation", "RoadBlocks.Block" + i, 2, 0, 2, "Operation of Speed Modification"));
            uuids.add(i, UUID.fromString(cfg.getString("UUID", "RoadBlocks.Block" + i, "", "UUID for Attribute modifier")));

            i++;
        }

        if (i == 1) {
            cfg.addCustomCategoryComment(CATEGORY_ROAD_BLOCKS, "MOVEMENT_SPEED Attribute is used for more Info about Modifier Amounts and Operations:\nhttps://minecraft.gamepedia.com/Attribute");
            cfg.addCustomCategoryComment(CATEGORY_ROAD_BLOCKS, "MOVEMENT_SPEED Attribute is used for more Info about Modifier Amounts and Operations:\nhttps://minecraft.gamepedia.com/Attribute");

            for (int q = 1; q <= 5; q++) {
                cfg.addCustomCategoryComment("RoadBlocks.Block"+q, "Road Block "+q);
            }

            for (int k = 1; k <= 5; k++) {
                blockIDs.add(k, cfg.getString("Block ID", "RoadBlocks.Block"+k, baseIDs[k-1], "ID of Road Block"));
                amounts.add(k, (double)cfg.getFloat("Modifier Amount","RoadBlocks.Block"+k, (float) baseAmounts[k-1],-1000.0f,1000.0f,"Amound of Speed Modification"));
                operations.add(k, cfg.getInt("Modifier Operation","RoadBlocks.Block"+k,baseOperations[k-1],0,2,"Operation of Speed Modification"));
                uuids.add(k, UUID.fromString(cfg.getString("UUID", "RoadBlocks.Block"+k, baseUUIDs[k-1], "UUID for Attribute modifier")));
            }
        }
    }
    //running shoes might be added at a later point
    private static void initShoeConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_SHOES, "Shoe configuration tbd");

    }

    public static String getBlockID(int number) {
        if (number < blockIDs.size()) {
            return blockIDs.get(number);
        } else {
            return null;
        }
    }

    public static double getAmount(int number) {
        if (number < blockIDs.size()) {
            return amounts.get(number);
        } else {
            return 0.0;
        }
    }

    public static int getOperation(int number) {
        if (number < blockIDs.size()) {
            return operations.get(number);
        } else {
            return 2;
        }
    }

    public static UUID getUUID(int number) {
        if (number < uuids.size()) {
            return uuids.get(number);
        } else {
            return null;
        }
    }

    public static int getBlockIDSize() {
        return blockIDs.size();
    }

    public static int getAmountSize() {
        return amounts.size();
    }

    public static int getOperationSize() {
        return operations.size();
    }

    public static int getUUIDSize() {
        return uuids.size();
    }
}
