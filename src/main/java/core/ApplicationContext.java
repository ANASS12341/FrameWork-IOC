package core;

import annotations.Component;
import annotations.Inject;
import xml.Beans;
import xml.Bean;
import xml.Property;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;

public class ApplicationContext {

    private Map<String, Object> beansById = new HashMap<>();
    private Map<Class<?>, Object> beansByType = new HashMap<>();

    public ApplicationContext(String configLocation) {
        loadFromXML(configLocation);
    }

    public ApplicationContext(Class<?>... componentClasses) {
        loadFromAnnotations(componentClasses);
    }

    private void loadFromXML(String configLocation) {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Beans beans = (Beans) context.createUnmarshaller().unmarshal(new File(configLocation));

            // First Pass: create instances
            for (Bean beanDef : beans.getBeans()) {
                Class<?> clazz = Class.forName(beanDef.getClassName());
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beansById.put(beanDef.getId(), instance);
                beansByType.put(clazz, instance);
            }

            // Second Pass: inject dependencies
            for (Bean beanDef : beans.getBeans()) {
                Object instance = beansById.get(beanDef.getId());
                for (Property prop : beanDef.getProperties()) {
                    Field field = instance.getClass().getDeclaredField(prop.getName());
                    field.setAccessible(true);
                    Object dependency = beansById.get(prop.getRef());
                    field.set(instance, dependency);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromAnnotations(Class<?>... componentClasses) {
        try {
            for (Class<?> clazz : componentClasses) {
                if (clazz.isAnnotationPresent(Component.class)) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    beansByType.put(clazz, instance);
                }
            }

            for (Class<?> clazz : componentClasses) {
                Object instance = beansByType.get(clazz);
                if (instance == null) continue;

                // Inject fields
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        field.setAccessible(true);
                        Object dependency = beansByType.get(field.getType());
                        field.set(instance, dependency);
                    }
                }

                // Inject via setter
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Inject.class)) {
                        Class<?> paramType = method.getParameterTypes()[0];
                        Object dependency = beansByType.get(paramType);
                        method.invoke(instance, dependency);
                    }
                }

                // Inject via constructor (simple version)
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if (constructor.isAnnotationPresent(Inject.class)) {
                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        Object[] params = new Object[paramTypes.length];
                        for (int i = 0; i < paramTypes.length; i++) {
                            params[i] = beansByType.get(paramTypes[i]);
                        }
                        Object newInstance = constructor.newInstance(params);
                        beansByType.put(clazz, newInstance);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beansByType.get(clazz));
    }
}
