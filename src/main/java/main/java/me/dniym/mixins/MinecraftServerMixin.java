package main.java.me.dniym.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Mixin 用于修改 MinecraftServer 中世界边界的绝对最大尺寸。
 * 将默认的 30,000,000 (0x01c9c370) 改为 1,073,741,824。
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    /**
     * @author 你的名字
     * @reason 覆盖原方法，返回自定义的绝对最大世界尺寸。
     */
    @Overwrite
    public int getAbsoluteMaxWorldSize() {
        return 1073741824; // 2^30
    }
}
