package SemanticAnalyze;

import javax.swing.JFrame;

public class SemanticAnalyzer {
	private SemanticRules SRules=new SemanticRules();
	public JFrame getSRFrame() {
		return SRules.getSRFrame();
	}
	public JFrame analyze() {
		return new JFrame();
	}
}
