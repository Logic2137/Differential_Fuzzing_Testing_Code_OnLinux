import java.io.*;
import java.util.ArrayList;

public class Compile_Module {
    File logFile;         //日志文件的存放对象
    FileWriter fileWriter;     //文件写入对象
    BufferedWriter bufferedWriter;    //将缓存区的内容写入到指定文件中
    Process process;     //进程对象
    String writeLine;    //寄存字符串，用于存储将要读的字符串，并将其后续写入到文件当中
    GetAllFile_Module files;

    MySQLConnector connector;

    String jdk11u_Graalvm_javac = "/root/jdk/jdk_version/Graalvm-ce-jdk11-22.0.0.2/bin/javac";   //转入Linux需要更改的
    String jdk11u_Graalvm_java = "/root/jdk/jdk_version/Graalvm-ce-jdk11-22.0.0.2/bin/java";

    String jdk11u_HotSpot_javac = "/root/jdk/jdk_version/HotSpot_jdk11.0.14+9/bin/javac";
    String jdk11u_HotSpot_java = "/root/jdk/jdk_version/HotSpot_jdk11.0.14+9/bin/java";

    String jdk11u_OpenJ9_javac = "/root/jdk/jdk_version/OpenJ9_jdk-11.0.16+8/bin/javac";
    String jdk11u_OpenJ9_java = "/root/jdk/jdk_version/OpenJ9_jdk-11.0.16+8/bin/java";

    String jdk8u_HotSpot_javac = "/root/jdk/jdk_version/HotSpot_jdk8u332/bin/javac";
    String jdk8u_HotSpot_java = "/root/jdk/jdk_version/HotSpot_jdk8u332/bin/java";
    String jdk8u_HotSpot_cp = "/root/jdk/jdk_version/HotSpot_jdk8u332/jre/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib/tools.jar";

    String jdk8u_OpenJ9_javac = "/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/bin/javac";
    String jdk8u_OpenJ9_java = "/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/bin/java";
    String jdk8u_OpenJ9_cp = "/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/jre/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib/tools.jar";

    String jdk8u_Zulu_javac = "/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/bin/javac";
    String jdk8u_Zulu_java = "/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/bin/java";
    String jdk8u_Zulu_cp = "/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/jre/lib:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/lib:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/lib/tools.jar";



    ArrayList<String> logOutputPathList;        //日志文件输出目录的列表
    ArrayList<String> compiledFileOutputPathList;   //编译后生成的.class文件存储路径的列表
    ArrayList<String> javacCommondList;
    ArrayList<String> classpathList;

    /**
     * 构造方法
     */
    Compile_Module(GetAllFile_Module nowFiles) {
        this.files = nowFiles;
        this.logOutputPathList = new ArrayList<String>();
        this.compiledFileOutputPathList = new ArrayList<>();
        this.javacCommondList = new ArrayList<>();
        this.classpathList = new ArrayList<>();
        this.connector = new MySQLConnector("root", "111111");  // 此处修改为自己mysql数据库的账号

        //日志文件输出列表初始化
        if (files.jdkVersion == 8) {
            logOutputPathList.add("/LogDirectory/jdk8u_HotSpot_compile_log.txt");
            logOutputPathList.add("/LogDirectory/jdk8u_OpenJ9_compile_log.txt");
            logOutputPathList.add("/LogDirectory/jdk8u_Zulu_compile_log.txt");
        }
        else if (files.jdkVersion == 11) {
            logOutputPathList.add("/LogDirectory/jdk11u_HotSpot_compile_log.txt");
            logOutputPathList.add("/LogDirectory/jdk11u_OpenJ9_compile_log.txt");
            logOutputPathList.add("/LogDirectory/jdk11u_Graalvm_compile_log.txt");
        }

        //生成的.class文件存储路径的列表
        if (files.jdkVersion == 8) {
            compiledFileOutputPathList.add("/Testcode_outClass/jdk8u_HotSpot");
            compiledFileOutputPathList.add("/Testcode_outClass/jdk8u_OpenJ9");
            compiledFileOutputPathList.add("/Testcode_outClass/jdk8u_Zulu");
        }
        else if (files.jdkVersion == 11) {
            compiledFileOutputPathList.add("/Testcode_outClass/jdk11u_HotSpot");
            compiledFileOutputPathList.add("/Testcode_outClass/jdk11u_OpenJ9");
            compiledFileOutputPathList.add("/Testcode_outClass/jdk11u_Graalvm");
        }

        //javac命令的列表
        if (files.jdkVersion == 8) {
            javacCommondList.add(jdk8u_HotSpot_javac);
            javacCommondList.add(jdk8u_OpenJ9_javac);
            javacCommondList.add(jdk8u_Zulu_javac);
        }
        else if (files.jdkVersion == 11) {
            javacCommondList.add(jdk11u_HotSpot_javac);
            javacCommondList.add(jdk11u_OpenJ9_javac);
            javacCommondList.add(jdk11u_Graalvm_javac);
        }

        //-classpath参数列表
        if (files.jdkVersion == 8) {
            classpathList.add(jdk8u_HotSpot_cp);
            classpathList.add(jdk8u_OpenJ9_cp);
            classpathList.add(jdk8u_Zulu_cp);
        }
        


    }

