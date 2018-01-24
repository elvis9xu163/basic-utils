package com.xjd.utils.basic;

import org.fest.assertions.api.Assertions;

/**
 * @author elvis.xu
 * @since 2018-01-17 15:22
 */
public class TernaryUtilsExample {
	public static void main(String[] args) {
		Assertions.assertThat(TernaryUtils.of("").$if(StringUtils::isNotBlank).$else()).isEqualTo(null);
		Assertions.assertThat(TernaryUtils.of("").$if(StringUtils::isNotBlank).$else("TTT")).isEqualTo("TTT");
		Assertions.assertThat(TernaryUtils.of("XXX").$if(StringUtils::isNotBlank).$else()).isEqualTo("XXX");
		Assertions.assertThat(TernaryUtils.of("XXX").$if(StringUtils::isNotBlank).$then("YYY").$else()).isEqualTo
				("YYY");
		Assertions.assertThat(TernaryUtils.of("XXX").$if(StringUtils::isNotBlank).$then(t -> {
			System.out.println(t);
		}).$else(t -> {System.out.println("ELSE");})).isEqualTo(null);

		Assertions.assertThat(TernaryUtils.$if(StringUtils.isNotBlank("")).$else("NULL")).isEqualTo("NULL");
	}
}
