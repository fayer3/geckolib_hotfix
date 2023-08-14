package geckolibhotfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoModel;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationProcessor;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
public abstract class GeoModelMixin<T extends GeoAnimatable> implements CoreGeoModel<T> {
    @Shadow(remap = false)
    public abstract AnimationProcessor<T> getAnimationProcessor();

    @Unique
    private long geckolibhotfix$lastAnimatedInstance;
    @Unique
    private double geckolibhotfix$animOffset = 0.00001;
    @Unique
    private long geckolibhotfix$firstInstance = 0;

    @Inject(at = @At(value = "TAIL"), method = "handleAnimations", remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private void geckolibhotfix$storeLastInstance(T animatable, long instanceId, AnimationState<T> animationState, CallbackInfo ci) {
        geckolibhotfix$lastAnimatedInstance = instanceId;
    }

    @ModifyVariable(at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/core/animation/AnimatableManager;isFirstTick()Z"), method = "handleAnimations", remap = false)
    private double geckolibhotfix$animate(double currentFrameTime, T animatable, long instanceId) {
        AnimatableManager<T> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        if (currentFrameTime >= animatableManager.getLastUpdateTime() - 0.0001 && currentFrameTime <= animatableManager.getLastUpdateTime() + 0.0001 && geckolibhotfix$lastAnimatedInstance != instanceId) {
            if (geckolibhotfix$firstInstance == instanceId)
                geckolibhotfix$animOffset += 0.00001;
            currentFrameTime = currentFrameTime + geckolibhotfix$animOffset;
        } else {
            geckolibhotfix$animOffset = 0.00001;
            geckolibhotfix$firstInstance = instanceId;
        }
        return currentFrameTime;
    }
}
