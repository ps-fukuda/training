package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	public static void main(String[] args) {
		String dirPath;
		if (args.length != 1) {
			System.out.println("予期せぬエラーが発生しました");
			System.exit(1);
		}
		if (args[0].endsWith("/")) {
			dirPath = args[0];
		} else {
			dirPath = args[0] + "/";
		}
		Map<String, String> branchNamesList = new HashMap<String, String>();
		Map<String, String> commodityNamesList = new HashMap<String, String>();

		FileImporter fileImporter = new FileImporter(dirPath);
		fileImporter.Branch();
		branchNamesList = fileImporter.getFileContents();
		fileImporter.Commodity();
		commodityNamesList = fileImporter.getFileContents();
		// NamesListを元にSalesListを作成
		Map<String, Long> branchSalesList = fileImporter.genSalesMap(branchNamesList);
		Map<String, Long> commoditySalesList = fileImporter.genSalesMap(commodityNamesList);
		// rcdファイル一覧を取得
		File dir = new File(dirPath);
		String[] files = dir.list(new ExtentFilter());
		// rcdファイルの連番の歯抜けチェック
		try {
			if (!fileNameCheck(files)) {
				System.out.println("売上ファイル名が連番になっていません");
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 0:branch 1:commodity
		Summary summary = new Summary(dirPath);
		try {
			branchSalesList = summary.checkRcdFile(files, branchSalesList, 0);
			commoditySalesList = summary.checkRcdFile(files, commoditySalesList, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// sort
		List<Map.Entry<String, Long>> branchSalesSortedList = sortList(branchSalesList);
		List<Map.Entry<String, Long>> commoditySalesSortedList = sortList(commoditySalesList);
		// output
		FileOutputer fileOutputer = new FileOutputer(dirPath);
		// 支店別集計ファイルの出力
		fileOutputer.Branch();
		fileOutputer.output(branchNamesList, branchSalesSortedList);
		// 商品別集計ファイルの出力
		fileOutputer.Commodity();
		fileOutputer.output(commodityNamesList, commoditySalesSortedList);
	}

	static List<Map.Entry<String, Long>> sortList(Map<String, Long> map) {
		List<Entry<String, Long>> entryMap = new ArrayList<Entry<String, Long>>(map.entrySet());
		Collections.sort(entryMap, new Comparator<Entry<String, Long>>() {
		    @Override
		    public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
		        return o2.getValue().compareTo(o1.getValue());
		    }
		});
		return entryMap;
	}

	static boolean fileNameCheck(String[] files) {
		String pattern = "[0-9]{8}";
		for (int i=0; i<files.length; i++) {
			String name = files[i].split("\\.")[0];
			if (!name.matches(pattern)) {
				System.out.println("売上ファイル名が連番になっていません");
				System.exit(1);
			}
			if ((i+1) != Integer.parseInt(name)) {
				return false;
			}
		}
		return true;
	}
}

class ExtentFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return name.endsWith(".rcd");
	}
}