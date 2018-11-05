package induction;

import induction.input.CSVParser;
import induction.output.JsonWriter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Inductor {
	private static final Logger logger = LoggerFactory.getLogger(Inductor.class);

	//private final InductionEngine2 engine = new InductionEngine2();
	private final InductionEngine3 engine = new InductionEngine3();
	private final JsonWriter jsonWriter = new JsonWriter();

	void execute(Writer writer, int additionalPremiseColumnsCount, File input, List<Integer> keyColumnNumber,
							 List<Integer> skipColumns,
							 List<Integer> forcedConjunctionColumns,
							 List<Integer> forcedDisjunctionColumns,
							 Integer minToQualifyPremise) throws IOException {

		CSVParser parser = new CSVParser(Arrays.asList("_", "&"));
		parser.setSeparator("\t");
		parser.parse(input);
		skipColumns.forEach(parser::skipColumn);

		List<String> columns = parser.getColumns();
		List<List<String>> rawRows = parser.getRows();

		for (int i = 0; i < rawRows.size(); i++) {
			List<String> rawRow = rawRows.get(i);
			if (rawRow.size() != columns.size()) {
				logger.warn(rawRow.toString());
				logger.warn("row column size does not match columns config. Removing row number " + i);
				rawRows.remove(i);
			}
		}

		List<Row> rows = new ArrayList<>(rawRows.size());
		for (int i = 0; i < rawRows.size(); i++) {
			List<String> rawRow = rawRows.get(i);
			try {
				rows.add(parseRow(rawRow, columns, keyColumnNumber, forcedConjunctionColumns, forcedDisjunctionColumns));
			} catch (Exception exception) {
				logger.error("parse exception, row number" + i, exception);
			}
		}


		Collection<SetRule> result = engine.perform(rows, additionalPremiseColumnsCount, minToQualifyPremise);
		jsonWriter.write(result, writer);

	}

	public static void main(String[] args) {
		Inductor inductor = new Inductor();

		Writer writer = null;
		File input = new File("ankieta6.tsv");
		List<Integer> keyColumnNumber = new ArrayList<>();
		List<Integer> skipColumns = new ArrayList<>();
		List<Integer> forcedConjunctionColumns = new ArrayList<>();
		List<Integer> forcedDisjunctionColumns = new ArrayList<>();
		int additionalPremiseColumnsCount = 2;

		try {
			CommandLineParser cmdParser = new DefaultParser();
			Options options = new Options();

			options.addOption("input", "input", true, "");
			options.addOption("output", "output", true, "");
			options.addOption("keyColumns", "keyColumns", true, "");
			options.addOption("skipColumns", "skipColumns", true, "");
			options.addOption("additionalPremiseColumnsCount", "additionalPremiseColumnsCount", true, "");
			options.addOption("forcedConjunctionColumns", "forcedConjunctionColumns", true, "");
			options.addOption("forcedDisjunctionColumns", "forcedDisjunctionColumns", true, "");
			options.addOption("minToQualifyPremise", "minToQualifyPremise", true, "");

			CommandLine cmd = cmdParser.parse(options, args);

			String inputFileParam = cmd.getOptionValue("input");
			String outputFileParam = cmd.getOptionValue("output");
			String keyColumnsParam = cmd.getOptionValue("keyColumns");
			String skipColumnsParam = cmd.getOptionValue("skipColumns");
			String additionalColumnsCountParam = cmd.getOptionValue("additionalPremiseColumnsCount");
			String forcedConjunctionColumnsParam = cmd.getOptionValue("forcedConjunctionColumns");
			String forcedDisjunctionColumnsParam = cmd.getOptionValue("forcedDisjunctionColumns");
			String minToQualifyPremiseParam = cmd.getOptionValue("minToQualifyPremise");

			String[] keyColumnsArray = keyColumnsParam.split(",");
			for (String keyColumn : keyColumnsArray) {
				keyColumnNumber.add(Integer.valueOf(keyColumn));
			}

			if (skipColumnsParam != null) {
				String[] skipColumnsArray = skipColumnsParam.split(",");
				for (String skipColumn : skipColumnsArray) {
					skipColumns.add(Integer.valueOf(skipColumn));
				}
			}
			if (nonNull(inputFileParam)) {
				input = new File(inputFileParam);
			}

			if (nonNull(outputFileParam)) {
				writer = new FileWriter(new File(outputFileParam));
			}

			if (nonNull(additionalColumnsCountParam)) {
				additionalPremiseColumnsCount = Integer.valueOf(additionalColumnsCountParam);
			}

			if (nonNull(forcedConjunctionColumnsParam)) {
				String[] forcedConjunctionArray = forcedConjunctionColumnsParam.split(",");
				for (String forcedConjunctionColumn : forcedConjunctionArray) {
					forcedConjunctionColumns.add(Integer.valueOf(forcedConjunctionColumn));
				}
			}

			if (nonNull(forcedDisjunctionColumnsParam)) {
				String[] forcedDisjunctionArray = forcedDisjunctionColumnsParam.split(",");
				for (String forcedDisjunctionColumn : forcedDisjunctionArray) {
					forcedDisjunctionColumns.add(Integer.valueOf(forcedDisjunctionColumn));
				}
			}

			Integer minToQualifyPremise = Optional.ofNullable(minToQualifyPremiseParam).map(Integer::valueOf).orElse(1);


			System.out.println("====================================================");
			System.out.println("input: " + inputFileParam);
			System.out.println("output: " + outputFileParam);
			System.out.println("keyColumns: " + keyColumnsParam);
			System.out.println("skipColumns: " + skipColumnsParam);
			System.out.println("additionalPremiseColumnsCount: " + additionalColumnsCountParam);
			System.out.println("forcedConjunctionColumns: " + forcedConjunctionColumnsParam);
			System.out.println("forcedDisjunctionColumns: " + forcedDisjunctionColumnsParam);
			System.out.println("minToQualifyPremise: " + minToQualifyPremiseParam);
			System.out.println("====================================================");


			inductor.execute(writer, additionalPremiseColumnsCount, input, keyColumnNumber, skipColumns,
					forcedConjunctionColumns, forcedDisjunctionColumns, minToQualifyPremise);
		} catch (ParseException exception) {
			logger.error("Parse error", exception);
		} catch (IOException exception) {
			logger.error("Write error", exception);
		}
	}

	private static Row parseRow(List<String> row, List<String> columns, List<Integer> keyColNumbers, List<Integer> forcedConjunctionColumns, List<Integer> forcedDisjunctionColumns) {
		Set<SetFact> premise = new HashSet<>();
		List<SetFact> conclusions = new ArrayList<>(columns.size());
		try {
			for (int colNumber = 0; colNumber < columns.size(); colNumber++) {
				SetFact fact;
				if (forcedConjunctionColumns.contains(colNumber)) {
					fact = SetFact.createConjunction(columns.get(colNumber) + Config.HEAD_BODY_SEPARATOR + row.get(colNumber));
				} else if (forcedDisjunctionColumns.contains(colNumber)) {
					fact = SetFact.createDisjunction(columns.get(colNumber) + Config.HEAD_BODY_SEPARATOR + row.get(colNumber));
				} else {
					fact = SetFact.create(columns.get(colNumber) + Config.HEAD_BODY_SEPARATOR + row.get(colNumber));
				}
				if (keyColNumbers.contains(colNumber)) {
					premise.add(fact);
				} else {
					conclusions.add(fact);
				}
			}
		} catch (Exception exception) {
			logger.error(row.toString());
		}
		return new Row(new SetPremise(premise), conclusions);
	}
}
