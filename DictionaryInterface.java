import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DictionaryInterface extends Remote {
	
	public String put(String key, String value, String sender) throws RemoteException;
	
	public String get(String key, String sender) throws RemoteException;
	
	public String delete(String key, String sender) throws RemoteException;
	
	public String print() throws RemoteException;
}
