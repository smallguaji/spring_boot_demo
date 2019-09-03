package com.example.demo.annotation;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		int level = role.level();
		JSONObject obj = (JSONObject) pjp.getArgs()[0];
        Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
        if(permission.getLevel() >= level) {
        	Object o = pjp.proceed();
        	return o;
		} else {
			Result re = new Result();
			re.setMsg("亲 这边建议你提升你的等级哦");
			return re;
		}
    }

	
}
