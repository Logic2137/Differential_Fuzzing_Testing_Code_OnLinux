//package org.gsfan.clustermonitor.dbconnector;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MySQLConnector {

	public String userName = null;
	public String passwd = null;
	
	public Connection connection = null;

//	public ResultSet resultSet = null;

	public PreparedStatement preStatement = null;
	
	private static final String dbDriver = "com.mysql.jdbc.Driver";

	//转入Linux需要更改的
	private static final String dbURL = "jdbc:mysql://120.46.203.224:3306/differential_testing_database?useSSL=false&useUnicode=true&characterEncoding=UTF-8";  //使用主机IP会出错，这是为什么？
	
	public MySQLConnector(String userName, String passwd){
		this.userName = userName;
		this.passwd = passwd;
		
		try {
			Class.forName(dbDriver) ;
			System.out.println("数据库驱动加载成功!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
 
		try {

			this.connection = DriverManager.getConnection(dbURL, this.userName, this.passwd);
			// System.out.println(this.connection);
			// String sql = "select * from testcase_jkd8u_list";
			// preStatement = this.connection.prepareStatement(sql);
			// resultSet = preStatement.executeQuery();

			// while(resultSet.next()){   //没有resultSet.next()会出现异常
			// 	System.out.println("id = " + resultSet.getString("id") + "\t fileName = " + resultSet.getString("fileName") +"\t canRun = "+resultSet.getString("canRun"));
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insertJavaFileInformation(String fileName, boolean enableRun) {
		PreparedStatement ps;
		int result = -1;
		int canRun = 0;
		if (enableRun)
			canRun = 1;
		try {
			String sql = "INSERT INTO testcase_jkd8u_list(fileName, canRun) VALUES(?, ?);";
			//System.out.println("jdbc连接对象hash值: " + connection);
			ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setString(1, fileName);
			ps.setInt(2, canRun);
			result = ps.executeUpdate();
			if (result > 0)
				System.out.println("testcase表插入正常,受影响的行数量为: " + result);
			else
				System.out.println("testcase表插入失败!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @function 将可执行的.class文件插入到数据库内
	 * @param fileId
	 * @param fileName
	 * @param jdkVersion
	 * @return
	 */
	public int insertEnableClassToRuncodeList(int fileId, String fileName, int jdkVersion) {
		String table = null;
		PreparedStatement ps;
		int result = -2;
		if (jdkVersion == 8) {
			table = "runcode_jkd8u_list";
		}
		try {
			String sql = "INSERT INTO " + table + "(id, fileName) VALUES(?, ?);";
			ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setInt(1, fileId);
			ps.setString(2, fileName);
			result = ps.executeUpdate();
			if (result > 0)
				System.out.println("runcode表插入正常,受影响的行数量为: " + result);
			else
				System.out.println("run表插入失败.");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @function 更新各java文件在不同jvm上的编译信息
	 * @param fileId 文件存储在数据库中的唯一id
	 * @param fileName 文件名
	 * @param commonLogText 正常编译输出
	 * @param errorLogText 错误编译输出
	 * @param jdkVersion 要测试的jdk版本
	 * @param jvmIndex 要测试的jvm型号
	 * @return
	 */
	public int updateCompileInformation(int fileId, String fileName, String commonLogText, String errorLogText, int jdkVersion, int jvmIndex) {
		int id = fileId;
		PreparedStatement ps;
		int result = -3;
		String tableName;
		if (jdkVersion == 8) {
			tableName = "testcase_jkd8u_list";
			ArrayList<String> errorColumnList = new ArrayList<>();
			errorColumnList.add("HotSpotErrorOutput");
			errorColumnList.add("OpenJ9ErrorOutput");
			errorColumnList.add("ZuluErrorOutput");
			try {
				String sql = "update " + tableName +" set " + errorColumnList.get(jvmIndex) + " = ? where id = ? and fileName = ?;";
				ps = connection.prepareStatement(sql);
				ps.setString(1, errorLogText);
				ps.setInt(2, fileId);
				ps.setString(3, fileName);
				result = ps.executeUpdate();
				if (result > 0) {
					System.out.println("编译信息已插入数据库.");
				} else {
					System.out.println("插入失败.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @function 更新java命令后各文件的输出，存在数据库内
	 * @param fileId 文件存储在数据库中的唯一id
	 * @param fileName 文件名
	 * @param commonLogText 正常运行输出
	 * @param errorLogText 错误运行输出
	 * @param jdkVersion 要测试的jdk版本
	 * @param jvmIndex 要测试的jvm型号
	 * @return 操作影响的行数
	 */
	public int updateRunInformation(int fileId, String fileName, String commonLogText, String errorLogText, int jdkVersion, int jvmIndex) {
		PreparedStatement ps;
		int result = -4;
		String tableName;
		if (jdkVersion == 8) {
			tableName = "runcode_jkd8u_list";
			ArrayList<String> commonColumnList = new ArrayList<>();
			commonColumnList.add("HotSpotCommonOutput");
			commonColumnList.add("OpenJ9CommonOutput");
			commonColumnList.add("ZuluCommonOutput");

			ArrayList<String> errorColumnList = new ArrayList<>();
			errorColumnList.add("HotSpotErrorOutput");
			errorColumnList.add("OpenJ9ErrorOutput");
			errorColumnList.add("ZuluErrorOutput");

			try {
				String sql = "update " + tableName + " set " + commonColumnList.get(jvmIndex) + " = ?, " + errorColumnList.get(jvmIndex) + " = ? where id = ? and fileName = ?;";
				ps = connection.prepareStatement(sql);
				ps.setString(1, commonLogText);
				ps.setString(2, errorLogText);
				ps.setInt(3, fileId);
				ps.setString(4, fileName);
				result = ps.executeUpdate();
				if (result > 0) {
					System.out.println("该程序运行结果已插入数据库.");
				} else {
					System.out.println("插入失败.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @function 根据唯一文件名查询存在表中的id
	 * @param fileName 文件名
	 * @param jdkVersion 要查找的jdk版本
	 * */
	public int queryIdByFileName(String fileName, int jdkVersion) {
		PreparedStatement ps;
		ResultSet resultSet;
		int resultId = -5;
		if (jdkVersion == 8) {
			String sql = "SELECT * from testcase_jkd8u_list where fileName = '" + fileName + "';";
			try {
				ps = (PreparedStatement) connection.prepareStatement(sql);
				resultSet = ps.executeQuery();
				while (resultSet.next()) {
					resultId = resultSet.getInt("id");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// if (resultId <= 0) {
		// 	System.out.println("通过id查询失败!");
		// }

		return resultId;
	}

	public HashMap<String, String> queryRunLogById(int fileId, String fileName,int jdkVersion) {
		PreparedStatement ps;
		ResultSet resultSet;
		String HotSpotCommonOutput = "";
		String HotSpotErrorOutput = "";
		String OpenJ9CommonOutput = "";
		String OpenJ9ErrorOutput = "";
		String ZuluCommonOutput = "";
		String ZuluErrorOutput = "";
		HashMap<String, String> LogMap = new HashMap<>();
		if (jdkVersion == 8) {
			try {
				String sql = "SELECT * from runcode_jkd8u_list where id = ? and fileName = ?;";
				ps = (PreparedStatement) connection.prepareStatement(sql);
				ps.setInt(1, fileId);
				ps.setString(2, fileName);
				resultSet = ps.executeQuery();
				while (resultSet.next()) {
					HotSpotCommonOutput = resultSet.getString("HotSpotCommonOutput");
					HotSpotErrorOutput = resultSet.getString("HotSpotErrorOutput");
					OpenJ9CommonOutput = resultSet.getString("OpenJ9CommonOutput");
					OpenJ9ErrorOutput = resultSet.getString("OpenJ9ErrorOutput");
					ZuluCommonOutput = resultSet.getString("ZuluCommonOutput");
					ZuluErrorOutput = resultSet.getString("ZuluErrorOutput");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogMap.put("HotSpotCommonOutput", HotSpotCommonOutput);
		LogMap.put("HotSpotErrorOutput", HotSpotErrorOutput);
		LogMap.put("OpenJ9CommonOutput", OpenJ9CommonOutput);
		LogMap.put("OpenJ9ErrorOutput", OpenJ9ErrorOutput);
		LogMap.put("ZuluCommonOutput", ZuluCommonOutput);
		LogMap.put("ZuluErrorOutput", ZuluErrorOutput);

		return LogMap;

	}

	// public static void main(String[] args) {
	// 	MySQLConnector con = new MySQLConnector("root", "111111");
	// }
}

//编译本程序  javac -Xlint:unchecked -encoding utf-8 -cp "./src/:./lib/" -d out ./src/MySQLConnector.java
//运行本程序  java -cp ".:./out:./lib/*" MySQLConnector