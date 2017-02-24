import java.io.IOException;

/**
 * Created by robin on 2017/2/24.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        MyUtil util=new MyUtil();
        //util.removeSelfEqual("total.txt","dd.txt");
        util.changeFlagToName("total.txt","dddd.txt","apk_name.txt");
    }
}