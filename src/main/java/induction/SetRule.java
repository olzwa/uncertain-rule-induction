package induction;

import java.util.Objects;

public class SetRule {
	private final SetPremise setPremise;
	private final SetFact conclusion;
	private final Counters counters;

	public SetRule(SetPremise setPremise, SetFact conclusion, Counters counters) {
		this.setPremise = setPremise;
		this.conclusion = conclusion;
		this.counters = counters;
	}

	public SetPremise getSetPremise() {
		return setPremise;
	}

	public SetFact getConclusion() {
		return conclusion;
	}

	public Counters getCounters() {
		return counters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SetRule setRule = (SetRule) o;
		return Objects.equals(setPremise, setRule.setPremise) &&
				Objects.equals(conclusion, setRule.conclusion) &&
				Objects.equals(counters, setRule.counters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(setPremise, conclusion, counters);
	}

	public String toSimpleString() {
		return
				"premise:" + setPremise +
						", conclusion:" + conclusion +
						", counters:" + counters;
	}

	@Override
	public String toString() {
		return "SetRule{" +
				"setPremise=" + setPremise +
				", conclusion=" + conclusion +
				", counters=" + counters +
				'}';
	}
}
