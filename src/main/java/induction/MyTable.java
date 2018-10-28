package induction;

import java.util.*;


public class MyTable<X, Y, Z> {
	private final Map<X, Map<Y, Z>> map;

	public MyTable() {
		map = new LinkedHashMap<>();
	}

	public void put(X x, Y y, Z z) {
		Map<Y, Z> yzMap = map.get(x);
		if (yzMap != null) {
			yzMap.put(y, z);
		} else {
			Map<Y, Z> values = new LinkedHashMap<>();
			values.put(y, z);
			map.put(x, values);
		}
	}

	public Map<Y, Z> row(X x) {
		Map<Y, Z> yzMap = map.get(x);
		if (yzMap != null) {
			return yzMap;
		} else {
			return Collections.emptyMap();
		}
	}

	public Map<X, Map<Y, Z>> rowMap() {
		return map;
	}

	public Z get(X x, Y y) {
		Map<Y, Z> yzMap = map.get(x);
		if (yzMap != null) {
			return yzMap.get(y);
		}
		return null;
	}
}
