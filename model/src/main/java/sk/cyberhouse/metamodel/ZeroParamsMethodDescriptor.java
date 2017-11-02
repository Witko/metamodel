package sk.cyberhouse.metamodel;

import static java.util.Collections.emptyList;

public class ZeroParamsMethodDescriptor<R> extends MethodDescriptor<R> {

    public ZeroParamsMethodDescriptor(String name, Class<R> returnType) {
        super(name, returnType, emptyList());
    }
}
