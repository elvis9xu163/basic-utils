package com.xjd.utils.basic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author elvis.xu
 * @since 2017-10-10 19:23
 */
public class LockUtilsExample {
	public static void main(String[] args) {
		final Lock lock = new ReentrantLock();
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

		try (LockUtils.LockResource r = LockUtils.lock(lock)) {
			System.out.println("lock");
		}

		try (LockUtils.LockResource r = LockUtils.tryLock(readWriteLock.readLock())) {
			if (r.isLocked()) {
				System.out.println("tryLock");
			}
		}

		try (LockUtils.LockResource r = LockUtils.tryLock(readWriteLock.readLock(), 1L, TimeUnit.SECONDS)) {
			if (r.isLocked()) {
				System.out.println("tryLock");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}