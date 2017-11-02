package sk.cyberhouse.metamodel;

@Generate
public interface TestInterface {

    void voidMethod();

    String oneParamMethod(String param1);

    String twoParamMethod(String param1, String param2);

    boolean threeParamMethod(String param1, String param2, String param3);

    Boolean fourParamMethod(String param1, String param2, boolean param3, long param4);

    int primitiveMethod(int param);
}
