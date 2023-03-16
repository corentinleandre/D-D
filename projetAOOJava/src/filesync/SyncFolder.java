package filesync;

import java.io.File;
import java.io.FileNotFoundException;

import filesync.exceptions.FileSyncException;
import filesync.exceptions.WrongTypeException;

public class SyncFolder {

	private final String name;
	private final String localpath;
	
	public SyncFolder(String path) throws FileSyncException, FileNotFoundException {
		File file = new File(path);
		
		if(!file.exists()) {
			throw new FileNotFoundException("file at " + file.getAbsolutePath() + " does not exist");
		}
		if(!file.isDirectory()) {
			throw new WrongTypeException("file at " + file.getAbsolutePath() + " is not a Directory");
		}
		
		this.localpath = file.getAbsolutePath();
		this.name = this.localpath.split("\\" + File.separator)[this.localpath.split("\\" + File.separator).length-1];
	}

	public String getName() {
		return name;
	}

	public String getLocalpath() {
		return localpath;
	}
	
	
}
