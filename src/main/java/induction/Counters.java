package induction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

public class Counters {
	private final AtomicInteger conclusionsRowsMatched;
	private final AtomicInteger premiseRowsMatched;
	private final int totalRows;

	private final BigDecimal irf;
	private final BigDecimal grf;

	public Counters(int conclusionsRowsMatched, int premiseRowsMatched, int totalRows) {
		this.conclusionsRowsMatched = new AtomicInteger(conclusionsRowsMatched);
		this.premiseRowsMatched = new AtomicInteger(premiseRowsMatched);
		this.totalRows = totalRows;

		this.irf = new BigDecimal(getConclusionsRowsMatched()).divide(new BigDecimal(getPremiseRowsMatched()), 2, RoundingMode.HALF_UP);
		this.grf = new BigDecimal(getPremiseRowsMatched()).divide(new BigDecimal(getTotalRows()), 2, RoundingMode.HALF_UP);

	}

	public BigDecimal getIrf() {
		return irf;
	}

	public BigDecimal getGrf() {
		return grf;
	}

	public int incrementConclusions() {
		return this.conclusionsRowsMatched.incrementAndGet();
	}

	public int getConclusionsRowsMatched() {
		return conclusionsRowsMatched.intValue();
	}

	public int getPremiseRowsMatched() {
		return premiseRowsMatched.intValue();
	}

	public int getTotalRows() {
		return totalRows;
	}

	@Override
	public String toString() {
		return "Counters{" +
				"conclusionsRowsMatched=" + conclusionsRowsMatched +
				", premiseRowsMatched=" + premiseRowsMatched +
				", totalRows=" + totalRows +
				", irf=" + irf +
				", grf=" + grf +
				'}';
	}
}
