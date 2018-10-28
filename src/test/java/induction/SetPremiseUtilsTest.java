package induction;

import induction.SetFact;
import induction.SetPremise;
import induction.SetPremiseUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SetPremiseUtilsTest {

	@Test
	public void shouldPremiseBeStrongerWhenAllFactsAreStronger() {
		//given
		SetFact fact1 = SetFact.createConjunction("sporty1=>caloroczne,zimowe");
		SetFact fact2 = SetFact.createConjunction("sporty2=>swieze");
		SetPremise premise1 = new SetPremise(fact1, fact2);

		SetFact fact3 = SetFact.createConjunction("sporty1=>caloroczne,letnie,zimowe");
		SetFact fact4 = SetFact.createConjunction("sporty2=>swieze,halowe");
		SetPremise premise2 = new SetPremise(fact3, fact4);
		//when
		boolean actual = SetPremiseUtils.isStrongerOrEquals(premise1, premise2);
		//then
		assertThat(actual).isTrue();
	}
}