package com.davqvist.restriction.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Stack;

public class RestrictionHelper{

    public static boolean canSeeSky( BlockPos pos, World world ){
        boolean solid = false;
        boolean reachedtop = false;
        while( !solid && !reachedtop ){
            pos = pos.up();
            if( world.getHeight() < pos.getY() ){
                reachedtop = true;
            }
            if( world.getBlockState( pos ).getBlock().isBlockSolid( world, pos, EnumFacing.DOWN ) ){
                solid = true;
            }
        }
        return !solid;
    }

    public static boolean isInRoom( BlockPos pos, World world, int minSize, String blockString, boolean ignoreMeta, int meta, int minAmount ){
        Stack<BlockPos> stack = new Stack<BlockPos>();
        stack.push( pos );
        final int maxSize = 10000;
        final HashSet<BlockPos> addableBlocks = new HashSet<BlockPos>();
        final HashSet<BlockPos> foundBlocks = new HashSet<BlockPos>();

        Object block = UtilityHelper.getBlock( blockString, ignoreMeta, meta );

        while( !stack.isEmpty() ){
            BlockPos stackElement = stack.pop();
            addableBlocks.add( stackElement );
            for( EnumFacing direction : EnumFacing.values() ){
                BlockPos searchNextPosition = stackElement.offset( direction );
                if( !addableBlocks.contains( searchNextPosition ) ){
                    if( addableBlocks.size() <= maxSize ){
                        if( !world.getBlockState( searchNextPosition ).getBlock().isBlockSolid( world, searchNextPosition, direction.getOpposite() ) ){
                            stack.push( searchNextPosition );
                        } else if( block != null && ( ( block instanceof Block && world.getBlockState( searchNextPosition ).getBlock() == block ) || ( block instanceof IBlockState && world.getBlockState( searchNextPosition ) == block ) ) && !foundBlocks.contains( searchNextPosition ) ){
                            foundBlocks.add( searchNextPosition );
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return ( addableBlocks.size() > minSize && foundBlocks.size() >= minAmount );
    }

    public static boolean isNearby( BlockPos pos, World world, int range, String blockString, boolean ignoreMeta, int meta, int minAmount ){
        Object block = UtilityHelper.getBlock( blockString, ignoreMeta, meta );

        int actualRange = Math.min( 5, range );
        int count = 0;
        for( int x = -actualRange; x <= actualRange; x++ ){
            for( int y = -actualRange; y <= actualRange; y++ ){
                for( int z = -actualRange; z <= actualRange; z++ ){
                    if( block != null && ( block instanceof Block && Block.isEqualTo( world.getBlockState( new BlockPos( pos.getX() + x, pos.getY() + y, pos.getZ() + z ) ).getBlock(), (Block) block ) || ( block instanceof IBlockState && world.getBlockState( new BlockPos( pos.getX() + x, pos.getY() + y, pos.getZ() + z ) ) == block ) ) ){
                        count++;
                    }
                    if( count >= minAmount ){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasLevels( EntityPlayer player, int minAmount ){
        if( minAmount > 0 ){
            return ( player != null && player.experienceLevel >= minAmount );
        }
        return true;
    }

    public static boolean hasMinHeight( BlockPos pos, int minAmount ){
        if( minAmount >= 0 ){
            return ( pos.getY() >= minAmount );
        }
        return true;
    }

    public static boolean isInDimension( World world, int dimId ){
        return ( world.provider.getDimension() == dimId );
    }
}
