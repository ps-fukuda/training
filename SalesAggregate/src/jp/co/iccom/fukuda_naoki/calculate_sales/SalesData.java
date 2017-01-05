package jp.co.iccom.fukuda_naoki.calculate_sales;

public class SalesData {
	private String braCode;
	private String comCode;
	private int profit;

	public SalesData(String[] list) {
		this.braCode = list[0];
		this.comCode = list[1];
		this.profit = Integer.parseInt(list[2]);
	}

	public String getBraCode() {
		return braCode;
	}

	public String getComCode() {
		return comCode;
	}

	public int getProfit() {
		return profit;
	}
}