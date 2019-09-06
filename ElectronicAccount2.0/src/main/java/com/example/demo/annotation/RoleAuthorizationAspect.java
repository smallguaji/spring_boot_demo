package com.example.demo.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Result;

@Aspect
@Component
public class RoleAuthorizationAspect {
	
    @Pointcut("@annotation(role)")
    public  void pointCut(RoleAuthorization role) {
    }
    
    //其实用before也可，around用作环绕增强
    @Around("pointCut(role)")
    public Object doAround(ProceedingJoinPoint pjp,RoleAuthorization role) throws Throwable{
		int level = role.level();
		JSONObject obj = (JSONObject) pjp.getArgs()[0];
        Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
        if(permission.getLevel() >= level) {
        	Object o = pjp.proceed();
        	return o;
		} else {
			Result<Object> re = new Result<Object>();
			re.setMsg("亲 这边建议你提升你的等级哦");
			return re;
		}
    }

	
}
