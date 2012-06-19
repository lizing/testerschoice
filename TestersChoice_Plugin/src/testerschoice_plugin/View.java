package testerschoice_plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import CodeGen.ClassSkeleton;
import CodeGen.MethodSkeleton;
import CodeGen.TestCaseTemplate;
import CodeGen.TypeVariable;

public class View extends ViewPart{

	private static final int MAX_TAB_SIZE = 16;
	public static final String ID = "TestersChoice_Plugin.view";
	public static final String FRAMEWORK_FILE_NAME = "ActivityProviderInstrumentationTestCase2.java";
	private Text text_path;
	private Text text_method_name;

	Composite composite;

	String activity_class;
	String provider_class;
	String authority;
	String pkg_name;
	String testcase_class_name;
	String[] methodNames;
	String projectName;
	String projectPath;
	
	Button btn_load, btn_delete_all_methods;
	Combo combo_activity, combo_provider, combo_layout_xml;
	Label lbl_app_name;
	Composite composite_view_list;
	ScrolledComposite mainComposite;
	TabFolder tabFolder;
	TabItem[] tabItem;

	HashMap<String, String> view_property;
	Button[] btn_layout_button;
	Button[] text_layout_Text;
	Label[] label_layout_button;
	Label[] label_layout_Text;
	
	ArrayList<MethodSkeleton> methods = new ArrayList<MethodSkeleton>();
	
	int tabSize = 0;
	int height[];
	int selectCount[];
	static int assertTipFlag = 0;

	HashMap<String, String> xmlHash = new HashMap<String, String>();
	
	private static final String [] assertContents = {
			"static void assertEquals(short expected, short actual)",
			"static void assertEquals(String message, int expected, int actual)",
			"static void assertEquals(String message, short expected, short actual)",
			"static void assertEquals(char expected, char actual)",
			"static void assertEquals(String message, String expected, String actual)",
			"static void assertEquals(int expected, int actual)",
			"static void assertEquals(String message, double expected, double actual, double delta)",
			"static void assertEquals(String message, long expected, long actual)",
			"static void assertEquals(byte expected, byte actual)",
			"static void assertEquals(Object expected, Object actual)",
			"static void assertEquals(boolean expected, boolean actual)",
			"static void assertEquals(String message, float expected, float actual, float delta)",
			"static void assertEquals(String message, boolean expected, boolean actual)",
			"static void assertEquals(String expected, String actual)",
			"static void assertEquals(float expected, float actual, float delta)",
			"static void assertEquals(String message, byte expected, byte actual)",
			"static void assertEquals(double expected, double actual, double delta)",
			"static void assertEquals(String message, char expected, char actual)",
			"static void assertEquals(String message, Object expected, Object actual)",
			"static void assertEquals(long expected, long actual)",
			"static void assertFalse(String message, boolean condition)",
			"static void assertFalse(boolean condition)",
			"static void assertNotNull(String message, Object object)",
			"static void assertNotNull(Object object)",
			"static void assertNotSame(Object expected, Object actual)",
			"static void assertNotSame(String message, Object expected, Object actual)",
			"static void assertNull(Object object)",
			"static void assertNull(String message, Object object)",
			"static void assertSame(Object expected, Object actual)",
			"static void assertSame(String message, Object expected, Object actual)",
			"static void assertTrue(String message, boolean condition)",
			"static void assertTrue(boolean condition)"
	};
	
	private static final String [] toolTipAssertContents = {
			"Asserts that two shorts are equal.",
			"Asserts that two ints are equal.",
			"Asserts that two shorts are equal.",
			"Asserts that two chars are equal.",
			"Asserts that two Strings are equal.",
			"Asserts that two ints are equal.",
			"Asserts that two doubles are equal concerning a delta.",
			"Asserts that two longs are equal.",
			"Asserts that two bytes are equal.",
			"Asserts that two objects are equal.",
			"Asserts that two booleans are equal.",
			"Asserts that two floats are equal concerning a delta.",
			"Asserts that two booleans are equal.",
			"Asserts that two Strings are equal.",
			"Asserts that two floats are equal concerning a delta.",
			"Asserts that two bytes are equal.",
			"Asserts that two doubles are equal concerning a delta.",
			"Asserts that two chars are equal.",
			"Asserts that two objects are equal.",
			"Asserts that two longs are equal.",
			"Asserts that a condition is false.",
			"Asserts that a condition is false.",
			"Asserts that an object isn't null.",
			"sserts that an object isn't null.",
			"Asserts that two objects do not refer to the same object.",
			"Asserts that two objects do not refer to the same object.",
			"Asserts that an object is null.",
			"Asserts that an object is null.",
			"Asserts that two objects refer to the same object.",
			"Asserts that two objects refer to the same object.",
			"Asserts that a condition is true.",
			"Asserts that a condition is true.",
	};
	
