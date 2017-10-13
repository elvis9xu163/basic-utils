package com.xjd.utils.basic.lock.impl;

import java.util.concurrent.locks.Lock;

import com.xjd.utils.basic.lock.ABLock;

/**
 * @author elvis.xu
 * @since 2017-10-13 09:59
 */
public class ThreadABLock implements ABLock {
	ThreadModulesLock moduleShareLock;

	public ThreadABLock() {
		moduleShareLock = new ThreadModulesLock(2);
	}

	@Override
	public Lock lockA() {
		return moduleShareLock.moduleLock(0);
	}

	@Override
	public Lock lockB() {
		return moduleShareLock.moduleLock(1);
	}
}
