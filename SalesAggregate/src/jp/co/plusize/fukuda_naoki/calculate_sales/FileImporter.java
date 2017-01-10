package jp.co.plusize.fukuda_naoki.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class FileImporter {
	private String path;
	private String fileName;
	private String notFoundError;
	private String formatError;
	private String codePattern;
	private String namePattern = "(?!.*,).+";

	FileImporter(String dirPath) {
		this.path = dirPath;
	}

	void Branch() {
		this.fileName = "branch.lst";
		this.notFoundError = "支店定義ファイルが存在しません";
		this.formatError = "支店定義ファイルのフォーマットが不正です";
		this.codePattern = "[0-9]{3}";
	}

	void Commodity() {
		this.fileName = "commodity.lst";
		this.notFoundError = "商品定義ファイルが存在しません";
		this.formatError = "商品定義ファイルのフォーマットが不正です";
		this.codePattern = "[a-zA-Z0-9]{8}";
	}

	Map<String, String> getFileContents() {
		Map<String, String> map = new HashMap<String, String>();
		FileReader fr = null;
		try {
			fr = new FileReader(new File(path, fileName));
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] item = line.split(",");
				if (item.length != 2) {
					System.out.println(formatError);
					return null;
				}
				if (item[0].matches(codePattern) && item[1].matches(namePattern)) {
					map.put(item[0], item[1]);
				} else {
					System.out.println(formatError);
					System.exit(1);
				}
			}
		} catch (IOException e) {
			System.out.println(notFoundError);
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if (fr != null) fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	Map<String, Long> genSalesMap(Map<String, String> nameMap) {
		Map<String, Long> salesMap = new HashMap<String, Long>();
		for (String key : nameMap.keySet()) {
			salesMap.put(key, (long)0);
		}
		return salesMap;
	}
}