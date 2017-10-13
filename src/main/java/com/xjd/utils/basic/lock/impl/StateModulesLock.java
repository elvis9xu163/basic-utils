package com.xjd.utils.basic.lock.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import com.xjd.utils.basic.LockUtils;
import com.xjd.utils.basic.lock.ModulesLock;

/**
 * @author elvis.xu
 * @since 2017-10-13 22:56
 */
public class StateModulesLock implements ModulesLock {

	protected ModuleLocker[] moduleLockers;

	public StateModulesLock(int num) {
		num = num < 0 ? 0 : num;
		moduleLockers = new ModuleLocker[num];
		for (int i = 0; i < num; i++) {
			moduleLockers[i] = new ModuleLocker(this, i);
		}
	}

	/** 锁状态: -1-初始态(未被任何模块获得), 0...-模块索引(表示当前正被索引对应模块锁定) */
	protected int state = -1;
	protected Lock stateLock = new ReentrantLock();
	protected Condition stateCondition = stateLock.newCondition();

	@Override
	public Lock moduleLock(int index) {
		return moduleLockers[index];
	}

	@Override
	public int moduleCount() {
		return moduleLockers.length;
	}


	public static class ModuleLocker implements Lock {
		protected StateModulesLock stateModulesLock;
		protected int idState;

		protected Map<Thread, Integer> lockedMap = new ConcurrentHashMap<>();
		protected ReadWriteLock lockedMapLock = new ReentrantReadWriteLock();
		protected Condition lockedMapCondition = lockedMapLock.writeLock().newCondition();

		public ModuleLocker(StateModulesLock stateModulesLock, int idState) {
			this.stateModulesLock = stateModulesLock;
			this.idState = idState;
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
			if (time < 0) time = 0;
			return tryLock(time, unit, true);
		}

		/**
		 * @param time        <0-无限等待, 0-立即返回, >0-等待相应时间
		 * @param unit
		 * @param interrupted
		 * @return
		 */
		protected boolean tryLock(long time, TimeUnit unit, boolean interrupted) throws InterruptedException {
			long end = time > 0 ? System.nanoTime() + unit.toNanos(time) : 0;
			while (true) {
				try (LockUtils.LockResource lr = LockUtils.lock(lockedMapLock.readLock())) {
					if (!lockedMap.isEmpty()) {
						Integer count = lockedMap.get(Thread.currentThread());
						lockedMap.put(Thread.currentThread(), count == null ? 1 : count + 1);
						return true;
					}
				}
				try (LockUtils.LockResource lr = LockUtils.lock(lockedMapLock.writeLock())) {
					if (!lockedMap.isEmpty()) {
						Integer count = lockedMap.get(Thread.currentThread());
						lockedMap.put(Thread.currentThread(), count == null ? 1 : count + 1);
						return true;
					}
					// lockedMap为空的情况
					try (LockUtils.LockResource lr2 = LockUtils.lock(stateModulesLock.stateLock)) {
						if (stateModulesLock.state == -1) { // 锁未被其它模块获得
							stateModulesLock.state = idState; // 获得锁
						}
						if (stateModulesLock.state == idState) { // 当前模块得到锁
							Integer count = lockedMap.get(Thread.currentThread());
							lockedMap.put(Thread.currentThread(), count == null ? 1 : count + 1);
							return true;
						} else { // 当前模块未得到锁
							if (time < 0) { // 无限等待
								if (interrupted) {
									stateModulesLock.stateCondition.await();
								} else {
									stateModulesLock.stateCondition.awaitUninterruptibly();
								}
							} else if (time == 0) { // 立即返回
								return false;

							} else { // 等待时间
								while (true) {
									try {
										boolean awaitSuccess = stateModulesLock.stateCondition.await(end - System.nanoTime(), TimeUnit.NANOSECONDS);
										if (!awaitSuccess) { // 时间耗尽
											return false;
										} else { // 有通知，再尝试
											break;
										}
									} catch (InterruptedException e) {
										if (interrupted) throw e;
									}
								}
							}
						}
					}
				}
			}
		}


		@Override
		public void unlock() {
			Integer count = lockedMap.get(Thread.currentThread());
			if (count == null) throw new IllegalMonitorStateException();
			if (count > 1) {
				lockedMap.put(Thread.currentThread(), count - 1);
				return;
			}
			// count == 1
			lockedMap.remove(Thread.currentThread());
			if (!lockedMap.isEmpty()) return;
			// empty
			try (LockUtils.LockResource lr = LockUtils.lock(lockedMapLock.writeLock())) {
				if (lockedMap.isEmpty()) {
					try (LockUtils.LockResource lr2 = LockUtils.lock(stateModulesLock.stateLock)) {
						if (lockedMap.isEmpty() && stateModulesLock.state == idState) {
							stateModulesLock.state = -1; // 改回初始态
							stateModulesLock.stateCondition.signalAll(); // 通知所有人来抢锁, 坏淫啊
						}
					}
				}
			}
		}

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}
	}
}
