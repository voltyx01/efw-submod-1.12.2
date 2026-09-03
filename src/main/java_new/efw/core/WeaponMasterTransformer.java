package efw.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

public class WeaponMasterTransformer implements IClassTransformer {

    private static final String CACHE_CLASS = "efw/core/WeaponMasterUniqueSettingsCache";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals(
                "com.minecraftserverzone.weaponmaster.itemlayers.HumanoidItemLayer")) {
            return basicClass;
        }

        ClassReader cr = new ClassReader(basicClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc,
                                             String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if (name.equals("uniqueSettings") &&
                        desc.equals("(Lnet/minecraft/item/ItemStack;I)[I")) {
                    return new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
                        @Override
                        protected void onMethodEnter() {
                            loadArg(0);
                            loadArg(1);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                                    CACHE_CLASS,
                                    "get",
                                    "(Lnet/minecraft/item/ItemStack;I)[I",
                                    false);
                            int resultVar = newLocal(Type.getType("[I"));
                            mv.visitVarInsn(Opcodes.ASTORE, resultVar);
                            mv.visitVarInsn(Opcodes.ALOAD, resultVar);
                            Label skip = new Label();
                            mv.visitJumpInsn(Opcodes.IFNULL, skip);
                            mv.visitVarInsn(Opcodes.ALOAD, resultVar);
                            mv.visitInsn(Opcodes.ARETURN);
                            mv.visitLabel(skip);
                        }

                        @Override
                        protected void onMethodExit(int opcode) {
                            if (opcode == Opcodes.ARETURN) {
                                mv.visitInsn(Opcodes.DUP);
                                loadArg(0);
                                loadArg(1);
                                mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                                        CACHE_CLASS,
                                        "put",
                                        "([ILnet/minecraft/item/ItemStack;I)V",
                                        false);
                            }
                        }
                    };
                }
                return mv;
            }
        }, ClassReader.EXPAND_FRAMES);

        return cw.toByteArray();
    }
}