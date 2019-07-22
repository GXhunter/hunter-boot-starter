package factory;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author wanggx
 * @date 2019/7/16 16:46
 */
public class HttpClientFactory implements FactoryBean<Object>{
    private String beanName;
    private Class<?> clazz;


    @Override
    public Object getObject() throws Exception{
        return null;
    }

    @Override
    public Class<?> getObjectType(){
        return clazz;
    }

}
