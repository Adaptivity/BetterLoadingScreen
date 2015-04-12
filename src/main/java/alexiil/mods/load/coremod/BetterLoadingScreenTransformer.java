package alexiil.mods.load.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import alexiil.mods.load.ProgressDisplayer;

public class BetterLoadingScreenTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.client.Minecraft"))
            return transformMinecraft(basicClass, transformedName == name);
        return basicClass;
    }

    private byte[] transformMinecraft(byte[] before, boolean dev) {
        boolean hasFoundStartGame = false;
        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(before);
        reader.accept(classNode, 0);

        boolean found = false;
        String tryingToFind = dev ? "drawSplashScreen" : "func_180510_a";
        String minecraftStartGame = dev ? "startGame" : "func_71384_a";

        for (MethodNode m : classNode.methods) {
            if (m.name.equals(tryingToFind)) {
                found = true;
                m.instructions.insertBefore(m.instructions.getFirst(), new InsnNode(Opcodes.RETURN));
                // just return from the method, as if nothing happened
                break;
            }
            if (m.name.equals(minecraftStartGame)) {
                for (int i = 0; i < m.instructions.size(); i++) {
                    AbstractInsnNode node = m.instructions.get(i);
                    if (node instanceof MethodInsnNode) {
                        MethodInsnNode method = (MethodInsnNode) node;
                        if (method.owner.equals(Type.getInternalName(FMLClientHandler.class)) && method.name.equals("instance")) {
                            MethodInsnNode newOne =
                                    new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ProgressDisplayer.class),
                                            "minecraftDisplayFirstProgress", "()V", false);
                            m.instructions.insertBefore(method, newOne);
                            hasFoundStartGame = true;
                            break;
                        }
                    }
                }
            }
        }

        if (!found) {
            System.out.println("Did not find " + tryingToFind + "! Could it have been any of these?");
            for (MethodNode m : classNode.methods) {
                System.out.println("  -" + m.name);
            }
        }

        if (!hasFoundStartGame) {
            System.out.println("Did not find " + minecraftStartGame + "! Could it have been any of these?");
            for (MethodNode m : classNode.methods) {
                System.out.println("  -" + m.name);
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        System.out.println("Transformed Minecraft");
        return cw.toByteArray();
    }
}