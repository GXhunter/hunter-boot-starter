import anon.HttpClient;
import factory.HttpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.MultiValueMap;

import java.util.Set;

/**
 * @author wanggx
 * @date 2019/7/12 14:22
 */
@Slf4j
public class HttpClientAutoConfiguredRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware, EnvironmentAware{
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;
    private Environment environment;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException{
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,BeanDefinitionRegistry registry){
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        AnnotationTypeFilter typeFilter = new AnnotationTypeFilter(HttpClient.class);
        scanner.addIncludeFilter(typeFilter);
        scanner.setResourceLoader(this.resourceLoader);
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents("com.test");
        for(BeanDefinition beanDefinition : beanDefinitionSet){
            if(beanDefinition instanceof AnnotatedBeanDefinition){
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
                MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(HttpClient.class.getCanonicalName());
                log.info("扫描包：com.test");
                log.info("将:" + beanDefinition.getBeanClassName() + "注入到spring容器");
                BeanDefinitionBuilder definition = null;
                try{
                    definition = BeanDefinitionBuilder
                            .genericBeanDefinition(HttpClientFactory.class)
                            .addPropertyValue("beanName",beanDefinition.getBeanClassName())
                            .addPropertyValue("beanClass",Class.forName(beanDefinition.getBeanClassName()))
                            .addPropertyValue("baseUrl",attributes.get("url"))
                            .addPropertyValue("connectTimeout",attributes.get("connectTimeout"))
                            .addPropertyValue("readTimeout",attributes.get("readTimeout"))
                            .addPropertyValue("writeTimeout",attributes.get("writeTimeout"))
                    ;
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }

                registry.registerBeanDefinition(beanDefinition.getBeanClassName(),definition.getBeanDefinition());
            }
        }

    }

    private ClassPathScanningCandidateComponentProvider getScanner(){
        return new ClassPathScanningCandidateComponentProvider(false,this.environment){
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition){
                boolean isCandidate = false;
//                独立类  且非注解
                if(beanDefinition.getMetadata().isIndependent()){
                    if(!beanDefinition.getMetadata().isAnnotation()){
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }

        };
    }

    @Override
    public void setEnvironment(Environment environment){
        this.environment = environment;
    }
}
