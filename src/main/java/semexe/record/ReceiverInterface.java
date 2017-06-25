package semexe.record;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * What the server will use to make callbacks to the client receiver.
 */
public interface ReceiverInterface extends Remote {
    void printOut(String s) throws RemoteException;

    void printErr(String s) throws RemoteException;

    void executeMandate(Mandate mandate) throws RemoteException;

    void flush() throws RemoteException;
}
