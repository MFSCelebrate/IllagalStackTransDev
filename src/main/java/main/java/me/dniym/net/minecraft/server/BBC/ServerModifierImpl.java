package main.java.me.dniym.net.minecraft.server.BBC;

import io.papermc.paper.plugin.server.ServerModifier;
import io.papermc.paper.plugin.server.ServerModifierContext;
import main.java.me.dniym.IllegalStack;
import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class ServerModifierImpl implements ServerModifier {

    @Override
    public void modifyServer(ServerModifierContext context) {
        IllegalStack plugin = IllegalStack.getPlugin();
        plugin.getLogger().info("开始应用 ServerModification 字节码修改...");

        Instrumentation instrumentation = context.getInstrumentation();
        if (instrumentation == null) {
            plugin.getLogger().warning("无法获取 Instrumentation，将不会应用字节码修改！");
            return;
        }

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                     ProtectionDomain protectionDomain, byte[] classfileBuffer)
                    throws IllegalClassFormatException {
                if ("net.minecraft.server.MinecraftServer".equals(className)) {
                    return patchMinecraftServer(classfileBuffer);
                }
                if ("net.minecraft.commands.arguments.WorldBorderCommand".equals(className)) {
                    return patchWorldBorderCommand(classfileBuffer);
                }
                return classfileBuffer;
            }
        }, true);

        plugin.getLogger().info("ServerModification 转换器已注册。");
    }

    private byte[] patchMinecraftServer(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                if ("getAbsoluteMaxWorldSize".equals(name) && "()I".equals(descriptor)) {
                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitIntInsn(int opcode, int operand) {
                            if (opcode == Opcodes.SIPUSH && operand == 30000000) {
                                super.visitIntInsn(Opcodes.LDC, 2147483646);
                            } else {
                                super.visitIntInsn(opcode, operand);
                            }
                        }

                        @Override
                        public void visitLdcInsn(Object value) {
                            if (value instanceof Integer && (int) value == 30000000) {
                                super.visitLdcInsn(2147483646);
                            } else {
                                super.visitLdcInsn(value);
                            }
                        }
                    };
                }
                return mv;
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    private byte[] patchWorldBorderCommand(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM9, mv) {
                    @Override
                    public void visitLdcInsn(Object value) {
                        if (value instanceof Double) {
                            double d = (Double) value;
                            if (d == 5.9999968E7) {
                                super.visitLdcInsn(2147483646.0);
                                return;
                            } else if (d == 2.9999984E7) {
                                super.visitLdcInsn(1073741823.0);
                                return;
                            }
                        }
                        super.visitLdcInsn(value);
                    }
                };
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }
}
