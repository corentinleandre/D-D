package filesync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exceptions.FileNotCreatedException;
import exceptions.TypeMismatchException;
import exceptions.WrongTypeException;
import utils.Copy;

public class SyncFolder extends UnicastRemoteObject implements RemoteSyncFolder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615814265120177769L;
	private static List<RemoteSyncFolder> localfolders = new ArrayList<RemoteSyncFolder>();
	private static Thread syncClock = null;
	private static boolean syncClockRunning = false;

	private String name = "";
	private String localpath = "";
	private Set<RemoteSyncFolder> pairedFolders = new HashSet<RemoteSyncFolder>();
	
	public SyncFolder(File file) throws RemoteException, FileNotFoundException{
		if(!file.exists()) throw new FileNotFoundException("Cannot create SyncFolder as the referenced folder does not exist");
		this.localpath = file.getAbsolutePath();
		this.name = file.getName();
		
		localfolders.add(this);
	}
	
	public SyncFolder(String path) throws RemoteException, FileNotFoundException {
		this(new File(path));
	}

	public String getName() {
		return name;
	}

	public String getLocalpath() {
		return localpath;
	}
	
	public String[] list() {
		File sf = new File(this.localpath);
		return sf.list();
	}
	
	public List<RemoteSyncFile> getFiles() throws WrongTypeException, FileNotFoundException, RemoteException {
		ArrayList<RemoteSyncFile> alsf = new ArrayList<RemoteSyncFile>();
		Map<String,File> toTreat = new HashMap<String,File>();
		File sf = new File(this.localpath);
		toTreat.put("", sf);
		File f;
		List<String> currentTreatment;
		while(!toTreat.isEmpty()) {
			currentTreatment = List.copyOf(toTreat.keySet());
			for (String p : currentTreatment) {
				f = toTreat.get(p);
				for(String s : f.list()) {
					f = new File(this.localpath + p + File.separator + s);
					if(f.isFile()) {
						alsf.add(new SyncFile(f,p + File.separator + s));
					}else if(f.isDirectory()) {
						toTreat.putIfAbsent(p + File.separator + s, f);
					}
				}
				toTreat.remove(p);
			}
		}
		
		return alsf;
	}
	
	public boolean pairWith(RemoteSyncFolder sf) throws RemoteException {
		if(this.pairedFolders.add(sf)) {
			sf.pairWith(this);
		}
		return true;
	}
	
	public List<String> getAllSignatures() throws WrongTypeException, FileNotFoundException, RemoteException{
		ArrayList<String> toRet = new ArrayList<String>();
		for(RemoteSyncFile sf : this.getFiles()) {
			toRet.add(sf.getSignature());
		}
		return toRet;
	}
	
	public List<RemoteSyncFile> getUnsynchronizedFiles(List<RemoteSyncFile> syncFiles) throws WrongTypeException, FileNotFoundException, RemoteException{
		List<RemoteSyncFile> toRet = new ArrayList<RemoteSyncFile>();
		for(RemoteSyncFile sf : this.getFiles()) {
			if(!syncFiles.contains(sf)) {
				toRet.add(sf);
			}
		}
		return toRet;
	}
	
	public void synchronizeTo(RemoteSyncFolder sf) 
			throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException 
	{
		for(RemoteSyncFile usf : sf.getUnsynchronizedFiles(this.getFiles())) {
			new Copy(usf.getFile(),new File(this.getLocalpath() + usf.getPathFromSyncFolder()));
		}
	}
	
	public void synchronizeToPaired() throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException {
		for(RemoteSyncFolder sf : this.pairedFolders) {
			this.synchronizeTo(sf);
		}
	}
	
	public static void startSyncClock(int millis) {
		
		syncClock = new Thread() {
			
			public void run() {
				while(syncClockRunning) {
					for(RemoteSyncFolder sf : localfolders) {
						try {
							sf.synchronizeToPaired();
						} catch (WrongTypeException e) {
							e.printStackTrace();
						} catch (TypeMismatchException e) {
							e.printStackTrace();
						} catch (FileNotCreatedException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try {
						sleep(millis);
					} catch (InterruptedException e) {
						e.printStackTrace();
						this.interrupt();
					}
				}
			}
		};
		
		syncClock.start();
		syncClockRunning = true;
	}
	
	public static void stopSyncClock() {
		syncClockRunning = false;
		syncClock = null;
	}
	
}
