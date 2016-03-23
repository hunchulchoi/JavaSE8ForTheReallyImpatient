package net.madhwang.exercise.chapter01;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

public class Question03 {

	@Test
	public void solution() {
		File dirFile = new File(".");

		Arrays.asList(dirFile.listFiles(f -> f.getName().endsWith("xml"))).forEach(System.out::println);
	}

}