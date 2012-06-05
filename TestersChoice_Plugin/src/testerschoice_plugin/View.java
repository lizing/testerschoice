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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
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

public class View extends ViewPart {
	public View() {
	}

	private static final int MAX_TAB_SIZE = 16;
	private static final int MAX_METHOD_LINE = 32;
	public static final String ID = "TestersChoice_Plugin.view";
	private Text text_path;
	private Text text_method_name;

	/*
	 * ACTIVITY_CLASS, PROVIDER_CLASS, AUTHORITY);
	 * skeleton.setClassName(TESTCASE_CLASS_NAME); skeleton.setPackageName(PKG);
	 */

	String activity_class;
	String provider_class;
	String authority;
	String pkg_name;
	String testcase_class_name;
	String[] methodNames;
	String projectName;
	String projectPath;
	
	Button btn_load;
	Combo combo_activity, combo_provider, combo_layout_xml;
	Label lbl_app_name;
	Composite composite_view_list;
	TabFolder tabFolder;
	TabItem[] tabItem;
	Composite[] tabComposite;
	

	static int[] tabFolderViewHeight;

	HashMap<String, String> view_property;
	Button[] btn_layout_button;
	Button[] text_layout_Text;
	Label[] label_layout_button;
	Label[] label_layout_Text;

	String[][] secuential_id;
	String[][] sequential_input_value;
	
	ArrayList<MethodSkeleton> methods = new ArrayList<MethodSkeleton>(); 
	// MethodSkeleton[] methods = new MethodSkeleton[MAX_TAB_SIZE];
	
	int tabSize = 0;
	static int assertTipFlag = 0;

	// ArrayList<TypeVariable> typeVariable;

	HashMap<String, String> xmlHash = new HashMap<String, String>();
	private Text text_authority;
	
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
		
		Composite composite = new Composite(parent, SWT.NONE);
		tabItem = new TabItem[MAX_TAB_SIZE];
		tabFolderViewHeight = new int[MAX_TAB_SIZE];
		tabComposite = new Composite[MAX_TAB_SIZE];
		methodNames = new String[MAX_TAB_SIZE];
		btn_layout_button = new Button[16];
		text_layout_Text = new Button[32];
		label_layout_button = new Label[16];
		label_layout_Text = new Label[32];
		view_property = new HashMap<String, String>();
		
		sequential_input_value = new String[MAX_TAB_SIZE][32];
		secuential_id = new String[MAX_TAB_SIZE][32];
		
		
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
		composite_view_list.setBounds(14, 293, 378, 366);

		Button btn_load = new Button(composite, SWT.NONE);
		btn_load.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_load.setBounds(315, 10, 76, 23);
		btn_load.setText("Browse...");
		btn_load.addSelectionListener(new BrowseButtonListener());

		Button btn_fetch = new Button(composite, SWT.NONE);
		btn_fetch.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_fetch.setText("Fetch View(s)");
		btn_fetch.setBounds(296, 252, 95, 35);
		btn_fetch.addSelectionListener(new FetchButtonListener());

		tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		tabFolder.setBounds(423, 69, 359, 517);

		text_method_name = new Text(composite, SWT.BORDER);
		text_method_name.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		text_method_name.setBounds(526, 14, 174, 20);

		Button btn_add_method = new Button(composite, SWT.NONE);
		btn_add_method.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_add_method.setBounds(706, 14, 76, 20);
		btn_add_method.setText("Create");
		btn_add_method.addSelectionListener(new NewMethodButtonListener());

		Button btn_generate = new Button(composite, SWT.NONE);
		btn_generate.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		btn_generate.setBounds(626, 626, 156, 43);
		btn_generate.setText("Preview Testcase Code");

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
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblNewLabel_1.setBounds(423, 14, 95, 20);
		lblNewLabel_1.setText("Method Name:");

		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblNewLabel_2.setBounds(14, 221, 56, 15);
		lblNewLabel_2.setText("Authority:");

		text_authority = new Text(composite, SWT.BORDER);
		text_authority.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		text_authority.setBounds(152, 219, 239, 20);

