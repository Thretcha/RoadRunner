package thretcha.roadrunner;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class RoadHandler {

    //public final static UUID ROAD_MODIFIER_UUID = UUID.fromString("8753f773-a718-4ea8-98db-4318be0159fe");
    public final static UUID[] ROAD_MODIFIER_UUIDS = {UUID.fromString("8753f773-a718-4ea8-98db-4318be0159fe"),UUID.fromString("b8b8b5b0-a92a-4721-a9ec-5b83b1950331"),UUID.fromString("8d560517-f314-47e5-8420-1b13b4740034"),UUID.fromString("6de83cb8-883b-4d6b-915c-b90e2fdfe154"),UUID.fromString("48cf6bac-5b95-44b7-b8b0-8f3e491b2c2f")};
   // public final static List<UUID> ROAD_MODIFIER_UUIDS = new ArrayList<>();
    public final static double ROAD_MODIFIER_AMOUNT =0.2;
    public final static int ROAD_MODIFIER_OPERATION = 2;
    //public static final ArrayList<String> ROAD_BLOCKS = new ArrayList<String>();
    public static Map<String,UUID> ROAD_BLOCKS = new HashMap<>();
    public static String []ROAD_BLOCK_IDS={"","","","",""};

    //check which Entities left the road and remove their ROAD_MODIFIER
    //private static ArrayList<EntityLivingBase> livingEntitiesOnRoad = new ArrayList<EntityLivingBase>();
    private static Map<EntityLivingBase, UUID> livingEntitiesOnRoad = new HashMap<>();

    @SubscribeEvent
    public void livingEntityOnRoad(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = (EntityLivingBase)event.getEntity();
        if (!entity.getEntityWorld().isRemote)
        {
            /*
                BlockPos position = ((EntityLivingBase) event.getEntity()).getPosition().down();

                if(ROAD_BLOCKS.contains(entity.getEntityWorld().getBlockState(position).getBlock().toString())&&!livingEntitiesOnRoad.contains(entity))
                {
                        livingEntitiesOnRoad.add(entity);
                        entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(ROAD_MODIFIER_UUID, "Road Speed Modifier", ROAD_MODIFIER_AMOUNT, ROAD_MODIFIER_OPERATION));


                }
                else
                {
                    if(livingEntitiesOnRoad.contains(entity))
                    {
                        livingEntitiesOnRoad.remove(entity);
                        entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(ROAD_MODIFIER_UUID);
                    }
                }*/

            BlockPos position = ((EntityLivingBase) event.getEntity()).getPosition().down();
            String BlockUnderEntity = entity.getEntityWorld().getBlockState(position).getBlock().toString();

            //entity no longer on a road block
            if(!ROAD_BLOCKS.containsKey(BlockUnderEntity)&&livingEntitiesOnRoad.containsKey(entity))
            {
                System.out.println("Entity no longer on Road\n");
                //remove road modifier
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(livingEntitiesOnRoad.get(entity));
                livingEntitiesOnRoad.remove(entity);
            }
            //entity walked from on road block to a different one
            else
            if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&livingEntitiesOnRoad.containsKey(entity)&&!ROAD_BLOCKS.get(BlockUnderEntity).equals(livingEntitiesOnRoad.get(entity)))
            {
                System.out.println("Entity switched Road\n");
                //remove old road modifier
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(livingEntitiesOnRoad.get(entity));
                livingEntitiesOnRoad.remove(entity);
                //add new road modifier
                livingEntitiesOnRoad.put(entity,ROAD_BLOCKS.get(BlockUnderEntity));
                addCorrectRoadModifier(entity, BlockUnderEntity);
            }
            //entity walked from non road block to a road block
            else
                if(ROAD_BLOCKS.containsKey(BlockUnderEntity)&&!livingEntitiesOnRoad.containsKey(entity))
                {
                    System.out.println("new Entity on Road\n");
                    livingEntitiesOnRoad.put(entity,ROAD_BLOCKS.get(BlockUnderEntity));
                    addCorrectRoadModifier(entity, BlockUnderEntity);
                }
        }
    }

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
    ////ROAD_BLOCKS.add("Block{minecraft:stonebrick}");
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