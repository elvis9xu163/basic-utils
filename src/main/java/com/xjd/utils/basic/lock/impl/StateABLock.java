package com.xjd.utils.basic.lock.impl;

import java.util.concurrent.locks.Lock;

import com.xjd.utils.basic.lock.ABLock;

/**
 * @author elvis.xu
 * @since 2017-10-13 09:59
 */
public class StateABLock implements ABLock {
	StateModulesLock stateModulesLock;

	public StateABLock() {
		stateModulesLock = new StateModulesLock(2);
	}

	@Override
	public Lock lockA() {
		return stateModulesLock.moduleLock(0);
	}

	@Override
	public Lock lockB() {
		return stateModulesLock.moduleLock(1);
	}
}