		Label label_5 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 242, 381, 2);

		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		lblNewLabel_3.setBounds(14, 69, 377, 15);
		lblNewLabel_3.setText("Please Select and Input Information below");

		Label label_6 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_6.setBounds(10, 66, 381, 2);

		Label label_7 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_7.setBounds(423, 6, 362, 2);

		Label label_8 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_8.setBounds(423, 45, 362, 2);

		Label label_9 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_9.setBounds(423, 592, 362, 2);

		Label label_10 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_10.setBounds(423, 675, 362, 2);

		Label label_11 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_11.setBounds(10, 675, 381, 2);
		btn_generate.addSelectionListener(new GenerateButtonListener());

	}

	class NewMethodButtonListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

			if (text_method_name.getText().length() > 0) {
				tabItem[tabSize] = new TabItem(tabFolder, SWT.NONE);
				methods.add(new MethodSkeleton());
				//tabComposite[tabSize] = new Composite(tabFolder, SWT.PUSH);
				//tabItem[tabSize].setControl(tabComposite[tabSize]);
				methodNames[tabSize] = text_method_name.getText();
				tabItem[tabSize].setText(methodNames[tabSize]);
				tabSize++;
			}
			text_method_name.setText("");
		}

	}

	class BrowseButtonListener implements SelectionListener {

		File selected_directory, validated_android_project;
		ArrayList<File> files;

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			combo_activity.removeAll();
			combo_layout_xml.removeAll();
			combo_provider.removeAll();

			DirectoryDialog dlg = new DirectoryDialog(new Shell(), SWT.OPEN);
			dlg.setText("Open...");
			dlg.setMessage("Open Target App Project");
			projectPath = dlg.open();
			
			if(projectPath == null){
				return;
			}
			
			text_path.setText(projectPath);
			

			selected_directory = new File(projectPath);
			validated_android_project = new File(projectPath + "\\AndroidManifest.xml");
			
			projectName = selected_directory.getName();
			lbl_app_name.setText(projectName);
			
			if(!validated_android_project.exists()){
				MessageDialog.openWarning(new Shell(), "Warning", "Please Select an Android Project");
				this.widgetSelected(arg0);
			}
			
			selected_directory = new File(projectPath + "//src");
			files = new ArrayList<File>();
			visitAllFiles(files, selected_directory);

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
			combo_activity.setText("Success To List Activity Classes");
			combo_provider.setText("Success To List ContentProvider Classes");

			selected_directory = new File(projectPath + "//res");
			files = new ArrayList<File>();
			visitAllFiles(files, selected_directory);
			for (File f : files) {
				String xmlFile = f.getName();
				if (xmlFile.endsWith(".xml")) {
					xmlHash.put(xmlFile, f.getAbsolutePath());
					combo_layout_xml.add(xmlFile);
				}
			}
			combo_layout_xml.setText("Success To List Layout XML Files");
		}

	}

	class FetchButtonListener implements SelectionListener {
		String text = "";
		int height = 0;

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
			activity_class = combo_activity.getText();
			provider_class = combo_provider.getText();
			authority = text_authority.getText();
			testcase_class_name = getFileNameWithoutFileExtention(activity_class) + "Test";
			
			if(activity_class == "" || provider_class == "" || authority == "" || combo_activity.getItemCount() == 0 || combo_layout_xml.getItemCount() == 0){
				MessageDialog.openWarning(new Shell(), "Warning", "Please Select a Project First");
				btn_load.setFocus();
				return;
			}
			
			AndroidXmlSaxParser buttonParser = new AndroidXmlSaxParser(xmlHash.get(combo_layout_xml.getText()), "Button");
			AndroidXmlSaxParser editTextParser = new AndroidXmlSaxParser(xmlHash.get(combo_layout_xml.getText()), "EditText");
			AndroidXmlSaxParser manifestParser = new AndroidXmlSaxParser(projectPath + "\\AndroidManifest.xml", "manifest");

			buttonParser.parse();
			editTextParser.parse();
			manifestParser.parse();
			
			ArrayList<String> Button_id = buttonParser.getIdArrayList();
			ArrayList<String> EditText_id = editTextParser.getIdArrayList();
			pkg_name = manifestParser.getPackageName();
			
			Text btnType = new Text(composite_view_list, SWT.PUSH);
			btnType.setBounds(15, 10, 350, 20);
			btnType.setText("< ButtonType >\t\t\t\t\t< EditTextType >");

			for (int i = 0; i < Button_id.size(); i++) {
				btn_layout_button[i] = new Button(composite_view_list, SWT.PUSH);
				btn_layout_button[i].setBounds(10, 40 + i * 25, 35, 20);
				//btn_layout_button[i].setData(Button_id.get(i));
				btn_layout_button[i].setData(new String[]{"Button", Button_id.get(i)});
				btn_layout_button[i].setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgBtnType.jpg"));
				btn_layout_button[i].addSelectionListener(new ViewSelectedListener());

				label_layout_button[i] = new Label(composite_view_list, SWT.PUSH);
				label_layout_button[i].setBounds(50, 40 + i * 25, 130, 20);
				label_layout_button[i].setText(Button_id.get(i));
			}
			
			for (int i = 0; i < EditText_id.size(); i++) {
				text_layout_Text[i] = new Button(composite_view_list, SWT.PUSH);
				text_layout_Text[i].setBounds(190, 40 + i * 25, 35, 20);
				//text_layout_Text[i].setData(EditText_id.get(i));
				text_layout_Text[i].setData(new String[]{"EditText", EditText_id.get(i)});
				text_layout_Text[i].setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgTextType.jpg"));
				text_layout_Text[i].addSelectionListener(new ViewSelectedListener());
				label_layout_Text[i] = new Label(composite_view_list, SWT.PUSH);
				label_layout_Text[i].setBounds(230, 40 + i * 25, 130, 20);
				label_layout_Text[i].setText(EditText_id.get(i));
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
			TestCaseTemplate gen = new TestCaseTemplate();
			String activity_name = getFileNameWithoutFileExtention(activity_class);
			String provider_name = getFileNameWithoutFileExtention(provider_class);

			ClassSkeleton skeleton = new ClassSkeleton(activity_name, provider_name, authority);
			skeleton.setClassName(testcase_class_name);
			skeleton.setPackageName(pkg_name);
	
			for(int i = 0; i < methods.size(); i++){
				methods.get(i).setMethodName(methodNames[i]);
				skeleton.setMethod(methods.get(i));
			}
			
			/*
			for(int i=0; i<tabSize; i++){
				MethodSkeleton method = new MethodSkeleton();
				method.setMethodName(methodNames[i]);
				for (int j = 0; j < tabFolderViewHeight[i]; j++) {
					String s = sequential_input_value[i][j];
					if (s != null)
						method.setVariable(MethodSkeleton.EDIT_TEXT, secuential_id[i][j], sequential_input_value[i][j]);
					else
						method.setVariable(MethodSkeleton.BUTTON, secuential_id[i][j], null);
				}
				skeleton.setMethod(method);
			}
			*/
			String file = gen.generate(skeleton);
			
			String [] assertContents = {
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
			
			String [] toolTipAssertContents = {
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
			
			Shell previewShell = new Shell();
			previewShell.setText("Preview");
		
			
			final Table table = new Table(previewShell, SWT.BORDER | SWT.FULL_SELECTION);
			table.setBounds(10, 5, 700, 120);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.setVisible(false);
			TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
			tblclmnNewColumn.setWidth(680);
			tblclmnNewColumn.setText("Assert Methods Tip...");

			for(int i=0; i<assertContents.length; i++){
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(assertContents[i]);
			}
			
			final StyledText text = new StyledText(previewShell,  SWT.BORDER &SWT.SHELL_TRIM| SWT.V_SCROLL|SWT.H_SCROLL);
			text.setEditable(true);
			text.setText(file);
			text.setBounds(10, 30, 700, 650);
			final Button showTip = new Button(previewShell, SWT.NONE);
			showTip.setBounds(660, 5, 30, 20);
			showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
			showTip.setToolTipText(" \"assert()\" Tooltip C ");
			
			
			Button confirm = new Button(previewShell, SWT.NONE);
			confirm.setText("Generate");
			confirm.setBounds(590, 680, 100, 30);
			
			
			previewShell.setSize(700, 730);
			previewShell.pack();
			previewShell.open();
			
			showTip.addSelectionListener(new ShowTooltipListener(text, table, showTip));
			confirm.addSelectionListener(new ConfirmButtonListener(previewShell, text));
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
				showTip.setToolTipText(" \"assert()\" Tooltip A");
			}
			else{
				text.setBounds(10, 30, 700, 650);
				//tipView.setVisible(false);
				table.setVisible(false);
				showTip.setBounds(660, 5, 30, 20);
				showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
				showTip.setToolTipText(" \"assert()\" Tooltip B");
			}
			
			assertTipFlag++;
		}

		
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class ViewSelectedListener implements SelectionListener{

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			/*
			MethodSkeleton method = new MethodSkeleton();
			String[] info = (String[])e.widget.getData();
			String type = info[0];
			String id = info[1];
			if(type == null)
				method.setVariable(type, id, null);
			else
				method.setVariable(type, id, "");
			methods.add(method);
			drawTabFolder();
			*/
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

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void drawTabFolder(){
		int tabIndex = tabFolder.getSelectionIndex();		
		Composite mainComposite = new Composite(tabFolder, SWT.NONE);
		
		RowLayout mainLayout = new RowLayout();
		mainLayout.wrap = true;
		mainLayout.pack = true;
		mainLayout.type = SWT.VERTICAL;
		mainComposite.setLayout(mainLayout);
		
		int lineLength = methods.get(tabFolder.getSelectionIndex()).getTypeVariables().size();
		
		if(lineLength <= 0){
			return;
		}
		
		for(int i = 0; i < lineLength; i++){
			Composite rowComposite = new Composite(mainComposite, SWT.NONE);
			RowLayout rowLayout = new RowLayout();
			rowLayout.wrap = true;
			rowLayout.pack = true;
			rowComposite.setLayout(rowLayout);
			
			String type = methods.get(tabIndex).getTypeVariables().get(i).getType();
			String id = methods.get(tabIndex).getTypeVariables().get(i).getId();
			String value = methods.get(tabIndex).getTypeVariables().get(i).getValue();
			
			Button typeButton = new Button(rowComposite, SWT.NONE);
			typeButton.setSize(35, 20);
			
			if(type.equals(MethodSkeleton.BUTTON))
				typeButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgBtnType.jpg"));
			else
				typeButton.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgTextType.jpg"));
			
			Label idLabel = new Label(rowComposite, SWT.BORDER);
			idLabel.setText(id);
			idLabel.setSize(150, 20);
			
			if(value != null){
				Text valueText = new Text(rowComposite, SWT.BORDER);
				valueText.setForeground(valueText.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				valueText.addFocusListener(new TextWidgetFocusListener(i, id, valueText));
			}else{
				Text valueText = new Text(rowComposite, SWT.BORDER);
				valueText.setVisible(false);
			}
			
			Button upButton = new Button(rowComposite, SWT.PUSH);
			Button downButton = new Button(rowComposite, SWT.PUSH);
			Button deleteButton = new Button(rowComposite, SWT.PUSH);
			
			upButton.setText("U"); // UP
			downButton.setText("D"); // Down
			deleteButton.setText("X"); // Delete
			
			upButton.addSelectionListener(new OrderButtonListener("UP", i));
			downButton.addSelectionListener(new OrderButtonListener("DOWN", i));
			deleteButton.addSelectionListener(new OrderButtonListener("DELETE", i));
			
			if(i == 0)
				upButton.setVisible(false);
			
			if(i == (lineLength - 1))
				downButton.setVisible(false);
		}
		
		tabItem[tabIndex].setControl(mainComposite);
	}
	
	class OrderButtonListener implements SelectionListener{

		String type;
		int index;
		
		public OrderButtonListener(String type, int index){
			this.type = type;
			this.index = index;
		}
		
		@Override
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

		@Override
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
			String value = text.getText();
			methods.get(tabFolder.getSelectionIndex()).getTypeVariables().set(index, new TypeVariable("EditText", id, value));
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
			// Show Alert Dialog

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
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}