	public View() {
		tabItem = new TabItem[MAX_TAB_SIZE];
		methodNames = new String[MAX_TAB_SIZE];
		btn_layout_button = new Button[16];
		text_layout_Text = new Button[32];
		label_layout_button = new Label[16];
		label_layout_Text = new Label[32];
		height = new int[MAX_TAB_SIZE];
		selectCount = new int[32];
		view_property = new HashMap<String, String>();
		for(int i=0; i<16; i++){
			height[i] = 450;
		}
	}
	
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		
		Label lblTargetApplication = new Label(composite, SWT.NONE);
		lblTargetApplication.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblTargetApplication.setBounds(14, 14, 95, 20);
		lblTargetApplication.setText("Target App:");

		Label lblActivity = new Label(composite, SWT.NONE);
		lblActivity.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblActivity.setText("Activity:");
		lblActivity.setBounds(14, 122, 132, 20);

		Label lblProvider = new Label(composite, SWT.NONE);
		lblProvider.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblProvider.setText("ContentProvider:");
		lblProvider.setBounds(14, 148, 132, 20);

		Label lblXml = new Label(composite, SWT.NONE);
		lblXml.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblXml.setText("Layout XML:");
		lblXml.setBounds(14, 185, 95, 20);

		lbl_app_name = new Label(composite, SWT.NONE);
		lbl_app_name.setAlignment(SWT.CENTER);
		lbl_app_name.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lbl_app_name.setBounds(152, 13, 156, 20);
		lbl_app_name.setText("Application");

		combo_activity = new Combo(composite, SWT.NONE);
		combo_activity.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		combo_activity.setBounds(152, 119, 239, 23);

		combo_provider = new Combo(composite, SWT.NONE);
		combo_provider.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		combo_provider.setBounds(152, 145, 239, 23);

		combo_layout_xml = new Combo(composite, SWT.NONE);
		combo_layout_xml.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		combo_layout_xml.setBounds(152, 182, 239, 23);

		Label lblPath = new Label(composite, SWT.NONE);
		lblPath.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblPath.setText("Project Path:");
		lblPath.setBounds(14, 40, 95, 20);

		text_path = new Text(composite, SWT.BORDER);
		text_path.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		text_path.setBounds(152, 37, 239, 21);

		composite_view_list = new Composite(composite, SWT.NONE);
		composite_view_list.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		composite_view_list.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		composite_view_list.setBounds(14, 260, 378, 399);

		Button btn_load = new Button(composite, SWT.NONE);
		btn_load.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_load.setBounds(315, 10, 76, 23);
		btn_load.setText("Browse...");
		btn_load.addSelectionListener(new BrowseButtonListener());

		Button btn_fetch = new Button(composite, SWT.NONE);
		btn_fetch.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_fetch.setText("Fetch View(s)");
		btn_fetch.setBounds(296, 219, 95, 35);
		btn_fetch.addSelectionListener(new FetchButtonListener());

		tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		tabFolder.setBounds(423, 88, 390, 517);

		text_method_name = new Text(composite, SWT.BORDER);
		text_method_name.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		text_method_name.setBounds(530, 14, 201, 20);

		Button btn_add_method = new Button(composite, SWT.NONE);
		btn_add_method.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_add_method.setBounds(737, 14, 76, 20);
		btn_add_method.setText("Create");
		btn_add_method.addSelectionListener(new NewMethodButtonListener());

