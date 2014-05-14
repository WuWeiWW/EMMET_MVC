package com.emmet.core.beans.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 读取资源的内部接口
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public interface Resource {

	InputStream getInputStream() throws IOException;
}
