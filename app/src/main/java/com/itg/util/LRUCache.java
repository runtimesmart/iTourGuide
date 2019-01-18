/**
* @FileName LRUCache.java
* @Package com.itg.util
* @Description TODO
* @Author Alpha
* @Date 2015-9-6 下午2:43:15 
* @Version V1.0

*/
package com.itg.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;

public class LRUCache {

	// 定义一个线程安全的有序map
		private Map<String, Bitmap> cache = Collections
				.synchronizedMap(new LinkedHashMap<String, Bitmap>());

		private long size = 0;
		private long limit = 100000;

		public void setLimit(long limit) {
			this.limit = limit;
		}

		public LRUCache() {
			setLimit(Runtime.getRuntime().maxMemory() /5);
		}

		public Bitmap getCache(String id) {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		}

		public void putCache(String id, Bitmap bitmap) {
			if (cache.containsKey(id))
				size -= getSizeByte(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeByte(bitmap);
			checkSize();
		}

		private void checkSize() {
			if (size > limit) {
				Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
				while (iter.hasNext()) {
					iter.remove();
					size -= getSizeByte(cache.get(iter.next().getValue()));
					if(size<limit)
					{
						break;
					}
				}
			}
		}
		
		private void clear()
		{
			cache.clear();
		}

		private long getSizeByte(Bitmap bitmap) {
			if (bitmap == null)
				return 0;
			return bitmap.getRowBytes() * bitmap.getHeight();
		}

}
