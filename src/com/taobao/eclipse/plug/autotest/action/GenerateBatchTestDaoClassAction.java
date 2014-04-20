/**
 * 
 */
package com.taobao.eclipse.plug.autotest.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.taobao.eclipse.plug.autotest.ui.BatchTestDaoWizardDialog;

/**
 * 
 * @author junyu.wy
 *
 */
public class GenerateBatchTestDaoClassAction implements IWorkbenchWindowActionDelegate {
	
	private Shell shell;

	@Override
	public void run(IAction action) {
		try {
			BatchTestDaoWizardDialog testDaoWizardDialog = new BatchTestDaoWizardDialog();
			WizardDialog wizardDialog = new WizardDialog(shell, testDaoWizardDialog); 
			wizardDialog.setMinimumPageSize(350, 200);
			wizardDialog.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IWorkbenchWindow window) {
		shell = window.getShell();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
