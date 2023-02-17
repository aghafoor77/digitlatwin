package org.ri.se.platform.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class ExternalStorage {
	
	private String url = "";
	public ExternalStorage (String url) {
		this.url = url;
	}
	public String storeLocalFile(byte [] data) throws IOException {
		File file = new File(url+File.separator+new Random().nextLong());
		Path path = Paths.get(file.getAbsolutePath());       
	    Files.write(path, data);       
		return file.getAbsolutePath();
	}
}
