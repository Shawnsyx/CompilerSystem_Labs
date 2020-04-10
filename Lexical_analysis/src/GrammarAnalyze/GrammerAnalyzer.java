package GrammarAnalyze;

import java.util.List;

import javax.swing.JFrame;

public class GrammerAnalyzer {
	private final GrammerRules GRules=new GrammerRules();
	public JFrame getGRFrame() {
		return GRules.getGRFrame();
	}
	public JFrame analyze(List<String[]>tokens) {
		return new JFrame();
	}
}
