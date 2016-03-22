package net.madhwang.exercise.chapter01;

import java.io.File;
import java.util.Arrays;

public class Question03 {

	public static void main(String[] argv) {
		File dirFile = new File(".");

		Arrays.asList(dirFile.listFiles(f -> f.getName().endsWith("xml"))).forEach(System.out::println);
	}

}