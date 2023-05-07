package filesync;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import exceptions.WrongTypeException;

public class SyncFile extends UnicastRemoteObject implements RemoteSyncFile {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1855317577960332013L;
	private File file;
	private String pathFromSyncFolder = "";
	
	public SyncFile(File f) throws WrongTypeException, FileNotFoundException, RemoteException {
		if(!f.isFile()) {
			throw new WrongTypeException("expected file but got something else");
		}else if(!f.exists()) {
			throw new FileNotFoundException("couldn't find file at " + f.getAbsolutePath());
		}
		this.file = f;
	}
	
	public SyncFile(String path) throws WrongTypeException, FileNotFoundException, RemoteException {
		this(new File(path));
	}
	
	public SyncFile(File f, String fromSyncFolder) throws WrongTypeException, FileNotFoundException, RemoteException {
		if(!f.isFile()) {
			throw new WrongTypeException("expected file but got something else");
		}else if(!f.exists()) {
			throw new FileNotFoundException("couldn't find file at " + f.getAbsolutePath());
		}
		this.file = f;
		this.pathFromSyncFolder = fromSyncFolder;
	}
	
	public SyncFile(String path, String fromSyncFolder) throws WrongTypeException, FileNotFoundException, RemoteException {
		this(new File(path, fromSyncFolder));
	}
	
	public String getPathFromSyncFolder() throws RemoteException{
		return pathFromSyncFolder;
	}

	public void setPathFromSyncFolder(String pathFromSyncFolder) throws RemoteException{
		this.pathFromSyncFolder = pathFromSyncFolder;
	}
	
	public File getFile() throws RemoteException{
		return this.file;
	}

	public String getSignature() throws RemoteException{
		return this.pathFromSyncFolder + " " + file.lastModified();
	}
	

	public boolean equals(Object o) {
		if(o instanceof RemoteSyncFile) {
			RemoteSyncFile sf = (RemoteSyncFile) o;
			try {
				return sf.getSignature().equalsIgnoreCase(this.getSignature());
			} catch (RemoteException e) {
				return false;
			}
		}
		return false;
	}

}
