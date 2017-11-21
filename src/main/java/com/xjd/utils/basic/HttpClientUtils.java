package com.xjd.utils.basic;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * HttpClient 工具类, 依赖httpclient
 *
 * @author elvis.xu
 * @since 2017-11-14 15:48
 */
public abstract class HttpClientUtils {
	public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public static HttpClient DEFAULT_HTTP_CLIENT = HttpClients.createDefault();


	public static HttpResponse get(String url, List<NameValuePair> params) {
		return get(url, params, null);
	}

	public static HttpResponse get(String url, List<NameValuePair> params, Charset charset) {
		return get(DEFAULT_HTTP_CLIENT, url, params, charset);
	}

	public static HttpResponse get(HttpClient httpClient, String url, List<NameValuePair> params, Charset charset) {
		charset = charset == null ? DEFAULT_CHARSET : charset;
		String paramString = null;
		if (params != null && !params.isEmpty()) {
			paramString = URLEncodedUtils.format(params, charset);
		}
		return get(httpClient, url, paramString);
	}

	public static HttpResponse get(String url) {
		return get(url, (String) null);
	}

	public static HttpResponse get(String url, String paramString) {
		return get(DEFAULT_HTTP_CLIENT, url, paramString);
	}

	public static HttpResponse get(HttpClient httpClient, String url, String paramString) {
		AssertUtils.assertArgumentNonBlank(url, "url cannot be blank");
		url = StringUtils.trim(url);
		httpClient = httpClient == null ? DEFAULT_HTTP_CLIENT : httpClient;

		paramString = StringUtils.trimToNull(paramString);
		if (paramString != null) {
			int idx = url.indexOf('?');
			if (idx == -1) { // 没有?
				url += "?";

			} else if (idx != url.length() - 1) { // 有且不是最后一个字符, 说明有参数
				url += "&";
			}
			url += paramString;
		}

		HttpGet httpGet = new HttpGet(url);
		try {
			return httpClient.execute(httpGet);
		} catch (IOException e) {
			throw ExceptionUtils.runtime(e);
		}
	}

	public static HttpResponse post(String url, List<NameValuePair> params) {
		return post(DEFAULT_HTTP_CLIENT, url, params, DEFAULT_CHARSET, false);
	}

	public static HttpResponse post(String url, List<NameValuePair> params, boolean multipart) {
		return post(DEFAULT_HTTP_CLIENT, url, params, DEFAULT_CHARSET, multipart);
	}

	public static HttpResponse post(String url, List<NameValuePair> params, Charset charset, boolean multipart) {
		return post(DEFAULT_HTTP_CLIENT, url, params, charset, multipart);
	}

	public static HttpResponse post(HttpClient httpClient, String url, List<NameValuePair> params, Charset charset, boolean multipart) {
		return post(httpClient, url, params, charset, null, multipart ? ContentType.MULTIPART_FORM_DATA : ContentType.APPLICATION_FORM_URLENCODED);
	}

	public static HttpResponse post(String url, List<NameValuePair> params, List<FormBodyPart> multiData) {
		return post(DEFAULT_HTTP_CLIENT, url, params, DEFAULT_CHARSET, multiData);
	}

	public static HttpResponse post(String url, List<NameValuePair> params, Charset charset, List<FormBodyPart> multiData) {
		return post(DEFAULT_HTTP_CLIENT, url, params, charset, multiData);
	}

	public static HttpResponse post(HttpClient httpClient, String url, List<NameValuePair> params, Charset charset, List<FormBodyPart> multiData) {
		return post(httpClient, url, params, charset, multiData, ContentType.MULTIPART_FORM_DATA);
	}

	protected static HttpResponse post(HttpClient httpClient, String url, List<NameValuePair> params, Charset charset, List<FormBodyPart> multiData, ContentType contentType) {
		charset = charset == null ? DEFAULT_CHARSET : charset;
		HttpEntity httpEntity = null;
		if (contentType == ContentType.MULTIPART_FORM_DATA || contentType.getMimeType().startsWith("multipart")) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create() // 注意解决中文乱码
					.setLaxMode() // 使用浏览器兼容模式, 否则中文会乱码
					.setContentType(ContentType.MULTIPART_FORM_DATA.withCharset(charset));// 解决中文乱码推荐UTF8

			if (params != null && !params.isEmpty()) {
				for (NameValuePair p : params) {
					builder.addTextBody(p.getName(), p.getValue(), ContentType.DEFAULT_TEXT.withCharset(charset));
				}
			}

			if (multiData != null && !multiData.isEmpty()) {
				multiData.forEach(d -> {
					builder.addPart(d);
				});
			}

			httpEntity = builder.build();

		} else {
			params = params == null ? new ArrayList<>(0) : params;
			httpEntity = new UrlEncodedFormEntity(params, charset);
		}

		return post(httpClient, url, httpEntity);
	}

	public static HttpResponse postPlainText(String url, String body, Charset charset) {
		charset = charset == null ? DEFAULT_CHARSET : charset;
		return post(DEFAULT_HTTP_CLIENT, url, body, ContentType.TEXT_PLAIN.withCharset(charset));
	}

	public static HttpResponse postJson(String url, String body, Charset charset) {
		charset = charset == null ? DEFAULT_CHARSET : charset;
		return post(DEFAULT_HTTP_CLIENT, url, body, ContentType.APPLICATION_JSON.withCharset(charset));
	}

	public static HttpResponse post(String url, String body, ContentType contentType) {
		StringEntity httpEntity = new StringEntity(body, contentType);
		return post(DEFAULT_HTTP_CLIENT, url, httpEntity);
	}

	public static HttpResponse post(HttpClient httpClient, String url, String body, ContentType contentType) {
		StringEntity httpEntity = new StringEntity(body, contentType);
		return post(httpClient, url, httpEntity);
	}

	public static HttpResponse post(HttpClient httpClient, String url, HttpEntity httpEntity) {
		AssertUtils.assertArgumentNonBlank(url, "url cannot be blank");
		url = StringUtils.trim(url);
		httpClient = httpClient == null ? DEFAULT_HTTP_CLIENT : httpClient;

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(httpEntity);
		try {
			return httpClient.execute(httpPost);
		} catch (IOException e) {
			throw ExceptionUtils.runtime(e);
		}
	}

	public static boolean responseOk(HttpResponse response) {
		return response == null ? false : response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
	}

	public static byte[] responseToBytes(HttpResponse response) {
		try {
			return EntityUtils.toByteArray(response.getEntity());
		} catch (IOException e) {
			throw ExceptionUtils.runtime(e);
		}
	}

	public static String responseToString(HttpResponse response) {
		return responseToString(response, DEFAULT_CHARSET);
	}

	public static String responseToString(HttpResponse response, Charset charset) {
		charset = charset == null ? DEFAULT_CHARSET : charset;
		try {
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			throw ExceptionUtils.runtime(e);
		}
	}

	public static <T> T responseToJson(HttpResponse response, Class<T> clazz) {
		return responseToJson(response, DEFAULT_CHARSET, clazz);
	}

	public static <T> T responseToJson(HttpResponse response, Charset charset, Class<T> clazz) {
		charset = charset == null ? DEFAULT_CHARSET : charset;
		try {
			String s = EntityUtils.toString(response.getEntity(), charset);
			return JsonUtils.fromJson(s, clazz);
		} catch (IOException e) {
			throw ExceptionUtils.runtime(e);
		}
	}
}
