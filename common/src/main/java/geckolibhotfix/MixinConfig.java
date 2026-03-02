package geckolibhotfix;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin {

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    private static final String FIELD_DEPENDENT_MIXIN =
        "L" + FieldDependentMixin.class.getName().replace(".", "/") + ";";

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        try {
            ClassNode mixinClass = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            if (mixinClass.visibleAnnotations != null) {
                for (AnnotationNode annotation : mixinClass.visibleAnnotations) {
                    if (annotation.desc.equals(FIELD_DEPENDENT_MIXIN)) {
                        String neededField = (String) annotation.values.get(1);
                        boolean missing = (boolean) annotation.values.get(3);
                        if (missing) {
                            if (MixinService.getService().getBytecodeProvider()
                                .getClassNode(targetClassName).fields.stream()
                                .anyMatch(f -> neededField.equals(f.name))) {
                                return false;
                            }
                        } else {
                            if (MixinService.getService().getBytecodeProvider()
                                .getClassNode(targetClassName).fields.stream()
                                .noneMatch(f -> neededField.equals(f.name))) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }

        return true;
    }
}
