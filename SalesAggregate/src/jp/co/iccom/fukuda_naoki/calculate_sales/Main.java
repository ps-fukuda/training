package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		String dirPath = args[0] + "/";
		List<String[]> branchList = new ArrayList<String[]>();
		List<String[]> productList = new ArrayList<String[]>();

		FileImporter fi = new FileImporter(dirPath);
		try {
			fi.Branch();
			branchList = fi.getFileContents();
			fi.Product();
			productList = fi.getFileContents();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File dir = new File(dirPath);
		String[] files = dir.list(new rcdCheck());
		List<SalesData> salesAll = fi.getRcdContents(files);
		Map<String, Integer> braMap = getBraSummary(salesAll);
		Map<String, Integer> comMap = getComSummary(salesAll);

		FileOutputer fo = new FileOutputer(dirPath);
		// 支店別集計ファイルの出力
		fo.Branch();
		List<String[]> braResult = fo.changeResultFormat(branchList, braMap);
		fo.fileOutput(braResult);
		// 商品別集計ファイルの出力
		fo.Commodity();
		List<String[]> comResult = fo.changeResultFormat(productList, comMap);
		fo.fileOutput(comResult);

		// HashMapCheck
	    /*for (String key : map.keySet()) {
	        System.out.println("val : " + map.get(key));
	    }*/
	}

	public static Map<String, Integer> getBraSummary(List<SalesData> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (SalesData d : list) {
			// mapに既にキーが存在しているか
			if (map.containsKey(d.getBraCode())) {
				// mapから既存値を取得後に合算
				map.put(d.getBraCode(), map.get(d.getBraCode()) + d.getProfit());
			} else {
				map.put(d.getBraCode(), d.getProfit());
			}
		}
		return map;
	}

	public static Map<String, Integer> getComSummary(List<SalesData> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (SalesData d : list) {
			// mapに既にキーが存在しているか
			if (map.containsKey(d.getComCode())) {
				// mapから既存値を取得後に合算
				map.put(d.getComCode(), map.get(d.getComCode()) + d.getProfit());
			} else {
				map.put(d.getComCode(), d.getProfit());
			}
		}
		return map;
	}
}

class rcdCheck implements FilenameFilter {
	private String namePattern = "[0-9]{8}.rcd";

	public boolean accept(File dir, String name) {
		return name.matches(namePattern);
	}
}