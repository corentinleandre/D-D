package projetAOOJava;

import java.io.File;
import java.io.FileNotFoundException;

import exceptions.FileSyncException;
import filesync.SyncFolder;
import utils.Copy;

public class projetAOOJava {

	public static void main(String[] args) {
		try {
			SyncFolder sf = new SyncFolder("..\\D-D members");
			System.out.println("File path : " + sf.getLocalpath());
			System.out.println("File name : " + sf.getName());
			System.out.println(sf.getMode().toString());
			for(String s : sf.getFiles()) {
				System.out.println(s);
			}
			SyncFolder sfp = new SyncFolder("..\\D-D' members");
			System.out.println(sfp.getMode().toString());
			
			File f1 = new File("..\\D-D members\\D-D List");
			File f2 = new File("..\\D-D' members\\D-D List");
			new Copy(f1,f2);
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
