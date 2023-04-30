package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import exceptions.WrongTypeException;

public class Copy implements Runnable {

	private File source;
	private File dest;
	
	public Copy(String from, String to) {
		
	}
	
	public Copy(File from, File to) throws WrongTypeException, FileNotFoundException {
		if(from.exists() && from.isFile()) {
			this.source = from;
		}else {
			if(!from.exists()) {
				throw new FileNotFoundException();
			}
			if(from.isDirectory()) {
				throw new WrongTypeException("input is a directory not a file");
			}
		}
		if(!to.exists()) {
			try {
				to.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			if(to.isDirectory()) {
				throw new WrongTypeException("output is a directory not a file");
			}
		}
		this.dest = to;
		
		new Thread(this).start();
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
			
			in.close();
			out.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
