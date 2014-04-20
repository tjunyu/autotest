package com.taobao.eclipse.plug.autotest.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.Wizard;
/**
 * @author junyu.wy
 * 
 */
public class SingleTestDaoWizardDialog extends Wizard{
	
	private IResource resource;
	
	public SingleTestDaoWizardDialog(IResource resource){
		this.resource = resource;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPage(new GenerateSingleTestDAOWizardPage("自动生成单个java文件测试用例",resource));
	}
}
