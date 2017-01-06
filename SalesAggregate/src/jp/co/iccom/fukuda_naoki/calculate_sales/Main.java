package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
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
		List<Map<String, Long>> branchSalesData = new ArrayList<>();
		List<Map<String, Long>> commoditySalesData = new ArrayList<>();;

		FileImporter fileImporter = new FileImporter(dirPath);
		try {
			fileImporter.Branch();
			branchList = fileImporter.getFileContents();
			fileImporter.Commodity();
			productList = fileImporter.getFileContents();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> codeList = getDefineCodeList(branchList, productList);

		File dir = new File(dirPath);
		String[] files = dir.list(new ExtentFilter());
		// 連番の歯抜けチェック
		try {
			if (!fileNameCheck(files)) {
				System.out.println("売上ファイル名が連番になっていません");
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 0:branch 1:commodity
		try {
			branchSalesData = fileImporter.getRcdContents(files, codeList, 0);
			commoditySalesData = fileImporter.getRcdContents(files, codeList, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Summary summary = new Summary();
			branchSummaryMap = summary.getSummary(branchSalesData);
			commoditySummaryMap = summary.getSummary(commoditySalesData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileOutputer fileOutputer = new FileOutputer(dirPath);
		// 支店別集計ファイルの出力
		fileOutputer.Branch();
		List<ResultData> branchResult = fileOutputer.changeResultFormat(branchList, branchSummaryMap);
		Collections.sort(branchResult);
		fileOutputer.output(branchResult);
		// 商品別集計ファイルの出力
		fileOutputer.Commodity();
		List<ResultData> commodityResult = fileOutputer.changeResultFormat(productList, commoditySummaryMap);
		Collections.sort(commodityResult);
		fileOutputer.output(commodityResult);
	}

	static boolean fileNameCheck(String[] files) {
		for (int i=0; i<files.length; i++) {
			String name = files[i].split("\\.")[0];
			if ((i+1) != Integer.parseInt(name)) {
				return false;
			}
		}
		return true;
	}

	static List<String> getDefineCodeList(List<String[]> branchList, List<String[]> productList) {
		List<String> codeList = new ArrayList<>();
		for (String[] list : branchList) {
			codeList.add(list[0]);
		}
		for (String[] list : productList) {
			codeList.add(list[0]);
		}
		return codeList;
	}
}

class ExtentFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return name.matches("[0-9]{8}.rcd");
	}
}