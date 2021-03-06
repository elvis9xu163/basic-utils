package com.xjd.utils.basic.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.xjd.utils.basic.lock.impl.StateABLock;

/**
 * @author elvis.xu
 * @since 2017-10-13 11:43
 */
public class ABLockExample2 {
	public static void main(String[] args) throws InterruptedException {
//		final ABLock abLock = new ThreadABLock();
		final ABLock abLock = new StateABLock();

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

			boolean f = lock.tryLock();
			if (f) {
				System.out.println(name + ": locked");
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lock.unlock();
				System.out.println(name + ": unlocked");
			} else {
				System.out.println(name + ": lock fail");
			}

		}
	}
}