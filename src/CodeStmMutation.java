import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeStmMutation {
    public  List<String> fileNameList;
    public  File[] filePathList;

    public String directoryPath;

    String forStmtRegex = "for(.*)(\\()(.*)(;)(.*)(\\s*)(.*)(;)(\\s*)(.*) | while(.*)(\\()(.*)(\\))";
    String checkedRegex = ".+(?<!;)$";
    ArrayList<String> insertForStmt;

    CodeStmMutation() {
        String directionPath = "/home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u/C1ObjectSpillInLogicOp.java";  // 转入Linux需要更改的
        File directory = new File(directionPath);
        fileNameList = new ArrayList<>();
        fileNameList.add(directionPath);
        filePathList = new File[1];
        filePathList[0] = new File(directionPath);

        insertForStmt = new ArrayList<>();
        for (int i = 0; i < 500; ++i) {
            insertForStmt.add("for (int newIndex" + i + " = 0; newIndex" + i + " < 20; ++newIndex" + i + ")");
        }

    }

    CodeStmMutation(String directionPath, int jdkVersion) {
        directionPath = directionPath + "/jdk" + jdkVersion + "u";
        this.directoryPath = directionPath;
        File directory = new File(directionPath);

        insertForStmt = new ArrayList<>();
        for (int i = 0; i < 500; ++i) {
            insertForStmt.add("for (int newIndex" + i + " = 0; newIndex" + i + " < 20; ++newIndex" + i + ")");
        }

        //获取所有的目录名
        if (directory.list() != null) {
            fileNameList = Arrays.asList(directory.list());
        }
        else
            System.out.println("Directory is not exist!");

        //获取路径下所有文件的路径
        if (directory.listFiles() != null) {
            filePathList = directory.listFiles();
        }
        else
            System.out.println("Directory is not exist!");

    }


    public int forStmMutationForeach() {
        int num = 0;
        for (int i = 0; i < filePathList.length; i++) {
            forStmMutation(filePathList[i].getAbsolutePath());
        }
        return num;
    }

    public boolean forStmMutation(String filePath) {
        int count = 0;
        boolean changed = false;
        StringBuilder stringBuilder = new StringBuilder();
        String[] packageArray;
        String[] classStringArray;
        String scannerLine = null;
        StringBuilder packageString = new StringBuilder();
        StringBuilder classString = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

            while ((scannerLine = bufferedReader.readLine()) != null) {

                Pattern p = Pattern.compile(forStmtRegex);
                Matcher matcher = p.matcher(scannerLine);


                if (matcher.find() && Pattern.matches(".+(?<!;)$", scannerLine)) {
                    changed = true;
                    stringBuilder.append("//方法已经for语句变异").append("\n");
                    stringBuilder.append(insertForStmt.get(count++)).append("\n");
                    //System.out.println(filePath + count + " " + scannerLine);
                }
                stringBuilder.append(scannerLine).append("\n");

            }
            System.out.println(stringBuilder.toString());

            if (changed) {
                int insertIndex = filePath.indexOf(".java");
                String newPath = filePath.substring(0, insertIndex)  + filePath.substring(insertIndex);
                File newMutationFile = new File(newPath);
                FileWriter fileWriter = new FileWriter(newPath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(stringBuilder.toString());

                bufferedWriter.close();
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return changed;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CodeStmMutation cs = new CodeStmMutation("/home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique", 8);  // 转入Linux需要更改的
//        CodeStmMutation cs = new CodeStmMutation();
        cs.forStmMutationForeach();

        }

}


//            FileInputStream in = new FileInputStream("D:\\Java_File\\Java_Project\\TestJavaparserProject\\src\\main\\java\\org\\example\\Test.java");
//            CompilationUnit cu = StaticJavaParser.parse(in);
//            ClassOrInterfaceDeclaration c = cu.findFirst(ClassOrInterfaceDeclaration.class).get();
//            List<MethodDeclaration> methods = c.getMethods();
//            for(MethodDeclaration m : methods) {
//                List<Statement> statementList = m.getBody().get().getStatements();
//                for (Statement stmt : statementList) {
//                    if (stmt.isForStmt() || stmt.isWhileStmt()) {
//
//                        Expression expression_compare = StaticJavaParser.parseExpression("j < 5000");
//
//                        ForStmt forStmt = new ForStmt(null, expression_compare, null, stmt);
//                        System.out.println("pp: " + forStmt.getBody().toString());
//                        stmt.replace(forStmt);
//                    }
//                }
//                //System.out.println(m.getBody().get().toString());
//            }
//            System.out.println(cu.toString());