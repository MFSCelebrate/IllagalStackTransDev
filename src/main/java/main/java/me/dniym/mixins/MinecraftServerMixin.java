package main.java.me.dniym.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Mixin 用于修改 MinecraftServer 中世界边界的绝对最大尺寸常量。
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    /**
     * 修改 ABSOLUTE_MAX_WORLD_SIZE 常量的值。
     * 原值：0x01c9c370 (30,000,000)
     * 新值：1073741824 (2^30)
     */
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 0x01c9c370))
    private int modifyAbsoluteMaxWorldSize(int original) {
        return 1073741824;
    }
}
