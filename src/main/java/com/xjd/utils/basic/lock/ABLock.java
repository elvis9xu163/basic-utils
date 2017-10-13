package com.xjd.utils.basic.lock;

import java.util.concurrent.locks.Lock;

/**
 * @author elvis.xu
 * @since 2017-10-13 09:57
 */
public interface ABLock {
	Lock lockA();
	Lock lockB();
}
