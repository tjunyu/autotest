package com.taobao.eclipse.plug.autotest.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.taobao.eclipse.plug.autotest.Generate.Generater;
import com.taobao.eclipse.plug.autotest.util.DataUtils;

/**
 * @author junyu.wy
 * 
 */
public class GenerateBatchTestDAOWizardPage extends WizardPage {

	public GenerateBatchTestDAOWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setMessage("java�������� �Զ����ɹ��ߣ�ֻ��Ҫ�ṩjavaԴĿ¼�������������Ŀ¼�Ϳ��Ե�");
	}

	private String daoDir;

	private String testDaoDir;

	private String daoPackge;

	private String testDaoPackge;

	private IProject daoPproject;

	private IProject testDdaoPproject;

	private List<String> files;

	private Table talbe;

	private String baseTestClass;
	// private Label labelProject;
	@Override
	public void createControl(Composite parent) {
		GridData gd = null;
		GridLayout layout = null;

		Composite composite = new Composite(parent, SWT.NULL);
		layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		setControl(composite);
		initializeDialogUnits(composite);

		final Composite compositeCommon = new Composite(composite, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 4;
		compositeCommon.setLayout(layout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		compositeCommon.setLayoutData(gd);

		Label label = new Label(compositeCommon, SWT.NONE);
		label.setText("*");
		Display display = Display.getCurrent();
		label.setForeground(display.getSystemColor(3));

		label = new Label(compositeCommon, SWT.NONE);
		label.setText("javaԴ·��:");

		final Text sourcePackage = new Text(compositeCommon, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 0;
		gd.heightHint = SWT.DEFAULT;
		gd.horizontalSpan = 1;
		sourcePackage.setLayoutData(gd);

		Button daoJavaPackage = new Button(compositeCommon, SWT.NONE);
		daoJavaPackage.setText(" Browse... ");
		daoJavaPackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IPath path = null;
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(null, null, false, "�����Ŀ��ѡ��javaԴ·��");
				dialog.setTitle("javaԴ·��ѡ��");
				dialog.showClosedProjects(false);
				if (dialog.open() == Window.OK) {
					Object[] results = dialog.getResult();
					if ((results != null) && (results.length > 0) && (results[0] instanceof IPath)) {
						path = ((IPath) results[0]).makeRelative();
						String fullPath = path.toString();
						String projectName = fullPath.split("/")[0];
						daoPproject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
						String realPath = daoPproject.getLocation().toString();
						realPath = realPath + "/" + path.removeFirstSegments(1).toString();
						sourcePackage.setText(path.removeFirstSegments(1).toString());
						daoDir = realPath;
						daoPackge = DataUtils.filter1(path.removeFirstSegments(1).toString().replaceAll("/", "."));
					}
				}
			}
		});

		label = new Label(compositeCommon, SWT.NONE);
		label.setText("*");
		label.setForeground(display.getSystemColor(3));

		label = new Label(compositeCommon, SWT.NONE);
		label.setText("Test�������·��:");

		final Text textTargetPath = new Text(compositeCommon, SWT.BORDER);
		textTargetPath.setLayoutData(gd);

		Button testDaoJavaPackage = new Button(compositeCommon, SWT.NONE);
		testDaoJavaPackage.setText(" Browse... ");
		testDaoJavaPackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IPath path = null;
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(null, null, false, "�����Ŀ��ѡ��Test���·��");
				dialog.setTitle("Test���·��");
				dialog.showClosedProjects(false);
				if (dialog.open() == Window.OK) {
					Object[] results = dialog.getResult();
					if ((results != null) && (results.length > 0) && (results[0] instanceof IPath)) {
						path = ((IPath) results[0]).makeRelative();
						String fullPath = path.toString();
						String projectName = fullPath.split("/")[0];
						testDdaoPproject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
						String realPath = testDdaoPproject.getLocation().toString();
						realPath = realPath + "/" + path.removeFirstSegments(1).toString();
						textTargetPath.setText(path.removeFirstSegments(1).toString());
						testDaoDir = realPath;
						testDaoPackge = DataUtils.filter1(path.removeFirstSegments(1).toString().replaceAll("/", "."));
					}
				}
			}
		});

		label = new Label(compositeCommon, SWT.NONE);
		final Button isNeedBase = new Button(compositeCommon, SWT.CHECK);
		isNeedBase.setText("��������java��:");
		final Text baseClass = new Text(compositeCommon, SWT.BORDER);
		baseClass.setLayoutData(gd);
		Button browse = new Button(compositeCommon, SWT.NONE);
		browse.setText(" Browse... ");
		browse.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IType type = DataUtils.chooseClass(getShell(), null, null);
				if (type != null)
					baseClass.setText(type.getFullyQualifiedName());
			}
		});

		label = new Label(compositeCommon, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		label.setLayoutData(gd);

		label = new Label(compositeCommon, SWT.NONE);
		label.setLayoutData(gd);
		label = new Label(compositeCommon, SWT.NONE);
		label.setLayoutData(gd);
		label = new Label(compositeCommon, SWT.NONE);
		label.setLayoutData(gd);
		label = new Label(compositeCommon, SWT.NONE);
		label = new Label(compositeCommon, SWT.NONE);
		label = new Label(compositeCommon, SWT.NONE);
		Button excute = new Button(compositeCommon, SWT.NONE);
		excute.setText("���ɲ�������");
		label = new Label(compositeCommon, SWT.NONE);
		label.setLayoutData(gd);
		label = new Label(compositeCommon, SWT.NONE);
		label.setLayoutData(gd);
		label = new Label(compositeCommon, SWT.NONE);
		label.setLayoutData(gd);
		label = new Label(compositeCommon, SWT.NONE);
		label.setText("*");
		Label name = new Label(compositeCommon, SWT.NONE);
		name.setText("�����������ڹ�������");
		final Text textProject = new Text(compositeCommon, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 0;
		gd.heightHint = SWT.DEFAULT;
		gd.horizontalSpan = 1;
		textProject.setLayoutData(gd);
		textProject.setEditable(false);
		label = new Label(compositeCommon, SWT.NONE);
		talbe = new Table(compositeCommon, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gd.horizontalSpan = 4;
		talbe.setLayoutData(gd);
		talbe.setHeaderVisible(true);
		talbe.setLinesVisible(true);
		TableColumn column = new TableColumn(talbe, SWT.NULL);
		column.setText("�����ļ��б�");
		column.setWidth(480);
		excute.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!StringUtils.isNotEmpty(sourcePackage.getText())) {
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�쳣����", "����дjavaԴ");
					return;
				}
				if (!StringUtils.isNotEmpty(textTargetPath.getText())) {
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�쳣����", "����д�����������·��");
					return;
				}
				if (isNeedBase.getSelection()) {
					baseTestClass = baseClass.getText();
				}
				ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
				try {
					progress.run(true, false, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
							monitor.beginTask("��������test�ļ�", IProgressMonitor.UNKNOWN);
							monitor.setTaskName("��������test�ļ�");
							try {
							Generater generater = new Generater();
							files = generater.exportBatchTestCase(daoDir, daoPackge, daoPproject, testDaoDir, testDaoPackge, testDdaoPproject, baseTestClass);
							
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										initTable();
									}
								});
							} catch (Exception e) {
								MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�쳣����", e.getMessage());
							}
							monitor.done();
						}

					});
					textProject.setText(testDdaoPproject.getName());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initTable() {
		talbe.removeAll();
		for (String file : files) {
			TableItem item = new TableItem(talbe, SWT.NULL);
			item.setText(new String[] { file });
		}
	}
}
