package com.davqvist.restriction.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UtilityHelper{

    public static String getBlockName( String blockString, boolean ignoreMeta, int meta ){

        if( blockString != null ){
            if( ignoreMeta ){
                return Block.getBlockFromName( blockString ).getLocalizedName();
            } else {
                return new ItemStack( Item.getItemFromBlock( Block.getBlockFromName( blockString ) ), 1, meta ).getDisplayName();
            }
        }
        return null;
    }

    public static Object getBlock( String blockString, boolean ignoreMeta, int meta ){

        if( blockString != null ){
            if( ignoreMeta ){
                return Block.getBlockFromName( blockString );
            } else {
                return Block.getBlockFromName( blockString ).getStateFromMeta( meta );
            }
        }
        return null;
    }

}
