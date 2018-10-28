package induction;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class SetFactUtilsTest {

	//TODO case when heads are not equals?
	//TODO case when comparing dis with conj?

	@Test
	public void shouldAlwaysConjunctionFactBeStrongerThanDisjunction() {
		//given
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a");
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a, b, c, d");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(weakDisjunctionFact, strongConjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldAlwaysConjunctionFactBeStrongerThanDisjunction2() {
		//given
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a, b, c, d");
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a, b");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(weakDisjunctionFact, strongConjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldAlwaysConjunctionFactBeStrongerThanDisjunction3() {
		//given
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a, b");
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a, c");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(weakDisjunctionFact, strongConjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldBiggerConjunctionFactBeStrongerThanSmallOne() {
		//given
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a, b, c, d");
		SetFact weakConjunctionFact = SetFact.createConjunction("sport=>a, b");
		//when
		boolean actual = SetFactUtils.isStrongerOrEquals(weakConjunctionFact, strongConjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldEqualConjunctionOneElementFactBeEqualToSameDisjunctionFact() {
		//given
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a");
		SetFact weakConjunctionFact = SetFact.createDisjunction("sport=>a");
		//when
		boolean actual = SetFactUtils.isStrongerOrEquals(strongConjunctionFact, weakConjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldEqualConjunctionFactBeEqualToSameDisjunctionFact() {
		//given
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a,b");
		SetFact disjunctionFact = SetFact.createDisjunction("sport=>a,b");
		//when
		boolean actual = SetFactUtils.isStrongerOrEquals(strongConjunctionFact, disjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldEqualConjunctionFactBeEqual() {
		//given
		SetFact fact1 = SetFact.createConjunction("sport=>a, b");
		SetFact fact2 = SetFact.createConjunction("sport=>a, b");
		//when
		boolean actual = SetFactUtils.isStrongerOrEquals(fact2, fact1);
		//then
		assertThat(actual).isTrue();
	}


	@Test
	public void shouldNotBiggerConjunctionFactBeWeakerThanSmallOne() {
		//given
		SetFact strongConjunctionFact = SetFact.createConjunction("sport=>a, b, c, d");
		SetFact weakConjunctionFact = SetFact.createConjunction("sport=>a, b");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(strongConjunctionFact, weakConjunctionFact);
		//then
		assertThat(actual).isNull();
	}

	@Test
	public void shouldBiggerDisjunctionFactBeWeakerThanSmallOne() {
		//given
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a, b, c, d");
		SetFact strongDisjunctionFact = SetFact.createDisjunction("sport=>a, b");
		//when
		boolean actual = SetFactUtils.isStrongerOrEquals(weakDisjunctionFact, strongDisjunctionFact);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldNotBiggerDisjunctionFactBeStrongerThanOnelem() {
		//given
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a");
		SetFact strongDisjunctionFact = SetFact.createDisjunction("sport=>a, b");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(weakDisjunctionFact, strongDisjunctionFact);
		//then
		assertThat(actual).isNull();
	}

	@Test
	public void shouldNotCompareWhenComparisionNotPossible() {
		//given
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a");
		SetFact strongDisjunctionFact = SetFact.createDisjunction("sport=>c");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(weakDisjunctionFact, strongDisjunctionFact);
		//then
		assertThat(actual).isNull();
	}

	@Test
	public void shouldNotBiggerDisjunctionFactBeStrongerThanSmallOne() {
		//given
		SetFact weakDisjunctionFact = SetFact.createDisjunction("sport=>a, b, c, d");
		SetFact strongDisjunctionFact = SetFact.createDisjunction("sport=>a, b");
		//when
		Boolean actual = SetFactUtils.isStrongerOrEquals(strongDisjunctionFact, weakDisjunctionFact);
		//then
		assertThat(actual).isNull();
	}

	@Test
	public void shouldEqualDisjunctionFactBeEqual() {
		//given
		SetFact fact1 = SetFact.createDisjunction("sport=>a, b");
		SetFact fact2 = SetFact.createDisjunction("sport=>a, b");
		//when
		boolean actual = SetFactUtils.isStrongerOrEquals(fact2, fact1);
		//then
		assertThat(actual).isTrue();
	}


	@Test
	public void should_not_match_when_first_is_subset_of_second_disjunction_SetFacts() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe|inne");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isFalse();
	}

	@Test //TODO wat?
	public void should_match_when_subset_disjunction_SetFacts() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe|inne");
		//when
		boolean actual = SetFactUtils.isMatch(fact2, fact1);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void should_match_equals_disjunction_SetFacts() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void should_match_equals_conjunction_SetFacts() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne,zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void should_match_equals_disjunction_with_conjunction_SetFacts() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isTrue();
	}

	//TODO wat?
	@Test
	public void should_not_match_equals_conjunction_with_disjunction_SetFacts() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_not_match_equals_conjunction_with_disjunction_SetFacts2() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact2, fact1);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_match_subset_when_disjunction_element1() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => caloroczne");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void should_match_subset_when_disjunction_element2() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void should_not_match_subset_single_element_matches_disjunctive() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => zimowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact2, fact1);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_not_match_when_not_subset() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("sporty => zimowe|inne");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_match_when_single_and_double_conjunction() {
		//given
		SetFact fact1 = SetFact.create("sporty => indywidualnie");
		SetFact fact2 = SetFact.create("sporty => indywidualnie,zespolowe");
		//when
		boolean actual = SetFactUtils.isMatch(fact1, fact2);
		//then
		assertThat(actual).isTrue();
	}

	@Test
	public void should_not_match_when_double_conjunction_and_single() {
		//given
		SetFact fact2 = SetFact.create("sporty => indywidualnie,zespolowe");
		SetFact fact1 = SetFact.create("sporty => indywidualnie");
		//when
		boolean actual = SetFactUtils.isMatch(fact2, fact1);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_not_match_when_different_disjunctive_and_conjunctive() {
		//given
		SetFact fact2 = SetFact.create("sporty => indywidualnie|zespolowe|inne");
		SetFact fact1 = SetFact.create("sporty => indywidualnie,inne");
		//when
		boolean actual = SetFactUtils.isMatch(fact2, fact1);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_not_match_sets_with_diff_size() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("wiek => 21");
		SetFact fact3 = SetFact.create("motywacja => inna");

		Set<SetFact> premise1 = new HashSet<>();
		premise1.add(fact1);
		premise1.add(fact2);
		premise1.add(fact3);

		Set<SetFact> premise2 = new HashSet<>();
		premise2.add(fact1);
		premise2.add(fact2);
		//when
		boolean actual = SetFactUtils.isMatch(premise1, premise2);
		//then
		assertThat(actual).isFalse();
	}

	@Test
	public void should_match_equals_sets() {
		//given
		SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
		SetFact fact2 = SetFact.create("wiek => 21");
		SetFact fact3 = SetFact.create("motywacja => inna");

		Set<SetFact> premise1 = new HashSet<>();
		premise1.add(fact1);
		premise1.add(fact2);
		premise1.add(fact3);

		Set<SetFact> premise2 = new HashSet<>();
		premise2.add(fact1);
		premise2.add(fact2);
		premise2.add(fact3);
		//when
		boolean actual = SetFactUtils.isMatch(premise1, premise2);
		//then
		assertThat(actual).isTrue();
	}


}