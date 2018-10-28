package induction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Array;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class InductorTest {
	private final Inductor subject = new Inductor();

	//simple facts, conjunction facts, depth 3, 5 columns
	@Test
	public void test1() throws Exception {
		//given
		ObjectMapper mapper = new ObjectMapper();
		File input = new File(this.getClass().getClassLoader().getResource("input1.tsv").getFile());
		Writer writer = new StringWriter();
		int additionalPremiseCount = 2;
		List<Integer> keyColumnNumber = Collections.singletonList(0);
		//when
		subject.execute(
				writer,
				additionalPremiseCount,
				input,
				keyColumnNumber,
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList(),
				1
		);
		String actualRawJson = writer.toString();
		List<SimpleRule> actual = mapper.readValue(actualRawJson, new TypeReference<List<SimpleRule>>() {
		});

		//then
		List<SimpleRule> expected = mapper.readValue(
				Thread
						.currentThread()
						.getContextClassLoader()
						.getResource("result1.txt"), new TypeReference<List<SimpleRule>>() {
				});
		assertThat(actual).containsAll(expected);
	}

	//simple facts, disjunction fact, depth 3, 5 columns
	@Test
	public void test2() throws Exception {
		//given
		ObjectMapper mapper = new ObjectMapper();
		File input = new File(this.getClass().getClassLoader().getResource("input2.tsv").getFile());
		Writer writer = new StringWriter();
		int additionalPremiseCount = 4;
		List<Integer> keyColumnNumber = Collections.singletonList(0);
		//when
		subject.execute(
				writer,
				additionalPremiseCount,
				input,
				keyColumnNumber,
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList(),
				1);
		String actualRawJson = writer.toString();
		List<SimpleRule> actual = mapper.readValue(actualRawJson, new TypeReference<List<SimpleRule>>() {
		});
		//then
		List<SimpleRule> expected = mapper.readValue(
				Thread
						.currentThread()
						.getContextClassLoader()
						.getResource("result2.txt"), new TypeReference<List<SimpleRule>>() {
				});

		assertThat(actual).containsAll(expected);
	}

	//using minToQualifyPremise flag
	@Test
	public void test3() throws Exception {
		//given
		File input = new File(this.getClass().getClassLoader().getResource("input2.tsv").getFile());
		Writer writer = new StringWriter();
		int additionalPremiseCount = 2;
		List<Integer> keyColumnNumber = Collections.singletonList(0);
		//when
		subject.execute(writer, additionalPremiseCount, input, keyColumnNumber, Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList(),
				3);
		String actual = writer.toString();
		//then
		String expected = toString(this.getClass().getClassLoader().getResourceAsStream("result3.txt"));
		assertThat(actual).isEqualTo(expected);
	}

	private static String toString(InputStream is) {
		try (java.util.Scanner s = new java.util.Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}
}