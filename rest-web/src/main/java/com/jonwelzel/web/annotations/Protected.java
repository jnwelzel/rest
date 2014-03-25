package com.jonwelzel.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jonwelzel.persistence.enumerations.RoleType;
import com.jonwelzel.web.validation.RequestAuthValidator;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Protected {

    Class<? extends RequestAuthValidator> validator();

    RoleType role();
}
