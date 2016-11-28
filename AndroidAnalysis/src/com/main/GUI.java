package com.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.Thread.AnalysisThread;
import com.Thread.RunThread;

/**
 * @author Robin
 * @date 2016年4月13日 上午8:29:56
 * @version 1.0
 */
public class GUI extends JFrame {
	// 1.成员类
	private class openL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(GUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION)
				choosePathReminder.setText(c.getCurrentDirectory().toString()
						+ File.separator + c.getSelectedFile().getName());
			if (rVal == JFileChooser.CANCEL_OPTION)
				choosePathReminder.setText("未选择文件");
			else {
				toCollection.setEnabled(true);
				apk = new File(choosePathReminder.getText());
			}
		}
	}

	private class collectionL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			toCollection.setEnabled(false);
			File folder = new File(choosePathReminder.getText());
			ct = new AnalysisThread(folder);
			ct.start();
		}
	}

	private class openLibL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int rVal = c.showOpenDialog(GUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION)
				chooseLibPathReminder.setText(c.getCurrentDirectory()
						.toString()
						+ File.separator
						+ c.getSelectedFile().getName());
			if (rVal == JFileChooser.CANCEL_OPTION)
				chooseLibPathReminder.setText("未选择文件");
			else {
				calculator.setEnabled(true);
				chooseLibPath.setEnabled(false);
			}
		}
	}

	private class calL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			calculator.setEnabled(false);
			conclusion.setText("");
			String libPath = chooseLibPathReminder.getText();
			String tmp=apk.getAbsolutePath();
			String filePath =tmp.substring(0,tmp.lastIndexOf("apk"))+"txt"; 
			RunThread run_ = new RunThread(libPath, filePath);
			run_.start();
		}
	}

	// 2.成员变量
	private JTextField title = new JTextField(12);

	private JButton choosePath = new JButton("选择待测apk文件");
	private JTextField choosePathReminder = new JTextField(30);

	private static JButton toCollection = new JButton("采集签名");
	private static JTextField toCollectionReminder = new JTextField(30);

	private static JButton chooseLibPath = new JButton("选择签名库文件夹");
	private JTextField chooseLibPathReminder = new JTextField(30);

	private JButton calculator = new JButton("计算相似度");
	private static JTextField showInfo = new JTextField(30);

	private static JTextArea conclusion = new JTextArea(15, 80);
	// 使用的是双反斜，与File.separator一样

	private AnalysisThread ct = null;
	private File apk = null;

	// 3.构造函数
	public GUI() {
		super("AndroidAnalysis");
		// 3.1.组件设置
		title.setText("安卓应用相似性检测");
		title.setBorder(null);
		title.setFont(new Font("Serif", 0, 24));

		choosePath.addActionListener(new openL());
		toCollection.addActionListener(new collectionL());
		chooseLibPath.addActionListener(new openLibL());
		calculator.addActionListener(new calL());

		title.setEditable(false);
		choosePathReminder.setEditable(false);
		toCollectionReminder.setEditable(false);
		chooseLibPathReminder.setEditable(false);
		showInfo.setEditable(false);

		this.toCollection.setEnabled(false);
		this.calculator.setEnabled(false);
		this.chooseLibPath.setEnabled(false);

		// 3.2.容器布局设置
		/*
		 * -1----mainPane ----2----operationPanel -------3----titlePanel
		 * -------3----pathChoose -------3----collectionOperation
		 * -------3----libChoose -------3----runOperation
		 * ----2----conclusionPanel
		 */
		Container mainPane = this.getContentPane();// 一级容器
		// 第一部分：操作选择
		JPanel operationPanel = new JPanel();// 二级容器
		operationPanel.setLayout(new GridLayout(5, 1, 0, 0));

		JPanel titlePanel = new JPanel(); // 三级容器
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(title);
		operationPanel.add(titlePanel);

		JPanel pathChoose = new JPanel();// 三级容器
		pathChoose.setLayout(new FlowLayout(FlowLayout.LEFT));// 容器设置布局
		pathChoose.add(choosePath);
		pathChoose.add(choosePathReminder);
		operationPanel.add(pathChoose);// 三级容器添加到二级容器

		JPanel collectionOperation = new JPanel();
		collectionOperation.setLayout(new FlowLayout(FlowLayout.LEFT));
		collectionOperation.add(toCollection);
		collectionOperation.add(toCollectionReminder);
		operationPanel.add(collectionOperation);// 三级容器添加到二级容器

		JPanel libChoose = new JPanel();// 三级容器
		libChoose.setLayout(new FlowLayout(FlowLayout.LEFT));// 容器设置布局
		libChoose.add(chooseLibPath);
		libChoose.add(chooseLibPathReminder);
		operationPanel.add(libChoose);// 三级容器添加到二级容器

		JPanel runOperation = new JPanel();
		runOperation.setLayout(new FlowLayout(FlowLayout.LEFT));
		runOperation.add(calculator);
		runOperation.add(showInfo);
		operationPanel.add(runOperation);// 三级容器添加到二级容器

		// 第二部分：结论显示
		JPanel conclusionPanel = new JPanel();// 二级容器
		conclusionPanel.setLayout(new GridLayout(1, 1, 10, 10));
		JScrollPane jsp = new JScrollPane(conclusion);
		conclusionPanel.add(jsp);

		// 二级容器添加到一级容器
		mainPane.add("North", operationPanel);
		mainPane.add("Center", conclusionPanel);
	}

	// 4.成员函数
	// 4.1 回调函数（被按钮监听器的子线程调用）
	public static void collectionCallFunction(String info) {
		if (info.equals("签名已保存！！")) { // 此处不符合面向对象的处理法则！！！！！！！！！！这是个潜在的bug
			chooseLibPath.setEnabled(true);
			toCollection.setEnabled(false);
		}
		toCollectionReminder.setText(info);
	}

	public static void collectionResultCallFunction(String info) {
		conclusion.append(info);
		conclusion.append("\n");
	}

	public static void callFuctionShowInfo(String str) {
		showInfo.setText(str);
	}

	// 5.主函数
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(800, 500);
		gui.setVisible(true);
	}
}
