package geckolibhotfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import geckolibhotfix.FieldDependentMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
@FieldDependentMixin(value = "lastRenderedInstance", missing = false)
public abstract class GeoModelMixin {

    @Group(name = "isFirstTick", min = 1, max = 1)
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/core/animation/AnimatableManager;isFirstTick()Z"), method = "handleAnimations", remap = false, expect = 0, require = 0)
    private boolean geckolibhotfix$animate(boolean original) {
        return true;
    }

    @Group(name = "isFirstTick", min = 1, max = 1)
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/animation/AnimatableManager;isFirstTick()Z"), method = "handleAnimations", remap = false, expect = 0, require = 0)
    private boolean geckolibhotfix$animate2(boolean original) {
        return true;
    }
}
