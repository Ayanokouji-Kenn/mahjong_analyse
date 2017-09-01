package com.uu.mahjong_analyse.mj;


import com.uu.mahjong_analyse.base.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SetTable {
	private HashMap<Integer, Boolean> m_tbl = new HashMap<Integer, Boolean>();

	public boolean check(Integer number) {
		return m_tbl.containsKey(number);
	}

	public void add(int key) {
		if (m_tbl.containsKey(key))
			return;

		m_tbl.put(key, true);
	}

	public void dump(String name) {
	}

	public void load(String path) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(MyApplication.getInstance().getAssets().open(path)));
			String line = "";
//			int num=0;
			while ((line = br.readLine()) != null) {
//				System.out.println("line:" + line);
				m_tbl.put(Integer.parseInt(line), true);
//				num++;
			}
			br.close();
//			System.out.printf("load %s: num=%d\n", path, num);
//			if(m_tbl.containsKey(200000000)){
//				System.out.println("contains 200000000");
//			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
