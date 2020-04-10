package LexicalAnalyze;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class LexicalAnalyzer {
	//关键字集合
	@SuppressWarnings("serial")
	private final Set<String>keyWords= new HashSet<String>(){{
		add("auto"); add("break"); add("case"); add("char"); add("const"); 
		add("continue"); add("default"); add("do"); add("double"); add("else"); 
		add("enum"); add("extern"); add("float"); add("for"); add("goto"); 
		add("if"); add("int"); add("long"); add("register"); add("return"); 
		add("short"); add("signed"); add("sizeof"); add("static"); add("struct"); 
		add("switch"); add("typedef"); add("union"); add("unsigned"); add("void"); 
		add("volatile"); add("while");
	}};
	//双字符运算符
	@SuppressWarnings("serial")
	private final Set<String>doubleCharOP=new HashSet<String>() {{
		add("+="); add("-="); add("*="); add("/="); add("++"); add("--"); add("!=");
		add("=="); add(">="); add("<="); add(">>"); add("<<"); add("&&"); add("||");
	}};
	//状态转换表
	DFATable dfa=new DFATable();
	//tokens词法单元
	private List<String[]>tokens;
	//trans状态转换过程
	private List<String[]>trans;
	//词法错误
	private List<String[]>errors;
	
	private final LexicalRules LRules=new LexicalRules();
	//词法分析界面
	private JFrame LAFrame=new JFrame("词法分析");
	private JPanel LAPanel=new JPanel();
	//token
	private JLabel tokenLabel=new JLabel("Token",JLabel.CENTER);
	private JTable tokenTable;
	private JScrollPane tokenJSP;
	//DFA状态转换
	private JLabel transLabel=new JLabel("DFA状态转换",JLabel.CENTER);
	private JTable transTable;
	private JScrollPane transJSP;
	//词法错误
	private JLabel errorLabel=new JLabel("词法错误",JLabel.CENTER);
	private JTable errorTable;
	private JScrollPane errorJSP;

	public LexicalAnalyzer() {
		//词法分析窗口
		LAFrame.setLocation(130, 130);
		LAFrame.setSize(1505, 835);
		LAFrame.setResizable(false);
		LAPanel.setLayout(null);
		LAPanel.setBounds(0, 0, 1500, 800);
		LAFrame.add(LAPanel);
		//token标签
		tokenLabel.setBounds(150, 0, 200, 40);
		tokenLabel.setFont(new Font("", Font.BOLD, 25));
		tokenLabel.setForeground(Color.BLUE);
		LAPanel.add(tokenLabel);	
		//DFA状态转换标签
		transLabel.setBounds(650, 0, 200, 40);
		transLabel.setFont(new Font("", Font.BOLD, 25));
		transLabel.setForeground(Color.BLUE);
		LAPanel.add(transLabel);
		//词法错误标签
		errorLabel.setBounds(1150, 0, 200, 40);
		errorLabel.setFont(new Font("", Font.BOLD, 25));
		errorLabel.setForeground(Color.BLUE);
		LAPanel.add(errorLabel);	
	}
	
	public JFrame getRulesFrame() {
		return LRules.getRulesFrame();
	}
	
	public JFrame analyze(List<String> lines) {
		//清空之前的文件的分析
		tokens=new ArrayList<>();
		trans=new ArrayList<>();
		errors=new ArrayList<>();
		//移除3个表格
		if (tokenJSP!=null) {
			LAPanel.remove(tokenJSP);
		}
		if (transJSP!=null) {
			LAPanel.remove(transJSP);
		}
		if (errorJSP!=null) {
			LAPanel.remove(errorJSP);
		}
		//开始词法分析
		int lineCount=lines.size();
		assert lineCount>0;
		String lastLine=lines.get(lineCount-1);
		lines.set(lineCount-1, lastLine+' ');//在文本末尾加一个空格，使得每一个字符都有下一个字符
		int line=0;//行号，显示界面时转换为从1开始
		String word="";//栈中接收的字符
		int curState=0;//当前状态
		String curTrans="";//从开始状态转移到当前状态的转移过程
		for(String lineStr:lines) {
			char[]charsInLine=lineStr.toCharArray();
			for (int i = 0; i < charsInLine.length; i++) {
				char c=charsInLine[i];
				
				int nextState=dfa.nextStateByChar(curState, c);//获取下一个状态
				if(nextState==-1) {//-1表示没有下一个状态，应该判断是否得到了一个token
					//若当前状态是终止状态，则接收
					if(dfa.isFinalState(curState)) {
						boolean received=true;
						switch (curState) {
						case 1:
							if (keyWords.contains(word)) {
								tokens.add(new String[]{word,"<"+word+",_>","关键字"});//一词一码
							}
							else {
								tokens.add(new String[]{word,"<IDN,"+word+">","标识符"});//多词一码
							}
							break;
						case 2:
						case 8:
							tokens.add(new String[]{word,"<DEC,"+word+">","10进制整型常量"});//一型一码
							break;
						case 4:
						case 7:
							tokens.add(new String[]{word,"<Float,"+word+">","浮点型常量"});//一型一码
							break;
						case 9:
							tokens.add(new String[]{word,"<OCT,"+word+">","8进制整型常量"});//一型一码
							break;
						case 11:
							tokens.add(new String[]{word,"<HEX,"+word+">","16进制整型常量"});//一型一码
							break;
						case 12:
						case 15:
							tokens.add(new String[]{word,"<"+word+",_>","运算符"});//一词一码
							break;
						case 13:
							if(doubleCharOP.contains(word)) {
								tokens.add(new String[]{word,"<"+word+",_>","运算符"});//一词一码
							}
							else {
								//报错：非法运算符
								errors.add(new String[]{word,"非法运算符",""+(line+1)});
								received=false;
							}
							break;
						case 14:
							tokens.add(new String[]{word,"<"+word+",_>","界符"});//一词一码
							break;
						case 18:
							tokens.add(new String[]{word,"<NOTE,"+"_>","注释"});//多词一码
							break;
						case 21:
							tokens.add(new String[]{word,"<String,"+word+">","字符串常量"});//一型一码
							break;
						case 23:
							tokens.add(new String[]{word,"<Charater,"+word+">","字符常量"});//一型一码
							break;
						default:
							assert false;
							break;
						}
						if(received) {//若成功识别token，则保存状态转换过程
							trans.add(new String[]{word,curTrans});
						}
					}
					else {//若当前状态不是终止状态，则错误。
						switch (curState) {
						case 0:
							if(c!=' '&&c!='\t') {
								errors.add(new String[]{c+"","非法字符",""+(line+1)});
								i++;//因为下面有i--，而应该跳过该字符
							}
							break;
						case 3:
						case 5:
						case 6:
							errors.add(new String[]{word,"错误的浮点数",""+(line+1)});
							break;
						case 16:
						case 17:
							errors.add(new String[]{word,"不完整的注释",""+(line+1)});
							break;
						case 19:
						case 20:
							errors.add(new String[]{word,"不完整的字符串",""+(line+1)});
							break;
						case 22:
						case 23:
							errors.add(new String[]{word,"不完整的字符常量",""+(line+1)});
							break;
						default:
							assert false;
							break;
						}
					}
					//无论是否成功接收还是错误，都要从状态0开始，清空栈，清空当前状态转换过程
					curState=0;
					word="";
					curTrans="";
					if (c!=' '&&c!='\t') {//无论是否成功接收还是错误，若不为空白字符，则重新识别c，若是空白字符则从c的下一个接收
						i--;//重新识别c
					}
				}
				else if (nextState==-2) {//-2表示遇到\n\r
					//忽略\n\r字符
				}
				else{//正常的状态转移
					curTrans+="<"+curState+","+c+","+nextState+"> ";
					curState=nextState;
					word+=c;
				}
			}
			line++;
		}
		//由于在文本末尾加了空格，若栈中仍有字符，则一定是未封闭的字符串或注释，接收了空格
		if(word.length()>0) {//若word长度大于0，意味着有未封闭的字符串或注释
			if (word.charAt(0)=='"') {
				errors.add(new String[]{word,"不完整的字符串",""+(line+1)});
			}
			else {
				errors.add(new String[]{word,"不完整的注释，缺少*/",""+(line+1)});
			}
		}
		
		
		//填token表
		int itemsCount1=tokens.size();
		String[][]items1=new String[itemsCount1][3];
		for (int i = 0; i < itemsCount1; i++) {
			items1[i]=tokens.get(i);
		}
		tokenTable=new JTable(items1, new String[] {"输入项","Token序列","类型"});
		tokenJSP=new JScrollPane(tokenTable);
		//token表格属性
		tokenTable.setRowHeight(30);//行高
		for (int i = 0; i <3; i++) {
			tokenTable.getColumnModel().getColumn(i).setPreferredWidth(161);//列宽
		}
		tokenTable.getTableHeader().setReorderingAllowed(false);//不能调整列的顺序
		tokenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//无需适应窗口大小
		tokenTable.setEnabled(false);//不能编辑
		DefaultTableCellRenderer r1=new DefaultTableCellRenderer();
		r1.setHorizontalAlignment(JLabel.CENTER);
		tokenTable.setDefaultRenderer(Object.class, r1);//数据居中显示
		tokenJSP.setBounds(0, 40, 500, 760);
		tokenJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tokenJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		LAPanel.add(tokenJSP);
		
		
		//填DAF状态转换过程
		int itemCount2=trans.size();
		String[][]item2=new String[itemCount2][2];
		for (int i = 0; i < itemCount2; i++) {
			item2[i]=trans.get(i);
		}
		transTable=new JTable(item2,new String[] {"识别的单词","DFA状态转换"});
		transJSP=new JScrollPane(transTable);
		//DFA状态转换表格属性
		transTable.setRowHeight(30);//行高
		TableColumnModel tcm2=transTable.getColumnModel();
		tcm2.getColumn(0).setPreferredWidth(160);
		tcm2.getColumn(1).setPreferredWidth(323);
		transTable.getTableHeader().setReorderingAllowed(false);//不能调整列的顺序
		transTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//无需适应窗口大小
		transTable.setEnabled(false);//不能编辑
		DefaultTableCellRenderer r2=new DefaultTableCellRenderer();
		r2.setHorizontalAlignment(JLabel.CENTER);
		transTable.setDefaultRenderer(Object.class, r2);//数据居中显示
		transJSP.setBounds(500, 40, 500, 760);
		transJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		transJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		LAPanel.add(transJSP);	
		
		
		//填词法错误项
		int itemCount3=errors.size();
		String[][]item3=new String[itemCount3][3];
		for (int i = 0; i < itemCount3; i++) {
			item3[i]=errors.get(i);
		}
		errorTable=new JTable(item3,new String[] {"错误项","错误原因","行数"});
		errorJSP=new JScrollPane(errorTable);
		//词法错误表格属性
		errorTable.setRowHeight(30);//行高
		TableColumnModel tcm3=errorTable.getColumnModel();
		tcm3.getColumn(0).setPreferredWidth(150);
		tcm3.getColumn(1).setPreferredWidth(283);
		tcm3.getColumn(2).setPreferredWidth(50);
		errorTable.getTableHeader().setReorderingAllowed(false);//不能调整列的顺序
		errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//无需适应窗口大小
		errorTable.setEnabled(false);//不能编辑
		DefaultTableCellRenderer r3=new DefaultTableCellRenderer();
		r3.setHorizontalAlignment(JLabel.CENTER);
		errorTable.setDefaultRenderer(Object.class, r3);//数据居中显示
		errorJSP.setBounds(1000, 40, 500, 760);
		errorJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		errorJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		LAPanel.add(errorJSP);
		
		return LAFrame;
	}
	
	
}
