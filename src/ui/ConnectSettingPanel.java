package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.fusesource.mqtt.client.MQTT;

import utils.Checker;

public class ConnectSettingPanel extends JPanel implements DocumentListener {
	private long connectAttemptsMax=1;
	private long reconnectAttemptsMax=1;
	private long reconnectDelay=10;
	private long reconnectDelayMax=30000;
	
	private double reconnectBackOffMultiplier=2;
	
	private int receiveBufferSize=65536;
	private int sendBufferSize=65536;
	private int trafficClass=8;
	private int maxReadRate=0;
	private int maxWriteRate=0;
	
	private String str[]= {"最大尝试连接次数","最大重连次数","重连延迟","最大重连延迟","指数退避重连乘数",
			"接收缓冲区大小","发送缓冲区大小","通信量类（Traffic Class）","最大读取字节流速率","最大写入字节流速率"};
	private JTextField jtf[];
	private String other[]= {"连接配置"};
	public ConnectSettingPanel() {
		this(300,305);
	}
	public ConnectSettingPanel(int width,int height){
		// TODO Auto-generated constructor stub
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel head=new JPanel();
	    JPanel body=new JPanel();
	    head.setLayout(new FlowLayout(FlowLayout.CENTER));
		body.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jtf=new JTextField [str.length];
		for(int i=0;i<str.length;i++) {
			jtf[i]=new JTextField(10);
			switch(i) {
				case 0:
					jtf[i].setText(connectAttemptsMax+"");
					break;
				case 1:
					jtf[i].setText(reconnectAttemptsMax+"");
					break;
				case 2:
					jtf[i].setText(reconnectDelay+"");
					break;
				case 3:
					jtf[i].setText(reconnectDelayMax+"");
					break;
				case 4:
					jtf[i].setText(reconnectBackOffMultiplier+"");
					break;
				case 5:
					jtf[i].setText(receiveBufferSize+"");
					break;
				case 6:
					jtf[i].setText(sendBufferSize+"");
					break;
				case 7:
					jtf[i].setText(trafficClass+"");
					break;
				case 8:
					jtf[i].setText(maxReadRate+"");
					break;
				case 9:
					jtf[i].setText(maxWriteRate+"");
					break;
			}
			jtf[i].getDocument().addDocumentListener(this);
			JLabel jl=new JLabel(str[i]+":");
			JPanel jp=new JPanel();
			jp.setLayout(new BoxLayout(jp,BoxLayout.LINE_AXIS));
			jp.add(jl);
			jp.add(jtf[i]);
			body.add(jp);
		}
		head.add(new JLabel(other[0]));
		head.setBackground(Color.GRAY);
		this.add(head,BorderLayout.NORTH);
		this.add(body,BorderLayout.CENTER);
	}
	
	public void setAllCanChange(boolean is) {
		for(int i=0;i<jtf.length;i++) {
			jtf[i].setEditable(is);
		}
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		checkAllTextFieldValue(e);
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		checkAllTextFieldValue(e);
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		checkAllTextFieldValue(e);
	}

	private void checkAllTextFieldValue(DocumentEvent arg0) {
		if(arg0==null) {return;}
		Document doc=arg0.getDocument();
		if(doc==jtf[0].getDocument()) {
			String temp=jtf[0].getText();
			if(Checker.checkText(str[0],temp,"^[0-9]{1,5}$")) {
				connectAttemptsMax=Long.valueOf(temp);
			}
		}else if(doc==jtf[1].getDocument()) {
			String temp=jtf[1].getText();
			if(Checker.checkText(str[1], temp, "^[0-9]{1,5}$")) {
				reconnectAttemptsMax=Long.valueOf(temp);
			}
		}else if(doc==jtf[2].getDocument()) {
			String temp=jtf[2].getText();
			if(Checker.checkText(str[2], temp, "^[0-9]{1,10}$")) {
				reconnectDelay=Long.valueOf(temp);
			}
		}else if(doc==jtf[3].getDocument()) {
			String temp=jtf[3].getText();
			if(Checker.checkText(str[3], temp, "^[0-9]{1,20}$")) {
				reconnectDelayMax=Long.valueOf(temp);
			}
		}else if(doc==jtf[4].getDocument()) {
			String temp=jtf[4].getText();
			if(Checker.checkText(str[4], temp, "^[0-9.]{1,3}$")) {
				reconnectBackOffMultiplier=Double.valueOf(temp);
			}
		}else if(doc==jtf[5].getDocument()) {
			String temp=jtf[5].getText();
			if(Checker.checkText(str[5], temp, "^[0-9]{1,20}$")) {
				receiveBufferSize=Integer.valueOf(temp);
			}
		}else if(doc==jtf[6].getDocument()) {
			String temp=jtf[6].getText();
			if(Checker.checkText(str[6], temp, "^[0-9]{1,20}$")) {
				sendBufferSize=Integer.valueOf(temp);
			}
		}else if(doc==jtf[7].getDocument()) {
			String temp=jtf[7].getText();
			if(Checker.checkText(str[7], temp, "^[0-9]{1,5}$")) {
				 trafficClass=Integer.valueOf(temp);
			}
		}else if(doc==jtf[8].getDocument()) {
			String temp=jtf[8].getText();
			if(Checker.checkText(str[8], temp, "^[0-9]{1,3}$")) {
				maxReadRate=Integer.valueOf(temp);
			}
		}else if(doc==jtf[9].getDocument()) {
			String temp=jtf[9].getText();
			if(Checker.checkText(str[9], temp, "^[0-9]{1,3}$")) {
				maxWriteRate=Integer.valueOf(temp);
			}
		}	
	}
	public long getConnectAttemptsMax() {
		return connectAttemptsMax;
	}
	public long getReconnectAttemptsMax() {
		return reconnectAttemptsMax;
	}
	public long getReconnectDelay() {
		return reconnectDelay;
	}
	public long getReconnectDelayMax() {
		return reconnectDelayMax;
	}
	public double getReconnectBackOffMultiplier() {
		return reconnectBackOffMultiplier;
	}
	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}
	public int getSendBufferSize() {
		return sendBufferSize;
	}
	public int getTrafficClass() {
		return trafficClass;
	}
	public int getMaxReadRate() {
		return maxReadRate;
	}
	public int getMaxWriteRate() {
		return maxWriteRate;
	}
	
}
