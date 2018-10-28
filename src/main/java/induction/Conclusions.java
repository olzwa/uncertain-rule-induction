package induction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Conclusions {
    private Map<String, SetFact> conclusions = new HashMap<>();

    public Conclusions(Collection<SetFact> facts) {
        facts.forEach(this::put);
    }

    public void put(SetFact fact) {
        conclusions.put(fact.getHead(), fact);
    }

    public Collection<SetFact> get() {
        return conclusions.values();
    }

    public Map<String, SetFact> getMap() {
        return conclusions;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
