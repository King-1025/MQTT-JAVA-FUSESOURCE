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
			System.out.println("Checker:checkText()->"+flag+"Ϊ�գ�");
			//JOptionPane.showMessageDialog(null, flag+"Ϊ�գ�");
		}else {
			if(test.matches(rule))
			{
				//System.out.println("Checker:checkText()->"+flag+":"+test);
				isOk=true;
			}else {
				System.out.println("Checker:checkText()->"+flag+"��ƥ��"+rule);
				JOptionPane.showMessageDialog(null, flag+"��Ч��");
			}
		}
		return isOk;
	}
}
