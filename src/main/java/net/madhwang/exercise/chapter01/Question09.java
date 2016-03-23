package net.madhwang.exercise.chapter01;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Test;

public class Question09 {

	public interface Collection2<T> extends Collection<T> {
		default void forEachIf(Consumer<T> action, Predicate<T> filter) {

			forEach(x -> {
				if (filter.test(x)) {
					action.accept(x);
				}
			});
		}

	}

	public static class MyList<T> extends ArrayList<T> implements Collection2<T> {
	}

	@Test
	public void solution() {
		MyList<String> strings = new MyList<>();
		strings.add("a");
		strings.add("ab");
		strings.add("abc");
		strings.add("abcd");

		strings.forEachIf(System.out::println, s -> s.length() > 2);
	}

}
