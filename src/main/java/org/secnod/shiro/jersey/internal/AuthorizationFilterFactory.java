package org.secnod.shiro.jersey.internal;

import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.authz.aop.*;
import org.secnod.shiro.jersey.AnnotationBehaviour;
import org.secnod.shiro.jersey.AnnotationBehaviourFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Contains an map of annotations which can be extended by supplying a custom map (from the {@link AuthorizationFilterFactory} class)
 *
 * This map then generates the AuthorizationHandlers for each annotation, as required by the Collections of annotations
 * passed into the {@link #createAuthorizationFilter(Collection) createAuthorizationFilter} method
 * Created by barry on 09/04/15.
 */
public class AuthorizationFilterFactory {

    public Map<Class<? extends Annotation>, AnnotationBehaviourFactory> annotationHandlerFactoryMap;

    public AuthorizationFilterFactory() {
        annotationHandlerFactoryMap = new HashMap<>();
        annotationHandlerFactoryMap.put(RequiresPermissions.class, new RequiresPermissionAnnotationHandlerFactory());
        annotationHandlerFactoryMap.put(RequiresRoles.class, new RequiresRolesAnnotationHandlerFactory());
        annotationHandlerFactoryMap.put(RequiresAuthentication.class, new RequiresAuthenticationAnnotationHandlerFactory());
        annotationHandlerFactoryMap.put(RequiresUser.class, new RequiresUserAnnotationHandlerFactory());
        annotationHandlerFactoryMap.put(RequiresGuest.class, new RequiresGuestAnnotationHandlerFactory());
    }


    public Map<Class<? extends Annotation>, AnnotationBehaviourFactory> getAnnotationHandlerFactoryMap() {
        return Collections.unmodifiableMap(annotationHandlerFactoryMap);
    }

    public void addCustomAnnotations(Map<Class<? extends Annotation>, AnnotationBehaviourFactory> customAnnotationMap) {
        annotationHandlerFactoryMap.putAll(customAnnotationMap);
    }

    public AuthorizationFilter createAuthorizationFilter(Collection<Annotation> authzSpecs) {
        Map<Annotation, AnnotationBehaviour> authChecks = new HashMap<>(authzSpecs.size());
        for (Annotation authSpec : authzSpecs) {
            authChecks.put(authSpec, createHandler(authSpec));
        }
        return new AuthorizationFilter(authChecks);
    }

    private AnnotationBehaviour createHandler(Annotation annotation) {
        Class<? extends Annotation> t = annotation.annotationType();
        if (annotationHandlerFactoryMap.containsKey(t)) return annotationHandlerFactoryMap.get(t).createBehaviour();
        else throw new IllegalArgumentException("Cannot create a handler for the unknown annotation " + t);
    }


    private static class RequiresPermissionAnnotationHandlerFactory extends AnnotationBehaviourFactory {
        @Override
        public AuthorizingAnnotationHandler createHandler() {
            return new PermissionAnnotationHandler();
        }
    }

    private static class RequiresRolesAnnotationHandlerFactory extends AnnotationBehaviourFactory {
        @Override
        public AuthorizingAnnotationHandler createHandler() {
            return new RoleAnnotationHandler();
        }
    }

    private static class RequiresAuthenticationAnnotationHandlerFactory extends AnnotationBehaviourFactory {
        @Override
        public AuthorizingAnnotationHandler createHandler() {
            return new AuthenticatedAnnotationHandler();
        }
    }

    private static class RequiresUserAnnotationHandlerFactory extends AnnotationBehaviourFactory {
        @Override
        public AuthorizingAnnotationHandler createHandler() {
            return new UserAnnotationHandler();
        }
    }

    private static class RequiresGuestAnnotationHandlerFactory extends AnnotationBehaviourFactory {
        @Override
        public AuthorizingAnnotationHandler createHandler() {
            return new GuestAnnotationHandler();
        }
    }
}
