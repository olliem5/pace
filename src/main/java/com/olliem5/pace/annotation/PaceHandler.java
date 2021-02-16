package com.olliem5.pace.annotation;

import com.olliem5.pace.modifier.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author olliem5
 * @since 9/02/21
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PaceHandler {
    int priority() default EventPriority.DEFAULT;
}
