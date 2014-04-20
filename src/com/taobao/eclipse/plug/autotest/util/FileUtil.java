package com.taobao.eclipse.plug.autotest.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.jmerge.JControlModel;
import org.eclipse.emf.codegen.jmerge.JMerger;
import org.eclipse.emf.common.util.URI;

import com.taobao.eclipse.plug.autotest.Activator;

/**
 * @author junyu.wy
 * 
 */
public class FileUtil {

	public static void createFile(File dir, String fileName, String content) throws IOException {

		if (!dir.exists()) {
			dir.mkdirs();
		}
		dir = new File(dir, fileName);
		if (dir.exists()) {
			content = merge(content, dir);
			dir.delete();
		}
		dir.createNewFile();

		FileWriter fw = new FileWriter(dir);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.flush();
		bw.close();
		fw.close();
	}

	private static String merge(String generated, File targetFie) throws FileNotFoundException {
		JMerger merger = getJMerger();
		merger.setSourceCompilationUnit(merger.createCompilationUnitForContents(generated));
		merger.setTargetCompilationUnit(merger.createCompilationUnitForInputStream(new FileInputStream(targetFie)));
		merger.merge();
		return merger.getTargetCompilationUnit().getContents();
	}

	private static JMerger getJMerger() { // Platform.getPlugin(Activator.PLUGIN_ID).getDescriptor().getInstallURL().toString()
	// Platform.getPlugin(Activator.PLUGIN_ID).getStateLocation().toString();
	// Platform.getBundle(Activator.PLUGIN_ID).getResource("com/taobao/eclipse/plug/autotest/resource/merge.xml").getPath()
		;
		String uri = URI.createPlatformPluginURI(Activator.PLUGIN_ID, true).toString();
		uri += "/resources/merge.xml";
		JMerger jmerger = new JMerger();
		JControlModel controlModel = new JControlModel(uri);
		jmerger.setControlModel(controlModel);
		return jmerger;
	}
}
