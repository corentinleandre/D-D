package filesync;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import exceptions.FileNotCreatedException;
import exceptions.TypeMismatchException;
import exceptions.WrongTypeException;

public interface RemoteSyncFolder extends Remote {
	
	public String getName() throws RemoteException;
	public String getLocalpath() throws RemoteException;
	public String[] list() throws RemoteException;
	public List<RemoteSyncFile> getFiles() throws WrongTypeException, FileNotFoundException, RemoteException;
	public boolean pairWith(RemoteSyncFolder sf) throws RemoteException;
	public List<String> getAllSignatures() throws WrongTypeException, FileNotFoundException, RemoteException;
	public List<RemoteSyncFile> getUnsynchronizedFiles(List<RemoteSyncFile> syncFiles) throws WrongTypeException, FileNotFoundException, RemoteException;
	public void synchronizeTo(RemoteSyncFolder sf) 
			throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException, RemoteException;
	public void synchronizeToPaired() throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException, RemoteException;

}
