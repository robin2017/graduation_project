import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robin on 2017/2/24.
 */
public class Main {
    private static String BASEROOT_PART1="/Users/robin/workspace/graduation_project/DATA/part1_social_before/HashTopologyHandle/";
    private static String BASEROOT_PART2="/Users/robin/workspace/graduation_project/DATA/part2/HashTopologyHandle/";
    private static int MAX_MAX=1000;
    public static void main(String[] args) throws IOException {

      //  System.out.println(getTwoCode(9));
    //    System.out.println(getDistanceFromGreyCode(1000,1001));
      //  System.out.println(getDistanceFromTwoCode(1023,1024));
        handleDate_part1();
//testCode();



       //handleDate_part2();
//        int[] arr=new int[]{1,4,2,3,5};
//        System.out.println(solve(arr));


//       MyUtil util=new MyUtil();
//       // util.ImageInit("source500");
//    // util.removeSelfEqual("a_total.txt","total1.txt");
//       // util.selectByThresholdAndDegree("total1.txt","result.txt","detail.txt");
//       util.addNameForShow("detail.txt","detail_name.txt","apk_name.txt");
//
//         //util.exceptAfromB("zz2.txt","zz1.txt","zzz1.txt");
    }

    public static void clean(String path){
        File folder=new File(path);
        File[] files=folder.listFiles();
        for(File file:files){
            if(!(file.getName().equals("apk_name.txt")||file.getName().equals("total.txt"))&&file.getName().endsWith("txt")) {
                System.out.println("--->" + file.getName());
                file.delete();
            }

        }
    }

    public static void handleDate_part1() throws IOException {
        clean(BASEROOT_PART1);
        MyUtil util=new MyUtil();
        util.removeSelfEqual("total.txt", "total1.txt",BASEROOT_PART1);
        util.selectByThresholdAndDegree("total1.txt","result.txt","detail.txt",BASEROOT_PART1);
        util.addNameForShow("detail.txt","detail_name.txt","apk_name.txt",BASEROOT_PART1);

    }

    public static void handleDate_part2() throws IOException {
        clean(BASEROOT_PART2);
        MyUtil util=new MyUtil();
        util.removeSelfEqual("total.txt", "total1.txt",BASEROOT_PART2);
        util.selectByThresholdAndDegree("total1.txt","result.txt","detail.txt",BASEROOT_PART2);
        util.addNameForShow("detail.txt","detail_name.txt","apk_name.txt",BASEROOT_PART2);

    }



    public static int solve(int [] arr)
    {
        Integer []res = new Integer[arr.length];
        for(int i =0;i<arr.length;i++)
        {
            res[i]=1;
            for(int j=0;j<i;j++)
            {
                if(arr[j]<arr[i]&&(res[j]+1>res[i]))
                    res[i]=res[j]+1;
            }
        }
        int max=0;
        for(int k=0;k<res.length;k++)
        {
            if(res[k]>max)
                max=res[k];
        }
        return max;
    }


    public static void testCode(){
        HashMap<Integer,ArrayList<Integer>> hashMapGray=new HashMap<Integer,ArrayList<Integer>>();
        HashMap<Integer,ArrayList<Integer>> hashMapTwo=new HashMap<Integer,ArrayList<Integer>>();
        ArrayList<Integer> tmp;
        int[] arr_gray_max=new int[MAX_MAX];
        double[] arr_gray_avg=new double[MAX_MAX];
        int[] arr_gray_min=new int[MAX_MAX];
        int[] arr_two_max=new int[MAX_MAX];
        double[] arr_two_avg=new double[MAX_MAX];
        int[] arr_two_min=new int[MAX_MAX];
        for(int i=0;i<MAX_MAX;i++){
            for(int j=i;j<MAX_MAX;j++){
                int gap=j-i;
                int res_grey=getDistanceFromGreyCode(j,i);
                int res_two=getDistanceFromTwoCode(j,i);
                System.out.println(gap+"    "+res_grey+"     "+res_two);

                if(arr_gray_max[gap]<res_grey)
                    arr_gray_max[gap]=res_grey;

                tmp=hashMapGray.get(gap);
                if(tmp==null)
                    tmp=new ArrayList<>();
                tmp.add(res_grey);
                hashMapGray.put(gap, tmp);



                if(arr_two_max[gap]<res_two)
                    arr_two_max[gap]=res_two;

                tmp=hashMapTwo.get(gap);
                if(tmp==null)
                    tmp=new ArrayList<>();
                tmp.add(res_two);
                hashMapTwo.put(gap,tmp);
            }
        }
        for(Map.Entry<Integer, ArrayList<Integer>> entry:hashMapGray.entrySet()){
           // System.out.println(entry.getValue());
            arr_gray_avg[entry.getKey()]=getAvg(entry.getValue());
        }
        for(Map.Entry<Integer, ArrayList<Integer>> entry:hashMapTwo.entrySet()){
            // System.out.println(entry.getValue());
            arr_two_avg[entry.getKey()]=getAvg(entry.getValue());
        }
        int end=0;
    }


    private static double getAvg(ArrayList<Integer> arrayList){
        int sum=0;
        for(int i=0;i<arrayList.size();i++){
            sum+=arrayList.get(i);
        }
        return sum*1.0/arrayList.size();
    }

    public static int getDistanceFromGreyCode(int a,int b){
        return minDistance(getGreyCode(a),getGreyCode(b));
    }

    public static int getDistanceFromTwoCode(int a,int b){
        return minDistance(getTwoCode(a),getTwoCode(b));
    }
        /*
         * public static void main(String[] args) { String str1 = "intention";
         * String str2 = "execution"; System.out.println(minDistance(str1, str2)); }
         */
        private static final int WEIGHT_SUBSTITUTION = 1;

        /*
         * 时间空间复杂度都为 nm 标准的动态规划算法 18ms,击败14%
         */
        public static int minDistance(String str1, String str2) {
            int m = str1.length();
            int n = str2.length();
            int[][] table = new int[m + 1][n + 1];
            for (int i = 0; i <= m; i++)
                table[i][0] = i;
            for (int j = 0; j <= n; j++)
                table[0][j] = j;
            for (int i = 1; i <= m; i++)
                for (int j = 1; j <= n; j++) {
                    table[i][j] = minThree(
                            table[i - 1][j] + 1,
                            table[i][j - 1] + 1,
                            table[i - 1][j - 1]
                                    + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0
                                    : WEIGHT_SUBSTITUTION));
                }
            return table[m][n];
        }
    private static int minThree(int a, int b, int c) {
            return a < b ? (a < c ? a : c) : (b < c ? b : c);
        }


    private static String getGreyCode(int num){
        StringBuffer sb=new StringBuffer();
        for(int i=16;i>=1;i--){
            if(num%Math.pow(2,i+1)>=Math.pow(2,i-1)&&num%Math.pow(2,i+1)<=Math.pow(2,i-1)+Math.pow(2,i)-1)
                sb.append("1");
            else
                sb.append("0");
        }
        return sb.toString();
    }

    private static String getTwoCode(int num){
        num++;
        StringBuffer sb=new StringBuffer();

        for(int i=16;i>=1;i--){
            if(num>Math.pow(2,i-1)) {
                sb.append("1");
                num -=  Math.pow(2, i - 1);
            }
            else
                sb.append("0");


        }
        return sb.toString();
    }

}