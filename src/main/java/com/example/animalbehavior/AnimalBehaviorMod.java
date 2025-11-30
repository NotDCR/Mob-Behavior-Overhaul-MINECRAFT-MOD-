package com.example.animalbehavior;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.Fox;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("animalbehaviormod")
@Mod.EventBusSubscriber(modid = "animalbehaviormod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnimalBehaviorMod {
    
    public static final String MODID = "animalbehaviormod";
    
    public AnimalBehaviorMod() {
        System.out.println("Animal Behavior Mod carregado!");
    }
    
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Llama llama) {
            modifyLlamaBehavior(llama);
        } else if (event.getEntity() instanceof Axolotl axolotl) {
            modifyAxolotlBehavior(axolotl);
        } else if (event.getEntity() instanceof Fox fox) {
            modifyFoxBehavior(fox);
        }
    }
    
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Llama llama && llama.isTamed()) {
            if (llama.tickCount % 20 == 0) {
                modifyLlamaBehavior(llama);
            }
        }
        if (event.getEntity() instanceof Axolotl axolotl) {
            if (axolotl.tickCount % 20 == 0 && axolotl.hasCustomName()) {
                modifyAxolotlBehavior(axolotl);
            }
        }
        if (event.getEntity() instanceof Fox fox) {
            if (fox.tickCount % 20 == 0 && fox.isTame()) {
                modifyFoxBehavior(fox);
            }
        }
    }
    
    private static void modifyLlamaBehavior(Llama llama) {
        if (!llama.isTamed()) return;
        llama.goalSelector.getAvailableGoals().removeIf(goal -> {
            String goalName = goal.getGoal().getClass().getSimpleName();
            return goalName.equals("FollowCaravanGoal");
        });
    }
    
    private static void modifyAxolotlBehavior(Axolotl axolotl) {
        if (!axolotl.hasCustomName() && !axolotl.fromBucket()) return;
        axolotl.targetSelector.getAvailableGoals().removeIf(goal -> {
            Goal g = goal.getGoal();
            return g instanceof NearestAttackableTargetGoal;
        });
    }
    
    private static void modifyFoxBehavior(Fox fox) {
        if (!fox.isTame()) return;
        fox.goalSelector.getAvailableGoals().removeIf(goal -> {
            String goalName = goal.getGoal().getClass().getSimpleName();
            return goalName.contains("Pounce") || 
                   goalName.contains("Attack") ||
                   goalName.contains("StalkPrey");
        });
        fox.targetSelector.getAvailableGoals().removeIf(goal -> {
            Goal g = goal.getGoal();
            return g instanceof NearestAttackableTargetGoal;
        });
    }
}