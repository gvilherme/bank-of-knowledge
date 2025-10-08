package com.gtechnologia.bank.config.aop;

import com.gtechnologia.bank.adapters.in.metrics.OperationCounterFactory;
import com.gtechnologia.bank.util.WrapAccountMetric;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccountMetricAspect {
    private final OperationCounterFactory operationCounterFactory;
    public AccountMetricAspect(OperationCounterFactory operationCounterFactory) {
        this.operationCounterFactory = operationCounterFactory;
    }

    @Around("@annotation(com.gtechnologia.bank.util.WrapAccountMetric)")
    public Object wrapAccountMetric(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        WrapAccountMetric annotation = signature.getMethod().getAnnotation(WrapAccountMetric.class);
        String metricType = annotation.value();

        var counter = operationCounterFactory.createCounter(metricType);
        try {
            return pjp.proceed();
        } finally {
            counter.increment();
        }
    }
}
