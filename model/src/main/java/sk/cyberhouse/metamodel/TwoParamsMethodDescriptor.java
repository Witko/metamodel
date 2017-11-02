package sk.cyberhouse.metamodel;

import static java.util.Arrays.asList;

public class TwoParamsMethodDescriptor<R, P1, P2> extends MethodDescriptor<R> {

    public TwoParamsMethodDescriptor(String name,
                                     Class<R> returnType,
                                     Class<P1> param1Type,
                                     Class<P2> param2Type) {
        super(name, returnType, asList(param1Type, param2Type));
    }

    @SuppressWarnings("unchecked")
    public Class<P1> getFirstParamType() {
        return (Class<P1>) super.getParamTypes().get(0);
    }

    @SuppressWarnings("unchecked")
    public Class<P2> getSecondParamType() {
        return (Class<P2>) super.getParamTypes().get(1);
    }
}
