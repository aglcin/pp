import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Bathroom2 {
	ConcurrentLinkedQueue<Thread> waitingQueue;
	int FEMALE = 1;
	int MALE = 2;
	int EMPTY = 3;
	int currentGender;
	int total;
	Object monitor;

	public Bathroom2() {
		waitingQueue = new ConcurrentLinkedQueue<Thread>();
		currentGender = EMPTY;
	}

	void enterGender(int gender) {
		Thread t = Thread.currentThread();
		waitingQueue.add(t);

		synchronized(monitor) {
			while (waitingQueue.peek() != t) {
				try {
					monitor.wait();
				} catch (Exception e) {}
			}

			while ((currentGender & gender) == 0) {
				try {
					monitor.wait();
				} catch (Exception e) {}
			}

			currentGender = gender;
			waitingQueue.poll();
			++total;
		}
	}

	void leaveGender(int gender) {
		if (currentGender != gender) {
			System.out.println("invalid state!\n");
		}

		synchronized(monitor) {
			--total;
			if (total == 0) {
				currentGender = EMPTY;
				monitor.notifyAll();
			}
		}
	}

	void enterMale() {
		enterGender(MALE);
		System.out.println("male joined\n");
	}

	void enterFemale() {
		enterGender(FEMALE);
		System.out.println("female joined\n");
	}

	void leaveMale() {
		System.out.println("male left\n");
		leaveGender(MALE);
	}

	void leaveFemale() {
		System.out.println("female left\n");
		leaveGender(FEMALE);
	}

	class Male extends Thread {
	    @Override
	    public void run() {
	    	enterMale();
	    	// brush teeth
	    	try {
	    		Thread.sleep(1500);
	    	} catch (Exception e) {}
	    	leaveMale();
	    }
	}

	class Female extends Thread {
	    @Override
	    public void run() {
	    	enterFemale();
	    	// brush teeth
	    	try {
	    		Thread.sleep(3000);
	    	} catch (Exception e) {}
	    	leaveFemale();
	    }
	}

	Male getMale() {
		return new Male();
	}

	Female getFemale() {
		return new Female();
	}

	public static void main(String[] args) throws InterruptedException, NoSuchMethodException {
		int nthreads = Integer.parseInt(args[0]);
		Random rand = new Random();
		Bathroom bathroom = new Bathroom();
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < nthreads; ++i) {
			Thread t;
			int k = rand.nextInt(2);
			if (k == 0) {
				t = bathroom.getMale();
			} else {
				t = bathroom.getFemale();
			}
			threads.add(t);
		}
		for (int i = 0; i < nthreads; ++i) {
			threads.get(i).start();
		}
	}
}