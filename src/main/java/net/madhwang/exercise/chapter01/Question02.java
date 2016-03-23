package net.madhwang.exercise.chapter01;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

public class Question02 {

	@Test
	public void solution() {
		File dirFile = new File(".");
		Arrays.asList(dirFile.listFiles(d -> d.isDirectory())).forEach(System.out::println);
		System.out.println("==============================");
		Arrays.asList(dirFile.listFiles(File::isDirectory)).forEach(System.out::println);

	}
}
