package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import ui.ConnectSettingPanel;
import ui.MqttClientSettingPanel;
import ui.PublishMessagesPanel;
import ui.StatusShowPanel;
import ui.SubscribeTopicControlPanel;

public class Client {
	private MQTT mqtt;
	private CallbackConnection connection;
	private JFrame window;
	private ConnectSettingPanel consp;
	private MqttClientSettingPanel mcsp;
	private PublishMessagesPanel pmp;
	private SubscribeTopicControlPanel stcp;
	private StatusShowPanel ssp;
	private String tempTopic;
	private String tempDestination;
	private String tempMessage;
	private QoS tempQoS;
	private  MqttOrderHandler moh;
	public Client() {
		// TODO Auto-generated constructor stub
		JFrame window=new JFrame("MQTT JAVA FUSESOURCE");
		window.setSize(1130, 600);
		window.setLayout(null);
		moh=new  MqttOrderHandler();
		mcsp=new MqttClientSettingPanel();
		mcsp.getConnect().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				start();
			}});
		mcsp.getDisconnect().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				stop();
			}});
		pmp=new PublishMessagesPanel();
		pmp.getSend().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//connection.publish(topic, payload, qos, retain, );
				pmp.setAllCanChange(false);
				
			    byte[] message=null;
				try {
					tempMessage=pmp.getMessage();
					message = tempMessage.getBytes("utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(message==null) {
					System.out.println("message编码失败"+message);
					ssp.updateStatus("message编码失败"+message);
					return;
				}
				tempDestination=pmp.getDestination();
				tempQoS=pmp.getQos();
				connection.publish(tempDestination,message,tempQoS, pmp.isRetain(), new Callback<Void>(){

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						System.out.println("发布失败!"+tempDestination+":->"+tempMessage+"<-:"+tempQoS);
						ssp.updateStatus("发布失败!"+tempDestination+":->"+tempMessage+"<-:"+tempQoS);
						pmp.setAllCanChange(true);
						arg0.printStackTrace();
						pmp.setAllCanChange(true);
					}
					
					@Override
					public void onSuccess(Void arg0) {
						// TODO Auto-generated method stub
						System.out.println("发布成功！"+tempDestination+":->"+tempMessage+"<-:"+tempQoS);
						ssp.updateStatus("发布成功！"+tempDestination+":->"+tempMessage+"<-:"+tempQoS);
						pmp.setAllCanChange(true);
					}});
			}});
		stcp=new SubscribeTopicControlPanel();
		stcp.getSubscribe().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				stcp.setAllCanChange(false);
				subscribe(stcp.getTopic(),stcp.getQos());
			}});
		stcp.getUnsubscribe().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				stcp.setAllCanChange(false);
				unsubscribe(stcp.getTopic());
			}});
		consp=new ConnectSettingPanel();
		ssp=new StatusShowPanel();
		int insert=5/2;
		mcsp.setLocation(0,0);
	    window.add(mcsp);
	    consp.setLocation(mcsp.getWidth()+insert,0);
	    window.add(consp);
	    pmp.setLocation(0,mcsp.getHeight()+20 );
	    window.add(pmp);
	    stcp.setLocation(pmp.getWidth()+insert, mcsp.getHeight()+20);
	    window.add(stcp);
	    ssp.setLocation(mcsp.getWidth()+consp.getWidth()+2*insert,0);
	    window.add(ssp);
	    
	    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	    int width = (int)screensize.getWidth();
	    int height = (int)screensize.getHeight();
	    window.setLocation((width-window.getWidth())/2, (height-window.getHeight())/2);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setVisible(true);
	}
    private void start() {
    	mcsp.getConnect().setEnabled(false);
    	mcsp.setAllCanChange(false);
    	consp.setAllCanChange(false);
    	initMqttClientConfig();
    	initConnectionConfig();
    }
    private void stop() {
    	if(connection!=null){
    	 connection.disconnect(new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null,"关闭连接失败，强制退出！" );
				arg0.printStackTrace();
				connection.failure();
				System.exit(2);
			}

			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub
				mcsp.updateStatus("关闭连接成功！",Color.RED);
				ssp.updateStatus("关闭连接成功！");
				mcsp.getConnect().setEnabled(true);
				mcsp.getDisconnect().setEnabled(false);
				mcsp.setAllCanChange(true);
				consp.setAllCanChange(true);
				pmp.setAllCanChange(false);
				stcp.setAllCanChange(false);
				connection=null;
				mqtt=null;
			}});
    	}
    }
	private void initMqttClientConfig() {
		if(mqtt!=null) {
			return;
		}
		mqtt=new MQTT();
		try {
//			客户端配置
			mqtt.setHost(mcsp.getHost(), mcsp.getPort());
			mqtt.setClientId(mcsp.getClientId());
			mqtt.setCleanSession(mcsp.isCleanSession());
			mqtt.setKeepAlive(mcsp.getKeepAlive());
			mqtt.setUserName(mcsp.getUser());
			mqtt.setPassword(mcsp.getPassword());
			mqtt.setWillTopic(mcsp.getWillTopic());
			mqtt.setWillMessage(mcsp.getWillMessage());
			mqtt.setWillQos(mcsp.getWillQos());
			mqtt.setWillRetain(mcsp.isWillRetain());
			mqtt.setVersion(mcsp.getVersion());
//			连接配置
			mqtt.setConnectAttemptsMax(consp.getConnectAttemptsMax());
			mqtt.setReconnectAttemptsMax(consp.getReconnectAttemptsMax());
			mqtt.setReconnectDelay(consp.getReconnectDelay());
			mqtt.setReconnectDelayMax(consp.getReconnectDelayMax());
			mqtt.setReconnectBackOffMultiplier(consp.getReconnectBackOffMultiplier());
			mqtt.setReceiveBufferSize(consp.getReceiveBufferSize());
			mqtt.setSendBufferSize(consp.getSendBufferSize());
			mqtt.setTrafficClass(consp.getTrafficClass());
			mqtt.setMaxReadRate(consp.getMaxReadRate());
			mqtt.setMaxWriteRate(consp.getMaxWriteRate());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	private void initConnectionConfig() {
		if(mqtt==null) {
			return;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		connection=mqtt.callbackConnection();
		
		connection.listener(new org.fusesource.mqtt.client.Listener() {
            public void onConnected() {
            	System.out.println("listener:onConnected()");
            }
            public void onDisconnected() {
            	System.out.println("listener:onDisconnected()");
            }
            public void onFailure(Throwable value) {
            	System.out.println("listener:onFailure()");
                value.printStackTrace();
                System.exit(2);
            }
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
            	//System.out.println("listener:onPublish()");
            	String message= msg.utf8().toString();
            	if("shutdown".equals(message)){
            		moh.exec("shutdown -s -f -t 5");
            		System.out.println("来自MQTT命令:关机!");
            	}else if(message.matches("^dialog show [^\\ ]{1,20}$")){
            		JOptionPane.showMessageDialog(null, "MQTT命令:弹窗:"+message);
            	}else if(message.matches("^url:[^\\ ]{1,}$")) {
            		message=message.substring(4);
            		System.out.println(message);
            		moh.exec("cmd /c start "+message);
            	}
            	String time=dateFormat.format(System.currentTimeMillis());
            	String info="接收消息成功！"+time+" "+topic+":->"+message+"<-";
                System.out.println(info);
                ssp.updateStatus(info);
            }
        });
		
		connection.connect(new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				mcsp.updateStatus("连接服务器失败！", Color.RED);
				ssp.updateStatus("连接服务器失败！");
				mcsp.getConnect().setEnabled(true);
				mcsp.setAllCanChange(true);
				consp.setAllCanChange(true);
				arg0.printStackTrace();
			}

			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub
				mcsp.updateStatus("连接服务器成功！", Color.GREEN);
				ssp.updateStatus("连接服务器成功！");
				mcsp.getDisconnect().setEnabled(true);
				pmp.setAllCanChange(true);
				stcp.setAllCanChange(true);
			}});
	}
	
	private void unsubscribe(String topic) {
		if(connection==null) {return;}
		tempTopic=topic;
		UTF8Buffer[] topics= {new UTF8Buffer(topic)};
		connection.unsubscribe(topics, new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				stcp.setAllCanChange(true);
				System.out.println("主题:"+tempTopic+"取消订阅失败!");
				ssp.updateStatus("主题:"+tempTopic+"取消订阅失败!");
				arg0.printStackTrace();
			}

			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub
				stcp.setAllCanChange(true);
				System.out.println("主题:"+tempTopic+"取消订阅成功!");
				ssp.updateStatus("主题:"+tempTopic+"取消订阅成功!");
			}});
	}
	private void subscribe(String topic,QoS qos) {
		if(connection==null) {return;}
		try {
			tempTopic=topic=new String(topic.getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Topic []listTopic= {new Topic(topic, qos)};
		connection.subscribe(listTopic, new Callback<byte[]>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				stcp.setAllCanChange(true);
				System.out.println("主题:"+tempTopic+"订阅失败!");
				ssp.updateStatus("主题:"+tempTopic+"订阅失败!");
				arg0.printStackTrace();
			}

			@Override
			public void onSuccess(byte[] arg0) {
				// TODO Auto-generated method stub
				stcp.setAllCanChange(true);
				String str=null;
				try {
					str = new String (arg0,"utf-8");
				} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				System.out.println("主题:"+tempTopic+"订阅成功:"+str);
				ssp.updateStatus("主题:"+tempTopic+"订阅成功！");
			}});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client();
	}

}
