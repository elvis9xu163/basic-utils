package com.xjd.utils.basic.lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.xjd.utils.basic.lock.impl.DefaultReserveLock;

/**
 * @author elvis.xu
 * @since 2017-11-08 12:07
 */
public class ReserveLockTest {
	@Test
	public void reserve() throws Exception {

		List<ReserveLock.Voucher> list = new ArrayList<>();
		ReserveLock reserveLock = new DefaultReserveLock();

		for (int i = 0; i < 3; i++) {
			list.add(reserveLock.reserve());
		}
		Collections.reverse(list);

		for (int i = 0; i < list.size(); i++) {
			final int b = i;
			new Thread(() -> {
				try (ReserveLock.Voucher voucher = list.get(b)) {
					System.out.println("开始: " + b);
					voucher.await();
					System.out.println("结束: " + b);
				}
			}, "" + i).start();
		}

		Thread.sleep(5000L);

	}

}