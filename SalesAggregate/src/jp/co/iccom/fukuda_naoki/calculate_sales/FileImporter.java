package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

	List<String[]> getFileContents() throws Exception {
		List<String[]> list = new ArrayList<>();
		try {
			FileReader fr = new FileReader(path + fileName);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] item = line.split(",");
				if (item[0].matches(codePattern) && item[1].matches(namePattern)) {
					list.add(item);
				} else {
					System.out.println(formatError);
					throw new Exception();
				}
			}
		} catch (IOException e) {
			System.out.println(notFoundError);
			throw new Exception();
		}
		return list;
	}

	List<SalesData> getRcdContents(String[] files) {
		List<SalesData> listAll = new ArrayList<>();
		for (String file : files) {
			try {
				byte[] fileContentBytes = Files.readAllBytes(Paths.get(path + file));
				String contentStr = new String(fileContentBytes, StandardCharsets.UTF_8);
				String[] contentList = contentStr.split("\r\n");
				if (contentList.length != 3) {
					System.out.println(file + "のフォーマットが不正です");
					throw new IOException();
				}
				listAll.add(new SalesData(contentList));
			} catch (Exception e) {
				//
			}
		}
		return listAll;
	}
}