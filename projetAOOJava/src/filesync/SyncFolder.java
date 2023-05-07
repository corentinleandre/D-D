package filesync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class SyncFolder {
	
	private static List<SyncFolder> localfolders = new ArrayList<SyncFolder>();
	private static Thread syncClock = null;
	private static boolean syncClockRunning = false;

	private String name = "";
	private String localpath = "";
	private SyncMode mode;
	private Set<SyncFolder> pairedFolders = new HashSet<SyncFolder>();
	
	public SyncFolder(File file) {
		this.localpath = file.getAbsolutePath();
		this.name = file.getName();
		
		localfolders.add(this);
	}
	
	public SyncFolder(String path) {
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
	
	public List<SyncFile> getFiles() throws WrongTypeException, FileNotFoundException {
		ArrayList<SyncFile> alsf = new ArrayList<SyncFile>();
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
	
	public boolean pairWith(SyncFolder sf) {
		if(this.pairedFolders.add(sf)) {
			sf.pairWith(this);
		}
		return true;
	}
	
	public List<String> getAllSignatures() throws WrongTypeException, FileNotFoundException{
		ArrayList<String> toRet = new ArrayList<String>();
		for(SyncFile sf : this.getFiles()) {
			toRet.add(sf.getSignature());
		}
		return toRet;
	}
	
	public List<SyncFile> getUnsynchronizedFiles(List<SyncFile> syncFiles) throws WrongTypeException, FileNotFoundException{
		List<SyncFile> toRet = new ArrayList<SyncFile>();
		for(SyncFile sf : this.getFiles()) {
			if(!syncFiles.contains(sf)) {
				toRet.add(sf);
			}
		}
		return toRet;
	}
	
	public void synchronizeTo(SyncFolder sf) 
			throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException 
	{
		for(SyncFile usf : sf.getUnsynchronizedFiles(this.getFiles())) {
			new Copy(usf.getFile(),new File(this.getLocalpath() + usf.getPathFromSyncFolder()));
		}
	}
	
	public void synchronizeToPaired() throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException {
		for(SyncFolder sf : this.pairedFolders) {
			this.synchronizeTo(sf);
		}
	}

	public SyncMode getMode() {
		return mode;
	}
	
	public static void startSyncClock(int millis) {
		
		syncClock = new Thread() {
			
			public void run() {
				while(syncClockRunning) {
					for(SyncFolder sf : localfolders) {
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
