package LexicalAnalyze;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;

public class LexicalRules {
	//词法规则界面
	private JFrame LRulesFrame=new JFrame("词法规则");
	//词法规则显示区域
	private JPanel LRulesPanel=new JPanel();
	private JLabel LRulesLabel=new JLabel("词法规则",JLabel.CENTER);
	private JTextArea LRulesTA=new JTextArea();
	private JScrollPane LRulesJSP=new JScrollPane(LRulesTA);
	//DFA状态转换表区域
	private JLabel dfaLabel=new JLabel("DFA状态转换表",JLabel.CENTER);
	//dfa状态转换表
	private DFATable dfa=new DFATable();
	private JTable dfaTable=dfa.getJTable();
	private JScrollPane dfaJSP=new JScrollPane(dfaTable);
	
	//词法规则内容
	private String rulesText="标识符： letter_ -> a|b|c|...|z|A|B|C|...|Z\r\n" + 
			"         digit   -> 0|1|2|3|4|5|6|7|8|9\r\n" + 
			"         id -> letter_(letter_|digit)*\r\n" + 
			"\r\n" + 
			"常数：   digits -> digit digit*\r\n" + 
			"         optionalFraction -> .digits|ε\r\n" + 
			"         optionalExponent -> E(+|-|ε)digits|ε\r\n" + 
			"         number -> digits optionalFraction optionalExponent\r\n" + 
			"\r\n" + 
			"运算符： op -> + | - | * | / | += | -= | *= | /= | % | ++ | -- | != | == | > | < | >= | <= | >> | << | ^ |  |  | & | && |  ||  | ! | ~\r\n" + 
			"\r\n" + 
			"界符：   Boundary -> { | } | [ | ] | ( | ) | , | ; | : | ? \r\n" + 
			"\r\n" + 
			"注释：   NOTE -> /*other*/\r\n" + 
			"         other指代其它字符\r\n" + 
			"\r\n" + 
			"8进制：  OCT -> 0(1|2|3|4|5|6|7)（0|1|2|3|4|5|6|7）*\r\n" + 
			"\r\n" + 
			"16进制： HEX -> 0x(1|…|9|a|…|f) (0|…|9|a|…|f)*\r\n" + 
			"\r\n" + 
			"字符常数：char -> '(a|b|c|...|z|A|B|C|...|Z)'\r\n" + 
			"\r\n" + 
			"字符串常数：String->\"(E)\"\r\n" + 
			"	E->ε|(\"之外字符)";
	
	protected LexicalRules() {
		initial();
	}
	
	private void initial() {
		//词法规则窗口
		LRulesFrame.setLocation(100, 100);
		LRulesFrame.setSize(1205, 835);
		LRulesFrame.setResizable(false);
		LRulesPanel.setLayout(null);
		LRulesPanel.setBounds(0, 0, 1200, 800);
		LRulesFrame.add(LRulesPanel);
		//词法规则标签
		LRulesLabel.setBounds(200, 0, 200, 40);
		LRulesLabel.setFont(new Font("", Font.BOLD, 25));
		LRulesLabel.setForeground(Color.BLUE);
		LRulesPanel.add(LRulesLabel);
		//词法规则内容
		LRulesTA.setText(rulesText);
		LRulesTA.setFont(new Font("", Font.PLAIN, 20));
		LRulesTA.setEditable(false);
		LRulesTA.setCaretPosition(0);
		LRulesJSP.setBounds(0, 40, 600, 760);
		LRulesJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		LRulesJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		LRulesPanel.add(LRulesJSP);
		
		//dfa标签
		dfaLabel.setBounds(700, 0, 400, 40);
		dfaLabel.setFont(new Font("", Font.BOLD, 25));
		dfaLabel.setForeground(Color.BLUE);
		LRulesPanel.add(dfaLabel);
		//dfa状态转换表
		dfaTable.setRowHeight(30);//行高
		dfaTable.getColumnModel().getColumn(0).setPreferredWidth(30);//第一列列宽
		for (int i = 1; i <=dfa.getInputTypeCount(); i++) {
			dfaTable.getColumnModel().getColumn(i).setPreferredWidth(50);//列宽
		}
		dfaTable.getTableHeader().setReorderingAllowed(false);//不能调整列的顺序
		dfaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//无需适应窗口大小
		dfaTable.setEnabled(false);//不能编辑
		DefaultTableCellRenderer r=new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);//单元格数据居中
		dfaTable.setDefaultRenderer(Object.class, r);
		dfaJSP.setBounds(600, 40, 600, 760);
		dfaJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		dfaJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		LRulesPanel.add(dfaJSP);
	}

	protected JFrame getRulesFrame() {
		return LRulesFrame;
	}
}
