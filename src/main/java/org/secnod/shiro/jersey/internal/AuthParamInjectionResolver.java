package org.secnod.shiro.jersey.internal;

import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.secnod.shiro.jaxrs.Auth;
import org.secnod.shiro.jersey.TypeFactory;

/**
 * For method parameter injection with the {@linkplain Auth} annotation.
 */
public class AuthParamInjectionResolver extends ParamInjectionResolver<Auth> {

    public AuthParamInjectionResolver() {
        super(TypeFactory.class);
    }
}
