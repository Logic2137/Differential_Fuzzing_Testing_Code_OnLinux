public interface SimpleStandardMBean {

    public String getState();

    public void setState(String s);

    public int getNbChanges();

    public void reset();
}
