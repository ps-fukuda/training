package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Summary {
	public String path;

	Summary(String dirPath) {
		this.path = dirPath;
	}

	Map<String, Long> checkRcdFile(String[] files, Map<String, Long> salesList, int index) throws Exception {
		String codeName = "";
		if (index == 0) codeName = "支店";
		if (index == 1) codeName = "商品";

		for (String file : files) {
			byte[] fileContentBytes = Files.readAllBytes(Paths.get(path + file));
			String rcdStr = new String(fileContentBytes, StandardCharsets.UTF_8);
			String[] rcdList = rcdStr.split("\r\n");
			if (rcdList.length != 3) {
				System.out.println(file + "のフォーマットが不正です");
				throw new Exception();
			}
			if (!salesList.containsKey(rcdList[index])) {
				System.out.println(file + "の" + codeName + "コードが不正です");
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