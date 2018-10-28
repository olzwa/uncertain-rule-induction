/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package induction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class SetFact {
	private final String head;
	private final Set<String> set;
	private final BigDecimal wParamter;
	private final boolean axiom;
	private final boolean conjunction;
	private final boolean negate;
	private final AtomicInteger counter;

	private final int hashCode;

	private SetFact(final String head, final Set<String> set,
									final BigDecimal wParameter, final boolean axiom, final boolean negate, final boolean conj) {
		this.head = head;
		this.set = set;
		this.wParamter = wParameter;
		this.axiom = axiom;
		this.negate = negate;
		this.conjunction = conj;
		this.counter = new AtomicInteger(1);
		this.hashCode = hashCode2();

	}

	public String getHead() {
		return head;
	}

	public Set<String> getSet() {
		return set;
	}

	public int getLength() {
		return set.size();
	}

	public BigDecimal getWParamter() {
		return wParamter;
	}

	public boolean isAxiom() {
		return axiom;
	}

	public boolean isConjunction() {
		return conjunction;
	}

	public boolean isNegated() {
		return this.negate;
	}

	public int getCounter() {
		return counter.intValue();
	}

	public int increment() {
		return this.counter.incrementAndGet();
	}

	public void reset() {
		this.counter.lazySet(1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SetFact that = (SetFact) o;

		if (conjunction != that.conjunction) return false;
		if (head != null ? !head.equals(that.head) : that.head != null) return false;
		if (set != null ? !set.equals(that.set) : that.set != null) return false;

		return true;
	}

	public int hashCode2() {
		int result = head != null ? head.hashCode() : 0;
		result = 31 * result + (set != null ? set.hashCode() : 0);
		result = 31 * result + (conjunction ? 1 : 0);
		return result;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	public String toSimpleString() {
		return head + "=" + set + ", conjunction=" + conjunction;
	}

	@Override
	public String toString() {
		return "SetFact{" +
				"head='" + head + '\'' +
				", set=" + set +
				", wParamter=" + wParamter +
				", axiom=" + axiom +
				", conjunction=" + conjunction +
				", negate=" + negate +
				'}';
	}

	public static SetFact copy(final SetFact fact) {
		return new SetFact(fact.getHead(), fact.getSet(), fact.getWParamter(), fact.isAxiom(), fact.isNegated(), fact.isConjunction());
	}

	public static SetFact create(final String fact) {
		boolean negate = false;

		boolean isConjunction = true;
		String split = Config.FACT_CONJUNCTION;

		String[] splitHead = fact.split(Config.HEAD_BODY_SEPARATOR);

		if (splitHead.length < 2) {
			throw new IllegalArgumentException();
		}
		if (splitHead[1].contains(Config.FACT_DISJUNCTION)) {
			isConjunction = false;
			split = Config.FACT_DISJUNCTION;
		} else if (splitHead[1].contains(Config.FACT_DISJUNCTION) && splitHead[1].contains(Config.FACT_CONJUNCTION)) {
			throw new IllegalArgumentException();
		}
		String[] parts = splitHead[1].split(Pattern.quote(split));
		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].trim();
		}
		if (parts.length > 1) {
			if (parts[1].trim().equals("!")) {
				negate = true;
			}
		}
		return new SetFact(splitHead[0], new HashSet<>(Arrays.asList(parts)), BigDecimal.ONE, false, negate, isConjunction);
	}

	public static SetFact createConjunction(final String fact) {
		boolean negate = false;
		boolean isConjunction = true;
		String bodySplit = Config.FACT_CONJUNCTION;

		String[] splitHead = fact.split(Config.HEAD_BODY_SEPARATOR);
		if (splitHead.length < 2) {
			throw new IllegalArgumentException();
		}

		if (splitHead[1].contains(Config.FACT_DISJUNCTION) && splitHead[1].contains(Config.FACT_CONJUNCTION)) {
			throw new IllegalArgumentException();
		}

		String[] parts = splitHead[1].split(Pattern.quote(bodySplit));
		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].trim();
		}
		if (parts.length > 1) {
			if (parts[1].trim().equals("!")) {
				negate = true;
			}
		}
		return new SetFact(splitHead[0], new HashSet<>(Arrays.asList(parts)), BigDecimal.ONE, false, negate, isConjunction);
	}

	public static SetFact createDisjunction(final String fact) {
		boolean negate = false;
		boolean isConjunction = false;
		String bodySplit = Config.FACT_CONJUNCTION;

		String[] splitHead = fact.split(Config.HEAD_BODY_SEPARATOR);
		if (splitHead.length < 2) {
			throw new IllegalArgumentException();
		}

		if (splitHead[1].contains(Config.FACT_DISJUNCTION) && splitHead[1].contains(Config.FACT_CONJUNCTION)) {
			throw new IllegalArgumentException();
		}

		String[] parts = splitHead[1].split(Pattern.quote(bodySplit));
		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].trim();
		}
		if (parts.length > 1) {
			if (parts[1].trim().equals("!")) {
				negate = true;
			}
		}
		return new SetFact(splitHead[0], new HashSet<>(Arrays.asList(parts)), BigDecimal.ONE, false, negate, isConjunction);
	}
}