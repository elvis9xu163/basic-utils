package com.xjd.utils.basic.lock;

import java.util.concurrent.locks.Lock;

/**
 * Lock exclusive between different modules, but shared in same module.
 * @author elvis.xu
 * @since 2017-10-13 11:30
 */
public interface ModuleShareLock {

	Lock moduleLock(int index);

	int moduleCount();
}
