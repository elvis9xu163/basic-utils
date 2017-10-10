package com.xjd.utils.basic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author elvis.xu
 * @since 2017-10-10 19:08
 */
public abstract class LockUtils {
	public static final LockResource NULL_LOCK_RESOURCE = new LockResource(null);

	public static LockResource lock(Lock lock) {
		if (lock == null) return NULL_LOCK_RESOURCE;
		lock.lock();
		return new LockResource(lock);
	}

	public static LockResource lockInterruptibly(Lock lock) throws InterruptedException {
		if (lock == null) return NULL_LOCK_RESOURCE;
		lock.lockInterruptibly();
		return new LockResource(lock);
	}

	public static LockResource tryLock(Lock lock) {
		if (lock == null) return NULL_LOCK_RESOURCE;
		if (!lock.tryLock()) {
			return NULL_LOCK_RESOURCE;
		}
		return new LockResource(lock);
	}

	public static LockResource tryLock(Lock lock, long time, TimeUnit unit) throws InterruptedException {
		if (lock == null) return NULL_LOCK_RESOURCE;
		if (!lock.tryLock(time, unit)) {
			return NULL_LOCK_RESOURCE;
		}
		return new LockResource(lock);
	}

	public static class LockResource implements AutoCloseable {
		protected Lock lock;

		protected LockResource(Lock lock) {
			this.lock = lock;
		}

		public boolean isLocked() {
			return lock != null;
		}

		@Override
		public void close() {
			if (lock != null) {
				lock.unlock();
			}
		}
	}
}
