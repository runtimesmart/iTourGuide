/**
* @FileName qplTest.java
* @Package com.itg.test
* @Description TODO
* @Author Alpha
* @Date 2016-1-22 上午10:59:25 
* @Version V1.0

*/
package com.itg.test;

import com.baidu.a.a.a.a;

import android.R.integer;

public class qplTest {

	static int[] arr={1,2,3,4};
	public static void main(String[] arg)
	{
		perm(arr, 0, 3);
	}
	private static void swap(int[] a,int i1,int i2)
	{
		int tem=a[i1];
		a[i1]=a[i2];
		a[i2]=tem;
	}
	
	private static void perm(int[] a ,int begin,int end)
	{
		if(begin==end)
		{
			for (int i = 0; i < a.length; i++) {
				System.out.print(a[i]+" ");
			}
			System.out.println("\n");
			return;
		}
		
		else {
			for (int j = begin; j <= end; j++) {
				swap(a,begin,j);
				perm(a, begin+1, end);
				swap(a,begin,j);
			}
		
		}
	
	}

}
