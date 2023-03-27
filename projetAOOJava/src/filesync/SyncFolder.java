package filesync;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import filesync.exceptions.FileSyncException;
import filesync.exceptions.WrongTypeException;

public class SyncFolder {

	private final String name;
	private final String localpath;
	private List<SyncFolder> pairedFolders;
	private SyncMode mode;
	
	public SyncFolder(String path) throws FileSyncException, FileNotFoundException {
		File file = new File(path);
		
		if(!file.exists()) {
			if(file.mkdir()) {
				this.mode = SyncMode.RECEIVER;
			}else {
				this.mode = SyncMode.ERROR;
			}
		}else if(!file.isDirectory()) {
			throw new WrongTypeException("file at " + file.getAbsolutePath() + " is not a Directory");
		}else {
			this.mode = SyncMode.SOURCE;
		}
		
		this.localpath = file.getAbsolutePath();
		this.name = this.localpath.split("\\" + File.separator)[this.localpath.split("\\" + File.separator).length-1];
		this.pairedFolders = new ArrayList<SyncFolder>();
	}

	public String getName() {
		return name;
	}

	public String getLocalpath() {
		return localpath;
	}
	
	public String[] getFiles() {
		File sf = new File(this.localpath);
		return sf.list();
	}

	public List<SyncFolder> getPairedFolders() {
		return pairedFolders;
	}

	public SyncMode getMode() {
		return mode;
	}
	
	
}
