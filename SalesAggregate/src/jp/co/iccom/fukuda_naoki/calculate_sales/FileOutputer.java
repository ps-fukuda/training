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

	public void output(List<ResultData> data) {
		try {
			FileWriter fw = new FileWriter(path + fileName, true);
			for (ResultData d : data) {
				fw.write(d.getData());
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ResultData> changeResultFormat(List<String[]> data, Map<String, Long> map) {
		List<ResultData> result = new ArrayList<>();
		for (String[] d : data) {
			if (map.containsKey(d[0])) {
				result.add(new ResultData(d[0], d[1], map.get(d[0])));
			} else {
				result.add(new ResultData(d[0], d[1], 0));
			}
		}
		return result;
	}
}