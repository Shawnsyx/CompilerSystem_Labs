package Application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import GrammarAnalyze.GrammerAnalyzer;
import LexicalAnalyze.LexicalAnalyzer;
import SemanticAnalyze.SemanticAnalyzer;

@SuppressWarnings("serial")
public class Main extends JFrame{
	private LexicalAnalyzer LA =new LexicalAnalyzer();
	private GrammerAnalyzer GA=new GrammerAnalyzer();
	private SemanticAnalyzer SA=new SemanticAnalyzer();
	//菜单panel及选项按钮
	private JPanel menuPanel=new JPanel();
	private JPanel fileContentPanel=new JPanel();
	private JButton fileB=new JButton("打开文件");
	private JButton clearB=new JButton("清空");
	private JButton LRulesB=new JButton("词法规则");
	private JFrame LRulesFrame=LA.getRulesFrame();
	private JButton LAB=new JButton("词法分析");
	private JButton GRulesB=new JButton("语法规则");
	private JButton GAB=new JButton("语法分析");
	private JButton SRulesB=new JButton("语义规则");
	private JButton SAB=new JButton("语义分析");
	
	//文件内容显示区域
	private JTextArea fileTA=new JTextArea();
	private JScrollPane fileContentJSP=new JScrollPane(fileTA);
	
	//文件内容
	private List<String>lines=new ArrayList<>();
	
	public static void main(String[] args) {
		Main m=new Main();
		m.initial();
	}
	private void initial() {
		JFrame jf=this;
		
		
		//菜单
		menuPanel.setBounds(0, 0, 800, 30);
		menuPanel.setBackground(Color.decode("#FFFF55"));
		menuPanel.setLayout(null);
		
		//"打开文件"按钮
		
		fileB.setBounds(0, 0, 100, 30);
		fileB.setBackground(Color.decode("#88FFE7"));
		fileB.setFocusPainted(false);
		fileB.setBorder(null);
		fileB.setFont(new Font("", Font.BOLD, 20));
		fileB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} 
				catch (Exception e1) {
				    e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(chooser);
				
				FileNameExtensionFilter filter=new FileNameExtensionFilter("Text File", "txt");
				chooser.setFileFilter(filter);
				int returnVal=chooser.showOpenDialog(jf);
				if (returnVal==JFileChooser.APPROVE_OPTION) {
					try {
						lines=Files.readAllLines(chooser.getSelectedFile().toPath());
						//确保读入文本后才更新
						fileTA.setText("");
						int lineN=0;
						for(String line:lines) {
							fileTA.append((++lineN)+":"+line+"\n");
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "文件错误", "", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		menuPanel.add(fileB);
		
		
		//"清空"按钮
		clearB.setBounds(100, 0, 100, 30);
		clearB.setBackground(Color.decode("#FFFF55"));
		clearB.setFocusPainted(false);
		clearB.setBorder(null);
		clearB.setFont(new Font("", Font.BOLD, 20));
		clearB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//清空lines
				lines.clear();
				fileTA.setText("");
			}
		});
		menuPanel.add(clearB);
		
		
		//"词法规则按钮"
		LRulesB.setBounds(200, 0, 100, 30);
		LRulesB.setBackground(Color.decode("#88FFE7"));
		LRulesB.setFocusPainted(false);
		LRulesB.setBorder(null);
		LRulesB.setFont(new Font("",Font.BOLD,20));
		LRulesB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//显示词法规则界面
				LRulesFrame.setVisible(true);
			}
		});
		menuPanel.add(LRulesB);
		
		//"词法分析"按钮
		LAB.setBounds(300, 0, 100, 30);
		LAB.setBackground(Color.decode("#FFFF55"));
		LAB.setFocusPainted(false);
		LAB.setBorder(null);
		LAB.setFont(new Font("", Font.BOLD, 20));
		LAB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (lines.size()==0) {
					JOptionPane.showMessageDialog(null, "未输入文件", "", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					LA.analyze(lines).setVisible(true);
				}
			}
		});
		menuPanel.add(LAB);
		add(menuPanel);
		
		
		//"语法规则按钮"
		GRulesB.setBounds(400, 0, 100, 30);
		GRulesB.setBackground(Color.decode("#88FFE7"));
		GRulesB.setFocusPainted(false);
		GRulesB.setBorder(null);
		GRulesB.setFont(new Font("",Font.BOLD,20));
		GRulesB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//显示语法规则界面
				GA.getGRFrame().setVisible(true);
			}
		});
		menuPanel.add(GRulesB);
		
		//"语法分析"按钮
		GAB.setBounds(500, 0, 100, 30);
		GAB.setBackground(Color.decode("#FFFF55"));
		GAB.setFocusPainted(false);
		GAB.setBorder(null);
		GAB.setFont(new Font("", Font.BOLD, 20));
		GAB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (lines.size()==0) {
					JOptionPane.showMessageDialog(null, "未输入文件", "", JOptionPane.INFORMATION_MESSAGE);
				}
				//显示语法分析界面
//				GA.analyze(tokens).setVisible(true);
			}
		});
		menuPanel.add(GAB);
		add(menuPanel);
		
		
		//"语义规则按钮"
		SRulesB.setBounds(600, 0, 100, 30);
		SRulesB.setBackground(Color.decode("#88FFE7"));
		SRulesB.setFocusPainted(false);
		SRulesB.setBorder(null);
		SRulesB.setFont(new Font("",Font.BOLD,20));
		SRulesB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//显示语义规则界面
				SA.getSRFrame().setVisible(true);
			}
		});
		menuPanel.add(SRulesB);
		
		//"语义分析"按钮
		SAB.setBounds(700, 0, 100, 30);
		SAB.setBackground(Color.decode("#FFFF55"));
		SAB.setFocusPainted(false);
		SAB.setBorder(null);
		SAB.setFont(new Font("", Font.BOLD, 20));
		SAB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (lines.size()==0) {
					JOptionPane.showMessageDialog(null, "未输入文件", "", JOptionPane.INFORMATION_MESSAGE);
				}
				//显示语义分析界面
//				SA.analyze().setVisible(true);
			}
		});
		menuPanel.add(SAB);
		add(menuPanel);
		
		
		//文件内容区域
		fileContentPanel.setLayout(null);
		fileContentPanel.setBounds(0, 30, 800, 820);
		fileContentPanel.setBackground(Color.pink);
		fileTA.setFont(new Font("", Font.PLAIN, 20));
		fileTA.setEditable(false);
		fileTA.setCaretPosition(0);
		fileContentJSP.setBounds(10, 10, 780, 800);
		fileContentJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		fileContentJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		fileContentPanel.add(fileContentJSP);
		add(fileContentPanel);

		

		setLayout(null);
		setLocation(70, 70);
		setSize(806, 886);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
