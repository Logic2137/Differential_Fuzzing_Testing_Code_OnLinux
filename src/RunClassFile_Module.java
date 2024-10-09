import java.io.*;
import java.util.ArrayList;

public class RunClassFile_Module {

    public ArrayList<EnableClass> enableClassList;

    File runFileLog;  //运行.class程序日志文件

    FileWriter fileWriter;   //文件写入对象

    BufferedWriter bufferedWriter;   //缓存区对象

    Process process;  //进程对象

    String writeLine;    //寄存String字符串

    String jdk11u_Graalvm_javac = "/root/jdk/jdk_version/Graalvm-ce-jdk11-22.0.0.2/bin/javac";   //转入Linux需要更改的
    String jdk11u_Graalvm_java = "/root/jdk/jdk_version/Graalvm-ce-jdk11-22.0.0.2/bin/java";

    String jdk11u_HotSpot_javac = "/root/jdk/jdk_version/HotSpot_jdk11.0.14+9/bin/javac";
    String jdk11u_HotSpot_java = "/root/jdk/jdk_version/HotSpot_jdk11.0.14+9/bin/java";

    String jdk11u_OpenJ9_javac = "/root/jdk/jdk_version/OpenJ9_jdk-11.0.16+8/bin/javac";
    String jdk11u_OpenJ9_java = "/root/jdk/jdk_version/OpenJ9_jdk-11.0.16+8/bin/java";

    String jdk8u_HotSpot_javac = "/root/jdk/jdk_version/HotSpot_jdk8u332/bin/javac";
    String jdk8u_HotSpot_java = "/root/jdk/jdk_version/HotSpot_jdk8u332/bin/java";
    String jdk8u_HotSpot_cp = ".:./Testcode_outClass/jdk8u_HotSpot:/root/jdk/jdk_version/HotSpot_jdk8u332/jre/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib/tools.jar";

    String jdk8u_OpenJ9_javac = "/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/bin/javac";
    String jdk8u_OpenJ9_java = "/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/bin/java";
    String jdk8u_OpenJ9_cp = ".:./Testcode_outClass/jdk8u_OpenJ9:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/jre/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib/tools.jar";

    String jdk8u_Zulu_javac = "/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/bin/javac";
    String jdk8u_Zulu_java = "/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/bin/java";
    String jdk8u_Zulu_cp = ".:./Testcode_outClass/jdk8u_Zulu:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/jre/lib:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/lib:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/lib/tools.jar";

    ArrayList<String> runResultOutputPathList;        //运行结果日志的目录列表
    ArrayList<String> javaCommondList;
    ArrayList<String> classpathList;

    GetAllFile_Module files;

    MySQLConnector connector;

    // String jvmOption = " -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining ";
    String jvmOption = "  ";

