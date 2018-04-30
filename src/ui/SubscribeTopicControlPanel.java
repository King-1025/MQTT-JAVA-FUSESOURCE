package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.fusesource.mqtt.client.QoS;

import utils.Checker;

public class SubscribeTopicControlPanel extends JPanel implements DocumentListener{
	private String topic="World"; 
	private QoS qos=QoS.EXACTLY_ONCE;
	
	private JTextField jtf;
	private JComboBox jcbqos;
	private JButton subscribe;
	private JButton unsubscribe;
	private String strqos[]= {"至少一次","至多一次","仅仅一次"};
	private String str[]={"主题","QoS"};
	private String other[]= {"确认订阅","取消订阅","订阅控制"};
	private int padding=2;
	public SubscribeTopicControlPanel() {
		// TODO Auto-generated constructor stub
		this(300,180);
	}
	public SubscribeTopicControlPanel(int width ,int height) {
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
	    JPanel jp[]=new JPanel[str.length];
	    jtf=new JTextField(10);
	    jcbqos=new JComboBox(strqos);
	    for(int i=0;i<str.length;i++) {
	    	jp[i]=new JPanel();
	    	jp[i].setLayout(new BoxLayout(jp[i],BoxLayout.LINE_AXIS));
	    	jp[i].add(new JLabel(str[i]+":"));
	    	switch(i){
	    		case 0:
	    			jtf.setText(topic);
	    	    	jtf.getDocument().addDocumentListener(this);
	    	    	jp[i].add(jtf);
	    	    	break;
	    		case 1:
	    			if(qos==QoS.AT_LEAST_ONCE) {
	    				jcbqos.setSelectedItem(strqos[0]);
	    			}else if(qos==QoS.AT_MOST_ONCE) {
	    				jcbqos.setSelectedItem(strqos[1]);
	    			}else if(qos==QoS.EXACTLY_ONCE) {
	    				jcbqos.setSelectedItem(strqos[2]);
	    			}
	    			jp[i].add(jcbqos);
	    			break;
	      }
	    }
	    JPanel control=new JPanel();
	    control.setLayout(new FlowLayout(FlowLayout.CENTER));
	    subscribe=new JButton(other[0]);
	    unsubscribe=new JButton(other[1]);
	    JLabel inset=new JLabel();
		inset.setBorder(new EmptyBorder(1, 15, 1, 15));
		control.add(subscribe);
		control.add(inset);
		control.add(unsubscribe);
		jp[0].setBounds(0,height/2-height/3,width,30);
		jp[1].setBounds(0,height/2,width, 30);
		control.setBounds(0,height/2+height/4,width, 50);
		head.add(new JLabel(other[2]));
		head.setBackground(Color.GRAY);
		body.add(jp[0]);
		body.add(jp[1]);
		body.add(control);
		this.add(head);
		this.add(body);
		setAllCanChange(false);
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
		if(doc==jtf.getDocument()) {
			String temp=jtf.getText();
			if(Checker.checkText(str[0], temp, "^[^`]{1,}$")) {
				topic=temp;
			}
		}
	}
	public String getTopic() {
		return topic;
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
		//System.out.println("SubscribeTopicControlPanel:qos:"+qos);
		return qos;
	}
	public JButton getSubscribe() {
		return subscribe;
	}
	public JButton getUnsubscribe() {
		return unsubscribe;
	}
	
	public void setAllCanChange(boolean is) {
		//jtf.setEditable(is);
		//jcbqos.setEnabled(is);
		subscribe.setEnabled(is);
		unsubscribe.setEnabled(is);
	}

}
