package com.taobao.eclipse.plug.autotest.manager;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.taobao.eclipse.plug.autotest.exception.GenException;
import com.taobao.eclipse.plug.autotest.util.VmResourceUtil;
/**
 * @author junyu.wy
 * 
 */
public class DefaultCodeExportManager implements ICodeExportManager{

	protected static VelocityEngine ve = null;


	/**
	 * 根据取得的class信息生成文件内容
	 * @return
	 * @throws GenException
	 */
	@Override
	public String genFileContent(Map<String,?> map) throws GenException {
		String content = null;
		initVelocityEngin();
		try {
			/* next, get the Template */
			Template t = ve.getTemplate(VmResourceUtil.findVmPathByClassInfo(map));

			/* now render the template into a StringWriter */
			StringWriter writer = new StringWriter();
			t.merge(createVeloContext(map), writer);

			content = writer.toString();
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

	private VelocityContext createVeloContext(Map<String,?> mp)throws GenException{

		if(mp==null || !mp.containsKey("testClassName")){
			throw new GenException("mp is null or does not have key : testClassName (sorry)");
		}

		VelocityContext ctx = new VelocityContext();

		//将map中的属性put到context
		java.util.Iterator<String> itr = mp.keySet().iterator();
		Object tmpKey = null;
		while(itr.hasNext()){
			tmpKey = itr.next();
			ctx.put(tmpKey.toString(), mp.get(tmpKey));
		}

		return ctx;
	}

	private static void initVelocityEngin() throws GenException{
		if(ve==null) {
			/* first, get and initialize an engine */
			ve = new VelocityEngine();
			Properties p = new Properties();
			p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			p.setProperty(RuntimeConstants.INPUT_ENCODING, "GBK");
			p.setProperty(RuntimeConstants.OUTPUT_ENCODING, "GBK");
			try {
				ve.init(p);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenException(e);
			}
		}
	}
}
