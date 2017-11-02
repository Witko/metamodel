package sk.cyberhouse.metamodel;

import static java.util.Collections.unmodifiableList;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MethodDescriptor<R> {

    public MethodDescriptor(String name,
                            Class<R> returnType,
                            List<Class<?>> paramTypes) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes = unmodifiableList(new ArrayList<>(paramTypes));
    }

    private final String name;

    private final Class<R> returnType;

    private final List<Class<?>> paramTypes;

    public int getParameterCount() {
        return paramTypes.size();
    }
}
