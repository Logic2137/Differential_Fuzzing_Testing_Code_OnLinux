import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Summary_Module {
    File summaryFileLog;  //运行.class程序日志文件

    FileWriter fileWriter;   //文件写入对象

    BufferedWriter bufferedWriter;   //缓存区对象

    Process process;  //进程对象

    String writeLine;    //寄存String字符串

    MySQLConnector connector;

    GetAllFile_Module files;

    /**
     * @function 比较模块
     * @param nowFiles 获取文件对象
     */
    Summary_Module(GetAllFile_Module nowFiles) {
        this.files = nowFiles;
        this.connector = new MySQLConnector("root", "111111");  // 转入Linux需要更改的
        String currentLogOutputPath = null;
        this.fileWriter = null;
        this.bufferedWriter = null;

        //设置日志存放地址
        if (this.files.jdkVersion == 8) {
            currentLogOutputPath = "./LogDirectory/jdk8u_summary_log.txt";
        }
        else if (this.files.jdkVersion == 11) {
            currentLogOutputPath = "./LogDirectory/jdk11u_summary_log.txt";
        }
        try {
            this.summaryFileLog = new File(currentLogOutputPath);
            this.fileWriter = new FileWriter(currentLogOutputPath);
            this.bufferedWriter = new BufferedWriter(fileWriter);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void summaryAllFile() {
        System.out.println("\n\n差异结果分析: \n");
        for (int index = 0; index < this.files.enableClassList.size(); ++index) {
            String fileName = this.files.enableClassList.get(index).className;
            int fileId = connector.queryIdByFileName(fileName + ".java", this.files.jdkVersion);
            HashMap<String, String> logMap = connector.queryRunLogById(fileId, fileName, this.files.jdkVersion);
            ArrayList<String> list = compareRunLog(logMap, this.files.jdkVersion);
            try {
                String writeLine = "\n\n类名: " + fileName + "\t本代码存在差异输出的编译器如下: \n";
                for (int i = 0; i < list.size(); i++) {
                    writeLine += list.get(i) + "\n";
                }
                bufferedWriter.write(writeLine);
                System.out.println(writeLine);
            } catch(IOException e) {
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



    /**
     *
     * @function 比较hash表内各元素
     * @param logMap
     * @return
     */
    public static ArrayList<String> compareRunLog(HashMap<String, String> logMap, int jdkVersion) {
        ArrayList<String> list = new ArrayList<>();
        if (jdkVersion == 8) {
            String hsco = logMap.get("HotSpotCommonOutput");
            String ojco = logMap.get("OpenJ9CommonOutput");
            String zco = logMap.get("ZuluCommonOutput");
            String HotSpotErrorOutput =  logMap.get("HotSpotErrorOutput");
            String OpenJ9ErrorOutput = logMap.get("OpenJ9ErrorOutput");
            String ZuluErrorOutput = logMap.get("ZuluErrorOutput");
            String result = "";
            if (hsco == null) {
                hsco = "";
            }
            if (ojco == null) {
                ojco = "";
            }
            if (zco == null) {
                zco = "";
            }
            if (HotSpotErrorOutput == null) {
                HotSpotErrorOutput = "";
            }
            if (OpenJ9ErrorOutput == null) {
                OpenJ9ErrorOutput = "";
            }
            if (ZuluErrorOutput == null) {
                ZuluErrorOutput = "";
            }

            if (!hsco.equals(ojco)) {
                result = "HotSpotCommonOutput ***** OpenJ9CommonOutput";
                list.add(result);
            }
            if (!hsco.equals(zco)) {
                result = "HotSpotCommonOutput ***** ZuluCommonOutput";
                list.add(result);
            }
            if (!ojco.equals(zco)) {
                result = "OpenJ9CommonOutput ***** ZuluCommonOutput";
                list.add(result);
            }

            if (!HotSpotErrorOutput.equals(OpenJ9ErrorOutput)) {
                result = "HotSpotErrorOutput ***** OpenJ9ErrorOutput";
                list.add(result);
            }
            if (!HotSpotErrorOutput.equals(ZuluErrorOutput)) {
                result = "HotSpotErrorOutput ***** ZuluErrorOutput";
                list.add(result);
            }
            if (!OpenJ9ErrorOutput.equals(ZuluErrorOutput)) {
                result = "OpenJ9ErrorOutput ***** ZuluErrorOutput";
                list.add(result);
            }
        }
        return list;
    }

}