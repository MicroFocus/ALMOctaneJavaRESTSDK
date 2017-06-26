package com.hpe.adm.nga.sdk.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by brucesp on 26-Jun-17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowedReferences {
    Class<? extends EntityModel>[] value();
}
