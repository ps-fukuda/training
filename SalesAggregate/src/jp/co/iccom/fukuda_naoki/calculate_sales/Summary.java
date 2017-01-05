package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Summary {

	public static final String MONEY_ERROR = "合計金額が10桁を超えました";

	public Map<String, Integer> getBraSummary(List<SalesData> list) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (SalesData d : list) {
			// mapに既にキーが存在しているか
			if (map.containsKey(d.getBraCode())) {
				// mapから既存値を取得後に合算
				int mon = map.get(d.getBraCode()) + d.getProfit();
				if (String.valueOf(mon).length() >= 10) {
					System.out.println(MONEY_ERROR);
					throw new Exception();
				}
				map.put(d.getBraCode(), map.get(d.getBraCode()) + d.getProfit());
			} else {
				if (String.valueOf(d.getProfit()).length() >= 10) {
					System.out.println(MONEY_ERROR);
					throw new Exception();
				}
				map.put(d.getBraCode(), d.getProfit());
			}
		}
		return map;
	}

	public Map<String, Integer> getComSummary(List<SalesData> list) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (SalesData d : list) {
			// mapに既にキーが存在しているか
			if (map.containsKey(d.getComCode())) {
				// mapから既存値を取得後に合算
				int mon = map.get(d.getComCode()) + d.getProfit();
				if (String.valueOf(mon).length() >= 10) {
					System.out.println(MONEY_ERROR);
					throw new Exception();
				}
				map.put(d.getComCode(), mon);
			} else {
				System.out.println(String.valueOf(d.getProfit()).length());
				if (String.valueOf(d.getProfit()).length() >= 10) {
					System.out.println(MONEY_ERROR);
					throw new Exception();
				}
				map.put(d.getComCode(), d.getProfit());
			}
		}
		return map;
	}
}
