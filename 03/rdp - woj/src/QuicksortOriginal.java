import java.util.Random;

public class QuicksortOriginal{
	private int number;
	
	private static int[] numbers;
	private final static int SIZE = 90000000;
	private final static int MAX = 9000000;
	
	public static void setUp() throws Exception {
		numbers = new int[SIZE];
		Random generator = new Random();
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = generator.nextInt(MAX);
		}
	}
	
	public static void main(String[] args) {
		try {
			setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		new QuicksortOriginal().sort(numbers);
	}
	
	
	public void sort(int[] values) {
		// check for empty or null array
		if (values ==null || values.length==0){
			return;
		}
		numbers = values;
		number = values.length;
		quicksort(0, number - 1);
	}
	
	private void quicksort(int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		int pivot = numbers[low + (high-low)/2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (numbers[i] < pivot) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (numbers[j] > pivot) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private void exchange(int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}
} 
