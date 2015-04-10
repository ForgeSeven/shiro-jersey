package org.secnod.shiro.jersey.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.GuestAnnotationHandler;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.glassfish.jersey.server.internal.process.MappableException;
import org.secnod.shiro.jersey.AnnotationBehaviour;
import org.secnod.shiro.jersey.AuthorizingAnnotationFailureHandler;

/**
 * A filter that grants or denies access to a JAX-RS resource based on the Shiro annotations on it.
 *
 * @see org.apache.shiro.authz.annotation
 */
public class AuthorizationFilter implements ContainerRequestFilter {

    private final Map<Annotation, AnnotationBehaviour> authzChecks;

    public AuthorizationFilter(Map<Annotation, AnnotationBehaviour> authzChecks) {
        this.authzChecks = authzChecks;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        for (Map.Entry<Annotation, AnnotationBehaviour> authzCheck : authzChecks.entrySet()) {

            Annotation authzSpec = authzCheck.getKey();
            AuthorizingAnnotationHandler handler = authzCheck.getValue().getHandler();
            AuthorizingAnnotationFailureHandler failureHandler = authzCheck.getValue().getFailureHandler();

            try {
                handler.assertAuthorized(authzSpec);
            } catch (AuthorizationException e) {
                failureHandler.authorizationFailed(e, requestContext);
            }
        }
    }

}
