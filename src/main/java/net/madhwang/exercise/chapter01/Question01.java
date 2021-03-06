package net.madhwang.exercise.chapter01;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

public class Question01 {

	@Test
	public void solution() {
		String[] arr = new String[] { "ABC", "GEH", "DEF" };

		Comparator<String> comp = (x, y) -> {
			System.out.println(String.format("Comparator thread: %d", Thread.currentThread().getId()));
			return x.compareTo(y);
		};

		System.out.println(String.format("Main thread: %d", Thread.currentThread().getId()));
		Arrays.sort(arr, comp);
		System.out.println(String.format("Sorting done: %s", Arrays.toString(arr)));

	}

}
