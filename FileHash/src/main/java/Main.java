import java.io.*;
import java.util.HashMap;

class Main{
    private static String sourcePath="/Users/robin/workspace/graduation_project/DATA/md5/SOURCE/";
    private static String aimPath="/Users/robin/workspace/graduation_project/DATA/md5/";
    private static String folderPath="/Users/robin/workspace/python/source/DownApplication/apk_backup_justcopy/test_finance_500";
    public static void main(String[] args) throws Exception {
        getAllMD5();
       // changeNameToMD5("table_4_4.txt");
      // getMD5FromFlag("table_4_7.txt");
       // getAllFlagMD5();
    }
    public static void getAllMD5() throws Exception {
        File folder=new File(folderPath);
        File[] files=folder.listFiles();
        int cnt=1;
        StringBuffer sb=new StringBuffer();
        for(File file:files){
            if(isAPK(file.getName())) {
                System.out.println(cnt++);
                System.out.println(file.getName() + ":::" + MD5Checksum.getMD5Checksum(file.getAbsolutePath()));
                sb.append(file.getName() + ":::" + MD5Checksum.getMD5Checksum(file.getAbsolutePath())+"\n");
            }

        }
        WriteTxtToFile.writeTXT_no_add(aimPath + "md5file_other.txt", sb.toString());
    }
    public static void getAllFlagMD5() throws Exception {
        File folder=new File(folderPath);
        File[] files=folder.listFiles();
        int cnt=1;
        StringBuffer sb=new StringBuffer();
        for(File file:files){
            if(isAPK(file.getName())) {
                System.out.println(cnt++);
                System.out.println(getFlag(file.getName()) + ":::" + MD5Checksum.getMD5Checksum(file.getAbsolutePath()));
                sb.append(getFlag(file.getName()) + ":::" + MD5Checksum.getMD5Checksum(file.getAbsolutePath())+"\n");
            }

        }
        WriteTxtToFile.writeTXT_no_add(aimPath + "md5flag.txt", sb.toString());
    }

    public static void changeNameToMD5(String tableName) throws IOException {
        HashMap<String,String> hashMap=new HashMap<String, String>();
        File file=new File(aimPath+"md5file.txt");
        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        try {
            String s;
            while ((s = in.readLine()) != null) {
                //System.out.println(s);
                String arr[]=s.split(":::");
                hashMap.put(arr[0],arr[1]);
            }
        } finally {
            in.close();
        }

        StringBuffer stringBuffer=new StringBuffer();
        File source=new File(sourcePath+tableName);
        BufferedReader inSource = new BufferedReader(new FileReader(source.getAbsoluteFile()));
        try {
            String s;
            while ((s = inSource.readLine()) != null) {
                System.out.println(s);
                System.out.println(removeTXTFlag(s)+"===>"+hashMap.get(removeTXTFlag(s)));
                stringBuffer.append(hashMap.get(removeTXTFlag(s))+"\r\n");
            }
        } finally {
            in.close();
        }
        WriteTxtToFile.writeTXT_no_add(sourcePath+tableName+".md5.txt",stringBuffer.toString());

    }

    public static void getMD5FromFlag(String tableName) throws IOException {
        HashMap<String,String> hashMap=new HashMap<String, String>();
        File file=new File(aimPath+"md5flag.txt");
        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        try {
            String s;
            while ((s = in.readLine()) != null) {
                //System.out.println(s);
                String arr[]=s.split(":::");
                hashMap.put(arr[0],arr[1]);
            }
        } finally {
            in.close();
        }

        StringBuffer stringBuffer=new StringBuffer();
        File source=new File(sourcePath+tableName);
        BufferedReader inSource = new BufferedReader(new FileReader(source.getAbsoluteFile()));
        try {
            String s;
            while ((s = inSource.readLine()) != null) {
                System.out.println(s);
                System.out.println(s+"===>"+hashMap.get(s));
                stringBuffer.append(hashMap.get(s)+"\r\n");
            }
        } finally {
            in.close();
        }
        WriteTxtToFile.writeTXT_no_add(sourcePath+tableName+".md5.txt",stringBuffer.toString());

    }
    private static String removeTXTFlag(String str){
        return str.substring(0,str.length()-4);
    }

    private static boolean isAPK(String  str){
        if(str.contains(".jpg"))
            return false;
        else if(str.contains(".apk"))
            return true;
        else
            return false;
    }
    private static String getFlag(String string){
        String[] arr=string.split("_");
        return arr[0]+"_"+arr[1];
    }
}