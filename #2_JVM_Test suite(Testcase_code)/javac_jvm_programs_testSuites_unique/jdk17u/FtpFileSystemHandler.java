public interface FtpFileSystemHandler {

    public boolean cd(String path);

    public boolean cdUp();

    public String pwd();

    public boolean fileExists(String name);

    public java.io.InputStream getFile(String name);

    public long getFileSize(String name);

    public java.io.InputStream listCurrentDir();

    public java.io.OutputStream putFile(String name);

    public boolean removeFile(String name);

    public boolean mkdir(String name);

    public boolean rename(String from, String to);
}
