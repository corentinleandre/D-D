package projetAOOJava;

import java.io.FileNotFoundException;
import java.io.IOException;

import exceptions.FileSyncException;
import filesync.SyncFolder;
public class projetAOOJava {

	public static void main(String[] args) {
		try {
			
			SyncFolder sf = new SyncFolder("..\\D-D Members");
			System.out.println("Folder path : " + sf.getLocalpath());
			System.out.println("Folder name : " + sf.getName());
			for(String s : sf.getAllSignatures()) {
				System.out.println(s);
			}
			
			SyncFolder sf2 = new SyncFolder("..\\AHA");
			System.out.println("Folder path : " + sf2.getLocalpath());
			System.out.println("Folder name : " + sf2.getName());
			for(String s : sf2.getAllSignatures()) {
				System.out.println(s);
			}
			
			sf2.synchronizeTo(sf);
			
			SyncFolder.startSyncClock(30000);
			//Thread to stop after 5min
			new Thread() {
				public void run() {
					try {
						sleep(600000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SyncFolder.stopSyncClock();
				}
			}.start();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (FileSyncException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
