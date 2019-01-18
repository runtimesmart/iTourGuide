/**
* @FileName MergeSortTest.java
* @Package com.itg.test
* @Description TODO
* @Author Alpha
* @Date 2016-1-22 下午3:13:38 
* @Version V1.0

*/
package com.itg.test;

public class MergeSortTest {

static int[] arr={5,9,0,3,2,5,2,7,10,3,6};
public static void main(String[] arg)
{
	sort(arr,0,10);
	for (int i = 0; i < arr.length; i++) {
		System.out.print(arr[i]+" ");
	}
}
private static int[] sort(int[] arr, int low,int high )
{
	int mid=(low+high)/2;
	if(low<high)
	{
		sort(arr,low, mid);
		sort(arr, mid+1, high);
		merge(arr, low, mid, high);
	}
	return arr;
}
private static void merge(int[] arr ,int begin,int middle,int end)
{
	int tem[]=new int[end-begin+1];

	int i=begin;
	int j=middle+1;
	int k=0;
	while(i<=middle && j<=end)
	{
		if(arr[i]>arr[j])
		{
			tem[k++]=arr[j++];
		}
		else {
			tem[k++]=arr[i++];
		}
	}
	while(i<=middle)
	{
		tem[k++]=arr[i++];
	}
	while (j<=end) {
		tem[k++]=arr[j++];
		
	}
	
	for (int k2 = 0; k2 < tem.length; k2++) {
		arr[k2+begin]=tem[k2];
	}
}

}
