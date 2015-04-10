package org.secnod.shiro.jersey;

import org.apache.shiro.authz.AuthorizationException;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by barry on 10/04/15.
 */
public interface AuthorizingAnnotationFailureHandler {

    void authorizationFailed(AuthorizationException e, ContainerRequestContext requestContext);

}
