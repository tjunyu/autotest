package com.taobao.eclipse.plug.autotest.ui;

import org.eclipse.jface.wizard.Wizard;
/**
 * @author junyu.wy
 * 
 */
public class BatchTestDaoWizardDialog extends Wizard{

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPage(new GenerateBatchTestDAOWizardPage("�Զ���������java�ļ���������"));
	}
}
