package utils;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public  class Checker {

	public Checker() {
		// TODO Auto-generated constructor stub
	}
	public static boolean checkText(String flag,String test,String rule) {
		boolean isOk=false;
		if(test.isEmpty())
		{
			System.out.println("Checker:checkText()->"+flag+"为空！");
			//JOptionPane.showMessageDialog(null, flag+"为空！");
		}else {
			if(test.matches(rule))
			{
				//System.out.println("Checker:checkText()->"+flag+":"+test);
				isOk=true;
			}else {
				System.out.println("Checker:checkText()->"+flag+"不匹配"+rule);
				JOptionPane.showMessageDialog(null, flag+"无效！");
			}
		}
		return isOk;
	}
}
