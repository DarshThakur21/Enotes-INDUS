package com.enotes.Enotes_INDUS.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.annotations.Cache;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogginAspect {

    @Before("execution(* com.enotes.Enotes_INDUS.controller..*(..))")
    public  void beforeController(JoinPoint joinPoint){
        Signature signature= joinPoint.getSignature();
        String className= signature.getDeclaringType().getSimpleName();
        String methodName= signature.getName();
        log.info("Calling :: {} :: {}() ",className,methodName);
    }

    @Around("execution(* com.enotes.Enotes_INDUS.controller..*(..))")
    public Object jointPointController(ProceedingJoinPoint joinPoint) throws Throwable{
        Signature signature= joinPoint.getSignature();
        String className= signature.getDeclaringType().getSimpleName();
        String methodName= signature.getName();
        long start=System.currentTimeMillis();
        log.info("Calling :: {} :: {}() :: {} MS",className,methodName,start);
        Object result=joinPoint.proceed();
        long duration=System.currentTimeMillis()-start;
        log.info("End Calling :: {} :: {}() :: {} MS",className,methodName,duration);
        return result;

    }


    @After("execution(* com.enotes.Enotes_INDUS.controller..*(..))")
    public  void endController(JoinPoint joinPoint){
        Signature signature= joinPoint.getSignature();
        String className= signature.getDeclaringType().getSimpleName();
        String methodName= signature.getName();
        log.info("End Calling :: {} :: {}() ",className,methodName);
    }



    @Before("execution(* com.enotes.Enotes_INDUS.service..*(..))")
        public  void beforeService(JoinPoint joinPoint){
        Signature signature= joinPoint.getSignature();
        String className= signature.getDeclaringType().getSimpleName();
        String methodName= signature.getName();
        log.info("Calling :: {} :: {}() ",className,methodName);
    }
    @Around("execution(* com.enotes.Enotes_INDUS.service..*(..))")
    public Object jointPointService(ProceedingJoinPoint joinPoint) throws Throwable{
        Signature signature= joinPoint.getSignature();
        String className= signature.getDeclaringType().getSimpleName();
        String methodName= signature.getName();
        long start=System.currentTimeMillis();
        log.info("Calling :: {} :: {}() :: {} MS",className,methodName,start);
        Object result=joinPoint.proceed();
        long duration=System.currentTimeMillis()-start;
        log.info("End Calling :: {} :: {}() :: {} MS",className,methodName,duration);
        return result;

    }

    @After("execution(* com.enotes.Enotes_INDUS.service..*(..))")
    public  void endService(JoinPoint joinPoint){
        Signature signature= joinPoint.getSignature();
        String className= signature.getDeclaringType().getSimpleName();
        String methodName= signature.getName();
        log.info("End Calling :: {} :: {}() ",className,methodName);
    }
}
