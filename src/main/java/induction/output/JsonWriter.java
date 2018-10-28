package induction.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import induction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JsonWriter {
	private static final Logger logger = LoggerFactory.getLogger(JsonWriter.class);
	private final ObjectMapper mapper = new ObjectMapper();

	public void write(Collection<SetRule> rules, Writer writer) throws IOException {
		AtomicInteger total = new AtomicInteger(0);
		logger.info("export started");
		try {
			writer.write("[");

			List<SetPremise> premiseList = rules
					.stream()
					.map(SetRule::getSetPremise)
					.collect(Collectors.toList());

			logger.info("premises count: " + premiseList.size());
			int i = 0;
			for (SetRule rule : rules) {
				if (i % 100 == 0) {
					logger.info("premises done: " + i);
				}
				ArrayNode premiseNode = create(rule.getSetPremise());

				ObjectNode ruleNode = mapper.createObjectNode();
				ruleNode.put("premises", premiseNode);
				ruleNode.put("conclusion", create(rule.getConclusion()));
				ruleNode.put("GrfIrf", create(rule.getCounters()));
				ruleNode.put("debugInfo", createOther(rule.getCounters()));

				String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ruleNode);
				if (total.intValue() == 0) {
					writer.write(jsonOutput);
				} else {
					writer.write("," + jsonOutput);
				}
				writer.flush();
				total.incrementAndGet();

				i++;
			}

			writer.write("]");
			writer.close();
			logger.info("total exported :" + total.intValue());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private ArrayNode create(SetPremise premise) {
		ArrayNode result = mapper.createArrayNode();
		premise.getFacts().forEach((fact) -> result.add(create(fact)));
		premise.getAdditional().values().forEach((fact) -> result.add(create(fact)));
		return result;
	}

	private ObjectNode create(SetFact fact) {
		ObjectNode result = mapper.createObjectNode();
		result.put("head", fact.getHead());
		ArrayNode factElements = result.putArray("set");
		for (String element : fact.getSet()) {
			factElements.add(element);
		}
		result.put("conjunction", fact.isConjunction());
		return result;
	}

	private ObjectNode create(Counters counter) {
		ObjectNode result = mapper.createObjectNode();
		try {
			result.put("irf", counter.getIrf());
			result.put("grf", counter.getGrf());
		} catch (ArithmeticException exception) {
			exception.printStackTrace();
		}
		return result;
	}

	private ObjectNode createOther(Counters counters) {
		ObjectNode result = mapper.createObjectNode();
		result.put("conclusionsMatchedCounter", counters.getConclusionsRowsMatched());
		result.put("premiseMatchedCounter", counters.getPremiseRowsMatched());
		result.put("totalRows", counters.getTotalRows());
		return result;
	}
}
