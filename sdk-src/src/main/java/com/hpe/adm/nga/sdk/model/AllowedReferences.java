package com.hpe.adm.nga.sdk.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that defines the true references that are allowed for the given method
 * When a reference field can return more than one type of reference this defines the actual
 * sub types that are returned
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowedReferences {
    Class<? extends Entity>[] value();
}
