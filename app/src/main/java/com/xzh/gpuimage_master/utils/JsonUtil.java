package com.xzh.gpuimage_master.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class JsonUtil {

	private static Gson gson = new Gson();

	@SuppressWarnings("hiding")
	public static <T> T parseJson(String response, Class<T> clazz) {
		try {
			return gson.fromJson(response, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T parseJson(String response, Type type){
		try{
			return gson.fromJson(response, type);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static <T> List<T> fromJsonToList(final JsonArray jsonArray, final Class<T> classOfT) {
		List<T> list = new ArrayList<T>();

		for (int i = 0, size = jsonArray.size(); i < size; i++) {
			list.add(gson.fromJson(jsonArray.get(i), classOfT));
		}

		return list;
	}

	public static String toJson(Object object) {
		try {
			return gson.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
