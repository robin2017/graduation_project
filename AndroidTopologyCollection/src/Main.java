import java.io.File;
import java.io.IOException;

/**
 * Created by robin on 2017/1/19.
 */
public class Main {
	public static String resultPath="e:\\WorkSpace_VisualStudio\\graduation_projection\\all_apk\\library_social_result";
    public static String libPath="e:\\WorkSpace_VisualStudio\\graduation_projection\\all_apk\\library_social";
    
    public static void main(String[] args) throws IOException{
       	File file=new File(resultPath);
    	if(!file.exists())
    		file.mkdir();
    	test_all();
    }
    public static void test_1() throws IOException{
    
    	String filePath=libPath+"\\1_1_微信.apk.txt";
        CompareFileToLib.compare(libPath,filePath);
    }
    
    public static void test_all() throws IOException{
    	File[] files = new File(libPath).listFiles();
    	int cnt=0;
    	for (File file : files) {
    		cnt++;
    		if(cnt<2)
    			continue;
    		String filePath=libPath+File.separator+file.getName();
            CompareFileToLib.compare(libPath,filePath);
          
    	}
    }
}
