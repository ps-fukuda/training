package jp.co.plusize.fukuda_naoki.calculate_sales;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public void output(Map<String, String> names, List<Entry<String, Long>> sales) {
		String separator = System.getProperty("line.separator");
		try {
			File file = new File(path + fileName);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(path + fileName, true);
			for (Entry<String, Long> s : sales) {
				fw.write(s.getKey() + "," + names.get(s.getKey()) + "," + s.getValue());
				fw.write(separator);
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public void output(Map<String, String> names, Map<String, Long> sales) {
		for (String s : sales.keySet()) {
			System.out.println(s + ":" + sales.get(s));
		}
		try {
			File file = new File(path + fileName);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(path + fileName, true);
			for (String key : sales.keySet()) {
				fw.write(key + "," + names.get(key) + "," + sales.get(key));
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}