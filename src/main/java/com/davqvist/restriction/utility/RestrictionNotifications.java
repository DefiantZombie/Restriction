package com.davqvist.restriction.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;

public class RestrictionNotifications{

    public static String getNotificationSeeSky( boolean reverse ){
        return "Block must " + ( reverse ? "not " : "" ) + "see the sky.";
    }

    public static String getNotificationClosedRoom( boolean reverse, int size, String blockString, boolean ignoreMeta, int meta, int amount ){
        String blockname = UtilityHelper.getBlockName( blockString, ignoreMeta, meta );
        return "Block must " + ( reverse ? "not " : "" ) + "be in closed room" + ( size > 0 ? " with a size of at least " + size + " blocks" : "" ) + ( blockString != null ? ( " made out of at least " + amount + " " + blockname ) : "" ) + ".";
    }

    public static String getNotificationDimension( boolean reverse, int id ){
        return "Block must " + ( reverse ? "not " : "" ) + "be in dimension " + DimensionType.getById( id ).getName() + " (" + id + ").";
    }

    public static String getNotificationNearbyBlocks( boolean reverse, int size, String blockString, boolean ignoreMeta, int meta, int amount ){
        String blockname = UtilityHelper.getBlockName( blockString, ignoreMeta, meta );
        return "Block must " + ( reverse ? "not " : "" ) + "be surrounded by at least " + amount + " " + blockname + " in a range of " + Math.min( 5, size ) + " blocks.";
    }

    public static String getNotificationExperience( boolean reverse, int amount ){
        return "You must " + ( reverse ? "not " : "" ) + "have at least " + amount + " levels of experience.";
    }

    public static String getNotificationMinHeight( boolean reverse, int amount ){
        return "Block must be at a " + ( reverse ? "maximum" : "minimum" ) + " height of " + amount;
    }
}
