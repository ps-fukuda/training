package jp.co.iccom.fukuda_naoki.calculate_sales;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileOutputer {
	String path;
	String separator = ",";
	String fileName;

	FileOutputer(String dirPath) {
		this.path = dirPath;
	}

	void Branch() {
		this.fileName = "branch.out";
	}

	void Commodity() {
		this.fileName = "commodity.out";
	}

	public void fileOutput(List<String[]> data) {
		try {
			FileWriter fw = new FileWriter(path + fileName, true);
			for (String[] d : data) {
				String buf = String.join(separator, d);
				System.out.println(buf);
				fw.write(buf);
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String[]> changeResultFormat(List<String[]> data, Map<String, Integer> map) {
		List<String[]> result = new ArrayList<>();
		for (String[] d : data) {
			if (map.containsKey(d[0])) {
				result.add(new String[]{d[0], d[1], String.valueOf(map.get(d[0]))});
			} else {
				result.add(new String[]{d[0], d[1]});
			}
		}
		return result;
	}
}
