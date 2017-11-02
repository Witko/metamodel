package sk.cyberhouse.metamodel.generator;

import sk.cyberhouse.metamodel.Generate;

@Generate
public interface TestInterface {

    void voidMethod_oneParam(String param1);

    String stringMethod_twoParams(String param1, Integer param2);

    long longMethod_primitives(long param1, int param2, short param3);
}
