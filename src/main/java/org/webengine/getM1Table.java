package org.webengine;

import java.util.ArrayList;

import org.einclusion.GUI.M1Table;

public class getM1Table {
	public getM1Table(){
		M1Table m1Table = new M1Table();
		m1Table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> list = M1Table.list;
//		Request.setAttribute("list", list);
		System.out.println(list.get(0).get(0));
	}
}
