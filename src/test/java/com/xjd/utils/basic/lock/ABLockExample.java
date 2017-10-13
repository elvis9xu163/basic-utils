package com.xjd.utils.basic.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import com.xjd.utils.basic.lock.impl.DefaultABLock;

/**
 * @author elvis.xu
 * @since 2017-10-13 11:43
 */
public class ABLockExample {
	public static void main(String[] args) throws InterruptedException {
		final ABLock abLock = new DefaultABLock();

		List<LockThread> list =new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			if (i <= 3) {
				list.add(new LockThread("A" + i, abLock.lockA()));
			} else {
				list.add(new LockThread("B" + i, abLock.lockB()));
			}
		}

		for (LockThread lockThread : list) {
			lockThread.start();
		}


		for (LockThread lockThread : list) {
			lockThread.join();
		}
	}

	public static class LockThread extends Thread {
		String name;
		Lock lock;

		public LockThread(String name, Lock lock) {
			this.name = name;
			this.lock = lock;
		}

		@Override
		public void run() {

			lock.lock();
			System.out.println(name + " lock");

			try {
				long time = (new Random().nextInt(3) + 1) * 1000L;
				System.out.println(name + " sleep " + time);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			lock.unlock();
			System.out.println(name + " unlock");

//			boolean f = lock.tryLock();
//			System.out.println(name + " lock");
//			if (f) {
//				lock.unlock();
//				System.out.println(name + " unlock");
//			}

		}
	}
}