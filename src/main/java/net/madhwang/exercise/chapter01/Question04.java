package net.madhwang.exercise.chapter01;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

public class Question04 {

	@Test
	public void solution() {

		File dirFile = new File("/usr");

		File[] files = dirFile.listFiles();

		Arrays.sort(files, (f1, f2) -> {
			if (f1.isDirectory() && f2.isDirectory() == false) {
				return -1;
			} else if (f1.isDirectory() == false && f2.isDirectory() == true) {
				return 1;
			} else {
				return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
			}

		});
		Arrays.asList(files).forEach(System.out::println);
	}

}
