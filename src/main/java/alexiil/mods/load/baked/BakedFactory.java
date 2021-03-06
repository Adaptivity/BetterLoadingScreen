package alexiil.mods.load.baked;

import alexiil.mods.load.baked.factory.FactoryElement;
import alexiil.mods.load.baked.func.BakedFunction;
import alexiil.mods.load.baked.func.FunctionException;
import alexiil.mods.load.render.MinecraftDisplayerRenderer;
import alexiil.mods.load.render.RenderingStatus;

public class BakedFactory extends BakedTickable {
    public final BakedFunction<Boolean> shouldCreate, shouldDestroy;
    public final BakedRenderingPart component;

    public BakedFactory(BakedFunction<Boolean> shouldCreate, BakedFunction<Boolean> shouldDestroy, BakedRenderingPart component) {
        this.shouldCreate = shouldCreate;
        this.shouldDestroy = shouldDestroy;
        this.component = component;
    }

    /** @return A list with all of the elements that should be added */
    public FactoryElement getNewIfShould(RenderingStatus status) throws FunctionException {
        if (shouldCreate.call(status)) {
            return new FactoryElement(this);
        }
        return null;
    }

    @Override
    public void tick(RenderingStatus status, MinecraftDisplayerRenderer renderer) throws FunctionException {
        FactoryElement element = getNewIfShould(status);
        if (element != null)
            renderer.elements.add(element);
    }
}
