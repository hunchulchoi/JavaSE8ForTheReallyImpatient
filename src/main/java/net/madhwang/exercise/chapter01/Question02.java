package net.madhwang.exercise.chapter01;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

public class Question02 {

	@Test
	public void solution() {
		File dirFile = new File(".");
		
		printDirectory(dirFile);
	}
	
	void printDirectory(File file) {

		System.out.println(file.getPath());

		Arrays.asList(file.listFiles(File::isDirectory)).forEach(f->printDirectory(f));
	    }
}
