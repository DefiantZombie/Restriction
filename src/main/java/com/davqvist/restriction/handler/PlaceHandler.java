package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.utility.RestrictionHelper;
import com.davqvist.restriction.utility.RestrictionNotifications;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlaceHandler{

    @SubscribeEvent
    public void onPlace( BlockEvent.PlaceEvent event ){
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        String message = "";
        if( player != null && !player.isCreative() && !player.world.isRemote && !event.isCanceled() && pos != null ){
            IBlockState state = event.getPlacedBlock();
            if( state != null ){
                for( RestrictionReader.RestrictionBlock block : Restriction.proxy.rr.root.entries ){
                    if( ( block.ignoreMeta && Block.isEqualTo( state.getBlock(), Block.getBlockFromName( block.block ) ) || state == Block.getBlockFromName( block.block ).getStateFromMeta( block.meta ) ) ){
                        for( RestrictionReader.RestrictionDesciptor desc : block.restrictions ){
                            if( desc.type == RestrictionReader.RestrictionType.SEESKY ){
                                if( RestrictionHelper.canSeeSky( pos, world ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationSeeSky( desc.reverse );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.CLOSEDROOM ){
                                if( RestrictionHelper.isInRoom( pos, world, ( desc.size == null ? 0 : desc.size ), desc.block, desc.ignoreMeta, desc.meta, ( desc.amount == null ? 0 : desc.amount ) ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationClosedRoom( desc.reverse, ( desc.size == null ? 0 : desc.size ), desc.block, desc.ignoreMeta, desc.meta, ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.DIMENSION ){
                                if( RestrictionHelper.isInDimension( world, desc.id ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationDimension( desc.reverse, desc.id );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.NEARBYBLOCKS ){
                                if( RestrictionHelper.isNearby( pos, world, ( desc.size == null ? 0 : desc.size ), desc.block, desc.ignoreMeta, desc.meta, ( desc.amount == null ? 0 : desc.amount ) ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationNearbyBlocks( desc.reverse, ( desc.size == null ? 0 : desc.size ), desc.block, desc.ignoreMeta, desc.meta, ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.EXPERIENCE ){
                                if( RestrictionHelper.hasLevels( player, desc.amount ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationExperience( desc.reverse, ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.MINHEIGHT ){
                                if( RestrictionHelper.hasMinHeight( pos, desc.amount ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationMinHeight( desc.reverse, ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                        }
                    }
                }
            }
        }
        if( event.isCanceled() ){
            event.getPlayer().sendStatusMessage( new TextComponentString( message ), true );
        }
    }

    private void cancelPlace( BlockEvent.PlaceEvent event ){
        event.setCanceled( true );
    }
}
