package com.pressurerisk.events;

import com.pressurerisk.core.NightPressureManager;
import com.pressurerisk.network.ModServerNetwork;
import com.pressurerisk.utils.ModConstants;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import static com.pressurerisk.network.ModServerNetwork.PRESSURE_XP;

public class ServerPressureNightCallback implements ServerPlayConnectionEvents.Join, ServerLivingEntityEvents.AfterDeath, ServerTickEvents.EndTick, ServerEntityCombatEvents.AfterKilledOtherEntity{
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState(handler.getPlayer().getServerWorld());
        server.execute(()-> nightPressureManager.addPlayerScore(handler.getPlayer().getUuid(),0));
    }

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity.isPlayer()){
            NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState((ServerWorld) entity.getWorld());
            nightPressureManager.resetPlayerScore(entity.getUuid());
        }
    }

    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState(world);
        if(!nightPressureManager.getNightState().equals(ModConstants.NIGHT_STATE.RUNNING)){
            return;
        }
        if(killedEntity.canTarget((LivingEntity) entity) && (((MobEntity)killedEntity).getVisibilityCache().canSee(entity))){
            world.getServer().execute(()-> nightPressureManager.addPlayerScore(entity.getUuid(),killedEntity.getXpToDrop()));
            PacketByteBuf packet = PacketByteBufs.create();
            ServerPlayNetworking.send((ServerPlayerEntity) entity, PRESSURE_XP,packet.writeInt(nightPressureManager.getPlayerBonus().getPlayerScoreByUUID(entity.getUuid())));
        }
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState(world);
        System.out.println(nightPressureManager.getTotemData());
        if(nightPressureManager.getTotemData().blockPos().isPresent()){
            if(world.isNight() && nightPressureManager.getNightState().equals(ModConstants.NIGHT_STATE.IDLE)){
                nightPressureManager.setNightState(ModConstants.NIGHT_STATE.RUNNING);
                world.getServer().execute(()->{
                    for(ServerPlayerEntity serverPlayer : world.getPlayers()){
                        ServerPlayNetworking.send(serverPlayer, ModServerNetwork.PRESSURE_EVENT_ALERT,PacketByteBufs.create().writeString("RUNNING"));
                    }
                });
                return;
            }
            if(world.isDay() && nightPressureManager.getNightState().equals(ModConstants.NIGHT_STATE.RUNNING)){
                nightPressureManager.setNightState(ModConstants.NIGHT_STATE.IDLE);
                world.getServer().execute(()-> nightPressureManager.claimPlayersScore(server.getOverworld(),NightPressureManager.getPressureLevel(server.getOverworld())));
            }
        }
    }
}
