package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewCopy {
    public String[] labels() default {};

    public ViewCopier.CopyMode mode() default ViewCopier.CopyMode.REFERENCE;
}
