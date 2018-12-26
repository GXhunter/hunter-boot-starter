package com.github.gxhunter.util;

import java.util.ResourceBundle;

/**
 * @author 树荫下的天空
 * @date 2018/12/26 16:55
 * 资源工具,解决ResourceBundle报错问题
 */
public class ResourceUtil{
    private String resource;
    private ResourceBundle rb;

    /**
     * @param res 资源文件
     */
    public ResourceUtil(String res){
        this.resource = res;
        rb = ResourceBundle.getBundle(res);
    }

    public String getString(String key,String defaultValue){
        String res;
        try{
            res = rb.getString(key);
        }catch(Exception e){
            if(defaultValue == null){
                throw new IllegalArgumentException(String.format("你没有在%s中配置key为%s的值",resource,key));
            }
            res = defaultValue;
        }
        return res;
    }

    public String getString(String key){
        return getString(key,null);
    }
}
