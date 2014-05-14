package com.emmet.core.io;

import com.emmet.core.beans.io.ResourceLoader;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * 测试资源是否加载成功
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class ResourceLoaderTest {


	@Test
	public void testGetResource() throws Exception {
		ResourceLoader resourceLoader = new ResourceLoader();
		assertNotNull(resourceLoader.getResource("beans.xml"));
	}
}
