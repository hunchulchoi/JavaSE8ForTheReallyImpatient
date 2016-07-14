package net.madhwang.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class Chapter02 {

	@Test
	public void test1() { /* 01. 반복에서 스트림 연산으로 */
		List<String> words = new ArrayList<String>();
		words.add("a");
		words.add("aa");
		words.add("b");
		words.add("abb");
		words.add("ac");

		System.out.println(words.stream().filter(w -> w.length() > 2).count());
		System.out.println(words.parallelStream().filter(w -> w.length() > 1).count());
	}

	@Test
	public void test2() { /* 02. 스트림 생성 */
		Stream<String> song = Stream.of("gently", "down", "the", "stream");
		System.out.println("song=" + song.allMatch(w -> w.length() > 2)); // 스트림 내의 문자열 모두 길이값이 2보다 크냐?

		String[] arr = new String[] { "macbook", "imac", "iphone", "ipad" };
		Stream<String> arrStream = Arrays.stream(arr);
		System.out.println("arrStream=" + arrStream.noneMatch(w -> w.startsWith("a"))); // 스트림내의 문자열 중 "a" 로 시작하는 것이 없느냐?

		Stream<String> empty = Stream.empty();
		System.out.println("empty=" + empty.filter(w -> w.length() > 1).count());

		Stream<String> echos = Stream.generate(() -> "Echo");
		// System.out.println(echos.filter(w -> w.length() > 1).count()); // 무한 루프

		Stream<Double> randoms = Stream.generate(Math::random);
		// randoms.filter(r -> r > 0.5).forEach(System.out::println); // random 값이 0.5 보다 크면 무한으로 출력

		Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE)).limit(5L);
		integers.filter(r -> r.longValue() < 5L).forEach(System.out::println); // 0부터 1씩 증가하면서 4까지 노출. limit가 없으면 무한루
	}

	@Test
	public void test3() { /* 03. filter, map, flatMap 메서드 */
		List<String> wordList = new ArrayList<String>();
		wordList.add("atrix");
		wordList.add("strong");
		wordList.add("jordan");
		wordList.add("terminator");

		Stream<String> words = wordList.stream();
		Stream<String> longWords = words.filter(w -> w.length() > 5);
		System.out.println("longWords======");
		longWords.forEach(System.out::println);

		words = wordList.stream();
		Stream<String> lowercaseWords = words.map(String::toLowerCase);
		System.out.println("lowercaseWords====");
		lowercaseWords.forEach(System.out::println);

		words = wordList.stream();
		Stream<Character> firstChars = words.map(s -> s.charAt(0));
		System.out.println("firstChars====");
		firstChars.forEach(System.out::println);

		words = wordList.stream();
		Stream<Stream<Character>> result = words.map(w -> characterStream(w));
		System.out.println("result====");
		result.forEach(System.out::println);

		words = wordList.stream();
		Stream<Character> letters = words.flatMap(w -> characterStream(w));
		System.out.println("letters====");
		letters.forEach(System.out::println);

	}

	public Stream<Character> characterStream(String s) {
		List<Character> result = new ArrayList<Character>();
		for (char c : s.toCharArray()) {
			result.add(c);
		}
		return result.stream();
	}

	@Test
	public void test4() { /* 04. 서브스트림 추출과 스트림 결합 */

		Stream<Double> randoms = Stream.generate(Math::random).limit(3);
		randoms.forEach(System.out::println);

		String content = "ttt:aaa:dddd:ccc:dddd";
		Stream<String> words = Stream.of(content.split(":")).skip(1);
		words.forEach(w -> System.out.println("TTT=" + w));

		Stream<Character> combined = Stream.concat(characterStream("Hello"), characterStream("World"));
		combined.forEach(System.out::println);

		/*
		 * peek 와 비슷한 역할을 forEach가 하나, peek 위치에는 들어가지 못한다. forEach 는 제일 끝에만 들어갈 수 있다. 즉, 이미 스트림 데이터가 생성되고 난 후 그 데이터를 개별적으로 처리할 수 있는게 forEach 이고, 스트림 데이터 생성중에 개별적으로 처리할 수 있는게 peek 가 되는 것 같다.
		 */
		Object[] powers = Stream.iterate(1.0, p -> p * 2).peek(e -> System.out.println("Fetching " + e)).limit(20).toArray();
	}

	@Test
	public void test5() { /* 05. 상태 유지 변환 */
		Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
		uniqueWords.forEach(System.out::println);

		/* uniqueWords 는 이미 사용되고 소멸되었기 때문에 아래와 같이 다시 생성 */
		uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
		Stream<String> longestFirst = uniqueWords.sorted(Comparator.comparing(String::length).reversed());
		longestFirst.forEach(System.out::println);
	}

	@Test
	public void test6() { /* 06. 단순리덕션 */
		List<String> words = new ArrayList<String>();
		words.add("a");
		words.add("aa");
		words.add("b");
		words.add("abb");
		words.add("ac");
		words.add("ca");
		words.add("cb");
		Stream<String> wordsStream = words.stream();
		wordsStream.forEach(System.out::println);
		wordsStream = words.stream();

		Optional<String> largest = wordsStream.max(String::compareToIgnoreCase);

		if (largest.isPresent()) {
			System.out.println("largest :" + largest.get());
		}

		Optional<String> startWithC = words.stream().filter(s -> s.startsWith("c")).findFirst();
		System.out.println("startWithC =" + startWithC.get());

		Optional<String> anyWithC = words.stream().filter(s -> s.startsWith("c")).findAny();
		System.out.println("anyWithC =" + anyWithC.get());

		boolean aWoardStartWithC = words.stream().parallel().anyMatch(s -> s.startsWith("c"));
		System.out.println("aWoardStartWithC = " + aWoardStartWithC);

	}

	@Test
	public void test7() { /* 7. 옵션 타입 */
		List<String> words = new ArrayList<String>();
		words.add("a");
		words.add("aa");
		words.add("b");
		words.add("abb");
		words.add("ac");
		words.add("ca");
		words.add("cb");

		List<String> wordsCopy = new ArrayList<String>();

		Optional<String> startWithC = words.stream().filter(s -> s.startsWith("c")).findFirst();
		startWithC.ifPresent(System.out::println);
		startWithC.ifPresent(v -> wordsCopy.add(v));
		System.out.println("wordsCopy = " + wordsCopy);
		startWithC.ifPresent(wordsCopy::add);
		System.out.println("wordsCopy = " + wordsCopy);
		Optional<Boolean> added = startWithC.map(wordsCopy::add);
		System.out.println("added = " + added);
		System.out.println("wordsCopy = " + wordsCopy);

		Optional<String> startWithD = words.stream().filter(s -> s.startsWith("d")).findFirst();
		String result = startWithD.orElse("");
		System.out.println("result = " + result);

		result = startWithD.orElseGet(() -> System.getProperty("user.dir"));
		System.out.println("result = " + result);

		try {
			result = startWithD.orElseThrow(NoSuchElementException::new);
		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException");
		}

		double a = 2.0;
		Optional<Double> b = a == 1.0 ? Optional.empty() : Optional.of(1 / a);
		System.out.println("b = " + b);
		System.out.println("b ofNullable = " + Optional.ofNullable(b));

		Optional<Double> c = a == 2.0 ? Optional.empty() : Optional.of(1 / a);
		System.out.println("c = " + Optional.ofNullable(c));
		System.out.println("c ofNullable = " + Optional.ofNullable(c));

	}

	@Test
	public void test8() /* 08. 리덕션 연산 */
	{

		List<Integer> values = new ArrayList<Integer>();
		values.add(1);
		values.add(2);
		values.add(3);
		values.add(4);
		values.add(5);

		Stream<Integer> valueStream = values.stream();
		Optional<Integer> sum = valueStream.reduce((x, y) -> x + y);
		System.out.println("sum = " + sum);

		valueStream = values.stream();
		System.out.println("valueStream.reduce((x, y) -> x + y) = " + valueStream.reduce((x, y) -> x + y));

		List<Integer> values2 = new ArrayList<Integer>();
		valueStream = values2.stream();
		System.out.println("valueStream.reduce(0, (x, y) -> x + y) = " + valueStream.reduce(0, (x, y) -> x + y));

		List<String> words = new ArrayList<String>();
		words.add("a");
		words.add("aa");
		words.add("b");
		words.add("abb");
		words.add("ac");
		words.add("ca");
		words.add("cb");

		Stream<String> wordStream = words.stream();
		System.out.println("wordStream.reduce(0, (total, word) -> total + word.length(), (total1, total2) -> total1 + total2) =" + wordStream.reduce(0, (total, word) -> total + word.length(), (total1, total2) -> total1 + total2));
		wordStream = words.stream();
		System.out.println("wordStream.mapToInt(String::length).sum() = " + wordStream.mapToInt(String::length).sum());

		valueStream = values.stream();
		System.out.println("" + valueStream.max((x, y) -> x - y));

		valueStream = values.stream();
		System.out.println("" + valueStream.min((x, y) -> x - y));

	}

	@Test
	public void test9() /* 09. 결과 모으기 */
	{

		List<String> words = new ArrayList<String>();
		words.add("a");
		words.add("aa");
		words.add("b");
		words.add("abb");
		words.add("ac");
		words.add("ca");
		words.add("cb");
		words.add("a");

		String[] result = words.stream().toArray(String[]::new);
		System.out.println("result = " + result);

		List<String> resultList = words.stream().collect(Collectors.toList());
		System.out.println("resultList = " + resultList);

		Set<String> resultSet = words.stream().collect(Collectors.toSet());
		System.out.println("resultSet = " + resultSet);

		TreeSet<String> resultTreeSet = words.stream().collect(Collectors.toCollection(TreeSet::new));
		System.out.println("resultTreeSet = " + resultTreeSet);

		String resultString = words.stream().collect(Collectors.joining());
		System.out.println("resultString = " + resultString);

		String resultString2 = words.stream().collect(Collectors.joining(","));
		System.out.println("resultString2 = " + resultString2);

		IntSummaryStatistics summary = words.stream().collect(Collectors.summarizingInt(String::length));
		System.out.println("summary = " + summary.getAverage());
	}

	class Person {
		Integer id;
		String name;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	@Test
	public void test10() {

		List<Person> personList = new ArrayList<Person>();
		for (int i = 0; i < 5; i++) {

			Person p = new Person();
			p.setId(i);
			p.setName("aa" + i);

			personList.add(p);
		}

		Map<Integer, String> idToName = personList.stream().collect(Collectors.toMap(Person::getId, Person::getName));
		System.out.println("idToName========" + idToName);

		Map<Integer, Person> idToPerson = personList.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
		System.out.println("idToPerson ========" + idToPerson);
		System.out.println("idToPerson.get(0).getName() ========" + idToPerson.get(0).getName());

	}

}
