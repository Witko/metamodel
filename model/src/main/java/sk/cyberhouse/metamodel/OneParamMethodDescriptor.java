package sk.cyberhouse.metamodel;

import static java.util.Collections.singletonList;

public class OneParamMethodDescriptor<R, P> extends MethodDescriptor<R> {

    public OneParamMethodDescriptor(String name,
                                    Class<R> returnType,
                                    Class<P> paramType) {
        super(name, returnType, singletonList(paramType));
    }

    @SuppressWarnings("unchecked")
    public Class<P> getParamType() {
        return (Class<P>) super.getParamTypes().get(0);
    }
}
