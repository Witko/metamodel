package sk.cyberhouse.metamodel;

import static java.util.Arrays.asList;

public class ThreeParamsMethodDescriptor<R, P1, P2, P3> extends MethodDescriptor<R> {

    public ThreeParamsMethodDescriptor(String name,
                                       Class<R> returnType,
                                       Class<P1> param1Type,
                                       Class<P2> param2Type,
                                       Class<P3> param3Type) {
        super(name, returnType, asList(param1Type, param2Type, param3Type));
    }

    @SuppressWarnings("unchecked")
    public Class<P1> getFirstParamType() {
        return (Class<P1>) super.getParamTypes().get(0);
    }

    @SuppressWarnings("unchecked")
    public Class<P2> getSecondParamType() {
        return (Class<P2>) super.getParamTypes().get(1);
    }

    @SuppressWarnings("unchecked")
    public Class<P3> getThirdParamType() {
        return (Class<P3>) super.getParamTypes().get(2);
    }
}
