package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.utility.RestrictionHelper;
import com.davqvist.restriction.utility.RestrictionNotifications;
import com.davqvist.restriction.utility.UtilityHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Stack;

public class RightClickHandler{

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
                                if( RestrictionHelper.canSeeSky( pos, world ) == desc.reverse ){
                                    cancelRightClick( event, RestrictionNotifications.getNotificationSeeSky( desc.reverse ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.CLOSEDROOM ){
                                if( RestrictionHelper.isInRoom( pos, world, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) ) == desc.reverse ){
                                    cancelRightClick( event, RestrictionNotifications.getNotificationClosedRoom( desc.reverse, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.DIMENSION ){
                                if( RestrictionHelper.isInDimension( world, desc.id ) == desc.reverse ){
                                    cancelRightClick( event, RestrictionNotifications.getNotificationDimension( desc.reverse, desc.id ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.NEARBYBLOCKS ){
                                if( RestrictionHelper.isNearby( pos, world, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) ) == desc.reverse ){
                                    cancelRightClick( event, RestrictionNotifications.getNotificationNearbyBlocks( desc.reverse, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.EXPERIENCE ){
                                if( RestrictionHelper.hasLevels( player, desc.amount ) == desc.reverse ){
                                    cancelRightClick( event, RestrictionNotifications.getNotificationExperience( desc.reverse, desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.MINHEIGHT ){
                                if( RestrictionHelper.hasMinHeight( pos, desc.amount ) == desc.reverse ){
                                    cancelRightClick( event, RestrictionNotifications.getNotificationMinHeight( desc.reverse, desc.amount ) );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void cancelRightClick( RightClickBlock event, String message ){
        event.setCanceled( true );
        if( event.getEntityPlayer().world.isRemote ){
            event.getEntityPlayer().sendStatusMessage( new TextComponentString( message ), true );
        }
    }
}
