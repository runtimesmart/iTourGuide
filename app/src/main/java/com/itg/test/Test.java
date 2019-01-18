/**
* @FileName Test.java
* @Package com.itg.test
* @Description TODO
* @Author Alpha
* @Date 2015-10-20 上午11:46:32 
* @Version V1.0

*/
package com.itg.test;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.location.h.l;

import android.R.integer;

public class Test {

//	private static  int tem;
//	private ExecutorService service=Executors.newFixedThreadPool(1);
//public static void main(String[] args)
//{
//	new Thread(new Runnable() {
//		
//		@Override
//		public void run() {
//			for (int i = 0; i <100; i++) {
//				tem+=1;
//				System.out.println("线程内"+tem);
//			}
//		
//		}
//	}).start();
//	System.out.println("线程外"+tem);
//	
//	}
//	static {
//		  a=4;
//		
//	}
//	private static final  int a;
//	public void exchange(String str)
//	{
//		str="test ok";
//	}
//	public static void main(String[] args)
//	{
////		Test test=new Test();
////		System.out.println(test.a);
////	  int[] i={4,9,6,1,2,11,5,6,7,0,8,10};
////	  cocktail_sort(i);
//////		DichotomySort(i);
////	    for (int j = 0; j < i.length; j++) {
////        	System.out.println(i[j]);
////        }
//		
////		String str=new String("hello");
////		Test test=new Test();
////		test.exchange(str);
//	//	str="1222";
////		int i=0;
////		i=i++;
////		System.out.println(i);
////		System.out.println(i);
//		//removeTest();
//		
////		quick_sort(i,0,i.length-1);
////		for (int j = 0; j < i.length; j++) {
////        	System.out.println(i[j]);
////        }
//		
//	//	insertionSort(A,2);
//		
//		
//	}
	
	 public static void Merge(int[] array, int low, int mid, int high) {
	        int i = low; // i是第一段序列的下标
	        int j = mid + 1; // j是第二段序列的下标
	        int k = 0; // k是临时存放合并序列的下标
	        int[] array2 = new int[high - low + 1]; // array2是临时合并序列

	        // 扫描第一段和第二段序列，直到有一个扫描结束
	        while (i <= mid && j <= high) {
	            // 判断第一段和第二段取出的数哪个更小，将其存入合并序列，并继续向下扫描
	            if (array[i] <= array[j]) {
	                array2[k] = array[i];
	                i++;
	                k++;
	            } else {
	                array2[k] = array[j];
	                j++;
	                k++;
	            }
	        }

	        // 若第一段序列还没扫描完，将其全部复制到合并序列
	        while (i <= mid) {
	            array2[k] = array[i];
	            i++;
	            k++;
	        }

	        // 若第二段序列还没扫描完，将其全部复制到合并序列
	        while (j <= high) {
	            array2[k] = array[j];
	            j++;
	            k++;
	        }

	        // 将合并序列复制到原始序列中
	        for (k = 0, i = low; i <= high; i++, k++) {
	            array[i] = array2[k];
	        }
	    }

	    public static void MergePass(int[] array, int gap, int length) {
	        int i = 0;
	        
	        // 归并gap长度的两个相邻子表
	        for (i = 0; i + 2 * gap - 1 < length; i = i + 2 * gap) {
	            Merge(array, i, i + gap - 1, i + 2 * gap - 1);
	        }
	        
	        // 余下两个子表，后者长度小于gap
	        if (i + gap - 1 < length) {
	            Merge(array, i, i + gap - 1, length - 1);
	        }
	    }

	    public static int[] sort(int[] list) {
	        for (int gap = 1; gap < list.length; gap = 2 * gap) {
	            MergePass(list, gap, list.length);
	            System.out.print("gap = " + gap + ":\t");
	            printAll(list);
	        }
	        return list;
	    }

