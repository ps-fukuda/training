package jp.co.plusize.fukuda_naoki.calculate_sales;

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
		if (args.length != 1) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		String dirPath = args[0];
		Map<String, String> branchNamesList = new HashMap<String, String>();
		Map<String, String> commodityNamesList = new HashMap<String, String>();

		FileImporter fileImporter = new FileImporter(dirPath);
		fileImporter.initBranch();
		branchNamesList = fileImporter.getFileContents();
		if (branchNamesList == null) return;
		fileImporter.initCommodity();
		commodityNamesList = fileImporter.getFileContents();
		if (commodityNamesList == null) return;
		// NamesListを元にSalesListを作成
		Map<String, Long> branchSalesList = fileImporter.genSalesMap(branchNamesList);
		Map<String, Long> commoditySalesList = fileImporter.genSalesMap(commodityNamesList);
		// rcdファイル一覧を取得
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new ExtentFilter());
		// 歯抜けチェック
		if (!fileNameCheck(files)) {
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}
		RcdReader rcdReader = new RcdReader(dirPath);
		try {
			branchSalesList = rcdReader.checkRcdFile(files, branchSalesList, RcdReader.BRANCH);
			commoditySalesList = rcdReader.checkRcdFile(files, commoditySalesList, RcdReader.COMMODITY);
		} catch (Exception e) {
			return;
		}
		// sort
		List<Entry<String, Long>> branchSalesSortedList = sortList(branchSalesList);
		List<Entry<String, Long>> commoditySalesSortedList = sortList(commoditySalesList);
		// output
		FileOutputer fileOutputer = new FileOutputer(dirPath);
		try {
			// 支店別集計ファイルの出力
			fileOutputer.initBranch();
			fileOutputer.output(branchNamesList, branchSalesSortedList);
			// 商品別集計ファイルの出力
			fileOutputer.initCommodity();
			fileOutputer.output(commodityNamesList, commoditySalesSortedList);
		} catch (Exception e) {
			return;
		}
	}

	static List<Entry<String, Long>> sortList(Map<String, Long> map) {
		List<Entry<String, Long>> entryMap = new ArrayList<>(map.entrySet());
		Collections.sort(entryMap, new Comparator<Entry<String, Long>>() {
		    @Override
		    public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
		        return o2.getValue().compareTo(o1.getValue());
		    }
		});
		return entryMap;
	}

	static boolean fileNameCheck(File[] files) {
		for (int i=0; i<files.length; i++) {
			String name = files[i].getName().split("\\.")[0];
			if ((i+1) != Integer.parseInt(name)) {
				return false;
			}
		}
		return true;
	}
}

class ExtentFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		String pattern = "^[0-9]{8}.rcd$";
		File file = new File(dir, name);
		if (file.isDirectory()) {
			return false;
		}
		return name.matches(pattern);
	}
}