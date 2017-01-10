package jp.co.plusize.fukuda_naoki.calculate_sales;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class RcdReader {
	public String path;
	public static final int BRANCH = 0;
	public static final int COMMODITY = 1;

	RcdReader(String dirPath) {
		this.path = dirPath;
	}

	Map<String, Long> checkRcdFile(File[] files, Map<String, Long> salesList, int index) throws Exception {
		String codeName = "";
		if (index == 0) codeName = "支店";
		if (index == 1) codeName = "商品";
		String separator = System.getProperty("line.separator");

		for (File file : files) {
			byte[] fileContentBytes = Files.readAllBytes(Paths.get(path, file.getName()));
			String rcdStr = new String(fileContentBytes, StandardCharsets.UTF_8);
			String[] rcdList = rcdStr.split(separator);
			if (rcdList.length != 3) {
				System.out.println(file + "のフォーマットが不正です");
				throw new Exception();
			}
			if (!salesList.containsKey(rcdList[index])) {
				System.out.println(file + "の" + codeName + "コードが不正です");
				throw new Exception();
			}
			if (!rcdList[2].matches("[0-9]+")) {
				System.out.println("予期せぬエラーが発生しました");
				throw new Exception();
			}
			long total = salesList.get(rcdList[index]) + Long.parseLong(rcdList[2]);
			if (String.valueOf(total).length() >= 10) {
				System.out.println("合計金額が10桁を超えました");
				throw new Exception();
			}
			// 既存値 + rcdの金額
			salesList.put(rcdList[index], total);
		}
		return salesList;
	}
}