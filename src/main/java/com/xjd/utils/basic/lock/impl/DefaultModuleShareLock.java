package com.xjd.utils.basic.lock.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.xjd.utils.basic.LockUtils;
import com.xjd.utils.basic.lock.ModuleShareLock;

/**
 * @author elvis.xu
 * @since 2017-10-13 09:59
 */
public class DefaultModuleShareLock implements ModuleShareLock {
	ReentrantLock lock;
	Locker[] moduleLocks;

	public DefaultModuleShareLock(int count) {
		lock = new ReentrantLock();
		moduleLocks = new Locker[count];
		for (int i = 0; i < count; i++) {
			moduleLocks[i] = new Locker(lock);
		}
	}

	@Override
	public Lock moduleLock(int index) {
		return moduleLocks[index];
	}

	@Override
	public int moduleCount() {
		return moduleLocks.length;
	}


	public static class Locker implements Lock {
		Lock lock;
		Thread lockThread;

		Lock internalLock = new ReentrantLock();
		Condition conditionInner = internalLock.newCondition();
		Condition conditionOuter = internalLock.newCondition();
		volatile int lockStatus = 0; // 0-not lock, 1-locking, 2-locked
		Map<Thread, Integer> threadLockCountMap = new ConcurrentHashMap<>();

		public Locker(Lock lock) {
			this.lock = lock;
			lockThread = new Thread() {
				@Override
				public void run() {
					doRun();
				}
			};
			lockThread.setDaemon(true);
			lockThread.start();
		}

		protected void doRun() {
			while (true) {
				try (LockUtils.LockResource lr = LockUtils.lock(internalLock)) {
					if (threadLockCountMap.isEmpty()) {
						if (lockStatus == 2) {
							lock.unlock();
							lockStatus = 0;
						} else if (lockStatus == 1) {
							lockStatus = 0;
						}
					} else {
						if (lockStatus == 0) {
							lockStatus = 1;
						} else if (lockStatus == 2) {
							conditionOuter.signalAll();
						}
					}
					if (lockStatus != 1) {
						try {
							conditionInner.await();
						} catch (InterruptedException e) {
						}
					}
				}
				if (lockStatus == 1) {
					try {
						lock.lockInterruptibly();
						lockStatus = 2;
					} catch (InterruptedException e) {
						lockStatus = 0;
					}
				}
			}
		}


		@Override
		public void lock() {
			try {
				tryLock(-1L, TimeUnit.NANOSECONDS, false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			tryLock(-1L, TimeUnit.NANOSECONDS, true);
		}

		@Override
		public boolean tryLock() {
			try {
				return tryLock(0L, TimeUnit.NANOSECONDS, false);
			} catch (InterruptedException e) {
				// impossible
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			return tryLock(time, unit, true);
		}

		/**
		 * @param time        <0-无限等待, 0-立即返回, >0-等待相应时间
		 * @param unit
		 * @param interrupted
		 * @return
		 */
		protected boolean tryLock(long time, TimeUnit unit, boolean interrupted) throws InterruptedException {
			Integer integer = threadLockCountMap.get(Thread.currentThread());
			if (integer != null) { // 说明上一次已经获得锁了
				threadLockCountMap.put(Thread.currentThread(), integer++);
				return true;

			} else { // 尚未取得锁
				threadLockCountMap.put(Thread.currentThread(), 1); // 先放一个进去表示需求
				try (LockUtils.LockResource lr = LockUtils.lock(internalLock)) {
					conditionInner.signalAll(); // 一定通知一下去获取哦
				}
				if (lockStatus == 2) return true; // 正锁则呢，直接返回

				Thread.yield(); // 让一让路
				try (LockUtils.LockResource lr = LockUtils.lock(internalLock)) {
					if (lockStatus == 2) return true; // 正锁则呢，直接返回
					long start = System.nanoTime();
					while (true) {
						try {
							if (time < 0) {
								conditionOuter.await();
								break;
							} else if (time == 0) {
								conditionOuter.awaitNanos(0L);
								break;
							} else {
								long remain = unit.toNanos(time) - (System.nanoTime() - start);
								if (remain <= 0) {
									break;
								}
								conditionOuter.awaitNanos(remain);
								break;
							}
						} catch (InterruptedException e) {
							if (interrupted) {
								threadLockCountMap.remove(Thread.currentThread());
								throw e;
							}
						}
					}
					if (lockStatus == 2) {
						return true;
					} else {
						threadLockCountMap.remove(Thread.currentThread());
						conditionInner.signalAll(); // 里面就通知
						lockThread.interrupt(); // 外面就中断一下
						return false;
					}
				}

			}
		}

		@Override
		public void unlock() {
			Integer integer = threadLockCountMap.get(Thread.currentThread());
			if (integer == null) throw new IllegalMonitorStateException();
			if (integer == 1) {
				threadLockCountMap.remove(Thread.currentThread());
				try (LockUtils.LockResource lr = LockUtils.lock(internalLock)) {
					conditionInner.signalAll(); // 里面就通知
					lockThread.interrupt(); // 外面就中断一下
				}
			} else {
				threadLockCountMap.put(Thread.currentThread(), integer - 1);
			}
		}

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}
	}

}
