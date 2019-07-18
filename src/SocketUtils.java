import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketUtils {
	private static DataInputStream dis = null;
	private static DataOutputStream dos = null;
	
	
	
	public static void Outputwrite(Socket socket ,String str) throws IOException {
		
		dos = new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(str);
		dos.flush();
		
	}




}
