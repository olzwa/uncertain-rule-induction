package Induction2;

import induction.Conclusions;
import induction.MyTable;
import induction.SetFact;
import induction.SetFactUtils;

import java.util.*;

import static java.util.Objects.nonNull;

public class Conclusions2 {
	private MyTable<String, SetFact, MatchedConclusions> grouped = new MyTable<>();
	private Map<String, Set<SetFact>> groupedFromEqualRows = new HashMap<>();
	private Set<Conclusions> allMatchedRows = new HashSet<>();

	public Conclusions2(Map<String, Map<SetFact, MatchedConclusions>> map, SetFact additional) {
		MatchedConclusions matchedConclusions1 = map.get(additional.getHead()).get(additional);

		for (Conclusions conclusions : matchedConclusions1.getEquals()) {
			Map<String, SetFact> map1 = conclusions.getMap();
			for (String key : map.keySet()) {
				if (key.equals(additional.getHead())) {
					continue;
				}
				groupedFromEqualRows.computeIfAbsent(key, s -> new HashSet<>()).add(map1.get(key));
			}
		}

		for (String head : map.keySet()) {
			if (head.equals(additional.getHead())) {
				continue;
			}
			for (SetFact fact : map.get(head).keySet()) {
				MatchedConclusions v = new MatchedConclusions(map.get(head).get(fact), matchedConclusions1);

				allMatchedRows.addAll(v.getAll());
				grouped.put(head, fact, v);
			}
		}

	}

	public Conclusions2() {

	}

	public void addAll(Conclusions conclusions) {
		for (SetFact toAddFact : conclusions.get()) {
			Map<SetFact, MatchedConclusions> groupedFact = grouped.row(toAddFact.getHead());
			for (SetFact fact : groupedFact.keySet()) {
				if (SetFactUtils.isMatch(fact, toAddFact) && !fact.equals(toAddFact)) {
					groupedFact.get(fact).addToMatched(conclusions);

					this.allMatchedRows.add(conclusions);
				}
			}
			MatchedConclusions matchedConclusions = grouped.get(toAddFact.getHead(), toAddFact);
			if (nonNull(matchedConclusions)) {
				matchedConclusions.addToEquals(conclusions);//If such a fact already exists you can add another row to equals collection

				//TODO performance issue
				//temporary workaround to expose only equals group
				Map<String, SetFact> map1 = conclusions.getMap();
				for (String key : map1.keySet()) {
					groupedFromEqualRows.computeIfAbsent(key, s -> new HashSet<>()).add(map1.get(key));
				}
				this.allMatchedRows.add(conclusions);
			} else {
				grouped.put(toAddFact.getHead(), toAddFact, new MatchedConclusions(new HashSet<>(Arrays.asList(conclusions)), Collections.emptySet()));
			}
		}

	}

	public int getAllCount() {
		return this.allMatchedRows.size();
	}

	public Set<Conclusions> getAllMatchedRows() {
		return allMatchedRows;
	}

	public Map<String, Map<SetFact, MatchedConclusions>> get() {
		return grouped.rowMap();
	}

	public Map<String, Set<SetFact>> getGroupedFromEqualRows() {
		return groupedFromEqualRows;
	}

	public static class MatchedConclusions {
		private Set<Conclusions> equals = new LinkedHashSet<>(50, 0.95f);
		private Set<Conclusions> matched = new LinkedHashSet<>(50, 0.95f);
		private Set<Conclusions> all = new LinkedHashSet<>(50, 0.95f);

		public MatchedConclusions(Set<Conclusions> equals, Set<Conclusions> matched) {
			this.equals.addAll(equals);
			this.matched.addAll(matched);
			this.all.addAll(equals);
			this.all.addAll(matched);
		}

		public MatchedConclusions(MatchedConclusions current, MatchedConclusions additional) {

			for (Conclusions additionalFactEqualRows : current.getEquals()) {
				if (additional.getEquals().contains(additionalFactEqualRows)) {
					this.equals.add(additionalFactEqualRows);
					this.all.add(additionalFactEqualRows);
				}
			}

			for (Conclusions additionalFactAllRows : current.getAll()) {
				if (additional.getAll().contains(additionalFactAllRows)) {
					this.matched.add(additionalFactAllRows);
					this.all.add(additionalFactAllRows);
				}
			}
		}

		public void addToMatched(Conclusions conclusions) {
			this.matched.add(conclusions);
			this.all.add(conclusions);
		}

		public void addToEquals(Conclusions conclusions) {
			this.equals.add(conclusions);
			this.all.add(conclusions);
		}

		public Set<Conclusions> getEquals() {
			return equals;
		}

		public Set<Conclusions> getAll() {
			return all;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			MatchedConclusions that = (MatchedConclusions) o;
			return Objects.equals(equals, that.equals) &&
					Objects.equals(matched, that.matched) &&
					Objects.equals(all, that.all);
		}

		@Override
		public int hashCode() {
			return Objects.hash(equals, matched, all);
		}
	}
}
