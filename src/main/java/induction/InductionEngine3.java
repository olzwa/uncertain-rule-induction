package induction;

import Induction2.Conclusions2;
import Induction2.Conclusions2.MatchedConclusions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.nonNull;


public class InductionEngine3 {
	private static final Logger logger = LoggerFactory.getLogger(InductionEngine3.class);

	public Collection<SetRule> perform(List<Row> rows, int maxAdditional, int minToQualify) {

		Table<Integer, SetPremise, Conclusions2> trees = HashBasedTable.create();
		for (Row row1 : rows) {
			for (Row row2 : rows) {
				if (row1 != row2) {
					if (SetFactUtils.isMatch(row1.getPremise(), row2.getPremise())) {
						Conclusions conclusions1 = row1.getConclusions();
						Conclusions conclusions2 = row2.getConclusions();

						Conclusions2 conclusions = trees.get(0, row1.getPremise());
						if (nonNull(conclusions)) {
							conclusions.addAll(conclusions2);
						} else {
							Conclusions2 newConclusions = new Conclusions2();
							newConclusions.addAll(conclusions1);
							newConclusions.addAll(conclusions2);
							trees.put(0, row1.getPremise(), newConclusions);
						}

					}
				}
			}
		}
		Map<Integer, Set<SetPremise>> premises = new LinkedHashMap<>();
		for (SetPremise setPremise : trees.row(0).keySet()) {
			Conclusions2 conclusions2 = trees.get(0, setPremise);
			MatchedConclusions matchedConclusions = new MatchedConclusions(conclusions2.getAllMatchedRows(), Collections.emptySet());
			premises.computeIfAbsent(0, key -> new HashSet<>()).add(setPremise);
			setPremise.setMatchedConclusions(matchedConclusions);
		}

		for (int depth = 1; depth <= maxAdditional; depth++) {
			long startNanos = System.nanoTime();
			Set<SetPremise> premisesForDepth = premises.get(depth - 1);

			int i = 0;
			for (SetPremise setPremise : premisesForDepth) {
				if (i % 100 == 0) {
					logger.info(String.valueOf(i));
				}
				Conclusions2 conclusions2 = trees.get(0, new SetPremise(setPremise.getFacts()));
				Map<String, Set<SetFact>> groupedFromEqualRows = conclusions2.getGroupedFromEqualRows();
				for (String additionalHead : groupedFromEqualRows.keySet()) {
					if (setPremise.getAdditional().keySet().contains(additionalHead)) {
						continue;
					}
					for (SetFact additionalFact : groupedFromEqualRows.get(additionalHead)) {

						SetPremise setPremiseWithAdditional = new SetPremise(setPremise.getFacts(), setPremise.getAdditional());
						setPremiseWithAdditional.addAdditional(additionalFact);

						if (premises.get(depth) != null && premises.get(depth).contains(setPremiseWithAdditional)) {//Avoid processing the same premise second time
							continue;
						}

						MatchedConclusions additionalMatchedConclusions = conclusions2.get().get(additionalHead).get(additionalFact);
						if (setPremise.getMatchedConclusions() != null) {
							MatchedConclusions matchedConclusions = new MatchedConclusions(setPremise.getMatchedConclusions(), additionalMatchedConclusions);
							if (matchedConclusions.getEquals().size() == 0) {
								continue;
							} else {
								additionalMatchedConclusions = matchedConclusions;
							}
						}
						premises.computeIfAbsent(depth, key -> new LinkedHashSet<>()).add(setPremiseWithAdditional);

						setPremiseWithAdditional.setMatchedConclusions(additionalMatchedConclusions);
					}
				}
				i++;
			}
			long endNanos = System.nanoTime();
			logger.info("generated premises :" + premises.get(depth).size());
			logger.info("milis depth time: " + String.valueOf((endNanos - startNanos) / 1000000));

		}

		Set<SetRule> result2 = new HashSet<>();
		Map<Integer, Set<SetRule>> perLevelResult = new HashMap<>();

		for (Integer level : premises.keySet()) {
			for (SetPremise toAddPremise : premises.get(level)) {
				Conclusions2 conclusions2 = trees.row(0).get(new SetPremise(toAddPremise.getFacts()));//TODO omg nab
				MatchedConclusions matchedConclusionsForPremise = toAddPremise.getMatchedConclusions();

				for (String head : conclusions2.get().keySet()) {
					if (toAddPremise.getAdditional().keySet().contains(head)) {
						continue;
					}
					for (SetFact setFact : conclusions2.get().get(head).keySet()) {
						//TODO think if it is possible to reuse previously created MatchedConclusions object
						MatchedConclusions matchedConclusionsForConclusion = new MatchedConclusions(conclusions2.get().get(head).get(setFact), matchedConclusionsForPremise);
						if (matchedConclusionsForConclusion.getEquals().size() > 0) {
							Counters counters = new Counters(matchedConclusionsForConclusion.getAll().size(), matchedConclusionsForPremise.getAll().size(), rows.size());

							SetRule toAddRule = new SetRule(toAddPremise, setFact, counters);
							//result2.add(toAddRule);


							//Niezachowanie dodatniej monotoniczności przy wystapieniu w bazie wiedzy reguł R1 i R2 (ze słabszej przesłanki w R1 niż w R2 wynika silniejsza konkluzja w R1 niż w R2)
						  boolean granted = true; //should be false
							for (SetRule rule : perLevelResult.getOrDefault(level, Collections.emptySet())) {
								Boolean isPremiseStrongerOrEqual = SetPremiseUtils.isStrongerOrEquals(toAddRule.getSetPremise(), rule.getSetPremise());
								if (isPremiseStrongerOrEqual != null && isPremiseStrongerOrEqual) {
									if (rule.getConclusion().getHead().equals(toAddRule.getConclusion().getHead())) {
										Boolean isConclusionStrongerOrEquals = SetFactUtils.isStrongerOrEquals(rule.getConclusion(), toAddRule.getConclusion());
										if (isConclusionStrongerOrEquals != null && isConclusionStrongerOrEquals) {
											//TODO compare as BigDecimal instead of doubleValue()
											if (rule.getCounters().getIrf().doubleValue() < toAddRule.getCounters().getIrf().doubleValue()) {
											logger.info(rule.toSimpleString());
											logger.info(toAddRule.toSimpleString());
											logger.info("--------------------------------------------------");
											granted = true;
											}
										}
										//result.put(toAddPremise, setFact, counters);
									}
								}
							}
							if(granted){
								perLevelResult.computeIfAbsent(level, HashSet::new).add(toAddRule);
							}


/*							for (SetPremise existingPremise : result.rowMap().keySet()) {
								if (!SetPremiseUtils.isStrongerOrEquals(existingPremise, counters, toAddPremise)) {
									result.put(toAddPremise, setFact, counters);
								}
							}*/
						}

					}
				}
			}
		}

		for (Integer level : perLevelResult.keySet()) {
			result2.addAll(perLevelResult.get(level));
		}

		return result2;
	}
}
