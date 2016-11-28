package com.main;

import java.io.File;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.Thread.RunThread;
import com.util.EditDistance;
import com.util.ReadTxtFromFile;

/**
 * @author Robin
 * @date 2016年5月17日 上午9:28:37
 * @version 1.0
 */

class MyComparator implements Comparator<Double> {// 降序比较器
	public int compare(Double o1, Double o2) {
		return o2.compareTo(o1);
	}
}

public class CompareFileToLib {
	public static double RATE = 0.6;
	public static int sameCnt = 0;
	public static int fileCnt = 0;

	public static TreeMap<Double, String> hm = new TreeMap<Double, String>(
			new MyComparator());

	public static void compare(String libPath, String filePath) {
		sameCnt = 0;// 可以对static成变赋值！！
		fileCnt = 0;

		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);

		File folder = new File(libPath);
		File[] files = folder.listFiles();
		String aimStr = ReadTxtFromFile.readTXT(filePath);
		int distance = 0;
		double degree = 0;
		for (File file : files) {
			fileCnt++;
			RunThread.threadCallFunctionShowInfo("共有签名个数：" + files.length
					+ "  现在比较第" + fileCnt + "个！！");
			String fileStr = ReadTxtFromFile.readTXT_File(file);
			distance = EditDistance.minDistance_n(aimStr, fileStr);
			degree = sameDegree(distance, aimStr.length(),
					fileStr.length());
			RunThread.threadCallFunction(formatOutToString(file.getName(),
					aimStr.length(), fileStr.length(), distance,
					nt.format(degree)));
			if (degree > RATE) {
				sameCnt++;
				hm.put(degree, file.getName());
			}
		}
		RunThread.threadCallFunctionShowInfo("\r\n\r\n\r\n共有相似应用个数： " + sameCnt);
		// printResult();
		RunThread.threadCallFunction("相似应用如下:");
		for (Entry<Double, String> entry : hm.entrySet()) {
			Double key = (Double) entry.getKey();
			String val = (String) entry.getValue();
			RunThread.threadCallFunction(nt.format(key) + "     " + val);
		}
	}

	public static double sameDegree(int distance, int a, int b) {
		return 1 - 1.0 * distance / (a + b);// 无需强制转化，因为默认类型为double
	}

	public static String formatOutToString(String name, int aimLength,
			int fileLength, int distance, String sameDegree) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-30s", name)
				+ String.format("%10d", aimLength)
				+ String.format("%10d", fileLength)
				+ String.format("%10d", distance)
				+ String.format("%10s", sameDegree));
		return sb.toString();
	}
}
