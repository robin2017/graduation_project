import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Robin
 * @date 2017年3月2日 下午2:06:25
 * @version 1.0
 */

public class HandleImage {
	private void changeName(String folder, int[] arr, int start_out,
			int start_in) {
		System.out.println("文件夹为 :  " + folder);
		File file = new File(folder);
		if (file.exists()) {
			File[] files = file.listFiles();
			System.out.println("文件夹内的文件个数为  : " + files.length);
			System.out.println("数组的元素个数为 : " + arr.length);
			System.out.println("数组的元素求和为 : " + getSum(arr));
			String parent = files[0].getParent();
			System.out.println(parent);

			if (files.length == getSum(arr))
				System.out.println("文件夹内的个数与数组内之和匹配，可以运算");
			else {
				System.out.println("文件夹内的个数与数组内之和不匹配，终止");
				System.exit(0);
			}

			File[] newFile = new File[files.length];
			int index = 0;
			int ind = 0;
			loop: for (int i = 0; i < files.length; i++) {
				for (int j = start_out; j <= 50; j++) {
					for (int k = start_in; k <= 10; k++) {
						if (index >= arr.length)
							break loop;
						for (int p = 1; p <= arr[index]; p++) {
							newFile[ind++] = new File(parent + "\\" + j + "_"
									+ k + "_" + p + ".png");
						}
						index++;
					}
				}
			}

			for (int i = 0; i < newFile.length; i++) {
				// System.out.println(newFile[i].getPath());
				files[i].renameTo(newFile[i]);
			}
		}
	}

	private int getSum(int[] arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++)
			sum += arr[i];
		return sum;
	}

	private String changeString(String path, String add) {
		return path + "_" + add + "\\";
	}
	// path 路径 ,旧文件名称 ,新文件名称,n 改变倍数
	private void changeImage(String oldpath, String newpath, String oldimg,
			String newimg, double n) {
		try {
			File file = new File(oldpath + "\\" + oldimg);
			Image img = ImageIO.read(file);
			// 构造Image对象
			int wideth = img.getWidth(null); // 得到源图宽
			int height = img.getHeight(null); // 得到源图长
			BufferedImage tag = new BufferedImage((int) (n * wideth),
					(int) (n * height), BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(img, 0, 0, (int) (n * wideth),
					(int) (n * height), null); // 绘制后的图
			FileOutputStream out = new FileOutputStream(newpath + newimg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag); // 近JPEG编码
			out.close();
		} catch (IOException e) {
			System.out.println("处理文件出现异常");
			e.printStackTrace();
		}
	}

	private void changeFolder(String oldpath, String newpath) {
		File newp = new File(newpath);
		if (!newp.exists())
			newp.mkdir();

		File file = new File(oldpath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(i + "   -------   " + files[i].getName());
				changeImage(oldpath, newpath, files[i].getName(),
						files[i].getName(), 0.5);
			}
		}
	}
	private String BASEPATH="E:\\WorkSpace_VisualStudio\\graduation_projection\\all_apk\\500个apk的截图加标识\\";
	
	public void changeImageNameAndSmallImage() {
		System.out.println("---start---");
		changeName(path, arr, start_out, 1);
		changeFolder(path, changeString(path, "small"));
	}

	
	//只需改变如下的字段
	private int[] arr = {4,3,6,5,2,6,5,7,7,7,
			6,5,4,1,0,7,4,6,4,0};
	private int start_out=5;
	 
	private String path = BASEPATH+"20170228-1-71";
			

}
