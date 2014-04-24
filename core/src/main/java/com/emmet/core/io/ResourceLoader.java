package com.emmet.core.io;

import java.io.IOException;
import java.net.URL;

/**
 * 获取资源并加载
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class ResourceLoader{

	public UrlResource getResource(String location) throws IOException {
		URL resource = this.getClass().getClassLoader().getResource(location);
		return new UrlResource(resource);
	}
}
