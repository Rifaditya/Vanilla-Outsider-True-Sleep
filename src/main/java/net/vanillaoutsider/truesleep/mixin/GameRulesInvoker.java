package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.class)
public interface GameRulesInvoker {
    @Invoker("registerInteger")
    static GameRule<Integer> invokeRegisterInteger(String id, GameRuleCategory category, int defaultValue, int min, int max) {
        throw new AssertionError();
    }

    @Invoker("registerBoolean")
    static GameRule<Boolean> invokeRegisterBoolean(String id, GameRuleCategory category, boolean defaultValue) {
        throw new AssertionError();
    }
}
