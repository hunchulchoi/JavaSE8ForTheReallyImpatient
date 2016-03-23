package net.madhwang.exercise.chapter01;

import org.junit.Test;

public class Question06 {

	@Test
	public void solution() {
		new Thread(uncheck(() -> {
			System.out.println("Zzz");
			Thread.sleep(1000);
		})).start();
	}

	public static Runnable uncheck(RunnableEx runner) {
		return () -> {
			try {
				runner.run();
			} catch (Exception ignored) {
			}
		};

	}
}

@FunctionalInterface
interface RunnableEx {
	void run() throws Exception;

}