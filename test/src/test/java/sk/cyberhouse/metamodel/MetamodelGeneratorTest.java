package sk.cyberhouse.metamodel;

import static org.assertj.core.api.Assertions.assertThat;
import static sk.cyberhouse.metamodel.TestInterface_meta.fourParamMethod;

import org.junit.Test;

public class MetamodelGeneratorTest {

    @Test
    public void test() throws Exception {
        assertThat(fourParamMethod.getName()).isEqualTo("fourParamMethod");

    }
}
