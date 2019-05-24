package com.lingyi.processor;

import com.google.auto.service.AutoService;
import com.lingyi.annotation.MappingProvider;
import com.lingyi.annotation.protocol.Cmd;
import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.provider.MappingBuilder;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class HitlerProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements elementUtil;
    private Types typeUtil;
    private Map<String,String> options ;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        elementUtil = processingEnv.getElementUtils();
        typeUtil = processingEnv.getTypeUtils();
        options = processingEnv.getOptions();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportAnnotationTypes = new HashSet<>();
        supportAnnotationTypes.add(Cmd.class.getCanonicalName());
        return supportAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> routeElements = roundEnvironment.getElementsAnnotatedWith(Cmd.class);
        if (routeElements == null || routeElements.size() == 0){
            return false;
        }

        for (Element element:routeElements){
            Cmd cmd = element.getAnnotation(Cmd.class);
            boolean isPublic = false;
            boolean isStatic = false;

            String packageName = "";
            String className = "";
            switch (element.getKind()){
                case ENUM:
                case CLASS:
                case INTERFACE:
                case ANNOTATION_TYPE:
                    className = element.getSimpleName().toString();
                    packageName = elementUtil.getPackageOf(element).getQualifiedName().toString();
                    MappingProvider.addMapping(MappingBuilder.build(cmd.funType(),cmd.buildType(),packageName+"."+className,cmd.targetClass()));
                    break;

                case METHOD:
                    ExecutableElement executeElement = (ExecutableElement) element;
                    packageName = elementUtil.getPackageOf(element.getEnclosingElement()).getQualifiedName().toString();
                    className = element.getEnclosingElement().getSimpleName().toString();
                    int paramsCount = executeElement.getParameters() == null?0:executeElement.getParameters().size() ;

                    for (VariableElement params : executeElement.getParameters()){
                        System.out.println("params:"+params.asType().toString());
                    }
                    Set<Modifier> modifiers = element.getModifiers();
                    if (modifiers != null && modifiers.size() > 0){
                        for (Modifier modifier : modifiers){
                            if (modifier == Modifier.PUBLIC){
                                isPublic = true;
                            }

                            if (modifier == Modifier.STATIC){
                                isStatic = true;
                            }
                        }
                    }

                    if (cmd.funType() == CmdType.ADDTRYCATCH){
                        isPublic = isStatic  = true;
                    }
                    if (!isPublic || !isStatic ){
                        continue;
                    }
                    MappingProvider.addMapping(MappingBuilder.build(cmd.funType(),cmd.buildType(),packageName+"."+className,cmd.targetClass(),element.getSimpleName().toString(),cmd.methodSignature(),cmd.targetMethodName(),paramsCount));
                    break;
            }

        }

        MappingProvider.createMappingJson(getProjectPath()+"/hitler_mapping.json");
        return true;
    }

    private static String getProjectPath(){
        String path  = System.getProperty("user.dir");
        if(path != null){
            if(path.endsWith(File.separator + "app")){
                path = path.substring(0,path.length() - 4);
            }
        }

        return path;
    }
}
