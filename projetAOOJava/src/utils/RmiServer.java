package utils;

import java.io.File;
import java.net.BindException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

import filesync.SyncFile;
import filesync.SyncFolder;

public class RmiServer {
	
	public static Registry registry = null;
	
	@SuppressWarnings({ "removal" })
	public static void initialize() throws RemoteException {
		if(registry == null) {
			try {
				registry = LocateRegistry.createRegistry(8080);
			} catch(ExportException e) {
				registry = LocateRegistry.getRegistry(8080);
			}
			if(registry != null) {
				for(String s : registry.list()) {
					try {
						registry.unbind(s);
					} catch (AccessException e) {
						System.out.println("a");
						e.printStackTrace();
					} catch (RemoteException e) {
						System.out.println("b");
						e.printStackTrace();
					} catch (NotBoundException e) {
						System.out.println("c");
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public static void post(SyncFolder sf) throws MalformedURLException, RemoteException, AlreadyBoundException, UnknownHostException {
		Naming.bind(makeAddress(sf.getName()), sf);
		System.out.println("Posted with url : " + makeAddress(sf.getName()));
	}
	
	public static void post(String syncFolderName, SyncFile sf) throws MalformedURLException, RemoteException, AlreadyBoundException, UnknownHostException {
		Naming.bind(makeAddress(syncFolderName + File.separator+ sf.getPathFromSyncFolder()), sf);
	}
	
	public static Remote lookup(String url) throws MalformedURLException, RemoteException, NotBoundException {
		return Naming.lookup(url);
	}
	
	public static String makeAddress(String address) throws UnknownHostException {
		return "rmi://" + InetAddress.getLocalHost().getHostAddress() + ":" + "8080" + "/" + address.replace(File.separatorChar, '/').replace(' ', '_');
	}
	
	public static String makeAddress(String address, String hostAddress, String hostPort) throws UnknownHostException {
		return "rmi://" + hostAddress + ":" + hostPort + "/" + address.replace(File.separatorChar, '/');
	}
	
	public static String makeAddress(String address, String hostAddress) throws UnknownHostException {
		return "rmi://" + hostAddress + ":8080/" + address.replace(File.separatorChar, '/');
	}

}
