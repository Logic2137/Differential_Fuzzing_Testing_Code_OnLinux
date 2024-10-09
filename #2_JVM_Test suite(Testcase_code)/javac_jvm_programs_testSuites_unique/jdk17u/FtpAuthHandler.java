public interface FtpAuthHandler {

    public int authType();

    public boolean authenticate(String user, String password);

    public boolean authenticate(String user, String password, String account);
}
