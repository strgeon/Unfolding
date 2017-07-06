package module6;

import java.util.*;

public class SelectionSort { 
		
	
	public SelectionSort (List<Integer> intarray) {
		
		Integer i, j, least;
		Boolean swap;
				
		for (i=0;i < intarray.size();i++) {
			least = i;
			swap = false;
			for (j=i+1; j < intarray.size(); j++) {
				if (intarray.get(j) < intarray.get(least)) {
					least=j;
					swap = true;
				}
			}
			if (swap) {
				Integer tempi = intarray.get(i);
				intarray.set(i, intarray.get(least));
				intarray.set(least, tempi);
			}
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> ia = Arrays.asList(5,3,6,3,2,6,8,44,22,3,1,2,66,4);
		SelectionSort ss = new SelectionSort(ia);
		System.out.println(ia);
	}

}
