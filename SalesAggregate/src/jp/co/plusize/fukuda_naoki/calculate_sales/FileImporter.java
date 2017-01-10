package jp.co.plusize.fukuda_naoki.calculate_sales;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	void initBranch() {
		this.fileName = "branch.lst";
		this.notFoundError = "支店定義ファイルが存在しません";
		this.formatError = "支店定義ファイルのフォーマットが不正です";
		this.codePattern = "[0-9]{3}";
	}

	void initCommodity() {
		this.fileName = "commodity.lst";
		this.notFoundError = "商品定義ファイルが存在しません";
		this.formatError = "商品定義ファイルのフォーマットが不正です";
		this.codePattern = "[a-zA-Z0-9]{8}";
	}

	Map<String, String> getFileContents() {
		Map<String, String> map = new HashMap<String, String>();
		Path filePath = Paths.get(path, fileName);
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {
			for(;;) {
				String line = reader.readLine();
				if (line != null) {
					String[] item = line.split(",");
					if (item.length != 2) {
						System.out.println(formatError);
						return null;
					}
					if (item[0].matches(codePattern) && item[1].matches(namePattern)) {
						map.put(item[0], item[1]);
					} else {
						System.out.println(formatError);
						return null;
					}
				} else {
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(notFoundError);
			return null;
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