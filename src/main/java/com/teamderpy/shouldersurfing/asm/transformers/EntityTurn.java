package com.teamderpy.shouldersurfing.asm.transformers;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.teamderpy.shouldersurfing.asm.IShoulderMethodTransformer;
import com.teamderpy.shouldersurfing.asm.Mappings;

public class EntityTurn implements IShoulderMethodTransformer
{
    @Override
    public InsnList searchList(Mappings mappings, boolean obf)
    {
        InsnList searchList = new InsnList();
        // Даем фреймворку команду найти первую загрузку 'this' (ALOAD 0),
        // чтобы он не крашился при поиске.
        searchList.add(new VarInsnNode(ALOAD, 0));
        return searchList;
    }

    @Override
    public void transform(Mappings mappings, boolean obf, MethodNode method, int offset)
    {
        InsnList inject = new InsnList();
        LabelNode skipReturn = new LabelNode();

        // Загружаем аргументы: this (Entity), yaw, pitch
        inject.add(new VarInsnNode(ALOAD, 0));
        inject.add(new VarInsnNode(FLOAD, 1));
        inject.add(new VarInsnNode(FLOAD, 2));

        // Вызываем наш хук
        inject.add(new MethodInsnNode(INVOKESTATIC, "com/teamderpy/shouldersurfing/asm/InjectionDelegation", "Entity_turn", "(L" + mappings.map("Entity", obf) + ";FF)Z", false));

        // Если хук вернул false, прыгаем к выполнению ванильного кода
        inject.add(new JumpInsnNode(IFEQ, skipReturn));

        // Если хук вернул true, прерываем метод (RETURN)
        inject.add(new InsnNode(RETURN));
        inject.add(skipReturn);

        // ВАЖНО: Вставляем код в САМОЕ НАЧАЛО метода (перед всеми ванильными инструкциями)
        method.instructions.insert(inject);
    }

    @Override
    public String getClassId()
    {
        return "Entity";
    }

    @Override
    public String getMethodId()
    {
        return "Entity#turn";
    }
}