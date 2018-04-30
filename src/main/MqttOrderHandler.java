package main;

import java.io.IOException;

public class MqttOrderHandler {

	public MqttOrderHandler() {
		// TODO Auto-generated constructor stub
	}
	public void exec(String str) {  
		if(str==null) {return;}
        try {  
            Runtime.getRuntime().exec(str);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
