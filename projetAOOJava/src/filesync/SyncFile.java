package filesync;

import java.io.File;
import java.io.FileNotFoundException;

import exceptions.WrongTypeException;

public class SyncFile {
	
	private File file;
	private String pathFromSyncFolder = "";
	
	public SyncFile(File f) throws WrongTypeException, FileNotFoundException {
		if(!f.isFile()) {
			throw new WrongTypeException("expected file but got something else");
		}else if(!f.exists()) {
			throw new FileNotFoundException("couldn't find file at " + f.getAbsolutePath());
		}
		this.file = f;
	}
	
	public SyncFile(String path) throws WrongTypeException, FileNotFoundException {
		this(new File(path));
	}
	
	public SyncFile(File f, String fromSyncFolder) throws WrongTypeException, FileNotFoundException {
		if(!f.isFile()) {
			throw new WrongTypeException("expected file but got something else");
		}else if(!f.exists()) {
			throw new FileNotFoundException("couldn't find file at " + f.getAbsolutePath());
		}
		this.file = f;
		this.pathFromSyncFolder = fromSyncFolder;
	}
	
	public SyncFile(String path, String fromSyncFolder) throws WrongTypeException, FileNotFoundException {
		this(new File(path, fromSyncFolder));
	}
	
	public String getPathFromSyncFolder() {
		return pathFromSyncFolder;
	}

	public void setPathFromSyncFolder(String pathFromSyncFolder) {
		this.pathFromSyncFolder = pathFromSyncFolder;
	}
	
	public File getFile() {
		return this.file;
	}

	public String getSignature() {
		return this.pathFromSyncFolder + " " + file.lastModified();
	}
	

	public boolean equals(Object o) {
		if(o instanceof SyncFile) {
			SyncFile sf = (SyncFile) o;
			return sf.getSignature().equalsIgnoreCase(this.getSignature());
		}
		return false;
	}

}
