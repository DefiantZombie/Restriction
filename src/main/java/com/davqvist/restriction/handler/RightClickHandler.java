package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Stack;

public class RightClickHandler {

    @SubscribeEvent
    public void onRightClick( RightClickBlock event ){
        EntityPlayer player = event.getEntityPlayer();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        if( player != null && !player.isCreative() && !event.isCanceled() && pos != null ){
            IBlockState state = world.getBlockState( event.getPos() );
            if( state != null ){
                for( RestrictionReader.RestrictionBlock block : Restriction.proxy.rr.root.entries ){
                    if( ( block.ignoreMeta && Block.isEqualTo( state.getBlock(), Block.getBlockFromName( block.block ) ) || state == Block.getBlockFromName( block.block ).getStateFromMeta( block.meta ) ) ){
                        for( RestrictionReader.RestrictionDesciptor desc : block.restrictions ){
                            if( desc.type == RestrictionReader.RestrictionType.SEESKY ){
                                if( canSeeSky( pos, world ) == desc.reverse ) {
                                    cancelRightClick( event, "Block must " + (desc.reverse?"not ":"") + "see the sky.");
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.CLOSEDROOM ){
                                if( desc.size == null ){ desc.size = 0; }
                                if( desc.amount == null ){ desc.amount = 0; }
                                if( desc.block != null ){
                                    if( desc.ignoreMeta ) {
                                        if( isInRoom( pos, world, desc.size, Block.getBlockFromName( desc.block ), desc.amount ) == desc.reverse ){
                                            cancelRightClick( event, "Block must " + (desc.reverse ? "not " : "") + "be in closed room" + (desc.size > 0 ? " with a size of at least " + desc.size + " blocks" : "")
                                                    + (desc.block != null ? " made out of at least " + desc.amount + " " + Block.getBlockFromName( desc.block ).getLocalizedName() : "") + ".");
                                        }
                                    } else{
                                        if( isInRoom( pos, world, desc.size, Block.getBlockFromName( desc.block ).getStateFromMeta( desc.meta ), desc.amount ) == desc.reverse ){
                                            ItemStack temp = new ItemStack( Item.getItemFromBlock( Block.getBlockFromName( desc.block ) ), 1, desc.meta );
                                            cancelRightClick( event, "Block must " + (desc.reverse ? "not " : "") + "be in closed room" + (desc.size > 0 ? " with a size of at least " + desc.size + " blocks" : "")
                                                    + (desc.block != null ? " made out of at least " + desc.amount + " " + temp.getDisplayName() : "") + ".");
                                        }
                                    }
                                } else {
                                    if( isInRoom( pos, world, desc.size ) == desc.reverse ){
                                        cancelRightClick(event, "Block must " + (desc.reverse ? "not " : "") + "be in closed room" + (desc.size > 0 ? " with a size of at least " + desc.size + " blocks." : "."));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void cancelRightClick( RightClickBlock event, String message ){
        event.setUseBlock( Event.Result.DENY );
        event.setCanceled( true );
        if( event.getEntityPlayer().world.isRemote ) {
            event.getEntityPlayer().sendMessage( new TextComponentString( message ) );
        }
    }

    private boolean canSeeSky( BlockPos pos, World world ){
        boolean solid = false;
        boolean reachedtop = false;
        while( !solid && !reachedtop ) {
            pos = pos.up();
            if( world.getHeight() < pos.getY() ){ reachedtop = true; }
            if( world.getBlockState( pos ).getBlock().isBlockSolid( world, pos, EnumFacing.DOWN ) ){ solid = true; }
        }
        return !solid;
    }

    private boolean isInRoom( BlockPos pos, World world, int minSize, Object block, int minBlock ){
        Stack<BlockPos> stack = new Stack<BlockPos>();
        stack.push( pos );
        final int maxSize = 10000;
        final HashSet<BlockPos> addableBlocks = new HashSet<BlockPos>();
        final HashSet<BlockPos> foundBlocks = new HashSet<BlockPos>();

        while( !stack.isEmpty() ) {
            BlockPos stackElement = stack.pop();
            addableBlocks.add( stackElement );
            for( EnumFacing direction : EnumFacing.values() ){
                BlockPos searchNextPosition = stackElement.offset( direction );
                if( !addableBlocks.contains( searchNextPosition ) ) {
                    if( addableBlocks.size() <= maxSize ){
                        if( !world.getBlockState( searchNextPosition ).getBlock().isBlockSolid( world, searchNextPosition, direction.getOpposite() )) {
                            stack.push( searchNextPosition );
                        } else if( block != null && ( ( block instanceof Block && world.getBlockState( searchNextPosition ).getBlock() == block ) || ( block instanceof IBlockState && world.getBlockState( searchNextPosition ) == block ) ) && !foundBlocks.contains( searchNextPosition ) ){
                            foundBlocks.add( searchNextPosition );
                        }
                    }
                    else{ return false; }
                }
            }
        }
        return( addableBlocks.size() > minSize && foundBlocks.size() >= minBlock );
    }

    private boolean isInRoom( BlockPos pos, World world ){
        return isInRoom( pos, world, 0 );
    }

    private boolean isInRoom( BlockPos pos, World world, int minSize ){
        return isInRoom( pos, world, minSize, null, 0 );
    }

    private boolean isInRoom( BlockPos pos, World world, Object block, int minBlock ){
        return isInRoom( pos, world, 0, block, minBlock );
    }
}
