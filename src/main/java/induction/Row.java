package induction;

import java.util.Collection;

public class Row {
    private final SetPremise premise;
    private final Conclusions conclusions;

    public Row(SetPremise premise, Collection<SetFact> conclusions) {
        this.premise = premise;
        this.conclusions = new Conclusions(conclusions);
    }

    public SetPremise getPremise() {
        return premise;
    }

    public Conclusions getConclusions() {
        return conclusions;
    }
}
