package induction;

import java.util.*;

public class SetFactUtils {

	public static Boolean isStrongerOrEquals(SetFact fact, SetFact isStrongerOrEquals) {
		if (!Objects.equals(fact.getHead(), isStrongerOrEquals.getHead())) {
			throw new IllegalStateException();
		}
		//Both conjunction
		if (fact.isConjunction() && isStrongerOrEquals.isConjunction()) {
			if (fact.getSet().size() <= isStrongerOrEquals.getSet().size()) {
				if (isStrongerOrEquals.getSet().containsAll(fact.getSet())) {//TODO is this containsAll neccessary?
					return true;
				}
			}
			//Both disjunction
		} else if (!fact.isConjunction() && !isStrongerOrEquals.isConjunction()) {
			if (fact.getSet().size() >= isStrongerOrEquals.getSet().size()) {
				if (fact.getSet().containsAll(isStrongerOrEquals.getSet())) {
					return true;
				}
			}
		} else {
			//isStronger is disjunction and second is conjunction and sets are equal
			if (isStrongerOrEquals.isConjunction()) {
				if (fact.getSet().containsAll(isStrongerOrEquals.getSet())) {
					return true;
				}

				if (isStrongerOrEquals.getSet().containsAll(fact.getSet())) {
					return true;
				}

			}
		}
		return null;
	}

	public static Boolean isStronger(SetFact fact, SetFact isStronger) {
		if (!Objects.equals(fact.getHead(), isStronger.getHead())) {
			throw new IllegalStateException();
		}
		//Both conjunction
		if (fact.isConjunction() && isStronger.isConjunction()) {
			if (fact.getSet().size() < isStronger.getSet().size()) {
				if (isStronger.getSet().containsAll(fact.getSet())) {//TODO is this containsAll neccessary?
					return true;
				}
			}
			//Both disjunction
		} else if (!fact.isConjunction() && !isStronger.isConjunction()) {
			if (fact.getSet().size() > isStronger.getSet().size()) {
				if (fact.getSet().containsAll(isStronger.getSet())) {
					return true;
				}
			}
		} else {
			if (isStronger.isConjunction()) {
				if (isStronger.getSet().containsAll(fact.getSet())) {
					return true;
				}
			}
		}
		return null;
	}


	public static boolean isMatch(SetPremise subset, SetPremise of) {
		return isMatch(subset.getFacts(), of.getFacts());
	}

	public static boolean isMatch(Set<SetFact> subset, Set<SetFact> of) {

		if (subset.size() != of.size()) {
			return false;
		}
		for (SetFact singleSubset : subset) {
			if (!isMatchAnyOf(singleSubset, of)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isMatchAnyOf(SetFact subset, Set<SetFact> of) {
		for (SetFact singleOf : of) {
			if (isMatch(subset, singleOf)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMatch(SetFact subset, SetFact of) {
		//no match when non equals heads
		if (!subset.getHead().equals(of.getHead())) {
			return false;
		}
		if (subset.equals(of)) {//if facts are equals
			return true;
		}
		//if both same type and sets are equal
		if (subset.isConjunction() == of.isConjunction() && subset.getSet().equals(of.getSet())) {
			return true;
		}
		//both conjunction and one is subset of another
		if (of.isConjunction() && subset.isConjunction()) {
			if (of.getSet().containsAll(subset.getSet())) {
				return true;
			}
		}
		//if disjunction with conjunction and sets are equal and there is at least one element in common
		if (!subset.isConjunction() && of.isConjunction()) {
			if (subset.getSet().equals(of.getSet())) {
				return true;
			}
			if (!Collections.disjoint(subset.getSet(), of.getSet())) {
				if (of.getSet().size() == 1) {
					return true;
				}
			}
		}

		return false;
	}

}


