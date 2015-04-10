package org.secnod.shiro.jersey;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.glassfish.jersey.server.internal.process.MappableException;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by barry on 10/04/15.
 */
public abstract class AnnotationBehaviourFactory {

    protected abstract AuthorizingAnnotationHandler createHandler();

    protected AuthorizingAnnotationFailureHandler createFailureHandler() {
        return new AuthorizingAnnotationFailureHandler() {
            @Override
            public void authorizationFailed(AuthorizationException e, ContainerRequestContext requestContext) {
                throw new MappableException(e); // TODO Try without wrapping
            }
        };
    }

    public AnnotationBehaviour createBehaviour() {
        return new AnnotationBehaviour(createHandler(), createFailureHandler());
    }
}
