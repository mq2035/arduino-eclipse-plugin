package it.baeyens.arduino.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import io.sloeber.core.api.BoardDescriptor;
import io.sloeber.core.api.CodeDescriptor;
import it.baeyens.arduino.common.Common;
import it.baeyens.arduino.common.Const;

/**
 * This class is the class related to the new arduino sketch
 * 
 * @author Jan Baeyens
 * 
 */
public class NewSketchWizard extends Wizard implements INewWizard, IExecutableExtension {
    private WizardNewProjectCreationPage mWizardPage;
    protected NewSketchWizardBoardPage mArduinoPage;
    protected NewSketchWizardCodeSelectionPage mNewArduinoSketchWizardCodeSelectionPage;
    private BuildConfigurationsPage mBuildCfgPage;
    private IConfigurationElement mConfig;
    private IProject mProject;

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
	String PlatformPath = NewSketchWizard.this.mArduinoPage.getBoardID().getPlatformPath().toString();
	NewSketchWizard.this.mNewArduinoSketchWizardCodeSelectionPage.setPlatformPath(PlatformPath);
	return super.getNextPage(page);
    }

    public NewSketchWizard() {
	super();
    }

    @Override
    /**
     * adds pages to the wizard. We are using the standard project wizard of
     * Eclipse
     */
    public void addPages() {
	//
	// We assume everything is OK as it is tested in the handler
	// create each page and fill in the title and description
	// first page to fill in the project name
	//
	this.mWizardPage = new WizardNewProjectCreationPage(Messages.ui_new_sketch_title);
	this.mWizardPage.setDescription(Messages.ui_new_sketch_title_help);
	this.mWizardPage.setTitle(Messages.ui_new_sketch_title); // $NON-NLS-1$
	//
	// settings for Arduino board etc
	//
	this.mArduinoPage = new NewSketchWizardBoardPage(Messages.ui_new_sketch_arduino_information);
	this.mArduinoPage.setTitle(Messages.ui_new_sketch_arduino_information_help);
	this.mArduinoPage.setDescription(Messages.ui_new_sketch_these_settings_cn_be_changed_later);
	//
	// settings for template file location
	//
	this.mNewArduinoSketchWizardCodeSelectionPage = new NewSketchWizardCodeSelectionPage(
		Messages.ui_new_sketch_sketch_template_location);
	this.mNewArduinoSketchWizardCodeSelectionPage.setTitle(Messages.ui_new_sketch_sketch_template_folder);
	this.mNewArduinoSketchWizardCodeSelectionPage
		.setDescription(Messages.ui_new_sketch_error_folder_must_contain_sketch_cpp);
	//
	// configuration page but I haven't seen it
	//
	this.mBuildCfgPage = new BuildConfigurationsPage(Messages.ui_new_sketch_build_configurations);
	this.mBuildCfgPage.setTitle(Messages.ui_new_sketch_Select_additional_configurations);
	this.mBuildCfgPage.setDescription(Messages.ui_new_sketch_Select_additional_configurations_help);
	//
	// actually add the pages to the wizard
	//
	addPage(this.mWizardPage);
	addPage(this.mArduinoPage);
	addPage(this.mNewArduinoSketchWizardCodeSelectionPage);
	addPage(this.mBuildCfgPage);

    }

    @Override
    public boolean performFinish() {
	if (this.mProject != null) {
	    return true;
	}

	WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
	    @Override
	    protected void execute(IProgressMonitor monitor) throws CoreException {
		createProjectWrapper(monitor);
	    }
	};

	try {
	    getContainer().run(false, true, op);
	} catch (InvocationTargetException | InterruptedException e) {
	    Common.log(new Status(IStatus.ERROR, Const.CORE_PLUGIN_ID,
		    Messages.ui_new_sketch_error_failed_to_create_project, e));
	    return false;
	}

	if (this.mProject == null) {
	    return false;
	}

	// so now we set Eclipse to the right perspective and switch to our just
	// created project

	BasicNewProjectResourceWizard.updatePerspective(this.mConfig);
	IWorkbenchWindow TheWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	BasicNewResourceWizard.selectAndReveal(this.mProject, TheWindow);

	return true;
    }

    protected void createProjectWrapper(IProgressMonitor monitor) {

	BoardDescriptor boardID = this.mArduinoPage.getBoardID();
	CodeDescriptor codeDescription = this.mNewArduinoSketchWizardCodeSelectionPage.getCodeDescription();
	try {
	    this.mProject = boardID.createProject(this.mWizardPage.getProjectName(),
		    (!this.mWizardPage.useDefaults()) ? this.mWizardPage.getLocationURI() : null,
		    this.mBuildCfgPage.getBuildConfigurationDescriptors(), codeDescription, monitor);

	} catch (Exception e) {
	    this.mProject = null;
	    Common.log(new Status(IStatus.ERROR, Const.CORE_PLUGIN_ID,
		    Messages.ui_new_sketch_error_failed_to_create_project, e));
	}
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
	// nothing to do here
    }

    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
	    throws CoreException {
	this.mConfig = config;

    }

}
