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
    //public static String []ROAD_BLOCK_IDS={"","","","",""};
    public static List<String> ROAD_BLOCK_IDS = new ArrayList<>();

    public static void initRoadModifierUUIDList(){
        return;
    }

    public static void initRoadModifierList(){
        int size = Config.getUUIDSize();
        for (int i = 1; i<size; i++) {
            ROAD_MODIFIER_LIST.add(new AttributeModifier(Config.getUUID(i), "Road "+i+" Speed Modifier", Config.getAmount(i), Config.getOperation(i)));
        }
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
        int size = ROAD_BLOCK_IDS.size();
        for(int i = 0; i<size; i++) {
            if(BlockUnderEntity.equals(ROAD_BLOCK_IDS.get(i))) {
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(ROAD_MODIFIER_LIST.get(i));
                break;
            }
        }
    }
    //To do figure out how to add roadblocks in the config in a non hard coded way
    public static void addRoadBlocksFromConfig(){
        for (int i = 1; i < Config.getBlockIDSize(); i++) {
            ROAD_BLOCKS.put("Block{" + Config.getBlockID(i) + "}", ROAD_MODIFIER_LIST.get(i-1).getID());
            ROAD_BLOCK_IDS.add(i-1, "Block{" + Config.getBlockID(i) + "}");
        }
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