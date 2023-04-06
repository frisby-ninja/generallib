package net.ninjaworks.generalapi.util;

import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.DefaultLoggingEventBuilder;
import org.slf4j.spi.LoggingEventBuilder;
import org.slf4j.spi.NOPLoggingEventBuilder;

import javax.print.attribute.standard.MediaSize;
import java.awt.*;

public class Logger implements org.slf4j.Logger {

    private final String NAME;
    private Level level;

    public static final Logger GENERAL_API_LOGGER = new Logger("general_api");

    public Logger(String name) {
        NAME = name;
        this.level = Level.TRACE;
    }

    public void printStringWithColorAndLevel(String string, String color, Level level) {
        System.out.println(
                color + "[" + NAME.toUpperCase() + "-LOGGER/" + level.toString().toUpperCase() + "] " +
                        string + SysColors.ANSI_RESET);
    }

    public void printException(Exception exception) {
        error(exception.getClass().getName());
        String msg = exception.getMessage();
        error(msg);
        for(StackTraceElement element : exception.getStackTrace()) {
            error(element.toString());
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public LoggingEventBuilder makeLoggingEventBuilder(Level level) {
        this.level = level;
        return new DefaultLoggingEventBuilder(this, level);
    }

    @Override
    public LoggingEventBuilder atLevel(Level level) {
        return this.isEnabledForLevel(level) ? this.makeLoggingEventBuilder(level) : NOPLoggingEventBuilder.singleton();
    }

    @Override
    public boolean isEnabledForLevel(Level level) {
        return switch (level) {
            case TRACE -> this.isTraceEnabled();
            case DEBUG -> this.isDebugEnabled();
            case INFO -> this.isInfoEnabled();
            case WARN -> this.isWarnEnabled();
            case ERROR -> this.isErrorEnabled();
        };
    }

    @Override
    public boolean isTraceEnabled() {
        return this.level.toInt() >= 0;
    }

    @Override
    public void trace(String s) {
        if(isTraceEnabled()) {
            printStringWithColorAndLevel(s, SysColors.ANSI_WHITE, Level.TRACE);
        }
    }

    @Override
    public void trace(String s, Object o) {

    }

    @Override
    public void trace(String s, Object o, Object o1) {

    }

    @Override
    public void trace(String s, Object... objects) {

    }

    @Override
    public void trace(String s, Throwable throwable) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public LoggingEventBuilder atTrace() {
        return org.slf4j.Logger.super.atTrace();
    }

    @Override
    public void trace(Marker marker, String s) {

    }

    @Override
    public void trace(Marker marker, String s, Object o) {

    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {

    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String s) {

    }

    @Override
    public void debug(String s, Object o) {

    }

    @Override
    public void debug(String s, Object o, Object o1) {

    }

    @Override
    public void debug(String s, Object... objects) {

    }

    @Override
    public void debug(String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String s) {

    }

    @Override
    public void debug(Marker marker, String s, Object o) {

    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {

    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public LoggingEventBuilder atDebug() {
        return org.slf4j.Logger.super.atDebug();
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String s) {

    }

    @Override
    public void info(String s, Object o) {

    }

    @Override
    public void info(String s, Object o, Object o1) {

    }

    @Override
    public void info(String s, Object... objects) {

    }

    @Override
    public void info(String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String s) {

    }

    @Override
    public void info(Marker marker, String s, Object o) {

    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void info(Marker marker, String s, Object... objects) {

    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public LoggingEventBuilder atInfo() {
        return org.slf4j.Logger.super.atInfo();
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String s) {

    }

    @Override
    public void warn(String s, Object o) {

    }

    @Override
    public void warn(String s, Object... objects) {

    }

    @Override
    public void warn(String s, Object o, Object o1) {

    }

    @Override
    public void warn(String s, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String s) {

    }

    @Override
    public void warn(Marker marker, String s, Object o) {

    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {

    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public LoggingEventBuilder atWarn() {
        return org.slf4j.Logger.super.atWarn();
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String s) {
        printStringWithColorAndLevel(s, SysColors.ANSI_RED, Level.ERROR);
    }

    @Override
    public void error(String s, Object o) {

    }

    @Override
    public void error(String s, Object o, Object o1) {

    }

    @Override
    public void error(String s, Object... objects) {

    }

    @Override
    public void error(String s, Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String s) {

    }

    @Override
    public void error(Marker marker, String s, Object o) {

    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void error(Marker marker, String s, Object... objects) {

    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public LoggingEventBuilder atError() {
        return org.slf4j.Logger.super.atError();
    }
}