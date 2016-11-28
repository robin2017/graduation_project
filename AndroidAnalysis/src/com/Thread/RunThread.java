package com.Thread;

import com.main.CompareFileToLib;
import com.main.GUI;

/**
 * @author Robin
 * @date 2016年5月17日 下午10:01:06
 * @version 1.0
 */
public class RunThread extends Thread {
	private String libPath;
	private String filePath;

	public RunThread(String libPath, String filePath) {
		this.libPath = libPath;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		CompareFileToLib.compare(libPath, filePath);
	}

	public static void threadCallFunction(String str) {
		GUI.collectionResultCallFunction(str);
	}

	public static void threadCallFunctionShowInfo(String str) {
		GUI.callFuctionShowInfo(str);
	}
}
