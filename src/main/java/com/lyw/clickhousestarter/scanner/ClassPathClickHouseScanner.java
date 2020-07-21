package com.lyw.clickhousestarter.scanner;

import com.lyw.clickhousestarter.annotation.Sql;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

public class ClassPathClickHouseScanner extends ClassPathBeanDefinitionScanner implements ResourceLoaderAware {

    public ClassPathClickHouseScanner(BeanDefinitionRegistry registry) {
        super(registry);
        addIncludeFilter((metadataReader, metadataReaderFactory) ->
                metadataReader.getAnnotationMetadata().hasAnnotatedMethods(Sql.class.getName()));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

}