		Button btn_generate = new Button(composite, SWT.NONE);
		btn_generate.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_generate.setBounds(657, 625, 156, 43);
		btn_generate.setText("Preview Testcase Code");
		btn_generate.addSelectionListener(new GenerateButtonListener());

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
		lblNewLabel.setBounds(14, 98, 56, 18);
		lblNewLabel.setText("Class");

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 90, 381, 2);

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(10, 174, 381, 2);

		Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(10, 6, 381, 2);

		Label label_3 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(10, 211, 381, 2);

		Label label_4 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_4.setBounds(408, 4, 2, 655);

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		lblNewLabel_1.setBounds(423, 14, 113, 20);
		lblNewLabel_1.setText("public void test");

		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblNewLabel_3.setBounds(14, 69, 377, 15);
		lblNewLabel_3.setText("Please Select and Input Information below");

		Label label_6 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_6.setBounds(10, 66, 381, 2);

		Label label_7 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_7.setBounds(423, 6, 390, 2);

		Label label_8 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_8.setBounds(423, 45, 390, 2);

		Label label_9 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_9.setBounds(423, 592, 390, 2);

		Label label_10 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_10.setBounds(423, 675, 390, 2);

		Label label_11 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_11.setBounds(10, 675, 381, 2);
		
		btn_delete_all_methods = new Button(composite, SWT.NONE);
		btn_delete_all_methods.setBounds(681, 54, 132, 28);
		btn_delete_all_methods.setText("Delete All Method(s)");
		btn_delete_all_methods.addSelectionListener(new DeleteMethodsListener());
		
		if(tabSize == 0){
			btn_delete_all_methods.setVisible(false);
		}
	}

	class NewMethodButtonListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			if(tabSize>MAX_TAB_SIZE-1)
			{
				MessageDialog.openWarning(new Shell(), "Warning...", "Can't add any more");
				return;
			}
			if (text_method_name.getText().length() > 0) {
				tabItem[tabSize] = new TabItem(tabFolder, SWT.NONE);
				methods.add(new MethodSkeleton());
				String methodName = text_method_name.getText();
				methods.get(tabSize).setMethodName(methodName);
				tabItem[tabSize].setText(methodName);
				tabFolder.setSelection(tabSize);
				tabSize++;
				btn_delete_all_methods.setVisible(true);
			}else if(text_method_name.getText().length() == 0){
				MessageDialog.openWarning(new Shell(), "Warning...", "Please insert Method name");
			}
			
			text_method_name.setText("");
		}

	}

	class BrowseButtonListener implements SelectionListener {

		File selectedDirectory, project;
		ArrayList<File> files;

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			DirectoryDialog dlg = new DirectoryDialog(new Shell(), SWT.OPEN);
			dlg.setText("Open...");
			dlg.setMessage("Open Target App Project");
			
			projectPath = dlg.open();
			
			
			
			if(projectPath == null){
				return;
			}
			
			selectedDirectory = new File(projectPath);
			project = new File(projectPath + File.separator +"AndroidManifest.xml");
			
			if(!project.exists()){
				MessageDialog.openWarning(new Shell(), "Warning", "Please Select an Android Project");
				return;
			}
			
			repaintCompositeViewList();
			deleteAllMethods();
			text_path.setText("");
			combo_activity.removeAll();
			combo_layout_xml.removeAll();
			combo_provider.removeAll();
			
			text_path.setText(projectPath);
			projectName = selectedDirectory.getName();
			lbl_app_name.setText(projectName);
			
			selectedDirectory = new File(projectPath + File.separator + "src");
			files = new ArrayList<File>();
			visitAllFiles(files, selectedDirectory);

			for (File f : files) {
				String javaFile = f.getName();
				if (javaFile.endsWith(".java")) {
					String superClassName = null;
					try {
						superClassName = getSuperClass(f.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(superClassName != null){
						if(superClassName.equals("Activity"))
							combo_activity.add(javaFile);
						else if(superClassName.equals("ContentProvider"))
							combo_provider.add(javaFile);
						else
							continue;
					}
				}
			}

			selectedDirectory = new File(projectPath + File.separator + "res");
			files = new ArrayList<File>();
			visitAllFiles(files, selectedDirectory);
			for (File f : files) {
				String xmlFile = f.getName();
				if (xmlFile.endsWith(".xml")) {
					xmlHash.put(xmlFile, f.getAbsolutePath());
					combo_layout_xml.add(xmlFile);
				}
			}
			
			if(!project.exists()){
				this.widgetSelected(arg0);
			}
			else
			{
				combo_activity.setText("Success To List Activity Classes");
				combo_provider.setText("Success To List ContentProvider Classes");
				combo_layout_xml.setText("Success To List Layout XML Files");
			}

		}

	}

	class FetchButtonListener implements SelectionListener {
		String text = "";

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			repaintCompositeViewList();
			deleteAllMethods();
			
			activity_class = combo_activity.getText();
			provider_class = combo_provider.getText();
			testcase_class_name = getFileNameWithoutFileExtention(activity_class) + "Test";
			
			if(activity_class == "" || provider_class == "" || combo_activity.getItemCount() == 0 || combo_layout_xml.getItemCount() == 0){
				MessageDialog.openWarning(new Shell(), "Warning", "Please Select a Project First");
				return;
			}
			
			AndroidXmlSaxParser buttonParser = new AndroidXmlSaxParser(xmlHash.get(combo_layout_xml.getText()), "Button");
			AndroidXmlSaxParser editTextParser = new AndroidXmlSaxParser(xmlHash.get(combo_layout_xml.getText()), "EditText");
			AndroidXmlSaxParser manifestParser = new AndroidXmlSaxParser(projectPath + File.separator + "AndroidManifest.xml", "manifest");
			AndroidXmlSaxParser authorityParser = new AndroidXmlSaxParser(projectPath + File.separator + "AndroidManifest.xml", "provider");

			buttonParser.parse();
			editTextParser.parse();
			manifestParser.parse();
			authorityParser.parse();
			
			ArrayList<String> buttonIds = buttonParser.getIdArrayList();
			ArrayList<String> editTextIds = editTextParser.getIdArrayList();
			pkg_name = manifestParser.getPackageName();
			authority = authorityParser.getAuthority();
			
			if(authority.isEmpty() || authority == null){
				MessageDialog.openWarning(new Shell(), "Warning", "Could not find authority");
				return;
			}
			
			Text btnType = new Text(composite_view_list, SWT.PUSH);
			btnType.setBounds(15, 10, 350, 20);
			btnType.setText("< ButtonType >\t\t\t\t\t< EditTextType >");

			for (int i = 0; i < buttonIds.size(); i++) {
				btn_layout_button[i] = new Button(composite_view_list, SWT.PUSH);
				btn_layout_button[i].setBounds(10, 40 + i * 25, 35, 20);
				btn_layout_button[i].setData(new String[]{"Button", buttonIds.get(i)});
				btn_layout_button[i].setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgBtnType.jpg"));
				btn_layout_button[i].addSelectionListener(new ViewSelectedListener());

				label_layout_button[i] = new Label(composite_view_list, SWT.PUSH);
				label_layout_button[i].setBounds(50, 40 + i * 25, 130, 20);
				label_layout_button[i].setText(buttonIds.get(i));
			}
			
			for (int i = 0; i < editTextIds.size(); i++) {
				text_layout_Text[i] = new Button(composite_view_list, SWT.PUSH);
				text_layout_Text[i].setBounds(190, 40 + i * 25, 35, 20);
				text_layout_Text[i].setData(new String[]{"EditText", editTextIds.get(i)});
				text_layout_Text[i].setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgTextType.jpg"));
				text_layout_Text[i].addSelectionListener(new ViewSelectedListener());
				
				label_layout_Text[i] = new Label(composite_view_list, SWT.PUSH);
				label_layout_Text[i].setBounds(230, 40 + i * 25, 130, 20);
				label_layout_Text[i].setText(editTextIds.get(i));
			}
			
			text_method_name.setFocus();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class GenerateButtonListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			if(activity_class == null || provider_class == null){
				//Show a dialog
				return;
			}
			TestCaseTemplate gen = new TestCaseTemplate();
			String activityName = getFileNameWithoutFileExtention(activity_class);
			String providerName = getFileNameWithoutFileExtention(provider_class);

			ClassSkeleton skeleton = new ClassSkeleton(activityName, providerName, authority);
			skeleton.setClassName(testcase_class_name);
			skeleton.setPackageName(pkg_name);
	
			for(int i = 0; i < methods.size(); i++){
				skeleton.setMethod(methods.get(i));
			}
			
			String file = gen.generate(skeleton);
			
			Shell previewShell = new Shell();
			previewShell.setText("Preview");
		
			final StyledText text = new StyledText(previewShell,  SWT.BORDER &SWT.SHELL_TRIM| SWT.V_SCROLL|SWT.H_SCROLL);
			text.setEditable(true);
			text.setText(file);
			text.setBounds(10, 30, 700, 650);
			
			final Table table = new Table(previewShell, SWT.BORDER | SWT.FULL_SELECTION);
			table.setBounds(10, 5, 700, 120);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.setVisible(false);
			table.addListener(SWT.MouseDoubleClick, new Listener(){

				public void handleEvent(Event event) {
					// TODO Auto-generated method stub
					TableItem[] selection = table.getSelection();
					
					Shell s = new Shell();
					boolean isConfirmed = MessageDialog.openConfirm(s,"Warning","Would you like to use this method " + "\n" +"'"+selection[0].getText()+"'"+ "?" +"\n\n"+"Description of selected this assert method: "+"\n"+"\""+toolTipAssertContents[event.index]+"\"");
					if(isConfirmed == true){
						String span = selection[0].getText()+";"+"\n";
						span = span.toString().substring(12, span.toString().length());
						text.replaceTextRange(text.getSelection().y, 0, span);
					}
					else{
						s.close();
					}
				}
			});
			
			for(int i = 0; i < assertContents.length; i++){
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(assertContents[i]);
			}
			
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setWidth(680);
			column.setText("Tips for assert() methods");
			
			Button showTipButton = new Button(previewShell, SWT.NONE);
			showTipButton.setBounds(660, 5, 30, 20);
			showTipButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
			showTipButton.setToolTipText("Show assert() Methods Tip");
			
			
			Button confirmButton = new Button(previewShell, SWT.NONE);
			confirmButton.setText("Generate");
			confirmButton.setBounds(590, 680, 100, 30);
			
			previewShell.setSize(700, 730);
			previewShell.pack();
			previewShell.open();
			
			showTipButton.addSelectionListener(new ShowTooltipListener(text, table, showTipButton));
			confirmButton.addSelectionListener(new ConfirmButtonListener(previewShell, text));
		}

	}
	
	class ConfirmButtonListener implements SelectionListener{

		StyledText text;
		Shell shell;
		
		public ConfirmButtonListener(Shell shell, StyledText text){
			this.text = text;
			this.shell = shell;
		}
		
		
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			if(writeFile(text.getText()))
				shell.close();
		}

		
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class ShowTooltipListener implements SelectionListener{

		StyledText text;
		Table table;
		Button showTip;
		
		public ShowTooltipListener(StyledText text, Table table, Button showTip){
			this.text = text;
			this.table = table;
			this.showTip = showTip;
		}

		
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			if(assertTipFlag % 2 == 0){
				text.setBounds(10, 160, 700, 520);
				//tipView.setVisible(true);
				table.setVisible(true);
				showTip.setBounds(660, 135, 30, 20);
				showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/hideTip.jpg"));
				showTip.setToolTipText("Hide assert() Methods Tip");
			}
			else{
				text.setBounds(10, 30, 700, 650);
				//tipView.setVisible(false);
				table.setVisible(false);
				showTip.setBounds(660, 5, 30, 20);
				showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
				showTip.setToolTipText("Show assert() Methods Tip");
			}
			
			assertTipFlag++;
		}

		
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class ViewSelectedListener implements SelectionListener{

		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			selectCount[tabFolder.getSelectionIndex()]++;
			if(selectCount[tabFolder.getSelectionIndex()]>32)
			{
				MessageDialog.openWarning(new Shell(), "Warning...", "Can't add any more");
				return;
			}
			if(selectCount[tabFolder.getSelectionIndex()]>9){
				
				height[tabFolder.getSelectionIndex()] += 53;
				mainComposite.setMinHeight(height[tabFolder.getSelectionIndex()]);
			}
			
			int tabIndex = tabFolder.getSelectionIndex();
			String[] info = (String[])e.widget.getData();
			String type = info[0];
			String id = info[1];
			
			if(type.equals("Button"))
				methods.get(tabIndex).setVariable(type, id, null);
			else
				methods.get(tabIndex).setVariable(type, id, "");
			
			drawTabFolder();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class DeleteMethodsListener implements SelectionListener{

		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			deleteAllMethods();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class OrderButtonListener implements SelectionListener{

		String type;
		int index;
		
		public OrderButtonListener(String type, int index){
			this.type = type;
			this.index = index;
		}
		
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub			
			TypeVariable temp;
			if(type.equals("UP")){
				temp = methods.get(tabFolder.getSelectionIndex()).getTypeVariables().remove(index);
				methods.get(tabFolder.getSelectionIndex()).getTypeVariables().add(index - 1, temp);
			}else if(type.equals("DOWN")){
				temp = methods.get(tabFolder.getSelectionIndex()).getTypeVariables().remove(index);
				methods.get(tabFolder.getSelectionIndex()).getTypeVariables().add(index + 1, temp);
			}else{
				if(index == 0){
					methods.get(tabFolder.getSelectionIndex()).getTypeVariables().clear();
				}else
					methods.get(tabFolder.getSelectionIndex()).getTypeVariables().remove(index);
			}
			
			drawTabFolder();
		}
		
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class TextWidgetFocusListener implements FocusListener{

		int index;
		Text text;
		String id;
		
		public TextWidgetFocusListener(int index, String id, Text text) {
			this.index = index;
			this.text = text;
			this.id = id;
		}
		
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Text selectedText = (Text)e.getSource();
			//String value = text.getText();
			String value = selectedText.getText();
			TypeVariable tv = new TypeVariable("EditText", id, value);
			methods.get(tabFolder.getSelectionIndex()).getTypeVariables().set(index, tv);
			//methods.get(tabFolder.getSelectionIndex()).getTypeVariables().set(index, new TypeVariable("EditText", id, value));
		}
		
	}

	private boolean writeFile(String text) {
		FileDialog fd = new FileDialog(new Shell(), SWT.SAVE);
		fd.setText("Save...");
		fd.setFilterNames(new String[] { "Java File" });
		fd.setFilterExtensions(new String[] { "*.java" });
		fd.setFileName(getFileNameWithoutFileExtention(activity_class) + "Test" );
		String fileName = fd.open();

		if (fileName == null) {
			return false;
		}

		File file = new File(fileName);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(text);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private String getFileNameWithoutFileExtention(String fileNameWithExtension) {
		String fileName = "";
		int index = fileNameWithExtension.lastIndexOf(".");
		if (index != -1) {
			fileName = fileNameWithExtension.substring(0, index);
		}
		return fileName;
	}
	
	private void visitAllFiles(ArrayList<File> files, File directory) {
		// TODO Auto-generated method stub
		if (directory.isDirectory()) {
			File[] children = directory.listFiles();
			for (File f : children) {
				visitAllFiles(files, f);
			}
		} else {
			files.add(directory);
		}
	}
	
	private void deleteAllMethods(){
		for(int i = tabSize - 1; i >= 0; i--){
			tabItem[i].dispose();
			methods.remove(i);
			tabSize--;
		}
		
		if(tabSize == 0){
			btn_delete_all_methods.setVisible(false);
		}
	}

	public String getSuperClass(String file) throws IOException{
		File f = new File(file);
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(f));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
		
		String superClassName = null;
		String s;
		
		while((s = br.readLine())!=null){
			if(s.contains(getFileNameWithoutFileExtention(f.getName())) && s.contains("class") ){
				if(s.contains("extends")){
					if(s.contains("Activity")){
						superClassName = "Activity";
					}
					else if(s.contains("ContentProvider")){
						superClassName = "ContentProvider";
					}
					else
						return null;
				}
			}
		}
		br.close();
		
		return superClassName;
	}
	
	private void repaintCompositeViewList(){
		composite_view_list.dispose();
		composite_view_list = new Composite(composite, SWT.NONE);
		composite_view_list.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		composite_view_list.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_view_list.setBounds(14, 260, 378, 399);
	}	
	
	private void drawTabFolder(){
		int tabIndex = tabFolder.getSelectionIndex();
		
		if(methods.get(tabIndex).getTypeVariables().isEmpty()){
			tabItem[tabIndex].setControl(null);
			return;
		}
		
		mainComposite = new ScrolledComposite(tabFolder,  SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		mainComposite.setExpandHorizontal(true);
		mainComposite.setExpandVertical(true);
		mainComposite.setMinWidth(350);
		mainComposite.setMinHeight(height[tabFolder.getSelectionIndex()]);
		Composite subComposite = new Composite(mainComposite, SWT.BORDER);
		
		RowLayout mainLayout = new RowLayout();
		mainLayout.wrap = false;
		mainLayout.pack = true;
		mainLayout.type = SWT.VERTICAL;
		//mainComposite.setLayout(mainLayout);
		subComposite.setLayout(mainLayout);
		
		
		int lineLength = methods.get(tabFolder.getSelectionIndex()).getTypeVariables().size();
		
		for(int i = 0; i < lineLength; i++){	
			Composite rowComposite = new Composite(subComposite, SWT.NONE);
			GridLayout gridLayout = new GridLayout(9, false);
			rowComposite.setLayout(gridLayout);
			
			//RowLayout rowLayout = new RowLayout();
			//rowLayout.wrap = true;
			//rowLayout.pack = false;
			//rowComposite.setLayout(rowLayout);
			
			String type = methods.get(tabIndex).getTypeVariables().get(i).getType();
			String id = methods.get(tabIndex).getTypeVariables().get(i).getId();
			String value = methods.get(tabIndex).getTypeVariables().get(i).getValue();
			
			Button typeButton = new Button(rowComposite, SWT.NONE);
			typeButton.setSize(35, 20);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.horizontalSpan = 2;
			typeButton.setLayoutData(gridData);
			
			if(type.equals(MethodSkeleton.BUTTON))
				typeButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgBtnType.jpg"));
			else
				typeButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgTextType.jpg"));
			
			Label idLabel = new Label(rowComposite, SWT.BORDER);
			idLabel.setText(id);
			idLabel.setSize(150, 20);
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.horizontalSpan = 2;
			idLabel.setLayoutData(gridData);
			
			Text valueText = new Text(rowComposite, SWT.BORDER);
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.horizontalSpan = 2;
			
			if(value != null){
				valueText.setSize(150, 20);
				valueText.setLayoutData(gridData);
				valueText.setForeground(valueText.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				valueText.setText(value);
				valueText.addFocusListener(new TextWidgetFocusListener(i, id, valueText));
			}else{
				//valueText.setText("hi");
				valueText.setVisible(false);
			}
			
			Button upButton = new Button(rowComposite, SWT.PUSH);
			Button downButton = new Button(rowComposite, SWT.PUSH);
			Button deleteButton = new Button(rowComposite, SWT.PUSH);
			
			upButton.setSize(35, 20);
			downButton.setSize(35, 20);
			deleteButton.setSize(35, 20);
			
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.horizontalSpan = 1;
		
			upButton.setLayoutData(gridData);
			downButton.setLayoutData(gridData);
			deleteButton.setLayoutData(gridData);
			
			upButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/Up.jpg"));
			downButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/Down.jpg"));
			deleteButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/Del.jpg"));
			
			upButton.addSelectionListener(new OrderButtonListener("UP", i));
			downButton.addSelectionListener(new OrderButtonListener("DOWN", i));
			deleteButton.addSelectionListener(new OrderButtonListener("DELETE", i));
			
			if(i == 0)
				upButton.setVisible(false);
			
			if(i == (lineLength - 1))
				downButton.setVisible(false);
		}
		
		tabItem[tabIndex].setControl(mainComposite);
		mainComposite.setContent(subComposite);
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}