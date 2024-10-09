public interface NotificationSenderMBean {

    public void sendNotifs(String type, int count);

    public int getListenerCount();
}
