package com.lyw.clickhousestarter.scanner;

import com.lyw.clickhousestarter.annotation.MapperScan;
import com.lyw.clickhousestarter.bean.ClickHouseFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Set;

public class ClickHouseScannerRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        ClassPathClickHouseScanner scanner = new ClassPathClickHouseScanner(registry);
        String[] packages = definePackages(annotationMetadata);
        Set<BeanDefinitionHolder> beanDefinitionSet = scanner.doScan(packages);
        handlePostScan(beanDefinitionSet);
    }

    private String[] definePackages(AnnotationMetadata annotationMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        String[] packages = null;
        if (annotationAttributes != null) {
            packages = annotationAttributes.getStringArray("basePackages");
        }
        if (packages == null || packages.length == 0) {
            packages = StringUtils.toStringArray(AutoConfigurationPackages.get(beanFactory));
        }
        return packages;
    }

    private void handlePostScan(Set<BeanDefinitionHolder> beanDefinitionSet) {
        beanDefinitionSet.forEach(beanDefinitionHolder -> {
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(ClickHouseFactoryBean.class);
        });
    }

}
