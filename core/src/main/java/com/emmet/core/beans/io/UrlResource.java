package com.emmet.core.beans.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL方式读取资源文件
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class UrlResource implements Resource {


	private final URL url;
	public UrlResource(URL resource) {
		this.url = resource;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		URLConnection urlConnection = url.openConnection();
		urlConnection.connect();
		return urlConnection.getInputStream();
	}
}
