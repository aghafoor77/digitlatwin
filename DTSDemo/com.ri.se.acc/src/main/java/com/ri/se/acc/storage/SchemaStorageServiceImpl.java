package com.ri.se.acc.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
public class SchemaStorageServiceImpl implements SchemaStorageService {

	private final Path root = Paths.get("schemas");

	@Override
	public void init() {
		try {

			Files.createDirectories(root);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public String save(String data) throws Exception {
		String ref = System.currentTimeMillis() + "";
		File file = new File(this.root.resolve(ref).toString());
		if (file.exists()) {
			throw new FileNotFoundException("File/Record already exist !");
		}
		Files.write(this.root.resolve(ref), data.getBytes());
		return ref;
	}

	@Override
	public byte[] load(String ref) throws Exception {

		File file = new File(this.root.resolve(ref).toString());
		if (!file.exists()) {
			throw new FileNotFoundException(ref+ " Record not found.");
		}

		Path filePath = this.root.resolve(ref);

		try {
			return Files.readAllBytes(filePath);
		} catch (IOException e) {
			throw new Exception("Problems when fetching record .");
		}
	}

	@Override
	public ArrayList<String> list() throws Exception {

		ArrayList<String> records = new ArrayList<String>();
		File directory = new File(this.root.toString());
		File[] files = directory.listFiles();
		Arrays.sort(files, Comparator.comparingLong(File::lastModified));
		for (File f : files) {
			records.add(f.getName());
		}
		return records;
	}
	
	@Override
	public void deleteAll() throws Exception {

		ArrayList<String> records = new ArrayList<String>();
		File directory = new File(this.root.toString());
		File[] files = directory.listFiles();
		Arrays.sort(files, Comparator.comparingLong(File::lastModified));
		for (File f : files) {
			f.delete();
		}
	}

	@Override
	public long schemaLength(String ref) {
		File newFile = new File(this.root.resolve(ref).toString());
		return newFile.length();
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
}