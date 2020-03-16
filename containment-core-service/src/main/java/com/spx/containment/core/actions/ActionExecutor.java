package com.spx.containment.core.actions;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
public class ActionExecutor {

    @Transactional
    public <R> R call(Callable<R> param) {
        String className = param.getClass()
            .getName();
        R result;
        try {
            log.debug("executing... " + className);
            result = param.call();
            log.debug("execution complete..." + className);
            return result;
        } catch (Throwable error) {
            log.error("Error occurred executing:" + className, error);
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            }
            throw new RuntimeException(error);
        }

    }

}
