package com.gtechnologia.bank.config.aop;

import com.gtechnologia.bank.domain.exception.account.AccountException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class AccountExceptionAspect {

    @Around("@annotation(com.gtechnologia.bank.util.WrapAccountException)")
    public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception e) {
            if (e instanceof AccountException) throw e;
            // Try to extract UUID from method arguments if needed
            UUID id = null;
            for (Object arg : pjp.getArgs()) {
                if (arg instanceof UUID) {
                    id = (UUID) arg;
                    break;
                }
            }
            throw new AccountException(e.getMessage(), id, e.getCause()) {};
        }
    }
}
