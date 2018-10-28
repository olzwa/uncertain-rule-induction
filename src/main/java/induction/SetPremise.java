package induction;

import Induction2.Conclusions2;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.*;
import java.util.function.Consumer;

public class SetPremise {

	private final Set<SetFact> premise;

	private Map<String, SetFact> additional = new HashMap<>();

	private Map<String, SetFact> allMap = new HashMap<>();

	private Conclusions2.MatchedConclusions matchedConclusions;

	private Set<Conclusions> conclusions = new HashSet<>();

	public SetPremise(Set<SetFact> premise) {
		this.premise = premise;
		this.additional = new LinkedHashMap<>();
		for (SetFact setFact : this.premise) {
			this.allMap.put(setFact.getHead(), setFact);
		}
	}

	public SetPremise(Set<SetFact> premise, Map<String, SetFact> additional) {
		this.premise = new HashSet<>(premise);
		this.additional = new LinkedHashMap<>(additional);
		for (SetFact setFact : this.premise) {
			this.allMap.put(setFact.getHead(), setFact);
		}
		this.allMap.putAll(additional);
	}

	public SetPremise(SetFact... premise) {
		this.premise = new HashSet<>(Arrays.asList(premise));
		for (SetFact setFact : this.premise) {
			this.allMap.put(setFact.getHead(), setFact);
		}
	}

	public SetPremise(Set<SetFact> premises, SetFact additional) {
		Set<SetFact> premise = new LinkedHashSet<>();
		premise.addAll(premises);
		premise.add(additional);

		this.premise = premise;
	}

	public void setMatchedConclusions(Conclusions2.MatchedConclusions matchedConclusions) {
		this.matchedConclusions = matchedConclusions;
	}

	public Conclusions2.MatchedConclusions getMatchedConclusions() {
		return matchedConclusions;
	}

	public void addAdditional(SetFact setFact) {
		this.additional.put(setFact.getHead(), setFact);
		this.allMap.putAll(additional);
	}

	public Map<String, SetFact> getAdditional() {
		return additional;
	}

	public Map<String, SetFact> getAllMap() {
		return allMap;
	}

	public Set<SetFact> getFacts() {
		return premise;
	}

	public String toSimpleString() {
		StringJoiner joiner = new StringJoiner(";");
		allMap.values().forEach(setFact -> {
			joiner.add(setFact.toSimpleString());
		});
		return joiner.toString();
	}

	@Override
	public String toString() {
		return "SetPremise{" +
				"premise=" + allMap +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SetPremise that = (SetPremise) o;
		return Objects.equals(premise, that.premise) &&
				Objects.equals(additional, that.additional);
	}

	@Override
	public int hashCode() {
		return Objects.hash(premise, additional);
	}
}
