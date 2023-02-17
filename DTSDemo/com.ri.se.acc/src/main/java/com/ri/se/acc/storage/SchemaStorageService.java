package com.ri.se.acc.storage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.springframework.core.io.ByteArrayResource;

public interface SchemaStorageService {
	public void init();

	public byte[] load(String filename) throws Exception;

	public Stream<Path> loadAll();

	public long schemaLength(String ref);

	public String save(String data) throws Exception;

	public ArrayList<String> list() throws Exception;

	public void deleteAll() throws Exception;

}