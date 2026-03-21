package main.java.me.dniym.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(method = "*", constant = @Constant(intValue = 30000000), remap = false)
    private int modifyMaxWorldSize(int original) {
        System.out.println("[IllegalStack] Mixin applied! Modifying max world size from " + original + " to 2147483646");
        return 2147483646;
    }
}
