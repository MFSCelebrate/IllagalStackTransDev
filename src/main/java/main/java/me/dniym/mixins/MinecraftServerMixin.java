package main.java.me.dniym.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(method = "*", constant = @Constant(intValue = 30000000))
    private int modifyMaxWorldSize(int original) {
        return 2147483646;
    }
}
