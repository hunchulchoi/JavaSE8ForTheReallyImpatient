package net.madhwang.exercise.chapter01;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Question08 {

	@Test
	public void solution() {

		String[] names = { "Peter", "Paul", "Mary" };

		List<Runnable> runners = new ArrayList<>();

		for (String name : names) {
			runners.add(() -> System.out.println(name));
		}

		for (Runnable runner : runners) {
			new Thread(runner).start();
		}

		runners.clear();

		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			runners.add(() -> System.out.println(name));
		}
		for (Runnable runner : runners) {
			new Thread(runner).start();
		}

	}

}
