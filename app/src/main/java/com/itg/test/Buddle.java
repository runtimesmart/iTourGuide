/**
* @FileName Buddle.java
* @Package com.itg.test
* @Description TODO
* @Author Alpha
* @Date 2016-1-27 上午8:20:09 
* @Version V1.0

*/
package com.itg.test;

import com.baidu.a.a.a.a;

public class Buddle {

static	int[] a={8,5,1,3,8,9,0,87,876,7,2};
	public static void main(String[] arg) {
		for (int i = 0; i < a.length-1; i++) {
			for (int j = i+1; j < a.length; j++) {
				if(a[i]>a[j])
				{
					int tem=a[i];
					a[i]=a[j];
					a[j]=tem;
				}
			}
		}
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+" ");
		}
	}

}
