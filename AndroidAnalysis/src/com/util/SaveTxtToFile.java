package com.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Robin
 * @date 2016年5月17日 上午10:36:50
 * @version 1.0
 */

public class SaveTxtToFile {
	public static void write(String text, String filePath) {
		try {
			PrintWriter out = new PrintWriter(
					new File(filePath).getAbsoluteFile());
			try {
				out.print(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
