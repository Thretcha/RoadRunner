package thretcha.roadrunner;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class RoadHandler {
    //Road Block modifier uuids
    public static List<UUID> ROAD_MODIFIER_UUID_LIST = new ArrayList<>();
    //list of all the defined Road Block modifiers from the config
    public static List<AttributeModifier> ROAD_MODIFIER_LIST = new ArrayList<>();
    //all the Road Block read by the config
    public static Map<String,UUID> ROAD_BLOCKS = new HashMap<>();
    //stores the Road Blocks in the correct order so their modifier uuids are easier to get
    public static String []ROAD_BLOCK_IDS={"","","","",""};

    public static void initRoadModifierUUIDList(){
        /*
        ROAD_MODIFIER_UUID_LIST.add(UUID.fromString("8753f773-a718-4ea8-98db-4318be0159fe"));
        ROAD_MODIFIER_UUID_LIST.add(UUID.fromString("b8b8b5b0-a92a-4721-a9ec-5b83b1950331"));
        ROAD_MODIFIER_UUID_LIST.add(UUID.fromString("8d560517-f314-47e5-8420-1b13b4740034"));
        ROAD_MODIFIER_UUID_LIST.add(UUID.fromString("6de83cb8-883b-4d6b-915c-b90e2fdfe154"));
        ROAD_MODIFIER_UUID_LIST.add(UUID.fromString("48cf6bac-5b95-44b7-b8b0-8f3e491b2c2f"));
        */
    }

    public static void initRoadModifierList(){
        int size = Config.getUUIDSize();
        for (int i = 1; i<size; i++) {
            ROAD_MODIFIER_LIST.add(new AttributeModifier(Config.getUUID(i), "Road "+i+" Speed Modifier", Config.getAmount(i), Config.getOperation(i)));
        }
        /*
        ROAD_MODIFIER_LIST.add(new AttributeModifier(ROAD_MODIFIER_UUID_LIST.get(0), "Road 1 Speed Modifier", Config.BLOCK_AMOUNT_1, Config.BLOCK_OPERATION_1));
        ROAD_MODIFIER_LIST.add(new AttributeModifier(ROAD_MODIFIER_UUID_LIST.get(1), "Road 2 Speed Modifier", Config.BLOCK_AMOUNT_2, Config.BLOCK_OPERATION_2));
        ROAD_MODIFIER_LIST.add(new AttributeModifier(ROAD_MODIFIER_UUID_LIST.get(2), "Road 3 Speed Modifier", Config.BLOCK_AMOUNT_3, Config.BLOCK_OPERATION_3));
        ROAD_MODIFIER_LIST.add(new AttributeModifier(ROAD_MODIFIER_UUID_LIST.get(3), "Road 4 Speed Modifier", Config.BLOCK_AMOUNT_4, Config.BLOCK_OPERATION_4));
        ROAD_MODIFIER_LIST.add(new AttributeModifier(ROAD_MODIFIER_UUID_LIST.get(4), "Road 5 Speed Modifier", Config.BLOCK_AMOUNT_5, Config.BLOCK_OPERATION_5));
        */
    }
    private boolean hasRoadModifier(EntityLivingBase entity){

        Set<AttributeModifier> modifierSet = Sets.<AttributeModifier>newHashSet();
        modifierSet= (Set)entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers();
        Boolean ret = false;
        for (AttributeModifier modifier : modifierSet) {
            if(ROAD_MODIFIER_LIST.contains(modifier)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    //returns null when the entity doesn't contain a road modifier
    private AttributeModifier getRoadModifier(EntityLivingBase entity){
        entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers();
        Set<AttributeModifier> modifierSet = Sets.<AttributeModifier>newHashSet();
        modifierSet= (Set)entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers();
        AttributeModifier ret = null;
        for (AttributeModifier modifier : modifierSet) {
            if(ROAD_MODIFIER_LIST.contains(modifier)) {
                ret=modifier;
                break;
            }
        }
        return ret;
    }

    @SubscribeEvent
    public void livingEntityOnRoad(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = (EntityLivingBase)event.getEntity();

        if (!entity.getEntityWorld().isRemote)
        {
            BlockPos position = ((EntityLivingBase) event.getEntity()).getPosition().down();
            String BlockUnderEntity = entity.getEntityWorld().getBlockState(position).getBlock().toString();


            //entity walked from non Road Block to a Road Block !livingEntitiesOnRoad.containsKey(entity)
            if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&!hasRoadModifier(entity))
            {
                //add new Road Block modifier
                addCorrectRoadModifier(entity, BlockUnderEntity);
            }
            //if the entity walked from a Road Block to a Road Block of the same Type
            //do nothing
           else if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&getRoadModifier(entity).getID().equals(ROAD_BLOCKS.get(BlockUnderEntity)))
            {

            }
            //entity walked from a Road Block to a non Road Block
            else if(!ROAD_BLOCKS.containsKey(BlockUnderEntity)&&hasRoadModifier(entity))
            {
                //remove Road Block modifier
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(getRoadModifier(entity).getID());
            }

            //entity walked from a Road Block to a different type of Road Block
            else if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&ROAD_BLOCKS.get(BlockUnderEntity).equals(getRoadModifier(entity).getID())) {

                //remove old Road Block modifier
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(getRoadModifier(entity).getID());
                //add new Road Block modifier
                addCorrectRoadModifier(entity,BlockUnderEntity);
            }


        }
    }
    //To do find a better way to do this that isn't soo painful to look at.
    private void addCorrectRoadModifier(EntityLivingBase entity,String BlockUnderEntity){
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[0]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(ROAD_MODIFIER_LIST.get(0));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[1]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(ROAD_MODIFIER_LIST.get(1));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[2]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(ROAD_MODIFIER_LIST.get(2));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[3]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(ROAD_MODIFIER_LIST.get(3));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[4]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(ROAD_MODIFIER_LIST.get(4));
        }
    }
    //To do figure out how to add roadblocks in the config in a non hard coded way
    public static void addRoadBlocksFromConfig(){
        for (int i = 1; i < Config.getBlockIDSize(); i++) {
            ROAD_BLOCKS.put("Block{" + Config.getBlockID(i) + "}", ROAD_MODIFIER_LIST.get(i-1).getID());
            ROAD_BLOCK_IDS[i-1] = "Block{" + Config.getBlockID(i) + "}";
        }
        /*
        if(Config.BLOCK_ID_1!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_1 + "}", ROAD_MODIFIER_LIST.get(0).getID());
            ROAD_BLOCK_IDS[0]="Block{" + Config.BLOCK_ID_1 + "}";
        }
        if(Config.BLOCK_ID_2!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_2 + "}", ROAD_MODIFIER_LIST.get(1).getID());
            ROAD_BLOCK_IDS[1]="Block{" + Config.BLOCK_ID_2 + "}";
        }
        if(Config.BLOCK_ID_3!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_3 + "}", ROAD_MODIFIER_LIST.get(2).getID());
            ROAD_BLOCK_IDS[2]="Block{" + Config.BLOCK_ID_3 + "}";
        }
        if(Config.BLOCK_ID_4!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_4 + "}", ROAD_MODIFIER_LIST.get(3).getID());
            ROAD_BLOCK_IDS[3]="Block{" + Config.BLOCK_ID_4 + "}";
        }
        if(Config.BLOCK_ID_5!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_5 + "}", ROAD_MODIFIER_LIST.get(4).getID());
            ROAD_BLOCK_IDS[4]="Block{" + Config.BLOCK_ID_5 + "}";
        }
        */
    }
}

/*
   entity.addTag()
   entity.getTags()
   instead of saving the entities


   entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers()
   to get the current active modifiers on the entity and don't have to save any data at all


   -upcoming release changes:
    #1.0.1
    *change the way the modifiers are saved and checked
    *#1.1.0
    *give the config file a version and implement auto update for the config
    *ingame gui and experimenting with changing the config without restart of the world or restart of minecraft
    *look for a dynamic way of adding road blocks(where do you get uuids from ?
    *a way to see the available road blocks ingame(outside of the mod config) maybe even notify players when the run on a road block for the first time
    *
 */