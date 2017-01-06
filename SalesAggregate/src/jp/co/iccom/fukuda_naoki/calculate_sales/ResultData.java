package jp.co.iccom.fukuda_naoki.calculate_sales;

public class ResultData implements Comparable<ResultData> {
	private String code;
	private String name;
	private long profit;

	public ResultData(String code, String name, long profit) {
		this.code = code;
		this.name = name;
		this.profit = profit;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public long getProfit() {
		return profit;
	}

	public String getData() {
		return code + "," + name + "," + profit;
	}

	@Override
	public int compareTo(ResultData o) {
		return (int) (o.profit - this.profit);
	}
}
