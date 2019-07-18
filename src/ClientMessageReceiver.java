import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientMessageReceiver implements Runnable{
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean stopFlag = false; 
	
	
	

	public ClientMessageReceiver(DataInputStream dis) {
		super();
		this.dis = dis;
	}

	@Override
	public void run() {
		while(!stopFlag) {
			try {
				System.out.println(dis.readUTF());
				
			} catch (IOException e) {
				if("Connection reset".equals(e.getMessage())) {
					System.out.println("与服务器的连接已中断！");
					break;
				}
				
				if(!stopFlag) {
					e.printStackTrace();					
				}
			}
		}
		
		
	}
	
	
	public void stop() {
		stopFlag = true;
	}

}
