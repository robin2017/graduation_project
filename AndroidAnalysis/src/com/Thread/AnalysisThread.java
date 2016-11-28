package com.Thread;

import java.io.File;
import java.io.IOException;

import com.main.Folder2String;
import com.main.GUI;
import com.util.ZipTest;

/**
 * @author Robin
 * @date 2016年4月26日 下午9:01:44
 * @version 1.0
 */

public class AnalysisThread extends Thread {

	/* 现在正在处理的apk:zip()之前就初始化 */
	private File file = null;
	/* 在zip()中初始化，jar(),class_()中调用 */
	private String apkFilePath = null;
	/* 在class_()中初始化,run_()中调用 */
	private String classFolderPath = null;

	/* 函数与函数，变量中间空一行 */
	public AnalysisThread(File file) {
		this.file = file;
	}

	public void run() {
		String tmp = file.getAbsolutePath();
		String suffix = tmp.substring(tmp.length() - 3, tmp.length());
		if (suffix.equals("apk")) {
			zip();
			jar();
			class_();
			run_();
			delete_();
		}
		GUI.collectionCallFunction(showInfo(10));
	}

	private void delete_() {
		// TODO Auto-generated method stub
		String tmp = file.getPath();
		String pac = tmp.substring(0, tmp.length() - 4);
		deleteHelper(new File(pac));
	}

	private static void deleteHelper(File file) {
		if (!file.isDirectory()) {
			/* 有可能删除失败！！！没有关闭文件流 */
			if (!file.delete())
				System.out.println("failed--->" + file.getPath());
			return;
		}
		File[] files = file.listFiles();
		for (File f : files)
			deleteHelper(f);
		file.delete();
	}

	private void run_() {
		String result = null;
		int number = 0;
		try {
			result = Folder2String.traverseFolder(classFolderPath,
					file.getName());
			number = Folder2String.getNumber();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GUI.collectionCallFunction(showInfo(5));
	}

	private void class_() {
		String jarFileName = apkFilePath + File.separator
				+ "classes.jar";

		String folder = jarFileName.substring(0, jarFileName.indexOf(".jar"))
				+ File.separator;
		GUI.collectionCallFunction(showInfo(3));
		try {
			ZipTest.unZipFiles(jarFileName, folder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//删除工程下面的两个文件(如果没有也不会报异常)
		File file1=new File(jarFileName);
		file1.delete();
		File file2=new File(jarFileName.replace("classes-dex2jar.jar", "classes-error.zip"));
		file2.delete();
		classFolderPath = apkFilePath + File.separator + "classes";
		GUI.collectionCallFunction(showInfo(4));
	}

	private void jar() {
		//由于得到的jar文件放在工程中，所以下一步的class的源要在工程中找，并且处理后，要将jar文件删除！！
		String projectPath=System.getProperty("user.dir");
		String dex2jarbatPath = projectPath+"/plug/dex2jar/d2j-dex2jar.sh";

		String dexPath = apkFilePath + File.separator + "classes.dex";
		String jarPath = apkFilePath + File.separator + "classes.jar";
		String cmd = "sh "+dex2jarbatPath + " " + dexPath+" -o "+jarPath;
		GUI.collectionCallFunction(showInfo(2));
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void zip() {
		String srcDir = file.getPath();
		String destDir = srcDir.substring(0, srcDir.lastIndexOf(".apk"))
				+ File.separator;
		// 成员变量赋值！！！
		apkFilePath = srcDir.substring(0, srcDir.lastIndexOf('.'));
		GUI.collectionCallFunction(showInfo(1));
		try {
			ZipTest.unZipFiles(srcDir, destDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String showInfo(int type) {
		String result = "";
		switch (type) {
		case 1:
			result += "正在解压...";
			break;
		case 2:
			result += "正在转JAR...";
			break;
		case 3:
			result += "正在转类文件";
			break;
		case 4:
			result += "完成！！！";
			break;
		case 5:
			result += "正在删除文件夹...";
			break;
		case 10:
			result = "签名已保存！！";
			break;
		}
		return result;
	}
}
