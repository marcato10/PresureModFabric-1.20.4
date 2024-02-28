package com.pressurerisk.mixin.entity;

import com.pressurerisk.core.NightPressureManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class PressureSkeletonBehavior extends MobEntity {
    protected PressureSkeletonBehavior(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement",at = @At("HEAD"))
    private void changeToMelee(CallbackInfo callbackInfo){
        if(!this.getEntityWorld().isClient && this.getTarget()!=null){
            if(NightPressureManager.getPressureLevel((ServerWorld) this.getEntityWorld()) == 2){
                if(this.squaredDistanceTo(this.getTarget()) < 15){
                    if(this.getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.BOW))
                            this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.STONE_SWORD));
                }
                else{
                    if(this.getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.STONE_SWORD))
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                }
            }
        }
    }
}
