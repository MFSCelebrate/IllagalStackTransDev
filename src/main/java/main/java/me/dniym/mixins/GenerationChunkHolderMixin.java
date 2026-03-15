package main.java.me.dniym.mixins;

import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 使 GenerationChunkHolder 完全失效：任何实例化都会抛出异常。
 */
@Mixin(GenerationChunkHolder.class)
public abstract class GenerationChunkHolderMixin {

    @Inject(method = "<init>(Lnet/minecraft/world/level/ChunkPos;)V", at = @At("HEAD"))
    private void onInit(ChunkPos chunkPos, CallbackInfo ci) {
        throw new UnsupportedOperationException("GenerationChunkHolder is disabled by plugin");
    }
}
