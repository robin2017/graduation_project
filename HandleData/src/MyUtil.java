import java.io.*;
import java.util.HashMap;

/**
 * Created by robin on 2017/2/24.
 */
public class MyUtil {
    private static String BASEROOT="data/";
    private static double THRESHOLD=1;   //阈值：threshold
    private static int DEGREE=5;   //度：degree

    /*选出符合条件的记录，并按照度的大小排序给出记录*/
    public void selectByThresholdAndDegree(String fileName,String newName,String detailName){

    }

    /*将文件中的标示替换成应用名*/
    public void changeFlagToName(String fileName,String newName,String tableName) throws IOException {
        String table=ReadTxtFromFile.readTXT(BASEROOT + tableName);
        String arr[]=table.split("\n");
        HashMap<String,String> hm=new HashMap<String,String>();
        for(String flagname:arr){
            int first=flagname.indexOf("\"");
            int second=flagname.lastIndexOf("_");
            int third=flagname.lastIndexOf(".")==-1? flagname.length(): flagname.lastIndexOf(".");
            String flag=flagname.substring(first+1,second);
            String name=flagname.substring(second + 1, third);
            hm.put(flag,name);
        }
        String txt=ReadTxtFromFile.readTXT(BASEROOT+fileName);
        StringBuffer sb=new StringBuffer();
        String[] arr_txt=txt.split("\n");
        for(String record:arr_txt){
            String[] arr_txt_detail=record.split(" ");
            sb.append(hm.get(arr_txt_detail[0]));
            sb.append(" ");
            sb.append(hm.get(arr_txt_detail[1]));
            sb.append(" ");
            sb.append(arr_txt_detail[2]);
            sb.append("\n");
        }
        WriteTxtToFile.writeTXT_no_add(BASEROOT+newName,sb.toString());
    }

    /*去除掉文件中自己与自己相似度为1的记录*/
    public void removeSelfEqual(String fileName,String newName) {

        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(
                    BASEROOT+fileName).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    String[] str=s.split(" ");
                    if(str[1].equals(str[0])&&str[2].equals("1"))
                        continue;
                    writeTXT_add(BASEROOT+newName,s+"\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void writeTXT_add(String fileName,String txt) throws IOException {
        File file=new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file,true);//追加
        out.write(txt.getBytes("utf-8"));
        out.close();
    }
}



