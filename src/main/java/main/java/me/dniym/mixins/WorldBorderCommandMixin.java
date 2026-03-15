package main.java.me.dniym.mixins;

import net.minecraft.server.commands.WorldBorderCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Mixin 用于修改 WorldBorderCommand 中的硬编码边界限制。
 * 将最大边界直径从 59,999,968 (5.9999968E7) 改为 2,147,483,646，
 * 将最大中心偏移从 29,999,984 (2.9999984E7) 改为 1,073,741,823。
 */
@Mixin(WorldBorderCommand.class)
public abstract class WorldBorderCommandMixin {

    /**
     * 修改所有出现 5.9999968E7 (59999968) 的地方为 2147483646。
     * 影响：命令参数范围、异常消息、setSize 方法中的检查。
     */
    @ModifyConstant(method = "*", constant = @Constant(doubleValue = 5.9999968E7))
    private double modifyMaxSize(double original) {
        return 2147483646.0;
    }

    /**
     * 修改所有出现 2.9999984E7 (29999984) 的地方为 1073741823。
     * 影响：命令参数范围、异常消息、setCenter 方法中的检查。
     */
    @ModifyConstant(method = "*", constant = @Constant(doubleValue = 2.9999984E7))
    private double modifyMaxCenterOffset(double original) {
        return 1073741823.0;
    }
}
