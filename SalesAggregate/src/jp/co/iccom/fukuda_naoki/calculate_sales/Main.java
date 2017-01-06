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
		Map<String, Long> branchSummaryMap = new HashMap<String, Long>();
		Map<String, Long> commoditySummaryMap = new HashMap<String, Long>();

		FileImporter fileImporter = new FileImporter(dirPath);
		try {
			fileImporter.Branch();
			branchList = fileImporter.getFileContents();
			fileImporter.Commodity();
			productList = fileImporter.getFileContents();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File dir = new File(dirPath);
		String[] files = dir.list(new ExtentFilter());
		List<Map<String, Long>> branchSalesData = fileImporter.getRcdContents(files, 0);
		List<Map<String, Long>> CommoditySalesData = fileImporter.getRcdContents(files, 1);
		try {
			Summary summary = new Summary();
			branchSummaryMap = summary.getSummary(branchSalesData);
			commoditySummaryMap = summary.getSummary(CommoditySalesData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileOutputer fileOutputer = new FileOutputer(dirPath);
		// 支店別集計ファイルの出力
		fileOutputer.Branch();
		List<String[]> branchResult = fileOutputer.changeResultFormat(branchList, branchSummaryMap);
		fileOutputer.output(branchResult);
		// 商品別集計ファイルの出力
		fileOutputer.Commodity();
		List<String[]> commodityResult = fileOutputer.changeResultFormat(productList, commoditySummaryMap);
		fileOutputer.output(commodityResult);
	}
}

class ExtentFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return name.matches("[0-9]{8}.rcd");
	}
}