    RunClassFile_Module() {
        this.enableClassList = null;
        this.runFileLog = new File("/home/Graduation_Project_Code/Differential_Fuzzing_Testing_Code/LogDirectory/jdk8u_run_log.txt");     //转入Linux需要更改的
        this.fileWriter = null;
        this.bufferedWriter = null;
        try {
            this.fileWriter = new FileWriter("/home/Graduation_Project_Code/Differential_Fuzzing_Testing_Code/LogDirectory/jdk8u_run_log.txt");   //转入Linux需要更改的
            this.bufferedWriter = new BufferedWriter(this.fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    RunClassFile_Module(GetAllFile_Module nowFiles) {
        this.enableClassList = nowFiles.enableClassList;
        this.runResultOutputPathList = new ArrayList<>();
        this.javaCommondList = new ArrayList<>();
        this.classpathList = new ArrayList<>();
        this.files = nowFiles;
        this.connector = new MySQLConnector("root", "111111");  // 转入Linux需要更改的

        //定义运行结果输出的存放地址
        if (this.files.jdkVersion == 8) {
            this.runResultOutputPathList.add("/LogDirectory/jdk8u_HotSpot_run_log.txt");
            this.runResultOutputPathList.add("/LogDirectory/jdk8u_OpenJ9_run_log.txt");
            this.runResultOutputPathList.add("/LogDirectory/jdk8u_Zulu_run_log.txt");
        }
        else if (this.files.jdkVersion == 11) {
            this.runResultOutputPathList.add("/LogDirectory/jdk11u_HotSpot_run_log.txt");
            this.runResultOutputPathList.add("/LogDirectory/jdk11u_OpenJ9_run_log.txt");
            this.runResultOutputPathList.add("/LogDirectory/jdk11u_Graalvm_run_log.txt");
        }

        //java命令的列表
        if (this.files.jdkVersion == 8) {
            this.javaCommondList.add(jdk8u_HotSpot_java);
            this.javaCommondList.add(jdk8u_OpenJ9_java);
            this.javaCommondList.add(jdk8u_Zulu_java);
        }
        else if (this.files.jdkVersion == 11) {
            this.javaCommondList.add(jdk11u_HotSpot_java);
            this.javaCommondList.add(jdk11u_OpenJ9_java);
            this.javaCommondList.add(jdk11u_Graalvm_java);
        }

        //不同jvm对应-classpath参数的列表
        if (this.files.jdkVersion == 8) {
            this.classpathList.add(jdk8u_HotSpot_cp);
            this.classpathList.add(jdk8u_OpenJ9_cp);
            this.classpathList.add(jdk8u_Zulu_cp);
        }


    }

    public void runAllClassFile() {
        //将所有的可执行.class文件存入数据库当中
        for (int index = 0; index < this.enableClassList.size(); index++) {
            String fileName = this.enableClassList.get(index).className;
            int fileId = connector.queryIdByFileName(fileName + ".java", this.files.jdkVersion);
            int result = connector.insertEnableClassToRuncodeList(fileId, fileName, this.files.jdkVersion);
        }

        

        for (int versionIndex = 0; versionIndex < 3; ++versionIndex) {
            //定义运行结果文件的相关流操作
            String currentLogOutputPath = "/home/Graduation_Project_Code/Differential_Fuzzing_Testing_Code" + this.runResultOutputPathList.get(versionIndex);   //转入Linux需要更改的
            this.runFileLog = new File(currentLogOutputPath);
            this.fileWriter = null;
            this.bufferedWriter = null;
            try {
                this.fileWriter = new FileWriter(currentLogOutputPath);
                this.bufferedWriter = new BufferedWriter(this.fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //执行每一个可执行的.class文件
            for (int index = 0; index < this.enableClassList.size(); index++) {
                String runClassFileRootPath = this.classpathList.get(versionIndex);        //转入Linux需要更改的
                String fileName = this.enableClassList.get(index).className;
                String packetPath = this.enableClassList.get(index).packageName;
                String allPath = packetPath + fileName;
                

                String cmd = this.javaCommondList.get(versionIndex) + jvmOption + " -cp " + runClassFileRootPath + " " + allPath;     //java运行.class文件的命令

                System.out.println("cmd命令是: " + cmd);

                try {
                    int lineIndex;
                    process = Runtime.getRuntime().exec(cmd);
                    writeLine = "运行.class文件完成序号: " + (index + 1);
                    System.out.print(writeLine);
                    bufferedWriter.write(writeLine);
                    writeLine = "\t\t类名: " + packetPath + fileName + '\n';
                    System.out.print(writeLine);
                    bufferedWriter.write(writeLine);

                    //以下打印该次命令的普通输出信息
                    BufferedReader commonBf = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String commonLine;
                    String mysqlCommonText = "";
                    lineIndex = 0;
                    while (((commonLine = commonBf.readLine()) != null)) {    //问题
                        if (lineIndex == 0) {
                            writeLine = "正常输出: \n";
                            bufferedWriter.write(writeLine);
                            System.out.print(writeLine);
                        }
                        ++lineIndex;
                        writeLine = "line" + lineIndex + ": \t" + commonLine;
                        bufferedWriter.write(writeLine);
                        bufferedWriter.newLine();
                        System.out.println(writeLine);
                        mysqlCommonText = mysqlCommonText + commonLine + "\n";
                    }

                    //以下打印该java命令产生的错误输出信息
                    BufferedReader errorBf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String errorLine;
                    String mysqlErrorText = "";
                    lineIndex = 0;
                    while ((errorLine = errorBf.readLine()) != null) {
                        if (lineIndex == 0) {
                            writeLine = "错误输出: \n";
                            bufferedWriter.write(writeLine);
                            System.out.print(writeLine);
                        }
                        ++lineIndex;
                        writeLine = "line" + lineIndex + ": \t" + errorLine;
                        bufferedWriter.write(writeLine);
                        bufferedWriter.newLine();
                        System.out.println("line" + lineIndex + ": \t" + errorLine);
                        mysqlErrorText = mysqlErrorText + errorLine + "\n";
                    }
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();

                    //以下是往服务器MySQL数据库内存储编译信息的操作
                    int fileId = connector.queryIdByFileName(fileName + ".java", this.files.jdkVersion);
                    connector.updateRunInformation(fileId, fileName, mysqlCommonText, mysqlErrorText, this.files.jdkVersion, versionIndex);

                    System.out.print("\n\n");
                    int exitValue = process.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
