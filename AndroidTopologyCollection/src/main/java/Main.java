import java.io.File;
import java.io.IOException;

/**
 * Created by robin on 2017/1/19.
 */
public class Main {

	public static String resultPath_windows="e:\\WorkSpace_VisualStudio\\graduation_projection\\all_apk\\library_social_result";
    public static String libPath_windows="e:\\WorkSpace_VisualStudio\\graduation_projection\\all_apk\\library_social";


    public static String resultPath_mac="/Users/robin/workspace/graduation_project/DATA/part1/HashLibraryCmp";
    public static String libPath_mac="/Users/robin/workspace/graduation_project/DATA/part1/HashLibrary";

    //如果签名低于最低长度，则不予计算
    //考虑到360加固 30---》40
    public static int LASTLENGTH=70;
    public static void main(String[] args) throws IOException{
        String resultPath=null;
        String libPath=null;
        if(System.getProperty("os.name").equals("Mac OS X")){
            resultPath=resultPath_mac;
            libPath=libPath_mac;
        }else{
            resultPath=resultPath_windows;
            libPath=libPath_windows;
        }
       	File file=new File(resultPath);
    	if(!file.exists())
    		file.mkdir();
    	test_all(libPath);
    }
    public static void test_1(String libPath) throws IOException{
    
    	String filePath=libPath+"\\1_1_微信.apk.txt";
        CompareFileToLib.compare(libPath,filePath);
    }
    
    public static void test_all(String libPath) throws IOException{
    	File[] files = new File(libPath).listFiles();
    	int cnt=0;
    	for (File file : files) {
    		cnt++;

    		String filePath=libPath+File.separator+file.getName();
            CompareFileToLib.compare(libPath,filePath);
          
    	}
    }
}
