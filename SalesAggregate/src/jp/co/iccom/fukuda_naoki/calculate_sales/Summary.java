package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Summary {

	public static final String MONEY_ERROR = "合計金額が10桁を超えました";

	public Map<String, Long> getSummary(List<Map<String, Long>> lists) throws Exception {
		Map<String, Long> summaryMap = new HashMap<String, Long>();
		for (Map<String, Long> list : lists) {
			for (String key : list.keySet()) {
				// mapに既にキーが存在しているか
				if (summaryMap.containsKey(key)) {
					// mapから既存値を取得後に合算
					long money = summaryMap.get(key) + list.get(key);
					if (String.valueOf(money).length() >= 10) {
						System.out.println(MONEY_ERROR);
						throw new Exception();
					}
					summaryMap.put(key, money);
				} else {
					if (String.valueOf(list.get(key)).length() >= 10) {
						System.out.println(MONEY_ERROR);
						throw new Exception();
					}
					summaryMap.put(key, list.get(key));
				}
			}
		}
		return summaryMap;
	}
}