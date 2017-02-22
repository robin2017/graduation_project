import java.io.*;

/**
 * Created by robin on 2017/2/22.
 */
public class Main {
    public static void main(String[] args){
        String dd=readTXT("total.txt");
        System.out.print("success");


    }

    public static String readTXT(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(
                    fileName).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    String[] str=s.split(" ");
                    if(str[1].equals(str[0])&&str[2].equals("1"))
                        continue;
//                    sb.append(s);
//                    sb.append("\n");
                    writeTXT_add("total_tt.txt",s+"\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
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
