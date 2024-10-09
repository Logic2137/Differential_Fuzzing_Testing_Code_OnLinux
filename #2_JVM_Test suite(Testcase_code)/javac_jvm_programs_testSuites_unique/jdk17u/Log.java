
package nsk.share.log;

public interface Log {

    public void info(Object o);

    public void debug(Object o);

    public void warn(Object o);

    public void error(Object o);

    public boolean isDebugEnabled();

    public boolean isInfoEnabled();

    public boolean isWarnEnabled();

    public boolean isErrorEnabled();

    public void setInfoEnabled(boolean infoEnabled);

    public void setDebugEnabled(boolean debugEnabled);

    public void setWarnEnabled(boolean warnEnabled);

    public void setErrorEnabled(boolean errorEnabled);
}
