package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	private String moneyErrorMsg = "";

	public static void main(String[] args) {
		String dirPath = args[0] + "/";
		List<String[]> branchList = new ArrayList<String[]>();
		List<String[]> productList = new ArrayList<String[]>();
		Map<String, Integer> braMap = new HashMap<String, Integer>();
		Map<String, Integer> comMap = new HashMap<String, Integer>();

		FileImporter fi = new FileImporter(dirPath);
		try {
			fi.Branch();
			branchList = fi.getFileContents();
			fi.Commodity();
			productList = fi.getFileContents();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File dir = new File(dirPath);
		String[] files = dir.list(new ExtentFilter());
		List<SalesData> salesAll = fi.getRcdContents(files);
		try {
			Summary summary = new Summary();
			braMap = summary.getBraSummary(salesAll);
			comMap = summary.getComSummary(salesAll);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
}

class ExtentFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return name.matches("[0-9]{8}.rcd");
	}
}