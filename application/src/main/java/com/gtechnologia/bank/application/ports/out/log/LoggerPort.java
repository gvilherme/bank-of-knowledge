package com.gtechnologia.bank.application.ports.out.log;

public interface LoggerPort {
    void info(String msg, Object... args);
    void warn(String msg, Object... args);
    void error(String msg, Object... args);
    void error(String msg, Throwable t);
}
