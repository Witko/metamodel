package sk.cyberhouse.metamodel.generator;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static javax.lang.model.util.ElementFilter.methodsIn;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import sk.cyberhouse.metamodel.Generate;
import sk.cyberhouse.metamodel.MethodDescriptor;
import sk.cyberhouse.metamodel.OneParamMethodDescriptor;
import sk.cyberhouse.metamodel.TwoParamsMethodDescriptor;
import sk.cyberhouse.metamodel.ZeroParamsMethodDescriptor;
import sk.cyberhouse.metamodel.utils.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MetamodelGenerator extends AbstractProcessor {

    public static final String METHOD_NAME = MethodDescriptor.class.getSimpleName();
    public static final String ZERO_PARAM_METHOD_NAME =
            ZeroParamsMethodDescriptor.class.getSimpleName();
    public static final String ONE_PARAM_METHOD_NAME =
            OneParamMethodDescriptor.class.getSimpleName();
    public static final String TWO_PARAM_METHOD_NAME =
            TwoParamsMethodDescriptor.class.getSimpleName();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {

            for (Element elem : roundEnv.getElementsAnnotatedWith(Generate.class)) {
                Generate annotation = elem.getAnnotation(Generate.class);
                String value = annotation.value();

                String className = elem.getSimpleName() + "_meta";
                if (!value.isEmpty()) {
                    className = value;
                }
                JavaFileObject builderFile = processingEnv.getFiler()
                                                          .createSourceFile(className, elem);
                try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                    out.append("package ");
                    out.append(processingEnv.getElementUtils()
                                            .getPackageOf(elem)
                                            .getQualifiedName()
                                            .toString());
                    out.append(";\n");
                    out.append("\n");
                    out.append(buildImports(MethodDescriptor.class,
                                            ZeroParamsMethodDescriptor.class,
                                            OneParamMethodDescriptor.class,
                                            TwoParamsMethodDescriptor.class,
                                            Generated.class,
                                            Arrays.class));
                    out.append("\n");
                    out.append("\n");
                    out.append("@Generated(\"");
                    out.append(getClass().getCanonicalName());
                    out.append("\")\n");
                    out.append("public interface ").append(className).append("{\n");

                    List<ExecutableElement> methods = methodsIn(elem.getEnclosedElements());

                    methods.forEach(method -> {
                        List<? extends VariableElement> parameters =
                                ((ExecutableElement) method).getParameters();
                        switch (parameters.size()) {
                            case 0:
                                appendZeroParamMethodMetaInfo(out, method);
                                break;
                            case 1:
                                appendOneParamMethodMetaInfo(out, method);
                                break;
                            case 2:
                                appendTwoParamMethodMetaInfo(out, method);
                                break;
                            case 3:
                                appendThreeParamMethodMetaInfo(out, method);
                                break;
                            default:
                                appendNParamMethodMetaInfo(out, method);
                                break;
                        }
                    });
                    out.append("}\n");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String buildImports(Class<?>... imports) {
        List<String> names = stream(imports).map(Class::getCanonicalName)
                                            .map(n -> "import " + n + ";")
                                            .collect(toList());
        return StringUtils.join(names, "\n");
    }

    private void appendNParamMethodMetaInfo(PrintWriter out, ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        String methodReturnType = handlePrimitives(method.getReturnType()).toString();

        List<? extends VariableElement> parameters = method.getParameters();
        List<String> paramClasses = parameters.stream()
                                              .map(VariableElement::asType)
                                              .map(TypeMirror::toString)
                                              .map(s -> s + ".class")
                                              .collect(toList());

        appendParamMetadataDeclaration(out,
                                       METHOD_NAME,
                                       toConstantName(methodName),
                                       singletonList(methodReturnType));

        out.append("<>");

        appendParameters(out,
                         "\"" + methodName + "\"",
                         methodReturnType + ".class",
                         "Arrays.asList(" + StringUtils.join(paramClasses) + ")");
        out.append(";");
        out.append("\n");
    }

    private void appendThreeParamMethodMetaInfo(PrintWriter out, ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        TypeName fieldType = TypeName.get(method.getReturnType());
        FieldSpec.Builder builder = FieldSpec.builder(fieldType, methodName);

        builder.initializer("new $S<$S>($S, $S, $S);");


    }

    private void appendTwoParamMethodMetaInfo(PrintWriter out, ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        TypeMirror returnType = method.getReturnType();
        ArrayList<Object> objects = new ArrayList<>();
//        method.getParameters().

//                appendMethodMetaInfo(out,
//                                     methodName,
//                                     TWO_PARAM_METHOD_NAME,
//                                     singletonList(handlePrimitives(returnType).toString()),
//                                     asList(returnType.toString(), ));
    }

    private void appendOneParamMethodMetaInfo(PrintWriter out, ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        TypeMirror methodReturnType = method.getReturnType();

        List<? extends VariableElement> parameters = method.getParameters();
        TypeMirror parameterType = parameters.get(0).asType();

        appendParamMetadataDeclaration(out,
                                       ONE_PARAM_METHOD_NAME,
                                       toConstantName(methodName),
                                       asList(handlePrimitives(methodReturnType).toString(),
                                              handlePrimitives(parameterType).toString()));

        out.append("<>");

        appendParameters(out,
                         "\"" + methodName + "\"",
                         methodReturnType + ".class",
                         parameterType + ".class");
        out.append(";");
        out.append("\n");
    }

    private void appendZeroParamMethodMetaInfo(PrintWriter out, ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        TypeMirror returnType = method.getReturnType();

        appendMethodMetaInfo(out,
                             methodName,
                             ZERO_PARAM_METHOD_NAME,
                             singletonList(handlePrimitives(returnType).toString()),
                             singletonList(returnType.toString()));
    }

    private void appendMethodMetaInfo(PrintWriter out,
                                      String methodName,
                                      String type,
                                      List<String> generics,
                                      List<String> parameters) {

        appendParamMetadataDeclaration(out,
                                       type,
                                       toConstantName(methodName),
                                       generics);
        out.append("<>");

        String[] params = concat(of(methodName).map(StringUtils::quote),
                                 parameters.stream()
                                           .map(s -> s + ".class"))
                .collect(toList())
                .toArray(new String[parameters.size() + 1]);

        appendParameters(out,
                         params);
        out.append(";");
        out.append("\n");
    }

    private TypeMirror handlePrimitives(TypeMirror type) {
        Types typeUtils = processingEnv.getTypeUtils();
        TypeKind kind = type.getKind();
        if (kind == TypeKind.VOID) {
            return processingEnv.getElementUtils()
                                .getTypeElement(Void.class.getCanonicalName())
                                .asType();
        }
        if (!kind.isPrimitive()) {
            return type;
        }
        return typeUtils.boxedClass((PrimitiveType) type).asType();
    }

    private void appendParameters(PrintWriter out,
                                  String... params) {

        out.append("(");
        out.append(StringUtils.join(params));
        out.append(")");
    }

    private void appendGenerics(PrintWriter out, List<String> genericArgs) {
        out.append("<");
        out.append(StringUtils.join(genericArgs));
        out.append(">");
    }

    private void appendGenerics(PrintWriter out, String... genericArgs) {
        out.append("<");
        out.append(StringUtils.join(genericArgs));
        out.append(">");
    }

    private void appendParamMetadataDeclaration(PrintWriter out,
                                                String type,
                                                String name,
                                                List<String> generics) {
        out.append("    ");
        out.append(type);
        appendGenerics(out, generics);
        out.append(" ");
        out.append(name);
        out.append(" = ");
        out.append(" new ");
        out.append(type);
    }

    private String toConstantName(String simpleName) {
        return simpleName;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Generate.class.getCanonicalName());
    }
}
