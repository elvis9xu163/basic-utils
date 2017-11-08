package com.xjd.utils.basic.lock.impl;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.basic.lock.ReserveLock;

/**
 * @author elvis.xu
 * @since 2017-11-08 11:42
 */
public class DefaultReserveLock implements ReserveLock {
	ConcurrentLinkedQueue<CountDownLatch> queue = new ConcurrentLinkedQueue<>();

	@Override
	public Voucher reserve() {
		CountDownLatch latch = new CountDownLatch(1);
		queue.add(latch);
		queue.peek().countDown(); // 多次countdown也没关系的
		InnerVoucher voucher = new InnerVoucher(this, latch);
		return voucher;
	}

	@Override
	public void close(Voucher voucher) {
		AssertUtils.assertArgumentNonNull(voucher, "voucher cannot be null");
		if (!(voucher instanceof InnerVoucher)) throw new IllegalArgumentException("unrecognized voucher class: " + voucher.getClass());

		CountDownLatch latch = ((InnerVoucher) voucher).latch;
		if (queue.remove(latch)) { // 有变化
			CountDownLatch first = queue.peek();
			if (first != null) {
				first.countDown(); // 多次countdown也没关系的
			}
		}
	}

	public static class InnerVoucher implements Voucher {
		protected DefaultReserveLock reserveLock;
		protected CountDownLatch latch;

		public InnerVoucher(DefaultReserveLock reserveLock, CountDownLatch latch) {
			this.reserveLock = reserveLock;
			this.latch = latch;
		}

		@Override
		public void await() {
			while (true) {
				try {
					awaitInterruptedly();
					break;
				} catch (InterruptedException e) {
				}
			}
		}

		@Override
		public boolean await(long timeout, TimeUnit unit) {
			while (true) {
				try {
					return awaitInterruptedly(timeout, unit);
				} catch (InterruptedException e) {
				}
			}
		}

		@Override
		public void awaitInterruptedly() throws InterruptedException {
			latch.await();
		}

		@Override
		public boolean awaitInterruptedly(long timeout, TimeUnit unit) throws InterruptedException {
			return latch.await(timeout, unit);
		}

		@Override
		public void close() {
			reserveLock.close(this);
		}
	}
}
