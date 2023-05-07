package projetAOOJava;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import exceptions.FileSyncException;
import filesync.GUI;
import filesync.RemoteSyncFolder;
import filesync.SyncFolder;
import utils.RmiServer;
public class projetAOOJava {

	public static void main(String[] args) {
		try {
			
			RmiServer.initialize();
			
			GUI gui = new GUI();
			
			SyncFolder sf = new SyncFolder("..\\D-D Members");
			System.out.println("Folder path : " + sf.getLocalpath());
			System.out.println("Folder name : " + sf.getName());
			for(String s : sf.getAllSignatures()) {
				System.out.println(s);
			}
			
			try {
				RmiServer.post(sf);
			} catch (AlreadyBoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			SyncFolder sf2 = new SyncFolder("..\\AHA");
			System.out.println("Folder path : " + sf2.getLocalpath());
			System.out.println("Folder name : " + sf2.getName());
			for(String s : sf2.getAllSignatures()) {
				System.out.println(s);
			}
			
			try {
				Remote r = RmiServer.lookup("rmi://192.168.1.26:8080/D-D_Members");
				if(r instanceof RemoteSyncFolder) {
					RemoteSyncFolder sfr = (RemoteSyncFolder) r;
					System.out.println("Folder path : " + sfr.getLocalpath());
					System.out.println("Folder name : " + sfr.getName());
					for(String s : sfr.getAllSignatures()) {
						System.out.println(s);
					}
					sf2.pairWith(sfr);
				}
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			
			sf2.synchronizeToPaired();
			
			
			
			
			
			
			
			
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
