package alexiil.mods.load.baked.insn;

import org.lwjgl.opengl.GL11;

import alexiil.mods.load.baked.func.BakedFunction;
import alexiil.mods.load.baked.func.FunctionException;
import alexiil.mods.load.render.RenderingStatus;

public class BakedPositionFunctional extends BakedInstruction {
    private final BakedFunction<Double> x, y, z;

    public BakedPositionFunctional(BakedFunction<Double> x, BakedFunction<Double> y, BakedFunction<Double> z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void render(RenderingStatus status) throws FunctionException {
        double x = this.x.call(status);
        double y = this.y.call(status);
        double z = this.z.call(status);
        GL11.glTranslated(x, y, z);
    }
}
