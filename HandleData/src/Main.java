import java.io.IOException;

/**
 * Created by robin on 2017/2/24.
 */
public class Main {
    public static void main(String[] args) throws IOException {


        MyUtil util=new MyUtil();
        util.removeSelfEqual("a_total.txt","total1.txt");
        util.selectByThresholdAndDegree("total1.txt","result.txt","detail.txt");
        util.addNameForShow("detail.txt","result2.txt","apk_name.txt");

       // util.exceptAfromB("zz3.txt","zz2.txt","zzz2.txt");
    }
}