package org.secnod.shiro.jersey;

import org.secnod.shiro.jersey.internal.AuthorizationFilter;
import org.secnod.shiro.jersey.internal.AuthorizationFilterFactory;

import java.lang.annotation.Annotation;
import java.util.*;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

/**
 * Wraps {@link AuthorizationFilter filters} around JAX-RS resources that are annotated with Shiro annotations.
 */
public class AuthorizationFilterFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {

        Map<Class<? extends Annotation>, AnnotationBehaviourFactory> customAnnotationMap = getCustomAnnotationMap();
        AuthorizationFilterFactory factory = new AuthorizationFilterFactory();
        factory.addCustomAnnotations(customAnnotationMap);

        List<Annotation> authzSpecs = new ArrayList<>();

        for (Class<? extends Annotation> annotationClass : factory.getAnnotationHandlerFactoryMap().keySet()) {
            // XXX What is the performance of getAnnotation vs getAnnotations?
            Annotation classAuthzSpec = resourceInfo.getResourceClass().getAnnotation(annotationClass);
            Annotation methodAuthzSpec = resourceInfo.getResourceMethod().getAnnotation(annotationClass);

            if (classAuthzSpec != null) authzSpecs.add(classAuthzSpec);
            if (methodAuthzSpec != null) authzSpecs.add(methodAuthzSpec);
        }

        if (!authzSpecs.isEmpty()) {
            context.register(factory.createAuthorizationFilter(authzSpecs), Priorities.AUTHORIZATION);
        }
    }

    /**
     * Override this to method to provide customer annotations to use in the filters along with
     * a factory providing the Handler class for the annotation.
     *
     * Default implementation provides an empty Map.
     * @return
     */
    protected Map<Class<? extends Annotation>, AnnotationBehaviourFactory> getCustomAnnotationMap() {
        return Collections.emptyMap();
    }

}
