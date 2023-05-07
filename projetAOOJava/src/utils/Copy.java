package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import exceptions.FileNotCreatedException;
import exceptions.TypeMismatchException;
import exceptions.WrongTypeException;

public class Copy implements Runnable {

	private File source;
	private File dest;
	
	public Copy(String from, String to) {
		
	}
	
	public Copy(File from, File to) 
			throws WrongTypeException, TypeMismatchException, FileNotCreatedException, IOException 
	{
		System.out.println("trying to copy from : " + from.getAbsolutePath() + " -to-> " + to.getAbsolutePath());
		if(from.isFile()) {
			if(from.exists()) {
				this.source = from;
			}else {
				if(!from.exists()) {
					throw new FileNotFoundException("could not find source file");
				}
			}
			if(!to.exists()) {
				Copy.createPathToFile(to);
			}
			this.dest = to;
			
			new Thread(this).start();
		}else if(from.isDirectory()) {
			if(to.isFile()) {
				throw new TypeMismatchException("input is a directory and output a file");
			}else if(!to.exists()){
				if(!Copy.createPathToDirectory(to)) {
					throw new FileNotCreatedException("could not create destination directory at "+to.getAbsolutePath());
				}
			}			
			for( File fs : from.listFiles()) {
				File fd = new File(to.getAbsolutePath()+File.separator+fs.getName());
				if(fs.isDirectory()) {
					if(!fd.exists() && !fd.mkdir()) {
						throw new FileNotCreatedException("could not create destination directory at "+fd.getAbsolutePath());
					}
				}
				new Copy(fs,fd);
			}
		}
			
			
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(dest);
			
			while(in.available() > 0) {
				out.write(in.read());
			}
			
			dest.setLastModified(source.lastModified());
			
			in.close();
			out.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean createPathToFile(File f) throws IOException {
		if(f.exists()) return true;
		Copy.createPathToDirectory(f.getParentFile());
		if(f.isFile()) {
			return f.createNewFile();
		}else if(f.isDirectory()) {
			return f.mkdir();
		}
		return false;
	}
	
	public static boolean createPathToDirectory(File f) {
		if(f.exists()) return true;
		if(f.getParentFile() != null && !f.getParentFile().exists()) {
			if(Copy.createPathToDirectory(f.getParentFile())) {
				return f.mkdir(); 
			}else {
				return false;
			}
		}else if(f.getParentFile().exists()) {
			return f.mkdir(); 
		} else  if(f.getParentFile() == null) {
			return false;
		}
		return false;
	}

}
