package com.bhyh.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BhArrayUtils {
	
	public static List<?> tranferArrToList(Object[] objArr){
		List<Object> asList = Arrays.asList(objArr);
		ArrayList<Object> list = new ArrayList<Object>(asList);
		return list;
	}
}
