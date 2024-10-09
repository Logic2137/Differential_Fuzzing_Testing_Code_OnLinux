<<<<<<< HEAD
# Differential_Fuzzing_Testing_Code_OnLinux
对主流JVM进行差分模糊测试的测试框架，主要使用Java完成
=======
## 进入操作目录
cd /home/Graduation_Project_Code/Differential_Fuzzing_Testing_Code

## 删除旧的.class文件和日志文件
rm -rf ./Testcode_outClass/jdk8u_HotSpot/*
rm -rf ./Testcode_outClass/jdk8u_OpenJ9/*
rm -rf ./Testcode_outClass/jdk8u_Zulu/*
rm -rf ./LogDirectory/*

## 先删除test_jdk8u文件夹下所有文件
rm -rf /home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u/*

## 复制jdk_testcase备份
cp -rf /home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique_backage/jdk8u/* /home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u


## 编译指令 编译变异程序
javac -cp "./src" -d out ./src/CodeStmMutation.java

## 执行.class指令 执行变异程序
java -cp "./out" CodeStmMutation

## 编译指令 编译差分测试框架
javac -Xlint:unchecked -encoding utf-8 -cp "./src/:./lib/" -d out ./src/DifferentialTesting.java

## 执行.class指令 执行差分测试框架
java -cp ".:./out:./lib/*" DifferentialTesting

----------
## 统计某一目录下的所有文件数目
ls -lR| grep "^-" | wc -l

### 显示开启JIT编译器
java -Xcomp 类文件名


javac -Xlint:unchecked -encoding utf-8 -cp /home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u -d ./Testcode_outClass/jdk8u /home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u/TestStackBangMonitorOwned.java

javac -Xlint:unchecked -encoding utf-8 -cp "./lib/*" -d out ./src/DifferentialTesting.java

java -cp "./out:./lib/*" DifferentialTesting


/root/jdk/jdk_version/HotSpot_jdk8u332/bin/java -cp .:./Testcode_outClass/jdk8u_HotSpot:/root/jdk/jdk_version/HotSpot_jdk8u332/jre/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib/tools.jar 

/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/bin/java  -cp .:./Testcode_outClass/jdk8u_OpenJ9:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/jre/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib/tools.jar 

/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/bin/java -Xcomp -cp .:./Testcode_outClass/jdk8u_Zulu:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/jre/lib:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/lib:/root/jdk/jdk_version/Zulujdk8.0.322-linux_x64/lib/tools.jar 

-Xms32m -Xmx256m

/root/jdk/jdk_version/HotSpot_jdk8u332/bin/javac -Xlint:unchecked -encoding utf-8 -cp .:./Testcode_outClass/jdk8u_HotSpot:/root/jdk/jdk_version/HotSpot_jdk8u332/jre/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib:/root/jdk/jdk_version/HotSpot_jdk8u332/lib/tools.jar -d jdk8u_HotSpot


/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/bin/javac -Xlint:unchecked -encoding utf-8 -cp .:./Testcode_outClass/jdk8u_OpenJ9:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/jre/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib:/root/jdk/jdk_version/OpenJ9_jdk8u322-b06/lib/tools.jar -d jdk8u_OpenJ9

HotSpot:
Execution time: 237ms

OpenJ9:
Execution time: 1761ms
>>>>>>> 0489993 (提交差分测试框架的所有代码部分_2024年)
