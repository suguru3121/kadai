package log_aggregation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class log_aggregation {

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public static void main(String[] args) {

		// 集計結果を格納するデータ構造を用意する
		List<Access> sampleList = getEvery5Minutes();

		// ログを全行読み取る（タブ区切りで分割したリストのリストにする）
		List<List<String>> splitRowOfFile = getSplitRowOfFile();

		// 読み込んだログのリストを１行ずつ、処理する
		for (int i = 0; i < splitRowOfFile.size(); i++) {

			List<String> lineItems = splitRowOfFile.get(i);
			LocalDateTime ld = LocalDateTime.parse(lineItems.get(0), dtf);

			// 読み取ったログ1行の１列めの時刻を５分の区切りに丸める（難しい？）
			//			System.out.println(ld.format(dtf) + "," +ld.getMinute());
			int minutes = ld.getMinute();
			int minute5 = minutes / 5 * 5;
			LocalDateTime ld2 = ld.withMinute(minute5);
			ld2 = ld2.withSecond(0);
			int responseTime = Integer.parseInt(lineItems.get(2));
			//			System.out.println(ld.format(dtf) + "," + ld2.format(dtf));

			// 丸めた時刻から、先に作成した結果を入れるいれもの（sampleList）の中から、時刻が一致するaccessオブジェクトを取り出す（若干難しい）
			for (Access sample : sampleList) {
				LocalDateTime every5minutes = sample.getEvery5minuts();
				if (ld2.equals(every5minutes)) {
					int j = 1;
					int k = 1;
					int l = 1;
					// 取り出したaccsessオブジェクトの適切な応答時間のカウンターを＋１する
					if (responseTime <= 500) {
						//						System.out.println("500ms :" + every5minutes + " > " + ld + " > " + responseTime);
						sample.setLessThan500ms(j);
						j++;
					} else if (500 < responseTime && responseTime <= 2000) {
						//						System.out.println("2000ms :" + every5minutes + " > " + ld + " > " + responseTime);
						sample.setLessThan2000ms(j);
						k++;
					} else {
						//						System.out.println("2001ms :" + every5minutes + " > " + ld + " > " + responseTime);
						sample.setMoreThan2001ms(l);
						l++;
					}
				}
				System.out.println(sample.getEvery5minuts() + " "+ sample.getLessThan500ms() +" "+ sample.getLessThan2000ms() +" "+ sample.getMoreThan2001ms());

			}

		}

		// あとはsampleListをダンプする（平均応答時間は一工夫必要）accessクラスに応答時間の累計を持たせれば計算で出せる

	}

	//ファイルを1行ごとに読み込むメソッド
	public static List<String> readFile() {
		List<String> rowList = new ArrayList<String>();
		try {
			//ファイルのパスを指定してください
			File file = new File("C:\\Users\\user\\Desktop\\ログ集計\\アクセスログ.txt");
			if (!file.exists()) {
				System.out.print("ファイルが存在しません");
			}
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String data;
			while ((data = bufferedReader.readLine()) != null) {
				rowList.add(data);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rowList.remove(0);
		return rowList;
	}

	//
	public static List<List<String>> getSplitRowOfFile() {
		List<List<String>> cellList = new ArrayList<>();
		for (String row : readFile()) {
			cellList.add(Arrays.asList(row.split("\t")));
		}
		return cellList;
	}

	public static List<Integer> getAccessCountList() {
		List<Integer> accessList = new ArrayList<>();
		int i = 0;
		for (List<String> row1 : getSplitRowOfFile()) {
			accessList.add(Integer.parseInt(row1.get(2)));
		}
		return accessList;
	}

	public static List<Access> getEvery5Minutes() {
		//2017年4月7日　00:00:00 から　4月8日までの時刻を入れる

		List<Access> every5MinutesList = new ArrayList<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime evere5minuts = LocalDateTime.of(2012, 4, 7, 00, 00, 00);

		for (int i = 0; evere5minuts.getDayOfMonth() < 8; i++) {
			Access access = new Access();
			access.setEvery5minuts(evere5minuts);
			evere5minuts = evere5minuts.plusMinutes(5);
			every5MinutesList.add(access);
		}

		return every5MinutesList;
	}

}