package com.gtechnologia.bank.adapters.infrastructure;

import com.gtechnologia.bank.application.ports.out.log.LoggerPort;
import org.springframework.stereotype.Component;

@Component
public class Slf4jLoggerAdapter implements LoggerPort {
    private static final org.slf4j.Logger LOG =
            org.slf4j.LoggerFactory.getLogger(Slf4jLoggerAdapter.class);

    @Override public void info(String msg, Object... args) { LOG.info(msg, args); }
    @Override public void warn(String msg, Object... args) { LOG.warn(msg, args); }
    @Override public void error(String msg, Object... args) { LOG.error(msg, args); }
    @Override public void error(String msg, Throwable t) { LOG.error(msg, t); }
}