    /**
     * @Author: Gao Shiyu
     * @functio 编译.java文件的模块
     */
    public void compileJavaFile() {
        for (int versionIndex = 0; versionIndex < 3; versionIndex++) {
            //日志文件的定义、操作
            String currentLogOutputPath = "/home/Graduation_Project_Code/Differential_Fuzzing_Testing_Code" + logOutputPathList.get(versionIndex);  //转入Linux需要更改的
            this.logFile = new File(currentLogOutputPath);
            this.fileWriter = null;
            this.bufferedWriter = null;
            try {
                fileWriter = new FileWriter(currentLogOutputPath);
                bufferedWriter = new BufferedWriter(fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //.class文件存储的定义
            String OutputClassFilePath = "." + compiledFileOutputPathList.get(versionIndex);      //转入Linux需要更改的
            String javac_substr = javacCommondList.get(versionIndex);
            String classpathAbsolutePath = classpathList.get(versionIndex);   //转入Linux需要更改的
            for (int index = 0; index < files.fileCount; ++index) {

                // String OutputClassFilePath = files.directoryAbsolutePath;
                String mysqlText = "";
                String fileName = files.filePathList[index].getName();
                String cmd = "";
                if (files.jdkVersion == 8) {
                    cmd =  javac_substr + " -Xlint:unchecked -encoding utf-8 -cp " + classpathAbsolutePath +" -d " + OutputClassFilePath + " " + files.filePathList[index].getAbsolutePath();
                }
                else if (files.jdkVersion == 11) {
                    cmd =  javac_substr + " -Xlint:unchecked -encoding utf-8" + " -d " + OutputClassFilePath + " " + files.filePathList[index].getAbsolutePath();
                }
                //String cmd =  javac_substr + " -Xlint:unchecked -encoding utf-8" + " -d " + OutputClassFilePath + " " + files.filePathList[index].getAbsolutePath();
                System.out.println("当前的cmd指令: " + cmd);

                //在cmd中执行规定的javac编译指令
                try {
                    //exec执行规定的命令
                    process = Runtime.getRuntime().exec(cmd);    //转入Linux需要更改的
                    writeLine = "当前的编译指令: " + cmd;
                    bufferedWriter.write(writeLine);
                    bufferedWriter.newLine();
                    writeLine = "编译文件名: " + fileName;
                    bufferedWriter.write(writeLine);
                    bufferedWriter.newLine();
                    System.out.println(writeLine);
                    writeLine = "java编译完成数: " + (index + 1);
                    bufferedWriter.write(writeLine);
                    bufferedWriter.newLine();
                    System.out.println(writeLine);


//                //以下打印该次命令的普通输出信息
//                BufferedReader commonBf = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String commonLine;
//                int lineIndex = 0;
//                while ((commonLine = commonBf.readLine()) != null) {    //问题
//                    ++lineIndex;
//                    System.out.println("line" + lineIndex + ": \t" + commonLine);
//                }

                    //以下打印该次命令的错误信息
                    BufferedReader errorBf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String errorLine = "编译错误输出: ";
                    int lineIndex = 0;
                    bufferedWriter.write(errorLine);
                    bufferedWriter.newLine();
                    while ((errorLine = errorBf.readLine()) != null) {
                        ++lineIndex;
                        writeLine = "line" + lineIndex + ": \t" + errorLine;
                        mysqlText = mysqlText + errorLine + "\n";
                        bufferedWriter.write(writeLine);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.newLine();

                    //以下是往服务器MySQL数据库内存储编译信息的操作
                    int fileId = connector.queryIdByFileName(fileName, this.files.jdkVersion);
                    connector.updateCompileInformation(fileId, fileName, null, mysqlText, this.files.jdkVersion, versionIndex);
                    
                    System.out.print("\n");
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
