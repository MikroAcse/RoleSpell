package ru.mikroacse.engine.listeners;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * Created by MikroAcse on 29.07.2016.
 */
public class ListenerSupportFactory {
    private ListenerSupportFactory() {
    }

    public static <T extends AbstractListener> T create(Class<T> listener) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{ListenerSupport.class, listener},
                new ListenerSupportFactory.ListenerInvocationHandler<>(listener));
    }

    private static class ListenerInvocationHandler<T extends AbstractListener> implements InvocationHandler {
        private final Class<T> listenerClass;

        private final List<T> listeners = Collections.synchronizedList(new ArrayList<T>());
        private final Set<String> currentEvents = Collections.synchronizedSet(new HashSet<String>());

        private ListenerInvocationHandler(Class<T> listenerClass) {
            this.listenerClass = listenerClass;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            String fullMethodName = method.getDeclaringClass().getName() + "#" + methodName;

            if (method.getDeclaringClass().equals(ListenerSupport.class)) {
                if ("addListener".equals(methodName)) {
                    listeners.add((T) args[0]);
                } else if ("removeListener".equals(methodName)) {
                    listeners.remove((T) args[0]);
                } else if ("clearListeners".equals(methodName)) {
                    listeners.clear();
                }
                return null;
            }

            if (method.getDeclaringClass().equals(listenerClass)) {
                if (currentEvents.contains(fullMethodName)) {
                    System.err.println("Cyclic event invocation detected: " + fullMethodName);
                }
                currentEvents.add(fullMethodName);
                for (int i = listeners.size() - 1; i >= 0; i--) {
                    T listener = listeners.get(i);
                    method.invoke(listener, args);
                }
                currentEvents.remove(fullMethodName);
                return null;
            }

            return method.invoke(this, args);
        }
    }

}
