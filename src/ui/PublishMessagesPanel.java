package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.fusesource.mqtt.client.QoS;

import utils.Checker;

public class PublishMessagesPanel extends JPanel implements DocumentListener,ItemListener {
	private String message="Hello";
	private String destination="World";
	private QoS qos=QoS.EXACTLY_ONCE;
	private boolean isRetain;
	
	private JTextArea jtames;
	private JTextField jtfdes;
	private JComboBox jcbqos;
	private JCheckBox retain;
	private JButton send;
	private String strqos[]= {"至少一次","至多一次","仅仅一次"};
	private String str[]= {"目的地","保留消息","Qos"};
	private String other[]= {"发布内容","发布"};
	private int padding=2;
	public PublishMessagesPanel() {
		this(400,180);
	}
	public PublishMessagesPanel(int width,int height) {
		// TODO Auto-generated constructor stub
		this.setSize(width,height);
		this.setLayout(null);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel head=new JPanel();
	    JPanel body=new JPanel();
	    head.setLayout(new FlowLayout(FlowLayout.CENTER));
	    body.setLayout(null);
	    head.setBounds(0, 0, width,30);
	    width=width-padding*2;
	    height=height-30-padding;
		body.setBounds(padding, 30, width,height);
		jtames=new JTextArea(10,20);
		jtames.setText(message);
		jtames.setLineWrap(true);// 激活自动换行功能  
        jtames.setWrapStyleWord(true);// 激活断行不断字功能
        jtames.getDocument().addDocumentListener(this);
		JScrollPane jsp=new JScrollPane(jtames);
		jsp.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jsp.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jtfdes=new JTextField(10);
		jcbqos=new JComboBox(strqos);
		retain=new JCheckBox();
		send=new JButton(other[1]);
		int len=str.length;
		JLabel jl[]=new JLabel[len];
		JPanel jp[]=new JPanel[len];
		for(int i=0;i<len;i++) {
			jp[i]=new JPanel();
			jp[i].setLayout(new BoxLayout(jp[i],BoxLayout.LINE_AXIS));
			jl[i]=new JLabel(str[i]+":");
			jp[i].add(jl[i]);
			switch(i){
				case 0:
					jp[i].add(jtfdes);
					jtfdes.setText(destination);
					jtfdes.getDocument().addDocumentListener(this);
					break;
				case 1:
					jp[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
					jp[i].add(retain);
					retain.setSelected(isRetain);
					retain.addItemListener(this);
					break;
				case 2:
					jp[i].add(jcbqos);
					if(qos==QoS.AT_LEAST_ONCE) {
						jcbqos.setSelectedItem(strqos[0]);
					}else if(qos==QoS.AT_MOST_ONCE) {
						jcbqos.setSelectedItem(strqos[1]);
					}else if(qos==QoS.EXACTLY_ONCE) {
						jcbqos.setSelectedItem(strqos[2]);
					}
					
					break;
			}
		}
		jp[0].setBounds(0, 0, width, height/5);
		jsp.setBounds(0,height/5, width, height*3/5);
		jp[1].setBounds(0,height*4/5, width/5, height/5);
		jp[2].setBounds(width/5, height*4/5,width*2/5, height/5);
		send.setBounds(width*3/5,height*4/5 ,width*2/5 , height/5);
	    head.add(new JLabel(other[0]));
		body.add(jp[0]);
		body.add(jsp);
		body.add(jp[1]);
		body.add(jp[2]);
		body.add(send);
		head.setBackground(Color.GRAY);
		
		this.add(head);
		this.add(body);
		setAllCanChange(false);
	}
	
	public String getMessage() {
		return message;
	}

	public String getDestination() {
		return destination;
	}

	public JButton getSend() {
		return send;
	}
	
	public QoS getQos() {
		String temp=(String)jcbqos.getSelectedItem();
		if(strqos[0]==temp) {
			qos=QoS.AT_LEAST_ONCE;
		}else if(strqos[1]==temp) {
			qos=QoS.AT_MOST_ONCE;
		}else if(strqos[2]==temp) {
			qos=QoS.EXACTLY_ONCE;
		}
		//System.out.println("qos:"+qos);
		return qos;
	}
	public boolean isRetain() {
		return isRetain;
	}
	public void setAllCanChange(boolean is) {
		send.setEnabled(is);
	}
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getItem()==retain) {
			isRetain=retain.isSelected();
			System.out.println("isRetain:"+isRetain);
		}
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		checkAllTextFieldValue(arg0);
	}
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		checkAllTextFieldValue(arg0);
	}
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		checkAllTextFieldValue(arg0);
	}
	
	private void checkAllTextFieldValue(DocumentEvent arg0) {
		if(arg0==null) {return;}
		Document doc=arg0.getDocument();
		if(doc==jtfdes.getDocument()) {
			String temp=jtfdes.getText();
			if(Checker.checkText(str[0], temp, "^[^`]{1,}$")) {
				destination=temp;
			}
			//System.out.println("checkAllTextFieldValue():jtfdes");
		}else if(doc==jtames.getDocument()) {
			String temp=jtames.getText();
			if(Checker.checkText(other[0], temp, "^[^`]{1,}$")) {
		    message=temp;
			}
			//System.out.println("checkAllTextFieldValue():jtames");
		}
	}

}
