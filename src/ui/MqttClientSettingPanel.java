package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.fusesource.mqtt.client.QoS;

public class MqttClientSettingPanel extends JPanel implements DocumentListener,ItemListener{
	private final static String HOST_0="localhost";
	private final static int PORT_0=61613;
	private final static String HOST_1="iot.eclipse.org";
	private final static int PORT_1=1883;

	private String host=HOST_1;
	private int port=PORT_1;
	
	private String clientId="fusesource_client-0";
	private short keepAlive=1000;
	private String user="admin";
	private String password="password";
	private String willTopic;
	private String willMessage;
	private QoS willQos=QoS.AT_MOST_ONCE;
	private boolean isCleanSession=true;
	private boolean isWillRetain=false;
	private String version="3.1";
	
	private String labeles[]= {"主机名","端口号","客户端ID"
			,"存活时间","用户名","密码"
			,"遗嘱主题","遗嘱消息","MQTT版本号"};
	private JTextField jtf[];
	private String strCheckBox[]= {"清除会话","保留遗嘱"};
	private JCheckBox jcb[];
	private String strQos[]= {"服务质量(QoS)","至少一次","至多一次","仅仅一次"};
	private JRadioButton jrb[];
	private String[] other= {"客户端配置","连接","断开"};
	private JLabel status;
	private JButton connect;
	private JButton disconnect;
	public MqttClientSettingPanel() {
		this(400,305);
	}
	public MqttClientSettingPanel(int width,int height) {
		// TODO Auto-generated constructor stub
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    jtf=new JTextField[labeles.length];
	    JPanel jp0=new JPanel();
	    jp0.setLayout(null);
		for(int i=0;i<labeles.length;i++) {
			jtf[i]=new JTextField(10);
			switch(i) {
				case 0:
					jtf[i].setText(host);
					break;
				case 1:
					jtf[i].setText(""+port);
					break;
				case 2:
					jtf[i].setText(clientId);
					break;
				case 3:
					jtf[i].setText(""+keepAlive);
					break;
				case 4:
					jtf[i].setText(user);
					break;
				case 5:
					jtf[i].setText(password);
					break;
				case 6:
					jtf[i].setText(willTopic);
					break;
				case 7:
					jtf[i].setText(willMessage);
					break;
				case 8:
					jtf[i].setText(version);
					break;			
			}
			jtf[i].getDocument().addDocumentListener(this);
		    JLabel jl=new JLabel(labeles[i]+":");
		    JPanel jp=new JPanel();
		    jp.setLayout(new FlowLayout(FlowLayout.RIGHT));
			jp.add(jl);
			jp.add(jtf[i]);
			jp.setBounds(-20, i*30, width*3/5, 30);
			jp0.add(jp);
		}
		jcb=new JCheckBox[strCheckBox.length];
		JPanel jp1=new JPanel();
		JPanel jp2=new JPanel();	
		jp1.setLayout(new BoxLayout(jp1,BoxLayout.Y_AXIS));
		jp2.setLayout(new BoxLayout(jp2,BoxLayout.LINE_AXIS));
		for(int i=0;i<strCheckBox.length;i++) {
			jcb[i]=new JCheckBox();
			
			if(i==0) {
				jcb[i].setSelected(isCleanSession);
			}else if(i==1) {
				jcb[i].setSelected(isWillRetain);
			}
			jcb[i].addItemListener(this);
			JLabel jl=new JLabel(strCheckBox[i]+":");
			jp2.add(jl);
			jp2.add(jcb[i]);
		}
		ButtonGroup bg=new ButtonGroup();
		jrb=new JRadioButton[strQos.length-1];
		JPanel jp3=new JPanel();
		JPanel jp4=new JPanel();
	    jp3.setLayout(new BoxLayout(jp3,BoxLayout.LINE_AXIS));
	    jp4.setLayout(new BoxLayout(jp4,BoxLayout.Y_AXIS));
	    jp3.add(new JLabel(strQos[0]+":"));
		for(int i=0;i<strQos.length-1;i++) {
			JLabel jl=new JLabel(strQos[i+1]);
			jrb[i]=new JRadioButton();
			if(willQos==QoS.AT_LEAST_ONCE&&i==0) {
				jrb[i].setSelected(true);
			}else if(willQos==QoS.AT_MOST_ONCE&&i==1) {
				jrb[i].setSelected(true);
			}else if(willQos==QoS.EXACTLY_ONCE&&i==2) {
				jrb[i].setSelected(true);
			}
			jrb[i].addItemListener(this);
			JPanel jp=new JPanel();
		    jp.setLayout(new BoxLayout(jp,BoxLayout.LINE_AXIS));
		    jp.add(jrb[i]);
		    jp.add(jl);
			bg.add(jrb[i]);
			jp4.add(jp);
		}
		jp3.add(jp4);
		JPanel info=new JPanel();
		info.setLayout(new BoxLayout(info,BoxLayout.LINE_AXIS));
		status=new JLabel();
		info.add(status);
		JPanel control=new JPanel();
		control.setLayout(new BoxLayout(control,BoxLayout.LINE_AXIS));
		connect=new JButton(other[1]);
		disconnect=new JButton(other[2]);
		disconnect.setEnabled(false);
		control.add(connect);
		JLabel inset=new JLabel();
		inset.setBorder(new EmptyBorder(1, 10, 1, 10));
		control.add(inset);
		control.add(disconnect);
		jp1.setBorder(new EmptyBorder(0, 0, 0, 10));
		info.setBorder(new EmptyBorder(0, 0, 30, 0));
		jp2.setBorder(new EmptyBorder(0, 0, 10, 0));
		jp3.setBorder(new EmptyBorder(0, 0, 35, 0));
		jp1.add(info);
		jp1.add(jp2);
	    jp1.add(jp3);
	    jp1.add(control);
	    JPanel head=new JPanel();
	    JPanel body=new JPanel();
	    body.setLayout(new BoxLayout(body,BoxLayout.X_AXIS));
	    head.setLayout(new FlowLayout(FlowLayout.CENTER));
	    body.add(jp0);
	    body.add(jp1);
	    head.add(new JLabel(other[0]));
	    head.setBackground(Color.GRAY);
		this.add(head,BorderLayout.NORTH);
		this.add(body,BorderLayout.CENTER);
	}

	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public String getClientId() {
		return clientId;
	}
	public boolean isCleanSession() {
		return isCleanSession;
	}
	public short getKeepAlive() {
		return keepAlive;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public String getWillTopic() {
		return willTopic;
	}
	public String getWillMessage() {
		return willMessage;
	}
	public QoS getWillQos() {
		return willQos;
	}
	public boolean isWillRetain() {
		return isWillRetain;
	}
	public String getVersion() {
		return version;
	}
	public JLabel getStatus() {
		return status;
	}
	public JButton getConnect() {
		return connect;
	}
	public JButton getDisconnect() {
		return disconnect;
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
		if(doc==jtf[0].getDocument()) {
			String temp=jtf[0].getText();
			if(!checkText(labeles[0],temp,"^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?$")){
				if(!checkText(labeles[0],temp,"^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")){
			        if(!checkText(labeles[0],temp,"^localhost$")){
			        	return;
			        }
			     }
			}
			host=temp;
		}else if(doc==jtf[1].getDocument()) {
			String temp=jtf[1].getText();
			if(checkText(labeles[1],temp,"^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])[0-9]?$")) {
				port=Integer.valueOf(temp);
			}
		}else if(doc==jtf[2].getDocument()) {
			String temp=jtf[2].getText();
			if(checkText(labeles[2],temp,"^[^\\*.`\\^]{1,23}$")) {
				clientId=temp;
			}
		}else if(doc==jtf[3].getDocument()) {
			String temp=jtf[3].getText();
			if(checkText(labeles[3],temp,"^[\\d]{1,5}$")) {
				keepAlive=Short.valueOf(temp);
			}
		}else if(doc==jtf[4].getDocument()) {
			String temp=jtf[4].getText();
			if(checkText(labeles[4],temp,"^[^\\ ]{1,20}$")) {
				user=temp;
			}
		}else if(doc==jtf[5].getDocument()) {
			String temp=jtf[5].getText();
			if(checkText(labeles[5],temp,"^[^\\*`~\\^]{3,20}$")) {
				password=temp;
			}
		}else if(doc==jtf[6].getDocument()) {
			String temp=jtf[6].getText();
			if(checkText(labeles[6],temp,"^[^`\\^]{1,20}$")) {
				willTopic=temp;
			}
		}else if(doc==jtf[7].getDocument()) {
			String temp=jtf[7].getText();
			if(checkText(labeles[7],temp,"^[^`\\^]{1,30}$")) {
				willMessage=temp;
			}
		}else if(doc==jtf[8].getDocument()) {
			String temp=jtf[8].getText();
			if(checkText(labeles[8],temp,"^3.1(.1)?$")) {
				version=temp;
			}
		}
	}
	private boolean checkText(String flag,String test,String rule) {
		boolean isOk=false;
		if(test.isEmpty())
		{
			updateStatus(flag+"为空!",Color.RED);
		}else {
			if(test.matches(rule))
			{
				updateStatus(flag+":"+test,Color.GREEN);
				isOk=true;
			}else {
				updateStatus(flag+"无效！",Color.CYAN);
			}
		}
		return isOk;
	}
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getItem()==jcb[0]) {
			isCleanSession=jcb[0].isSelected();
			updateStatus(strCheckBox[0]+":"+isCleanSession,Color.BLUE);
		}else if(arg0.getItem()==jcb[1]) {
			isWillRetain=jcb[1].isSelected();
			updateStatus(strCheckBox[1]+":"+isWillRetain,Color.BLUE);
		}else if(arg0.getItem()==jrb[0]){
			if(jrb[0].isSelected()) {
				willQos=QoS.AT_LEAST_ONCE;
				updateStatus(strQos[0]+":"+strQos[1],Color.MAGENTA);
			}
		}else if(arg0.getItem()==jrb[1]){
			if(jrb[1].isSelected()) {
				willQos=QoS.AT_MOST_ONCE;
				updateStatus(strQos[0]+":"+strQos[2],Color.MAGENTA);
			}
		}else if(arg0.getItem()==jrb[2]){
			if(jrb[2].isSelected()) {
				willQos=QoS.EXACTLY_ONCE;
				updateStatus(strQos[0]+":"+strQos[3],Color.MAGENTA);
			}
		}
	}
	
	public void updateStatus(String info,Color color) {
		status.setForeground(color);
		status.setText(info);
	}
	
	public void setAllCanChange(boolean is) {
		for(int i=0;i<jtf.length;i++) {
			jtf[i].setEditable(is);
		}
		for(int i=0;i<jcb.length;i++) {
			jcb[i].setEnabled(is);
		}
		for(int i=0;i<jrb.length;i++) {
			jrb[i].setEnabled(is);
		}
	}
	
}
