package net.madhwang.exercise.chapter01;

import org.junit.Test;

public class Question07 {

	@Test
	public void solution() {
		new Thread(andThen(() -> System.out.println("First"), () -> System.out.println("Second")));
	}

	public static Runnable andThen(Runnable first, Runnable second) {
		return () -> {
			first.run();
			second.run();
		};
	}

}