	    // 打印完整序列
	    public static void printAll(int[] list) {
	        for (int value : list) {
	            System.out.print(value + "\t");
	        }
	        System.out.println();
	    }

	    public static void main(String[] args) {
	        int[] array = { 9, 1, 5, 3, 4, 2, 6, 8, 7};
//	        MergeSort merge = new MergeSort();
	        System.out.print("排序前:\t\t");
	        printAll(array);
	        sort(array);
	        System.out.print("排序后:\t\t");
	        printAll(array);
	    }
//	static int A[]={19,2,33,4,6,7,8,3,6,39};
//	
//    public static int[] insertionSort(int[] A, int n) {
//        int i, j, temp;
//         
//       for(i = 1; i < A.length; i++){
//           temp = A[i];
//           for(j = i; j > 0 && A[j - 1] > temp; j-- ){
//               A[j] = A[j - 1];
//           }
//           A[j] = temp;
//       }
//         
//       return A;
       // write code here
   
	
	private static void cocktail_sort(int[] s)
	{
		
		 for (int i = 1; i < s.length; i++)
         {
             if (s[i - 1] > s[i])
             {
                 int temp = s[i];
                 int j = i;
                 while (j > 0 && s[j - 1] > temp)
                 {
                     s[j] = s[j - 1];
                     j--;
                 }
                 s[j] = temp;
             }
         }
//		boolean isTranslate=false;
//		do {
//			for (int j = 0; j <s.length-1; j++) {
//					if(s[j]>s[j+1])
//					{
//						int tem=s[j];
//						s[j]=s[j+1];
//						s[j+1]=tem;
//						isTranslate=true;
//					}
//			}
//			isTranslate=false;
//			for (int i = s.length-1; i >1; i--) {
//				if(s[i-1]>s[i])
//				{
//					int tem=s[i-1];
//					s[i-1]=s[i];
//					s[i]=tem;
//					isTranslate=true;
//				}
//			}
//		} while (isTranslate);
		
		
	}
	
//	private void quick_sort(int[] s,int f,int l)
//	{
//		int i=f,j=l;
//		
//	}
	
	
	
	
	
	

		//快速排序
//	private static 	void quick_sort(int s[], int l, int r)
//		{
//		    if (l < r)
//		    {
//				//Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1
//		        int i = l, j = r, x = s[l];
//		        while (i < j)
//		        {
//		            while(i < j && s[j] >= x) // 从右向左找第一个小于x的数
//						j--;  
//		            if(i < j) 
//						s[i++] = s[j];
//					
//		            while(i < j && s[i] < x) // 从左向右找第一个大于等于x的数
//						i++;  
//		            if(i < j) 
//						s[j--] = s[i];
//		        }
//		        s[i] = x;
//		        quick_sort(s, l, i - 1); // 递归调用 
//		        quick_sort(s, i + 1, r);
//		    }
//		}
	
	
	private static void removeTest()
	{
		Map<Integer,String> set=new HashMap<Integer,String>();
		set.put(1,"1");
		set.put(2,"2");
		set.put(3,"3");
		set.put(4,"4");
		set.put(5,"5");
		set.put(6,"6");
		set.put(7,"7");
		set.put(8,"8");
		Iterator< Entry<Integer, String>> itr=set.entrySet().iterator();
		itr.next();
		itr.remove();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
		Stack<String> stack=new Stack<String>();

	}
	public static void DichotomySort(int[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            int start, end, mid;
            start = 0;
            end = i - 1;
            mid = 0;
            int temp = array[i];
            while (start <= end)
            {
                mid = (start + end) / 2;
                if (array[mid] > temp)//要排序元素在已经排过序的数组左边
                {
                    end = mid - 1;
                }
                else
                {
                    start = mid + 1;
                }
            }
            for (int j = i - 1; j > end; j--)//找到了要插入的位置，然后将这个位置以后的所有元素向后移动
            {
                array[j + 1] = array[j];
            }
            array[end + 1] = temp;


        }
    }

}
