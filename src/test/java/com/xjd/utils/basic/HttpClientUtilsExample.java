package com.xjd.utils.basic;

import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author elvis.xu
 * @since 2017-11-14 17:15
 */
public class HttpClientUtilsExample {
	public static void main(String[] args) {
//		get();
//		formPost();
//		multipartFormPost();
		postJson();
	}

	public static void get() {
		{
			HttpResponse response = HttpClientUtils.get("http://hq.sinajs.cn/list=sh601006");
			System.out.println(HttpClientUtils.responseToString(response));
		}
		{
			HttpResponse response = HttpClientUtils.get("http://www.baidu.com/s",
					Arrays.asList(new BasicNameValuePair("wd", "x20")));
			System.out.println(HttpClientUtils.responseToString(response));
		}
	}

	public static void formPost() {
		{
			HttpResponse response = HttpClientUtils.post("http://www.baidu.com/s",
					Arrays.asList(new BasicNameValuePair("wd", "x20")), true);
			System.out.println(HttpClientUtils.responseToString(response));
		}
	}

	public static void multipartFormPost() {
		{
			HttpResponse response = HttpClientUtils.post("http://www.baidu.com/s",
					Arrays.asList(new BasicNameValuePair("wd", "x20")),
					Arrays.asList(FormBodyPartBuilder.create("file", new ByteArrayBody("xxx".getBytes(), "中文")).build()));
			System.out.println(HttpClientUtils.responseToString(response));
		}
	}

	public static void postJson() {
		HttpClientUtils.setDefaultHttpClientWithTrustAllSSL();
		HttpResponse post = HttpClientUtils.post("https://api.wozai4u.com/manage/resource/api/verifycode", null);
		System.out.println(HttpClientUtils.responseToString(post));
	}

	public static void postText() {

	}
}