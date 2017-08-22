package thretcha.roadrunner;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import thretcha.roadrunner.proxy.CommonProxy;

public class Config {
    private static final String CATEGORY_ROAD_BLOCKS = "RoadBlocks";
    private static final String SUBCATEGORY_BLOCK_1 = "RoadBlocks.Block1";
    private static final String SUBCATEGORY_BLOCK_2 = "RoadBlocks.Block2";
    private static final String SUBCATEGORY_BLOCK_3 = "RoadBlocks.Block3";
    private static final String SUBCATEGORY_BLOCK_4 = "RoadBlocks.Block4";
    private static final String SUBCATEGORY_BLOCK_5 = "RoadBlocks.Block5";
    private static final String CATEGORY_SHOES = "shoes";

    // This values below you can access elsewhere in your mod:
    //public static boolean isThisAGoodTutorial = true;
    //public static String yourRealName = "Steve";

    //block id/name
    //amount
    //operation
    public static String BLOCK_ID_1 ="minecraft:stonebrick";
    public static double BLOCK_AMOUNT_1 = 0.2;
    public static int BLOCK_OPERATION_1 = 2;

    public static String BLOCK_ID_2 ="minecraft:grass_path";
    public static double BLOCK_AMOUNT_2 = 0.1;
    public static int BLOCK_OPERATION_2 = 2;

    public static String BLOCK_ID_3="";
    public static double BLOCK_AMOUNT_3=0;
    public static int BLOCK_OPERATION_3=0;

    public static String BLOCK_ID_4="";
    public static double BLOCK_AMOUNT_4=0;
    public static int BLOCK_OPERATION_4=0;

    public static String BLOCK_ID_5="";
    public static double BLOCK_AMOUNT_5=0;
    public static int BLOCK_OPERATION_5=0;

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
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

    private static void initRoadBlockConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_ROAD_BLOCKS, "MOVEMENT_SPEED Attribute is used for more Info about Modifier Amounts and Operations:\nhttps://minecraft.gamepedia.com/Attribute");
        cfg.addCustomCategoryComment(SUBCATEGORY_BLOCK_1, "Road Block 1");
        cfg.addCustomCategoryComment(SUBCATEGORY_BLOCK_2, "Road Block 2");
        cfg.addCustomCategoryComment(SUBCATEGORY_BLOCK_3, "Road Block 3");
        cfg.addCustomCategoryComment(SUBCATEGORY_BLOCK_4, "Road Block 4");
        cfg.addCustomCategoryComment(SUBCATEGORY_BLOCK_5, "Road Block 5");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.

        BLOCK_ID_1 = cfg.getString("Block ID",SUBCATEGORY_BLOCK_1,BLOCK_ID_1,"ID of Road Block");
        BLOCK_AMOUNT_1 = (double)cfg.getFloat("Modifier Amount",SUBCATEGORY_BLOCK_1,(float)BLOCK_AMOUNT_1,-1000.0f,1000.0f,"Amound of Speed Modification");
        BLOCK_OPERATION_1 = cfg.getInt("Modifier Operation",SUBCATEGORY_BLOCK_1,BLOCK_OPERATION_1,0,2,"Operation of Speed Modification");

        BLOCK_ID_2 = cfg.getString("Block ID",SUBCATEGORY_BLOCK_2,BLOCK_ID_2,"ID of Road Block");
        BLOCK_AMOUNT_2 = (double)cfg.getFloat("Modifier Amount",SUBCATEGORY_BLOCK_2,(float)BLOCK_AMOUNT_2,-1000.0f,1000.0f,"Amound of Speed Modification");
        BLOCK_OPERATION_2 = cfg.getInt("Modifier Operation",SUBCATEGORY_BLOCK_2,BLOCK_OPERATION_2,0,2,"Operation of Speed Modification");

        BLOCK_ID_3 = cfg.getString("Block ID",SUBCATEGORY_BLOCK_3,BLOCK_ID_3,"ID of Road Block");
        BLOCK_AMOUNT_3 = (double)cfg.getFloat("Modifier Amount",SUBCATEGORY_BLOCK_3,(float)BLOCK_AMOUNT_3,-1000.0f,1000.0f,"Amound of Speed Modification");
        BLOCK_OPERATION_3 = cfg.getInt("Modifier Operation",SUBCATEGORY_BLOCK_3,BLOCK_OPERATION_3,0,2,"Operation of Speed Modification");

        BLOCK_ID_4 = cfg.getString("Block ID",SUBCATEGORY_BLOCK_4,BLOCK_ID_4,"ID of Road Block");
        BLOCK_AMOUNT_4 = (double)cfg.getFloat("Modifier Amount",SUBCATEGORY_BLOCK_4,(float)BLOCK_AMOUNT_4,-1000.0f,1000.0f,"Amound of Speed Modification");
        BLOCK_OPERATION_4 = cfg.getInt("Modifier Operation",SUBCATEGORY_BLOCK_4,BLOCK_OPERATION_4,0,2,"Operation of Speed Modification");

        BLOCK_ID_5 = cfg.getString("Block ID",SUBCATEGORY_BLOCK_5,BLOCK_ID_5,"ID of Road Block");
        BLOCK_AMOUNT_5 = (double)cfg.getFloat("Modifier Amount",SUBCATEGORY_BLOCK_5,(float)BLOCK_AMOUNT_5,-1000.0f,1000.0f,"Amound of Speed Modification");
        BLOCK_OPERATION_5 = cfg.getInt("Modifier Operation",SUBCATEGORY_BLOCK_5,BLOCK_OPERATION_5,0,2,"Operation of Speed Modification");
    }

    private static void initShoeConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_SHOES, "Shoe configuration tbd");

    }
}
