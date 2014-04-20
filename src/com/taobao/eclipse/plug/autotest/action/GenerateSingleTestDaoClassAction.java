/**
 * 
 */
package com.taobao.eclipse.plug.autotest.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.taobao.eclipse.plug.autotest.ui.SingleTestDaoWizardDialog;


/**
 * 
 * @author junyu.wy
 *
 */
public class GenerateSingleTestDaoClassAction implements IObjectActionDelegate {
	
	private IResource javaFile;
    private Shell shell;
	@Override
	public void run(IAction action) {
		try {
			SingleTestDaoWizardDialog testDaoWizardDialog = new SingleTestDaoWizardDialog(javaFile);
			WizardDialog wizardDialog = new WizardDialog(shell, testDaoWizardDialog); 
			wizardDialog.setMinimumPageSize(350, 200);
			wizardDialog.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		if ((selection != null & selection instanceof IStructuredSelection)) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			this.javaFile = ((IResource) strucSelection.getFirstElement());
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

}
