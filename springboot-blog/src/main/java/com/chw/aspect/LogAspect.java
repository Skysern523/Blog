package com.chw.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by CHW on 2020/9/21 19:03
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.chw.controller..*(..))")
    public void log(){ }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURI();
        String ip = request.getRequestURI();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Requestlog requestlog = new Requestlog(url,ip,classMethod,args);

        logger.info("Request : {}",requestlog);
    }

    @After("log()")
    public void doafter(){
       // logger.info("------------doAfter-------------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterRuturn(Object result){
        logger.info("result:{}"+ result);
    }

    private class Requestlog{
        private String url;
        private String ip;
        private String CLassMethod;
        private Object[] args;

        public Requestlog(String url, String ip, String CLassMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.CLassMethod = CLassMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "Requestlog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", CLassMethod='" + CLassMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
