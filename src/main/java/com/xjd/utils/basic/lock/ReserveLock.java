package com.xjd.utils.basic.lock;

import java.util.concurrent.TimeUnit;

/**
 * reservation lock. e.g.
 * <pre>
 * In one place:
 *    Voucher voucher = reserveLock.reserve();
 *
 * In another place(may be another thread):
 *    try {
 *        voucher.await();
 *        // Do things
 *    } finally {
 *        voucher.close()
 *        // or reserveLock.close(voucher)
 *    }
 * </pre>
 *
 * @author elvis.xu
 * @since 2017-11-08 11:27
 */
public interface ReserveLock {

	Voucher reserve();

	void close(Voucher voucher);

	public static interface Voucher extends AutoCloseable {
		public void await();

		public boolean await(long timeout, TimeUnit unit);

		public void awaitInterruptedly() throws InterruptedException;

		public boolean awaitInterruptedly(long timeout, TimeUnit unit) throws InterruptedException;

		@Override
		void close();
	}
}
