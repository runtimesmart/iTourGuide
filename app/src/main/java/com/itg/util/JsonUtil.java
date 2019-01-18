/**
 * @FileName Gson.java
 * @Package com.itg.util
 * @Description TODO
 * @Author Alpha
 * @Date 2015-8-21 下午7:50:35 
 * @Version V1.0

 */
package com.itg.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

//	/**
//	 * 反序列化泛型集合
//	 * 
//	 * @param jsonString
//	 * @param collectionClazz
//	 * @param genericType
//	 * @return
//	 * @throws JSONException
//	 */
//	public static <T> Collection<T> parseCollection(String jsonString, Class<?> collectionClazz,  
//            Class<T> genericType) throws JSONException {  
//        if (collectionClazz == null || genericType == null || jsonString == null  
//                || jsonString.length() == 0) {  
//            return null;  
//        }  
//        JSONArray jo = null;  
//        try {  
//            jo = new JSONArray(jsonString);  
//        } catch (JSONException e) {  
//            e.printStackTrace();  
//        }  
//  
//        if (isNull(jo)) {  
//            return null;
//        }
//        return parseCollection(jo, collectionClazz, genericType);  
//	}
//	/**
//	 * 反序列化泛型集合
//	 * 
//	 * @param ja
//	 * @param collectionClazz
//	 * @param genericType
//	 * @return
//	 * @throws JSONException
//	 */
//	@SuppressWarnings("unchecked")
//	public static <T> Collection<T> parseCollection(JSONArray ja,
//			Class<?> collectionClazz, Class<T> genericType)
//			throws JSONException {
//
//		if (collectionClazz == null || genericType == null || isNull(ja)) {
//			return null;
//		}
//
//		Collection<T> collection = (Collection<T>) newInstance(collectionClazz);
//
//		for (int i = 0; i < ja.length(); ++i) {
//			try {
//				JSONObject jo = ja.getJSONObject(i);
//				T o = parseObject(jo, genericType);
//				collection.add(o);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return collection;
//	}
//
//	/**
//	 * 判断对象是否为空
//	 * 
//	 * @param obj
//	 * @return
//	 */
//	private static boolean isNull(Object obj) {
//		if (obj instanceof JSONObject) {
//			return JSONObject.NULL.equals(obj);
//		}
//		return obj == null;
//	}
//
//	/**
//	 * 根据类型创建对象
//	 * 
//	 * @param clazz
//	 * @return
//	 * @throws JSONException
//	 */
//	private static <T> T newInstance(Class<T> clazz) throws JSONException {
//		if (clazz == null)
//			return null;
//		T obj = null;
//		if (clazz.isInterface()) {
//			if (clazz.equals(Map.class)) {
//				obj = (T) new HashMap();
//			} else if (clazz.equals(List.class)) {
//				obj = (T) new ArrayList();
//			} else if (clazz.equals(Set.class)) {
//				obj = (T) new HashSet();
//			} else {
//				throw new JSONException("unknown interface: " + clazz);
//			}
//		} else {
//			try {
//				obj = clazz.newInstance();
//			} catch (Exception e) {
//				throw new JSONException("unknown class type: " + clazz);
//			}
//		}
//		return obj;
//	}
//
//	/**
//	 * 反序列化简单对象
//	 * 
//	 * @param jo
//	 * @param clazz
//	 * @return
//	 * @throws JSONException
//	 */
//	public static <T> T parseObject(JSONObject jo, Class<T> clazz)
//			throws JSONException {
//		if (clazz == null || isNull(jo)) {
//			return null;
//		}
//
//		T obj = newInstance(clazz);
//		if (obj == null) {
//			return null;
//		}
//		if (isMap(clazz)) {
//			setField(obj, jo);
//		} else {
//			// 取出bean里的所有方法
//			Method[] methods = clazz.getDeclaredMethods();
//			Field[] fields = clazz.getDeclaredFields();
//			for (Field f : fields) {
//				String setMetodName = parseMethodName(f.getName(), "set");
//				if (!haveMethod(methods, setMetodName)) {
//					continue;
//				}
//				try {
//					Method fieldMethod = clazz.getMethod(setMetodName,
//							f.getType());
//					setField(obj, fieldMethod, f, jo);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return obj;
//	}
//
//	/**
//	 * 给对象的字段赋值
//	 * 
//	 * @param obj
//	 * @param fieldSetMethod
//	 * @param fieldType
//	 * @param value
//	 */
//	public static void setFiedlValue(Object obj, Method fieldSetMethod,
//			String fieldType, Object value) {
//
//		try {
//			if (null != value && !"".equals(value)) {
//				if ("String".equals(fieldType)) {
//					fieldSetMethod.invoke(obj, value.toString());
//				} else if ("Date".equals(fieldType)) {
//					SimpleDateFormat sdf = new SimpleDateFormat(
//							"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//					Date temp = sdf.parse(value.toString());
//					fieldSetMethod.invoke(obj, temp);
//				} else if ("Integer".equals(fieldType)
//						|| "int".equals(fieldType)) {
//					Integer intval = Integer.parseInt(value.toString());
//					fieldSetMethod.invoke(obj, intval);
//				} else if ("Long".equalsIgnoreCase(fieldType)) {
//					Long temp = Long.parseLong(value.toString());
//					fieldSetMethod.invoke(obj, temp);
//				} else if ("Double".equalsIgnoreCase(fieldType)) {
//					Double temp = Double.parseDouble(value.toString());
//					fieldSetMethod.invoke(obj, temp);
//				} else if ("Boolean".equalsIgnoreCase(fieldType)) {
//					Boolean temp = Boolean.parseBoolean(value.toString());
//					fieldSetMethod.invoke(obj, temp);
//				} else {
//					fieldSetMethod.invoke(obj, value);
//
//				}
//			}
//
//		} catch (Exception e) {
//
//		}
//
//	}
//
//	/**
//	 * 设定字段的值
//	 * 
//	 * @param obj
//	 * @param fieldSetMethod
//	 * @param f
//	 * @param jo
//	 */
//	private static void setField(Object obj, Method fieldSetMethod, Field f,
//			JSONObject jo) {
//		String name = f.getName();
//		Class<?> clazz = f.getType();
//		try {
//			if (isArray(clazz)) { // 数组
//				Class<?> c = clazz.getComponentType();
//				JSONArray ja = jo.optJSONArray(name);
//				if (!isNull(ja)) {
//					Object array = parseArray(ja, c);
//					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(),
//							array);
//				}
//			} else if (isCollection(clazz)) { // 泛型集合
//				// 获取定义的泛型类型
//				Class<?> c = null;
//				Type gType = f.getGenericType();
//				if (gType instanceof ParameterizedType) {
//					ParameterizedType ptype = (ParameterizedType) gType;
//					Type[] targs = ptype.getActualTypeArguments();
//					if (targs != null && targs.length > 0) {
//						Type t = targs[0];
//						c = (Class<?>) t;
//					}
//				}
//
//				JSONArray ja = jo.optJSONArray(name);
//				if (!isNull(ja)) {
//					Object o = parseCollection(ja, clazz, c);
//					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
//				}
//			} else if (isSingle(clazz)) { // 值类型
//				Object o = jo.opt(name);
//				if (o != null) {
//					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
//				}
//			} else if (isObject(clazz)) { // 对象
//				JSONObject j = jo.optJSONObject(name);
//				if (!isNull(j)) {
//					Object o = parseObject(j, clazz);
//					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
//				}
//			} else if (isList(clazz)) { // 列表
//			// JSONObject j = jo.optJSONObject(name);
//			// if (!isNull(j)) {
//			// Object o = parseObject(j, clazz);
//			// f.set(obj, o);
//			// }
//			} else {
//				throw new Exception("unknow type!");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 反序列化数组对象
//	 * 
//	 * @param ja
//	 * @param clazz
//	 * @return
//	 */
//	public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
//		if (clazz == null || isNull(ja)) {
//			return null;
//		}
//
//		int len = ja.length();
//
//		@SuppressWarnings("unchecked")
//		T[] array = (T[]) Array.newInstance(clazz, len);
//
//		for (int i = 0; i < len; ++i) {
//			try {
//				JSONObject jo = ja.getJSONObject(i);
//				T o = parseObject(jo, clazz);
//				array[i] = o;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return array;
//	}
//
//	/**
//	 * 反序列化数组对象
//	 * 
//	 * @param jsonString
//	 * @param clazz
//	 * @return
//	 */
//	public static <T> T[] parseArray(String jsonString, Class<T> clazz) {
//		if (clazz == null || jsonString == null || jsonString.length() == 0) {
//			return null;
//		}
//		JSONArray jo = null;
//		try {
//			jo = new JSONArray(jsonString);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		if (isNull(jo)) {
//			return null;
//		}
//
//		return parseArray(jo, clazz);
//	}
//
//	/**
//	 * 判断是否存在某属性的 get方法
//	 * 
//	 * @param methods
//	 * @param fieldGetMet
//	 * @return boolean
//	 */
//	public static boolean haveMethod(Method[] methods, String fieldMethod) {
//		for (Method met : methods) {
//			if (fieldMethod.equals(met.getName())) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * 拼接某属性的 get或者set方法
//	 * 
//	 * @param fieldName
//	 * @param methodType
//	 * @return
//	 */
//	public static String parseMethodName(String fieldName, String methodType) {
//		if (null == fieldName || "".equals(fieldName)) {
//			return null;
//		}
//		return methodType + fieldName.substring(0, 1).toUpperCase()
//				+ fieldName.substring(1);
//	}
//
//	/**
//	 * 设定Map的值
//	 * 
//	 * @param obj
//	 * @param jo
//	 */
//	private static void setField(Object obj, JSONObject jo) {
//		try {
//			@SuppressWarnings("unchecked")
//			Iterator<String> keyIter = jo.keys();
//			String key;
//			Object value;
//			@SuppressWarnings("unchecked")
//			Map<String, Object> valueMap = (Map<String, Object>) obj;
//			while (keyIter.hasNext()) {
//				key = (String) keyIter.next();
//				value = jo.get(key);
//				valueMap.put(key, value);
//
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 判断是否是值类型
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	private static boolean isSingle(Class<?> clazz) {
//		return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
//	}
//
//	/**
//	 * 是否布尔值
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isBoolean(Class<?> clazz) {
//		return (clazz != null)
//				&& ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
//						.isAssignableFrom(clazz)));
//	}
//
//	/**
//	 * 是否数值
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isNumber(Class<?> clazz) {
//		return (clazz != null)
//				&& ((Byte.TYPE.isAssignableFrom(clazz))
//						|| (Short.TYPE.isAssignableFrom(clazz))
//						|| (Integer.TYPE.isAssignableFrom(clazz))
//						|| (Long.TYPE.isAssignableFrom(clazz))
//						|| (Float.TYPE.isAssignableFrom(clazz))
//						|| (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
//							.isAssignableFrom(clazz)));
//	}
//
//	/**
//	 * 判断是否是字符串
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isString(Class<?> clazz) {
//		return (clazz != null)
//				&& ((String.class.isAssignableFrom(clazz))
//						|| (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
//							.isAssignableFrom(clazz)));
//	}
//
//	/**
//	 * 判断是否是对象
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	private static boolean isObject(Class<?> clazz) {
//		return clazz != null && !isSingle(clazz) && !isArray(clazz)
//				&& !isCollection(clazz);
//	}
//
//	/**
//	 * 判断是否是数组
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isArray(Class<?> clazz) {
//		return clazz != null && clazz.isArray();
//	}
//
//	/**
//	 * 判断是否是集合
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isCollection(Class<?> clazz) {
//		return clazz != null && Collection.class.isAssignableFrom(clazz);
//	}
//
//	/**
//	 * 判断是否是Map
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isMap(Class<?> clazz) {
//		return clazz != null && Map.class.isAssignableFrom(clazz);
//	}
//
//	/**
//	 * 判断是否是列表
//	 * 
//	 * @param clazz
//	 * @return
//	 */
//	public static boolean isList(Class<?> clazz) {
//		return clazz != null && List.class.isAssignableFrom(clazz);
//	}

}