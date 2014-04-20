package com.taobao.eclipse.plug.autotest.config;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.taobao.eclipse.plug.autotest.exception.ConfigException;

/**
 * @author junyu.wy
 * 
 */
public class Configure {
	private int type = -99;
	public final static int TYPE_UNDIFINED = -99;

	private String packageStr;
	private String outputdir;

	private File packageDir = null;
	private File outputDir = null;

	private String packge; // 包名，项目下可以找到的，可通过配置文件修改 重要
	private String testPackge; // 输出文
	private IProject testProject;
	private IJavaProject javaProject;
	private String javaFile;

	public Configure(String dir, String packge, IProject project, String testDir, String testPackge, IProject testProject) throws CoreException {
		this.packageStr = dir;
		this.outputdir = testDir;
		this.packge = packge;
		this.testPackge = testPackge;
		this.testProject = testProject;
		javaProject = (IJavaProject) project.getNature("org.eclipse.jdt.core.javanature");
	}

	public Configure(String javaFile, IProject project, String testDir, String testPackge, IProject testProject) throws CoreException {
		this.javaFile = javaFile;
		this.outputdir = testDir;
		this.testPackge = testPackge;
		this.testProject = testProject;
		javaProject = (IJavaProject) project.getNature("org.eclipse.jdt.core.javanature");
	}

	public void load() throws ConfigException {
		outputDir = new File(outputdir);
		if (!outputDir.isDirectory()) {
//			System.out.println("outputDir : " + outputDir.getAbsolutePath() + " is not a dir,please check again");
			throw new ConfigException("outputDir : " + outputDir.getAbsolutePath() + " is not a dir,please check again");
		}
		if (packageStr != null)
			packageDir = new File(packageStr);
	}

	public String getPackageStr() {
		return packageStr;
	}

	public File getPackageDir() {
		return packageDir;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public int getType() {
		return type;
	}

	public String getDaoPackge() {
		return packge;
	}

	public String getTestDaoPackge() {
		return testPackge;
	}

	public String getRootUrl() {
		String daopk = packge.replaceAll("\\.", "/");
		return packageStr.replace(daopk, "");
	}

	public IType getType(String className) throws JavaModelException {
		return javaProject.findType(className);
	}

	public void refreshFolder() {
		try {
			testProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "刷新工程异常提醒", e.getMessage());
		}
	}

	public String getJavaFile() {
		return javaFile;
	}
}
