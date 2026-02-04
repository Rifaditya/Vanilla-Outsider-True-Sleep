import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // Drown guard removed. Frozen mobs do not tick, therefore they do not consume air or take drowning damage.
    // If freezing is disabled via Gamerule, the user accepts the risk of natural consequence (drowning).
}
