package semexe.record;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RecordServerInterface extends Remote {
    ResultReceiver processCommand(String line, ReceiverInterface receiver) throws RemoteException;

    String getPrompt() throws RemoteException;
}
