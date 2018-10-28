package induction;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetPremiseUtils {
	private static final Logger logger = LoggerFactory.getLogger(SetPremiseUtils.class);

	public static Boolean isStrongerOrEquals(SetPremise premise, SetPremise isStrongerOrEquals) {
		//Level should be the same

		ImmutableSet<String> intersection = Sets.intersection(premise.getAllMap().keySet(), isStrongerOrEquals.getAllMap().keySet()).immutableCopy();

		int isStrongerStrength = 0;
		if (intersection.size() == premise.getAllMap().keySet().size()) {
			for (String key1 : premise.getAllMap().keySet()) {
				SetFact setFactFromPremise = premise.getAllMap().get(key1);
				SetFact setFactFromIsStrongerOrEquals = isStrongerOrEquals.getAllMap().get(key1);
				Boolean isPremiseStrongerOrEquals = SetFactUtils.isStrongerOrEquals(setFactFromPremise, setFactFromIsStrongerOrEquals);
				if (isPremiseStrongerOrEquals == null) {
					logger.warn("Cannot determine strength for: " + setFactFromPremise + "and" + setFactFromIsStrongerOrEquals);
					return null;
				} else if (isPremiseStrongerOrEquals) {
					isStrongerStrength += 1;
				} else if (isPremiseStrongerOrEquals == null) {
					return null;
				}
			}
			return isStrongerStrength >= isStrongerOrEquals.getAllMap().keySet().size();
		} else if (!intersection.isEmpty()) {
			
		}
		return false;
	}
}



