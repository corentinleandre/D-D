package projetAOOJava;

import java.io.File;
import java.io.FileNotFoundException;

import filesync.SyncFolder;
import filesync.exceptions.FileSyncException;

public class projetAOOJava {

	public static void main(String[] args) {
		try {
			SyncFolder sf = new SyncFolder("..\\king D-D-D");
			System.out.println(sf.getLocalpath());
			System.out.println(sf.getName());
			for(String s : sf.getFiles()) {
				System.out.println(s);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (FileSyncException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
