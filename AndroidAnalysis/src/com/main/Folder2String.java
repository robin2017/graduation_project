package com.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.util.SaveTxtToFile;

/**
 * @author Robin
 * @date 2016年4月26日 上午10:11:41
 * @version 1.0
 */

public class Folder2String {
	public static String currentProjectPath = System.getProperty("user.dir");
	private static int cnt = 0;

	public static int getNumber() {
		return cnt;
	}

	public static String traverseFolder(String path, String apkName)
			throws IOException {
		cnt = 0;// 计时器初始化
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("Files Not Found!");
			return null;
		}
		StringBuilder result = new StringBuilder();
		String parentPath = null;
		if (path.lastIndexOf("//") != -1)
			parentPath = path.substring(0, path.lastIndexOf("/"));
		else
			parentPath = path.substring(0, path.lastIndexOf("/"));
		helper(file, result);

		// 保存result到文件中！！！
		String txtPath = path.substring(0, path.lastIndexOf("/")) + ".txt";
		SaveTxtToFile.write(result.toString(), txtPath);

		return result.toString();
	}

	private static void helper(File file, StringBuilder sb) {
		cnt++;
		if (!file.isDirectory()) {// 叶子节点
			String name = file.getName().substring(0,
					file.getName().lastIndexOf("."));
			sb.append(weakHash(name));
			return;
		}
		sb.append(weakHash(file.getName()));// 内部节点也放在字符串中
		File[] files = file.listFiles();
		for (File f : files)
			helper(f, sb);
	}

	private static String weakHash(String source) {
		if (source == null)
			return null;
		if (source.length() == 0)
			return "";
		return "" + source.charAt(0) + source.charAt(source.length() - 1);
	}
}
