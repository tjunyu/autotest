package com.taobao.eclipse.plug.autotest.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * @author junyu.wy
 * 
 */
public class DataUtils {

	private static final char PACKAGE_SEPARATOR = '/';
	private static final char TYPE_SEPARATOR = '$';

	public static String getResolvedType(String typeToResolve, IType declaringType) throws JavaModelException {
		StringBuffer sb = new StringBuffer();
		int arrayCount = Signature.getArrayCount(typeToResolve);
		boolean isPrimitive = isPrimitiveType(typeToResolve.charAt(arrayCount));
		if (isPrimitive) {
			sb.append(Signature.getSignatureSimpleName(typeToResolve));
		} else {
			boolean isUnresolvedType = isUnresolvedType(typeToResolve, arrayCount);
			if (!isUnresolvedType) {
				sb.append(typeToResolve);
			} else {
				String resolved = getResolvedTypeName(typeToResolve, declaringType);
				if (resolved != null) {
					while (arrayCount > 0) {
						sb.append(Signature.C_ARRAY);
						arrayCount--;
					}
					resolved = Signature.getElementType(resolved);
					resolved = resolved.replaceAll("/", ".");
					sb.append(resolved);
					// System.out.println(Signature.getElementType(resolved));
				}
			}
		}
		return sb.toString();
	}

	private static boolean isPrimitiveType(char first) {
		return (first != Signature.C_RESOLVED && first != Signature.C_UNRESOLVED);
	}

	private static boolean isUnresolvedType(String refTypeSig, int arrayCount) {
		char type = refTypeSig.charAt(arrayCount);
		return type == Signature.C_UNRESOLVED;
	}

	private static String getResolvedTypeName(String refTypeSig, IType declaringType) throws JavaModelException {
		int arrayCount = Signature.getArrayCount(refTypeSig);
		if (isUnresolvedType(refTypeSig, arrayCount)) {
			String name = ""; //$NON-NLS-1$
			int bracket = refTypeSig.indexOf(Signature.C_GENERIC_START, arrayCount + 1);
			if (bracket > 0) {
				name = refTypeSig.substring(arrayCount + 1, bracket);
			} else {
				int semi = refTypeSig.indexOf(Signature.C_SEMICOLON, arrayCount + 1);
				if (semi == -1) {
					throw new IllegalArgumentException();
				}
				name = refTypeSig.substring(arrayCount + 1, semi);
			}
			String[][] resolvedNames = declaringType.resolveType(name);
			if (resolvedNames != null && resolvedNames.length > 0) {
				return concatenateName(resolvedNames[0][0], resolvedNames[0][1]);
			}
			return null;
		}
		return refTypeSig.substring(arrayCount);// Signature.toString(substring);
	}

	private static String concatenateName(String packageName, String className) {
		StringBuffer buf = new StringBuffer();
		if (packageName != null && packageName.length() > 0) {
			packageName = packageName.replace(Signature.C_DOT, PACKAGE_SEPARATOR);
			buf.append(packageName);
		}
		if (className != null && className.length() > 0) {
			if (buf.length() > 0) {
				buf.append(PACKAGE_SEPARATOR);
			}
			className = className.replace(Signature.C_DOT, TYPE_SEPARATOR);
			buf.append(className);
		}
		return buf.toString();
	}
	
	public static void refreshFolder( IResource resource, IProject project){
		try{
			resource.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (Exception e) {
		}
	}
	
	public static String filter1(String url){
		if(url.contains("src.main.java.")){
			url = url.replace("src.main.java.", "");
		} else if(url.contains("src.test.java.")){
			url = url.replace("src.test.java.", "");
		} else if (url.contains("src.")){
			url = url.replace("src.", "");
		}
		return url;
	}
	
	public static String filter2(String url){
		if(url.contains("src/main/java/")){
			url = url.replace("src/main/java/", "");
		} else if(url.contains("src/test/java/")){
			url = url.replace("src/test/java/", "");
		} else if (url.contains("src/")){
			url = url.replace("src/", "");
		}
		return url;
	}
	public static IType chooseClass(Shell shell, IRunnableContext context, IProject project) {
		IProject[] pros = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		IJavaElement[] elements = new IJavaElement[pros.length]; // {
		for (int i = 0; i < elements.length; i++) {
			elements[i] = JavaCore.create(pros[i]);
		}
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
		FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(shell, false, context, scope, IJavaSearchConstants.TYPE);
		dialog.setTitle("Ñ¡ÔñBaseTestClass");
		dialog.setMessage("BaseTestClass");
		if (Window.OK == dialog.open()) {
			Object obj = dialog.getFirstResult();
			IType type = (IType) obj;
			return type;
		}
		return null;
	}
}
