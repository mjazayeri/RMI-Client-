import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

//
public class TCPCommunication implements Runnable {
	
	private Socket client;
	private Logger serverLogger;
	private DataDictionary dictionary;
	private String clientID;
	
	public TCPCommunication(Socket communicationSocket, Logger logger, DataDictionary dictionary){
		client = communicationSocket;
		clientID = client.getInetAddress().toString() ;
		
		serverLogger = logger;
		
		this.dictionary = dictionary;
	}
	
	//ExecuterService runs this method for each client
	@Override 
	public void run() {
		
		serverLogger.WriteLog("Connection From <"+clientID+"> accepted");
		communicate();
	}
	
	//receives a client's requests and reply to them
	private void communicate(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

			while(true){

				String request = reader.readLine();
				if(!SendResponse(request))
					return;
			}
		}
		catch(Exception e){
			serverLogger.WriteLog("server->GetResponse->exception:"+ e.getMessage());
		}
	}
	
	private boolean SendResponse(String request)
	{
		
		try {
			//To test client's timeout handling uncomment bellow
			//Thread.sleep(10000);
			DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
			
			//client requests to close the connection -> not mentioned in client-side on purpose!
			if(request.equals("done")){

				serverLogger.WriteLog("close socket requested and server accepted");
				client.close();
				return false;
			}
			else{

				serverLogger.WriteLog("Request of <" + clientID + "> : " + request);
				String response = dictionary.ExecuteCommand(request, clientID);
				outputStream.writeBytes(response + "\n");
				serverLogger.WriteLog("Server Response to <"+clientID+"> : "+response);
			}
		} 
		catch (Exception e) {
			serverLogger.WriteLog("server->SendResponse->exception:"+ e.getMessage());
			return false;
		}
		
		return true;
	}
}
