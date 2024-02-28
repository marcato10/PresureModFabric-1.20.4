package com.pressurerisk.mixin;

import com.pressurerisk.core.NightPressureManager;
import com.pressurerisk.utils.ModConstants;
import net.minecraft.entity.Entity;
import net.minecraft.network.encryption.PublicPlayerSession;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin {
	@Shadow public abstract ServerWorld getServerWorld();

	@Shadow public abstract ServerStatHandler getStatHandler();

	@Shadow @Nullable public abstract PublicPlayerSession getSession();

	@Shadow public abstract Entity getCameraEntity();

	@Inject(at = @At("TAIL"), method = "addExperience")
	private void init(int experience,CallbackInfo info) {
		NightPressureManager nightPressureManager = NightPressureManager.getServerWorldState(this.getServerWorld());
		if(nightPressureManager.getNightState().equals(ModConstants.NIGHT_STATE.RUNNING)){
			nightPressureManager.addPlayerScore(this.getCameraEntity().getUuid(),experience);

		}
	}
}