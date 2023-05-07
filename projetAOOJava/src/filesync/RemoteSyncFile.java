package filesync;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSyncFile extends Remote {
	
	public String getPathFromSyncFolder() throws RemoteException;
	public void setPathFromSyncFolder(String pathFromSyncFolder)throws RemoteException;	
	public File getFile()throws RemoteException;
	public String getSignature()throws RemoteException;
	//public boolean equals(Object o);
	
}
