package com.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import com.util.SaveTxtToFile;

/**
 * @author Robin
 * @date 2016年4月26日 上午10:11:41
 * @version 1.0
 */

public class Folder2String {
	public static HashMap<Character,Integer> map_char2int;
	private static void map_int(){
		map_char2int=new HashMap<>();
		map_char2int.put('A',0);
		map_char2int.put('B',1);
		map_char2int.put('C',2);
		map_char2int.put('D',3);
		map_char2int.put('E',4);
		map_char2int.put('F',5);
		map_char2int.put('G',6);
		map_char2int.put('H',7);
		map_char2int.put('I',8);
		map_char2int.put('J',9);
		map_char2int.put('K',10);
		map_char2int.put('L',11);
		map_char2int.put('M',12);
		map_char2int.put('N',13);
		map_char2int.put('O',14);
		map_char2int.put('P',15);
		map_char2int.put('Q',16);
		map_char2int.put('R',17);
		map_char2int.put('S',18);
		map_char2int.put('T',19);
		map_char2int.put('U',20);
		map_char2int.put('V',21);
		map_char2int.put('W',22);
		map_char2int.put('X',23);
		map_char2int.put('Y',24);
		map_char2int.put('Z',25);
		map_char2int.put('a',26);
		map_char2int.put('b',27);
		map_char2int.put('c',28);
		map_char2int.put('d',29);
		map_char2int.put('e',30);
		map_char2int.put('f',31);
		map_char2int.put('g',32);
		map_char2int.put('h',33);
		map_char2int.put('i',34);
		map_char2int.put('j',35);
		map_char2int.put('k',36);
		map_char2int.put('l',37);
		map_char2int.put('m',38);
		map_char2int.put('n',39);
		map_char2int.put('o',40);
		map_char2int.put('p',41);
		map_char2int.put('q',42);
		map_char2int.put('r',43);
		map_char2int.put('s',44);
		map_char2int.put('t',45);
		map_char2int.put('u',46);
		map_char2int.put('v',47);
		map_char2int.put('w',48);
		map_char2int.put('x',49);
		map_char2int.put('y',50);
		map_char2int.put('z',51);
		map_char2int.put('1',52);
		map_char2int.put('2',53);
		map_char2int.put('3',54);
		map_char2int.put('4',55);
		map_char2int.put('5',56);
		map_char2int.put('6',57);
		map_char2int.put('7',58);
		map_char2int.put('8',59);
		map_char2int.put('9',60);
		map_char2int.put('0',61);
		map_char2int.put('$',62);
		map_char2int.put('_',63);
		map_char2int.put('-',64);
	}


	public static char[] map_int2char=new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M',
			'N','O','P','Q','R','S','T','U','V','W','X','Y','X','a','b','c','d','e',
			'f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	
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
		if (path.lastIndexOf(File.separator) != -1)
			parentPath = path.substring(0, path.lastIndexOf(File.separator));
		else
			parentPath = path.substring(0, path.lastIndexOf(File.separator));
		helper(file, result);

		// 保存result到文件中！！！
		String txtPath = path.substring(0, path.lastIndexOf(File.separator)) + ".txt";
		SaveTxtToFile.write(result.toString(), txtPath);

		return result.toString();
	}

	private static void helper(File file, StringBuilder sb) {
		cnt++;
		if (!file.isDirectory()) {// 叶子节点
			String name = file.getName().substring(0,
					file.getName().lastIndexOf("."));
			sb.append(linkedHash(name));
			return;
		}
		sb.append(linkedHash(file.getName()));// 内部节点也放在字符串中
		File[] files = file.listFiles();
		for (File f : files)
			helper(f, sb);
	}

	private static String weakHash_(String source) {
		if (source == null)
			return null;
		if (source.length() == 0)
			return "";
		return "" + source.charAt(0) + source.charAt(source.length() - 1);
	}
	private static String linkedHash(String source){
		if (source == null)
			return null;
		if (source.length() == 0)
			return "";
		int period=0;
		if(source.length()<=3)
			period=source.length();
		else
			period=3;


		int[] arr=new int[period];
		map_int();
		for(int i=0;i<source.length();i++)
			try {
				arr[i % period] += map_char2int.get(source.charAt(i));
			}catch (Exception e){
				System.out.println("--->"+source.charAt(i));
				arr[i % period] += 52;
			}finally {

			}
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<period;i++)
			sb.append(map_int2char[arr[i]%52]);
		return sb.toString();
	}
}
