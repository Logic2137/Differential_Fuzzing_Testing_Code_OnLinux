import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAllFile_Module {
    public List<String> fileNameList;
    public File[] filePathList;
    public ArrayList<EnableClass> enableClassList; // 可直接通过main()执行.class文件的文件列表
    public String directoryAbsolutePath;
    public int fileCount; // 编译的总文件数目
    public int jdkVersion; // 要测试的jdk版本

    public MySQLConnector connector;

    /**
     * @Author: Gao Shiyu
     * @Function: 默认构造函数
     */
    GetAllFile_Module() {
        this.fileCount = 0;
        this.enableClassList = new ArrayList<>();
        this.directoryAbsolutePath = "";
        this.jdkVersion = 8;
    }

    /**
     * @Author: Gao Shiyu
     * @Function: 构造方法
     * @param version 要测试的JDK版本
     */
    GetAllFile_Module(int version) {
        this.fileCount = 0;
        this.enableClassList = new ArrayList<>();
        this.directoryAbsolutePath = "";
        this.jdkVersion = version;
    }

    /**
     * @Author: Gao Shiyu
     * @Function: 获取指定路径下的所有文件名及其路径
     * @param directoryPath 指定的读取路径
     */
    public void getFilesData(String directoryPath) {

        if (this.jdkVersion == 8) {
            directoryPath = directoryPath + "jdk8u";
        } else if (this.jdkVersion == 11) {
            directoryPath = directoryPath + "jdk11u";
        }

        File directory = new File(directoryPath);
        this.directoryAbsolutePath = directoryPath;

        // 获取目录下所有的文件名
        if (directory.list() != null)
            fileNameList = Arrays.asList(directory.list());
        else
            System.out.println("Directory is not exist!");

        // 获取目录下所有文件的路径
        if (directory.listFiles() != null)
            filePathList = directory.listFiles();
        else
            System.out.println("Directory is not exist!");

        connector = new MySQLConnector("root", "111111");  // 转入Linux需要更改的

        // 打印输出文件
        try {
            System.out.println("本次测试jdk" + this.jdkVersion);
            this.fileCount = filePathList.length;
            System.out.println("文件夹: " + directory.getAbsolutePath() + " 下包含" + this.fileCount + "个文件。");
            for (int index = 0; index < fileNameList.size(); ++index) {
                assert filePathList != null;
                boolean isHave = getMainAndPackageForClass(filePathList[index].getAbsolutePath(), enableClassList);
                System.out.println("\n" + "fileName: " + filePathList[index].getName() + "\tfilePath: "
                        + filePathList[index].getAbsolutePath());
                if (isHave) {
                    System.out.println("本用例可直接java命令运行, 详情如下: ");
                    System.out.println("className: " + enableClassList.get(enableClassList.size() - 1).className);
                    System.out.println("packagePath: " + enableClassList.get(enableClassList.size() - 1).packageName);
                }
                int id = connector.queryIdByFileName(filePathList[index].getName(), this.jdkVersion);
                if (id <= 0) {    //未查询到对应的文件，就插入本条文件信息
                    connector.insertJavaFileInformation(filePathList[index].getName(), isHave);
                }
                else {
                    System.out.println(filePathList[index].getName() + "文件已存在数据库当中!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author: Gao Shiyu
     * @function: 获取指定.java文件中的main()函数以及其所存在的包路径
     * @param filePath  .java文件路径
     * @param classList 可执行类文件列表
     * @throws IOException IO异常捕获
     */
    public static boolean getMainAndPackageForClass(String filePath, ArrayList<EnableClass> classList)
            throws IOException {
        EnableClass newClassElement = new EnableClass();
        String[] packageArray;
        String[] classStringArray;
        String scannerLine = null;
        StringBuilder packageString = new StringBuilder();
        StringBuilder classString = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        boolean isEnable = false;
        while ((scannerLine = bufferedReader.readLine()) != null) {
            if (packageString.toString().equals("") && scannerLine.contains("package")) {
                packageArray = scannerLine.trim().split("\\s+|\\.|;");

                int len = packageArray.length;
                for (int i = 1; i < len; i++) {
                    packageString.append(packageArray[i]).append("/");
                }
            }

            if (scannerLine.contains("public static void main(String[] args)")) {
                isEnable = true;
                break;
            }

            if (scannerLine.contains("class") && !scannerLine.contains("static")) {
                int flag = -1;
                classStringArray = scannerLine.trim().split("\\s+");
                for (int i = 0; i < classStringArray.length; i++) {
                    if (classStringArray[i].equals("class")) {
                        flag = i + 1;
                        break;
                    }
                }
                if (flag != -1)
                    classString = new StringBuilder(classStringArray[flag]);
            }
        }

        if (isEnable) {
            newClassElement.className = classString.toString();
            newClassElement.packageName = packageString.toString();
            classList.add(newClassElement);
        }
        return isEnable;
    }
}

class EnableClass {
    public String className; // 可直接执行的.class类名
    public String packageName; // 该文件存在的包名路径
}
