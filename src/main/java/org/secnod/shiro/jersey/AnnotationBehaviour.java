package org.secnod.shiro.jersey;

import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

/**
 * Created by barry on 10/04/15.
 */
public class AnnotationBehaviour {

    private AuthorizingAnnotationHandler handler;

    private AuthorizingAnnotationFailureHandler failureHandler;

    public AnnotationBehaviour(AuthorizingAnnotationHandler handler, AuthorizingAnnotationFailureHandler failureHandler) {
        this.handler = handler;
        this.failureHandler = failureHandler;
    }

    public AuthorizingAnnotationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public AuthorizingAnnotationHandler getHandler() {
        return handler;
    }
}
