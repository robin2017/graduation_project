import java.io.*;
import java.util.*;

/**
 * Created by robin on 2017/2/24.
 */
public class MyUtil {
 //   private static String BASEROOT="data/image_data/";
    private static String IMAGEROOT="data/image/";
    private static double IMAGETHRESHOLD=0.1;
    //part1 阈值为  1，    0.9      0.4
    //part2 阈值为  1,0.9,0.4
    private static double THRESHOLD=0.55;   //阈值：threshold
    private static int DEGREE=1;   //度：degree    20,10,7

    public void ImageInit(String fold) throws IOException {
        File folder=new File(IMAGEROOT+fold);
        File[] files=folder.listFiles();
        for(File file:files){
            System.out.println(file.getName());
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                try {
                    String s;
                    while ((s = in.readLine()) != null) {
                        String[] arr=s.split(" ");
                        if(Double.parseDouble(arr[2])<IMAGETHRESHOLD)
                            continue;
                        sb.append(getFlatFromPNGTXT(arr[0])+" "+getFlatFromPNGTXT(arr[1])+" "+arr[2]);
                        sb.append("\n");
                    }
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(sb);
            WriteTxtToFile.writeTXT_add(IMAGEROOT+"RES.txt",sb.toString());
        }
    }
    private String getFlatFromPNGTXT(String str){
        return str.substring(0,str.length()-10);
    }

    /*从文件a中选出非文件b的内容*/
    public void exceptAfromB(String fileA,String fileB,String Ab,String BASEROOT) throws IOException {
        String strA=ReadTxtFromFile.readTXT(BASEROOT+fileA);
        String strB=ReadTxtFromFile.readTXT(BASEROOT+fileB);
        HashSet<String> set=new HashSet<String>();
        String[] arr=strB.split("\n");
        for(String record:arr){
            set.add(record);
        }
        StringBuffer sb=new StringBuffer();
        String[] arr2=strA.split("\n");
        for(String record:arr2){
            if(!set.contains(record))
                sb.append(record+"\n");
        }
        WriteTxtToFile.writeTXT_no_add(BASEROOT+Ab,sb.toString());
    }

    /*选出符合条件的记录，并按照度的大小排序给出记录*/
    public void selectByThresholdAndDegree(String fileName,String newName,String detailName,String BASEROOT) throws IOException {
        //第一轮先对达到阈值的进行统计
        HashMap<String,Integer> hashMap=new HashMap<String,Integer>();
        String str=ReadTxtFromFile.readTXT(BASEROOT + fileName);
        String arr[]=str.split("\n");
        for(String record:arr){
            String[] items=record.split(" ");
            if(Double.parseDouble(items[2])>=THRESHOLD){
                if (hashMap.containsKey(items[0]))
                    hashMap.put(items[0], hashMap.get(items[0]) + 1);
                else
                    hashMap.put(items[0], 1);

//                if (hashMap.containsKey(items[1]))
//                    hashMap.put(items[1], hashMap.get(items[1]) + 1);
//                else
//                    hashMap.put(items[1], 1);

            }
        }

        //第二轮：选出满足度数的名称（标示）
        HashMap<String,Integer> hm=new HashMap<String,Integer>();
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue()>=DEGREE){
                hm.put(entry.getKey(),entry.getValue());
            }
        }
        //第三轮：将这些名称排序并输出到文档
        HashMap<String,Integer> detail=sortByComparator(hm);
        StringBuffer sb=new StringBuffer();
        for(Map.Entry<String,Integer> entry:detail.entrySet()){
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
       // WriteTxtToFile.writeTXT_no_add(BASEROOT+detailName,sb.toString());
        //第四步：在文本中选出带有这些名称的记录
        HashMap<String,Integer> res_det=new HashMap<String,Integer>();

        StringBuffer res=new StringBuffer();
        for(String record:arr) {
            String[] items = record.split(" ");
            if((hm.containsKey(items[0])|hm.containsKey(items[1]))&Double.parseDouble(items[2])>=THRESHOLD){
                res.append(record+"\n");
                if (res_det.containsKey(items[0]))
                    res_det.put(items[0], res_det.get(items[0]) + 1);
                else
                    res_det.put(items[0], 1);

//                if (res_det.containsKey(items[1]))
//                    res_det.put(items[1], res_det.get(items[1]) + 1);
//                else
//                    res_det.put(items[1], 1);

            }
        }
        WriteTxtToFile.writeTXT_no_add(BASEROOT+newName,res.toString());

        HashMap<String,Integer> detail2=sortByComparator(res_det);
        StringBuffer sb2=new StringBuffer();
        for(Map.Entry<String,Integer> entry:detail2.entrySet()){
            sb2.append(entry.getKey());
            sb2.append(" ");
            sb2.append(entry.getValue());
            sb2.append("\n");
        }
         WriteTxtToFile.writeTXT_no_add(BASEROOT+detailName,sb2.toString());
    }


    private static HashMap sortByComparator(HashMap unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());
        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
        HashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    /*将应用名添加到结果中*/
    public void addNameForShow(String fileName,String newName,String tableName,String BASEROOT) throws IOException {
        String table=ReadTxtFromFile.readTXT(BASEROOT + tableName);
        String arr[]=table.split("\n");
        HashMap<String,String> hm=new HashMap<String,String>();
        for(String flagname:arr){
            int first=flagname.indexOf("\"");
            int second=flagname.lastIndexOf("_");
            int third=flagname.lastIndexOf(".")==-1? flagname.length(): flagname.lastIndexOf(".");
            String flag=flagname.substring(first+1,second);
            String name=flagname.substring(second + 1, third);
            name=name.replace(' ','-');
            hm.put(flag,name);
        }

        String txt=ReadTxtFromFile.readTXT(BASEROOT+fileName);
        StringBuffer sb=new StringBuffer();
        String[] arr_txt=txt.split("\n");
        for(String record:arr_txt){
            String[] arr_txt_detail=record.split(" ");
            sb.append(arr_txt_detail[0]);
            sb.append(" ");
            sb.append(hm.get(arr_txt_detail[0]));
            sb.append(" ");
            sb.append(arr_txt_detail[1]);

            sb.append("\n");
        }
        WriteTxtToFile.writeTXT_no_add(BASEROOT+newName,sb.toString());

    }

    /*将文件中的标示替换成应用名*/
    public void changeFlagToName(String fileName,String newName,String tableName,String BASEROOT) throws IOException {
        String table=ReadTxtFromFile.readTXT(BASEROOT + tableName);
        String arr[]=table.split("\n");
        HashMap<String,String> hm=new HashMap<String,String>();
        for(String flagname:arr){
            int first=flagname.indexOf("\"");
            int second=flagname.lastIndexOf("_");
            int third=flagname.lastIndexOf(".")==-1? flagname.length(): flagname.lastIndexOf(".");
            String flag=flagname.substring(first+1,second);
            String name=flagname.substring(second + 1, third);
            name=name.replace(' ','-');
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
    public void removeSelfEqual(String fileName,String newName,String BASEROOT) {

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



