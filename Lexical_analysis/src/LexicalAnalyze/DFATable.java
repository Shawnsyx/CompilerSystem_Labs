package LexicalAnalyze;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JTable;

public class DFATable {
	
	//25个状态
	private final int stateCount=25;
	
	//21个输入类型
	private final int inputTypeCount=21;
	
	//14个终止状态
	private final int finalStateCount=14;
	
	//0~24共25个状态，0~20共21个输入，第1列为状态编号
	private final Integer[][]TABLE={
			{ 0, 1, 8,-1,-1, 2,-1,-1,-1,-1,12,-1,-1,-1,15,14,-1,22,-1,19,-1,-1},
			{ 1, 1,-1,-1, 1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 2,-1,-1,-1, 2,-1, 3, 5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 3,-1,-1,-1, 4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 4,-1,-1,-1, 4,-1,-1, 5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 5,-1,-1,-1, 7,-1,-1,-1,-1,-1,-1, 6, 6,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 6,-1,-1,-1, 7,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 7,-1,-1,-1, 7,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 8,-1,-1, 9,-1,-1, 3,-1,10,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{ 9,-1,-1, 9,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{10,-1,-1,-1,-1,-1,-1,-1,-1,11,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{11,-1,-1,-1,-1,-1,-1,-1,-1,11,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{12,-1,-1,-1,-1,-1,-1,-1,-1,-1,13,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{13,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{14,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{15,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1,-1,-1,-1,-1,-1,-1,-1},
			{16,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,17,-1,-1,-1,-1,-1,-1,-1,16},
			{17,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,17,18,-1,16,-1,-1,-1,-1,-1},
			{18,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{19,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,21,20,-1},
			{20,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,21,20,-1},
			{21,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{22,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,23,-1,-1,-1},
			{23,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,24,-1,-1,-1,-1},
			{24,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}
	};
	
	//14个终止状态
	@SuppressWarnings("serial")
	private final Set<Integer> finalStates= new HashSet<Integer>(){{
		add(1); add(2); add(4); add(7); add(8); add(9); add(11); add(12); 
		add(13); add(14); add(15); add(18); add(21); add(24);
	}};
	
	//21个输入类型
	private final String[]Columns=new String[] {" ","letter_","0","0-7","0-9","1-9",".","E","x","0-9a-f","/之外sop","+",
			"-","*","/","deli","*和/之外","'","'之外","\"","\"之外","*之外"};
	
	//(输入类型,标号)键值对
	private final Map<String, Integer>inputTypeToNum=new HashMap<String,Integer>();

	//状态转换表
	private final JTable JT;
	
	public DFATable() {
		
		//初始化(输入类型,标号)键值对
		for (int i = 0; i < inputTypeCount; i++) {
			inputTypeToNum.put(Columns[i+1], i);
		}

		//构造DFA状态转换表的JTable
		String[][] t=new String[stateCount][inputTypeCount+1];
		for (int i = 0; i < stateCount; i++) {
			for (int j = 0; j < inputTypeCount+1; j++) {
				int k=TABLE[i][j];
				if(k==-1) {
					t[i][j]="";
				}
				else if (finalStates.contains(k)) {
					t[i][j]=k+"*";
				}
				else {
					t[i][j]=""+k;
				}
			}
		}
		JT=new JTable(t,Columns);
	}
	
	
	
	/**
	 * 获取状态总数
	 */
	public int getStateCount() {
		return stateCount;
	}
	
	
	
	/**
	 * 
	 *  获取输入类型总数
	 */
	public int getInputTypeCount() {
		return inputTypeCount;
	}
	
	
	
	/**
	 * 获取终止状态总数
	 */
	public int getFinalStateCount() {
		return finalStateCount;
	}
	
	
	
	/**
	 * 获取对应的JTable
	 */
	public JTable getJTable() {
		return JT;
	}
	
	
	
	/**
	 * 获取num对应的输入类型
	 */
	public String numToInputType(int num) {
		assert num>=0&&num<inputTypeCount;
		return Columns[num+1];
	}
	
	
	
	/**
	 * 获取输入类型对应的标号
	 */
	public int inputTypeToNum(String inputType) {
		Integer i=inputTypeToNum.get(inputType);
		return i==null?-1:i;
	}
	
	
	
	/**
	 *  根据输入字符获取输入类型编号
	 */
	private Set<Integer> charToInputType(char c){
		//因为一个字符可能对应多个输入，但满足条件的最多一个
		String s=""+c;
		Set<Integer>inputSet=new HashSet<>();
		if(Pattern.compile("[a-zA-Z]|_").matcher(s).matches()) {
			inputSet.add(0);
		}
		if(Pattern.compile("[0-7]").matcher(s).matches()) {
			inputSet.add(2);
		}
		if(Pattern.compile("[0-9]").matcher(s).matches()) {
			inputSet.add(3);
		}
		if(Pattern.compile("[1-9]").matcher(s).matches()) {
			inputSet.add(4);
		}
		if(Pattern.compile("[0-9a-f]").matcher(s).matches()) {
			inputSet.add(8);
		}
		//单个运算符，两个字符组成的运算符可以由两个单个运算符组成，但需要判断是否合法
		if(c=='+'||c=='-'||c=='*'||c=='='||c=='%'||c=='<'||c=='>'||c=='^'||c=='|'||c=='&'||c=='!'||c=='~') {
			inputSet.add(9);
		}
		if(c!='*'&&c!='/') {
			inputSet.add(15);
		}
		if(c!='\'') {
			inputSet.add(17);
		}
		if(c!='"') {
			inputSet.add(19);
		}
		if(c!='*') {
			inputSet.add(20);
		}
		switch (c) {
		case '0':
			inputSet.add(1);
			break;
		case '.':
			inputSet.add(5);
			inputSet.add(14);
			break;
		case 'E':
			inputSet.add(6);
			break;
		case 'x':
			inputSet.add(7);
			break;
		case '+':
			inputSet.add(10);
			break;
		case '-':
			inputSet.add(11);
			break;
		case '*':
			inputSet.add(12);
			break;
		case '/':
			inputSet.add(13);
			break;
		case '{':
		case '}':
		case '[':
		case ']':
		case '(':
		case ')':
		case ',':
		case ';':
		case ':':
		case '?':
			inputSet.add(14);
			break;
		case '\'':
			inputSet.add(16);
			break;
		case '"':
			inputSet.add(18);
			break;
		}
		return inputSet;
	}
	
	
	
	/**
	 * 根据当前状态和输入字符获取下一个状态
	 */
	public int nextStateByChar(int state,char c){
		assert state>=0&&state<stateCount;
		if (c=='\n'||c=='\t') {
			return -2;
		}
		Set<Integer>possibleInputs=charToInputType(c);
		int nextState=-1;
		//选出不为-1的那一个
		for(Integer j:possibleInputs) {
			nextState=TABLE[state][j+1];
			if((nextState=TABLE[state][j+1])!=-1) {
				return nextState;
			}
		}
		return nextState;//若无状态转换，返回-1
	}
	
	
	
	/**
	 * 根据当前状态和输入字符的编号获取下一个状态
	 * @param state
	 * @param num
	 * @return
	 */
	public int nextStateByNum(int state,int num) {
		assert state>=0&&state<stateCount;
		assert num>=0&&num<inputTypeCount;
		return TABLE[state][num+1];
	}
	
	
	
	/**
	 * 判断state是否为终止状态
	 */
	public boolean isFinalState(int state) {
		return finalStates.contains(state);
	}
}
