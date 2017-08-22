package thretcha.roadrunner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class RoadHandler {
    //road modifier uuids
    public final static UUID[] ROAD_MODIFIER_UUIDS = {UUID.fromString("8753f773-a718-4ea8-98db-4318be0159fe"),UUID.fromString("b8b8b5b0-a92a-4721-a9ec-5b83b1950331"),UUID.fromString("8d560517-f314-47e5-8420-1b13b4740034"),UUID.fromString("6de83cb8-883b-4d6b-915c-b90e2fdfe154"),UUID.fromString("48cf6bac-5b95-44b7-b8b0-8f3e491b2c2f")};
    //all the Road Block read by the config
    public static Map<String,UUID> ROAD_BLOCKS = new HashMap<>();
    //stores the Road Blocks in the correct order so their modifier uuids are easier to get
    public static String []ROAD_BLOCK_IDS={"","","","",""};
    //all livingEntities that stood on a Road Block during their last LivingUpdateEvent with the UUID of their current modifier
    private static Map<EntityLivingBase, UUID> livingEntitiesOnRoad = new HashMap<>();

    @SubscribeEvent
    public void livingEntityOnRoad(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = (EntityLivingBase)event.getEntity();
        if (!entity.getEntityWorld().isRemote)
        {
            BlockPos position = ((EntityLivingBase) event.getEntity()).getPosition().down();
            String BlockUnderEntity = entity.getEntityWorld().getBlockState(position).getBlock().toString();

            //entity walked from a Road Block to a non Road Block
            if(!ROAD_BLOCKS.containsKey(BlockUnderEntity)&&livingEntitiesOnRoad.containsKey(entity))
            {
                //System.out.println("Entity no longer on Road\n");
                //remove road modifier
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(livingEntitiesOnRoad.get(entity));
                livingEntitiesOnRoad.remove(entity);
            }
            //entity walked from a Road Block to a different type of Road Block
            else if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&livingEntitiesOnRoad.containsKey(entity)&&!ROAD_BLOCKS.get(BlockUnderEntity).equals(livingEntitiesOnRoad.get(entity)))
                {
                 //System.out.println("Entity switched Road\n");
                 //remove old road modifier
                  entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(livingEntitiesOnRoad.get(entity));
                 livingEntitiesOnRoad.remove(entity);
                 //add new road modifier
                 livingEntitiesOnRoad.put(entity,ROAD_BLOCKS.get(BlockUnderEntity));
                  addCorrectRoadModifier(entity, BlockUnderEntity);
             }
            //entity walked from non Road Block to a Road Block
            else if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&!livingEntitiesOnRoad.containsKey(entity))
            {
                //System.out.println("new Entity on Road\n");
                livingEntitiesOnRoad.put(entity,ROAD_BLOCKS.get(BlockUnderEntity));
                addCorrectRoadModifier(entity, BlockUnderEntity);
            }
            //if the entity walked from a Road Block to a Road Block of the same Type
            //do nothing
        }
    }
    //To do find a better way to do this that isn't soo painful to look at.
    public void addCorrectRoadModifier(EntityLivingBase entity,String BlockUnderEntity){
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[0]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(ROAD_MODIFIER_UUIDS[0], "Road 1 Speed Modifier", Config.BLOCK_AMOUNT_1, Config.BLOCK_OPERATION_1));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[1]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(ROAD_MODIFIER_UUIDS[1], "Road 2 Speed Modifier", Config.BLOCK_AMOUNT_2, Config.BLOCK_OPERATION_2));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[2]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(ROAD_MODIFIER_UUIDS[2], "Road 3 Speed Modifier", Config.BLOCK_AMOUNT_3, Config.BLOCK_OPERATION_3));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[3]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(ROAD_MODIFIER_UUIDS[3], "Road 4 Speed Modifier", Config.BLOCK_AMOUNT_4, Config.BLOCK_OPERATION_4));
        }
        if(BlockUnderEntity.equals(ROAD_BLOCK_IDS[4]))
        {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(ROAD_MODIFIER_UUIDS[4], "Road 5 Speed Modifier", Config.BLOCK_AMOUNT_5, Config.BLOCK_OPERATION_5));
        }

    }
    //To do figure out how to add roadblocks in the config in a non hard coded way
    public static void addRoadBlocksFromConfig(){
        if(Config.BLOCK_ID_1!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_1 + "}", ROAD_MODIFIER_UUIDS[0]);
            ROAD_BLOCK_IDS[0]="Block{" + Config.BLOCK_ID_1 + "}";
        }
        if(Config.BLOCK_ID_2!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_2 + "}", ROAD_MODIFIER_UUIDS[1]);
            ROAD_BLOCK_IDS[1]="Block{" + Config.BLOCK_ID_2 + "}";
        }
        if(Config.BLOCK_ID_3!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_3 + "}", ROAD_MODIFIER_UUIDS[2]);
            ROAD_BLOCK_IDS[2]="Block{" + Config.BLOCK_ID_3 + "}";
        }
        if(Config.BLOCK_ID_4!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_4 + "}", ROAD_MODIFIER_UUIDS[3]);
            ROAD_BLOCK_IDS[3]="Block{" + Config.BLOCK_ID_4 + "}";
        }
        if(Config.BLOCK_ID_5!="") {
            ROAD_BLOCKS.put("Block{" + Config.BLOCK_ID_5 + "}", ROAD_MODIFIER_UUIDS[4]);
            ROAD_BLOCK_IDS[4]="Block{" + Config.BLOCK_ID_5 + "}";
        }
    }
}