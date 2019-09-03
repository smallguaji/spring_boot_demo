package com.example.demo.dynamic;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;


/***
 * 用作动态生成类
 * @author 小呱唧
 */

public class DynamicBean {
	//动态生成的类
    private Object object = null; 
  //存放属性名称以及属性的类型
    private BeanMap beanMap = null; 
 
    public DynamicBean() {
        super();
    }
 
    public DynamicBean(Map propertyMap) {
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }
 
    /**
     * 给bean属性赋值
     * @param property 属性名
     * @param value 值
     */
    public void setValue(Object property, Object value) {
        beanMap.put(property, value);
    }
    
    /**
     * 通过属性名得到属性值
     * @param property 属性名
     * @return 值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }
    
    /**
     * 得到该实体bean对象
     * @return
     */
    public Object getObject() {
        return this.object;
    }
    
    /**
     * 生成动态类
     * @param propertyMap
     * @return
     */
    private Object generateBean(Map propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        //给动态类中添加属性
        for(Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            generator.addProperty(key, (Class) propertyMap.get(key));
        }
        
        return generator.create();
    }

}
