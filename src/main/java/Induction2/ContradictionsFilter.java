package Induction2;

import com.google.common.collect.Table;
import induction.Counters;
import induction.SetFact;
import induction.SetFactUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ContradictionsFilter {
	static boolean checkForContradictions(Table<String, SetFact, Counters> conclusionsPerPremise, SetFact isStrongerOrEquals, Counters counterForConclusion) {
		String head = isStrongerOrEquals.getHead();
		Map<SetFact, Counters> conclusionPerPremiseByHead = conclusionsPerPremise.row(head);
		for (SetFact conclusionToCompare : conclusionPerPremiseByHead.keySet()) {
			if (SetFactUtils.isStrongerOrEquals(conclusionToCompare, isStrongerOrEquals)) {
				if (counterForConclusion.getIrf().compareTo(conclusionPerPremiseByHead.get(conclusionToCompare).getIrf()) > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
