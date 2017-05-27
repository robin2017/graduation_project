import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author  Robin
 * @date 2017年1月19日 下午12:39:49 
 * @version 1.0 
 */

public class WriteTxtToFile {
	public static void writeTXT_no_add(String fileName,String txt) throws IOException {
		File file=new File(fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file,false);//不用追加
		out.write(txt.getBytes("utf-8"));
		out.close();
	}
	public static void writeTXT_add(String fileName,String txt) throws IOException {
		File file=new File(fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file,true);//追加
		out.write(txt.getBytes("utf-8"));
		out.close();
	}
}
