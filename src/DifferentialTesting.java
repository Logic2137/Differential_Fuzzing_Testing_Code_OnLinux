import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
// import Compile_Module;
// import GetAllFile_Module;
// import RunClassFile_Module;

public class DifferentialTesting {
    public static void main(String[] args) {

        //选择要进行差分测试的版本: 8 or 11
        System.out.println("select want to differential testing version: ");
        Scanner sc = new Scanner(System.in);
        int selectVersion = sc.nextInt();

        //获取该目录下所有的文件路径
        GetAllFile_Module files = new GetAllFile_Module(selectVersion);
        files.getFilesData("/home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/");  //转入Linux需要更改的

        //对所有的.java文件进行编译
        Compile_Module compile_module = new Compile_Module(files);
        compile_module.compileJavaFile();

        //运行所有的.class文件
        RunClassFile_Module runCodeObject = new RunClassFile_Module(files);
        runCodeObject.runAllClassFile();

        //比较差分结果，若有差异就进行标记
        Summary_Module summary_Module = new Summary_Module(files);
        summary_Module.summaryAllFile();
    }
}

/*
编译指令  
javac -Xlint:unchecked -encoding utf-8 -cp "./src/:./lib/*" -d out ./src/DifferentialTesting.java

执行.class指令
java -cp ".:./out:./lib/*" DifferentialTesting
*/