package alexiil.mods.load.baked.insn;

import org.lwjgl.opengl.GL11;

import alexiil.mods.load.baked.func.BakedFunction;
import alexiil.mods.load.baked.func.FunctionException;
import alexiil.mods.load.render.RenderingStatus;

public class BakedColourFunctional extends BakedInstruction {
    private final BakedFunction<Double> red, green, blue, alpha;

    public BakedColourFunctional(BakedFunction<Double> red, BakedFunction<Double> green, BakedFunction<Double> blue, BakedFunction<Double> alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public void render(RenderingStatus status) throws FunctionException {
        GL11.glColor4d(red.call(status), green.call(status), blue.call(status), alpha.call(status));
    }
}
