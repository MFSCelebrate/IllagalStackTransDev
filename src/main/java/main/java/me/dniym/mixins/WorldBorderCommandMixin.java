package main.java.me.dniym.mixins;

import net.minecraft.server.commands.WorldBorderCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldBorderCommand.class)
public class WorldBorderCommandMixin {
    @ModifyConstant(method = "*", constant = @Constant(doubleValue = 5.9999968E7))
    private double modifyMaxSize(double original) {
        return 2147483646.0;
    }

    @ModifyConstant(method = "*", constant = @Constant(doubleValue = 2.9999984E7))
    private double modifyHalfMaxSize(double original) {
        return 1073741823.0;
    }
}
