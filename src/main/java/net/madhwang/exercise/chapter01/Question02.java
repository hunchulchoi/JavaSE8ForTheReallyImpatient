package net.madhwang.exercise.chapter01;

import java.io.File;
import java.util.Arrays;

public class Question02 {

	public static void main(String[] argv) {
		File dirFile = new File(".");
		Arrays.asList(dirFile.listFiles(d -> d.isDirectory())).forEach(System.out::println);
		System.out.println("==============================");
		Arrays.asList(dirFile.listFiles(File::isDirectory)).forEach(System.out::println);

	}
}
