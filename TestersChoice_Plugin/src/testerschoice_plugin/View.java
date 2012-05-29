package testerschoice_plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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

public class View extends ViewPart {
	public View() {
	}

	private static final int MAX_TAB_SIZE = 16;
	public static final String ID = "TestersChoice_Plugin.view";
	private Text text_path;
	private Text text_method_name;

	/*
	 * ACTIVITY_CLASS, PROVIDER_CLASS, AUTHORITY);
	 * skeleton.setClassName(TESTCASE_CLASS_NAME); skeleton.setPackageName(PKG);
	 */

	String activity_class;
	String provider_class;
	String authority = "";
	String pkg_name = "com.testerschoice.moneybook";
	String testcase_class_name;
	String[] methodNames;
	
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
	ArrayList<Text> secuential_input_value;
	ArrayList<String> secuential_id;

	String[][] secuential_id1;
	String[][] secuential_input_value1;
	
	int tabSize = 0;
	static int z = 0;

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

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
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
		secuential_input_value = new ArrayList<Text>();
		secuential_id = new ArrayList<String>();
		
		secuential_input_value1 = new String[MAX_TAB_SIZE][32];
		secuential_id1 = new String[MAX_TAB_SIZE][32];
		
		
		Label lblTargetApplication = new Label(composite, SWT.NONE);
		lblTargetApplication.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9,
				SWT.NORMAL));
		lblTargetApplication.setBounds(14, 14, 95, 20);
		lblTargetApplication.setText("Target App:");

		Label lblActivity = new Label(composite, SWT.NONE);
		lblActivity.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		lblActivity.setText("Activity:");
		lblActivity.setBounds(14, 122, 132, 20);

		Label lblProvider = new Label(composite, SWT.NONE);
		lblProvider.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		lblProvider.setText("ContentProvider:");
		lblProvider.setBounds(14, 148, 132, 20);

		Label lblXml = new Label(composite, SWT.NONE);
		lblXml.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		lblXml.setText("Layout XML:");
		lblXml.setBounds(14, 185, 95, 20);

		lbl_app_name = new Label(composite, SWT.NONE);
		lbl_app_name.setAlignment(SWT.CENTER);
		lbl_app_name.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		lbl_app_name.setBounds(152, 13, 156, 20);
		lbl_app_name.setText("Application");

		combo_activity = new Combo(composite, SWT.NONE);
		combo_activity.setBounds(152, 119, 239, 23);

		combo_provider = new Combo(composite, SWT.NONE);
		combo_provider.setBounds(152, 145, 239, 23);

		combo_layout_xml = new Combo(composite, SWT.NONE);
		combo_layout_xml.setBounds(152, 182, 239, 23);

		Label lblPath = new Label(composite, SWT.NONE);
		lblPath.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		lblPath.setText("Project Path:");
		lblPath.setBounds(14, 40, 95, 20);

		text_path = new Text(composite, SWT.BORDER);
		text_path.setBounds(152, 37, 239, 21);

		composite_view_list = new Composite(composite, SWT.NONE);
		composite_view_list.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		composite_view_list.setBounds(14, 293, 378, 366);

		Button btn_load = new Button(composite, SWT.NONE);
		btn_load.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		btn_load.setBounds(315, 10, 76, 23);
		btn_load.setText("Browse...");
		btn_load.addSelectionListener(new BrowseButtonListener());

		Button btn_fetch = new Button(composite, SWT.NONE);
		btn_fetch.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 9, SWT.NORMAL));
		btn_fetch.setText("Fetch View(s)");
		btn_fetch.setBounds(296, 252, 95, 35);
		btn_fetch.addSelectionListener(new FetchButtonListener());

		tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setBounds(423, 69, 359, 517);

		text_method_name = new Text(composite, SWT.BORDER);
		text_method_name.setBounds(526, 14, 174, 20);

		Button btn_add_method = new Button(composite, SWT.NONE);
		btn_add_method.setBounds(706, 14, 76, 20);
		btn_add_method.setText("Create");
		btn_add_method.addSelectionListener(new NewMethodButtonListener());

		Button btn_generate = new Button(composite, SWT.NONE);
		btn_generate.setBounds(650, 626, 132, 43);
		btn_generate.setText("Preview Testcase Code");

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("¸¼Àº°íµñ", 11, SWT.NORMAL));
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
		lblNewLabel_1.setBounds(423, 14, 95, 20);
		lblNewLabel_1.setText("Method Name:");

		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setBounds(14, 221, 56, 15);
		lblNewLabel_2.setText("Authority:");

		text_authority = new Text(composite, SWT.BORDER);
		text_authority.setBounds(152, 219, 239, 20);

		Label label_5 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 242, 381, 2);

		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
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
				tabComposite[tabSize] = new Composite(tabFolder, SWT.PUSH);
				tabItem[tabSize].setControl(tabComposite[tabSize]);
				methodNames[tabSize] = text_method_name.getText();
				tabItem[tabSize].setText(methodNames[tabSize]);
				tabSize++;
			}
			text_method_name.setText("");
		}

	}

	class BrowseButtonListener implements SelectionListener {

		File selected_directory;
		ArrayList<File> files;

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			String projectName;
			String projectPath;

			combo_activity.removeAll();
			combo_layout_xml.removeAll();
			combo_provider.removeAll();

			DirectoryDialog dlg = new DirectoryDialog(new Shell(), SWT.OPEN);
			dlg.setText("Open...");
			dlg.setMessage("Open Target App Project");
			projectPath = dlg.open();
			text_path.setText(projectPath);

			if (projectPath == null) {
				// Show Alert Dialog

				return;
			}

			selected_directory = new File(projectPath);
			projectName = selected_directory.getName();
			lbl_app_name.setText(projectName);

			selected_directory = new File(projectPath + "//src");
			files = new ArrayList<File>();
			visitAllFiles(files, selected_directory);

			for (File f : files) {
				String javaFile = f.getName();
				if (javaFile.endsWith(".java")) {
					/* ¿¬½Ä - Activity¿Í Provider ÀÚ¹ÙÆÄÀÏ¸¸ µû·Îµû·Î ³Ö´Â °Å ÇØº¸´ÂÁß
					 * try {
						int d = fileReads(f);
						if (d == ACTIVITY) {
							System.out.println(ccc+"  fileRead(f) == ACTIVITY");
							combo_activity.add(javaFile);
						} else if (d == PROVIDER) {
							System.out.println(ccc+"  fileRead(f) == PROVIDER");
							combo_provider.add(javaFile);
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(ccc+"  ¸¶Áö¸·");
*/
					 combo_activity.add(javaFile);
					 combo_provider.add(javaFile);

				}
			}
			combo_activity.setText("Successful creation of Class list");
			combo_provider
					.setText("Successful creation of ContentProvider list");

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
			combo_layout_xml.setText("Successful creation of XML list");
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
			testcase_class_name = removeFileExtention(activity_class) + "Test";

			AndroidXmlSaxParser saxB = new AndroidXmlSaxParser(
					xmlHash.get(combo_layout_xml.getText()), "Button");
			AndroidXmlSaxParser saxE = new AndroidXmlSaxParser(
					xmlHash.get(combo_layout_xml.getText()), "EditText");

			saxB.parse();
			saxE.parse();

			ArrayList<String> Button_id = saxB.getIdArrayList();
			ArrayList<String> EditText_id = saxE.getIdArrayList();

			Text btnType = new Text(composite_view_list, SWT.PUSH);
			btnType.setBounds(15, 10, 350, 20);
			btnType.setText("< ButtonType >\t\t\t\t\t< EditTextType >");

			
			for (int i = 0; i < Button_id.size(); i++) {
				
				btn_layout_button[i] = new Button(composite_view_list, SWT.PUSH);
				btn_layout_button[i].setBounds(10, 40 + i * 25, 35, 20);
				btn_layout_button[i].setData(Button_id.get(i));
				btn_layout_button[i].setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgBtnType.jpg"));
				btn_layout_button[i]
						.addSelectionListener(new ButtonSelectedListener());

				label_layout_button[i] = new Label(composite_view_list,
						SWT.PUSH);
				label_layout_button[i].setBounds(50, 40 + i * 25, 130, 20);
				label_layout_button[i].setText(Button_id.get(i));

			}
			for (int i = 0; i < EditText_id.size(); i++) {

				text_layout_Text[i] = new Button(composite_view_list, SWT.PUSH);
				text_layout_Text[i].setBounds(190, 40 + i * 25, 35, 20);
				text_layout_Text[i].setData(EditText_id.get(i));
				text_layout_Text[i].setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgTextType.jpg"));
				text_layout_Text[i]
						.addSelectionListener(new TextSelectedListener());
				label_layout_Text[i] = new Label(composite_view_list, SWT.PUSH);
				label_layout_Text[i].setBounds(230, 40 + i * 25, 130, 20);
				label_layout_Text[i].setText(EditText_id.get(i));
			}
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
			String activity_name = removeFileExtention(activity_class);
			String provider_name = removeFileExtention(provider_class);

			ClassSkeleton skeleton = new ClassSkeleton(activity_name,
					provider_name, authority);
			skeleton.setClassName(testcase_class_name);
			skeleton.setPackageName(pkg_name);
	
			for(int i=0; i<tabSize; i++){
				MethodSkeleton method1 = new MethodSkeleton();
				method1.setMethodName(methodNames[i]);
	
				
				for (int j = 0; j < tabFolderViewHeight[i]; j++) {
					if (secuential_input_value1[i][j] != null)
						method1.setVariable(MethodSkeleton.EDIT_TEXT, secuential_id1[i][j], secuential_input_value1[i][j]);
					else
						method1.setVariable(MethodSkeleton.EDIT_TEXT, secuential_id1[i][j], null);
				}
	
				skeleton.setMethod(method1);
			}
			
			/*		
			for(int i=0; i<tabSize; i++){
				MethodSkeleton method1 = new MethodSkeleton();
				method1.setMethodName(methodNames[tabSize]);
	
				
				for (int j = 0; j < tabFolderViewHeight[i]; j++) {
					if (secuential_input_value.get(j) != null)
						method1.setVariable(MethodSkeleton.EDIT_TEXT, secuential_id
								.get(j), secuential_input_value.get(j).getText());
					else
						method1.setVariable(MethodSkeleton.EDIT_TEXT,
								secuential_id.get(j), null);
				}
	
				skeleton.setMethod(method1);
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
			String [] toolTipassertContents = {
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
			
			Shell pick = new Shell();
			pick.setText("Preview");
		
			
			final Table table = new Table(pick, SWT.BORDER | SWT.FULL_SELECTION);
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
			
			final StyledText text = new StyledText(pick,  SWT.BORDER &SWT.SHELL_TRIM| SWT.V_SCROLL|SWT.H_SCROLL);
			text.setEditable(true);
			text.setText(file);
			text.setBounds(10, 30, 700, 650);
			final Button showTip = new Button(pick, SWT.NONE);
			showTip.setBounds(660, 5, 30, 20);
			showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
			showTip.setToolTipText(" \"assert()\" ¹®¿¡ °üÇÑ TipÀ» º¸¿©ÁÝ´Ï´Ù ");
			
			
			Button confirm = new Button(pick, SWT.NONE);
			confirm.setText("Generate");
			confirm.setBounds(590, 680, 100, 30);
			
			
			pick.setSize(700, 730);
			pick.pack();
			pick.open();
			
			showTip.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					if(z%2 == 0){
						text.setBounds(10, 160, 700, 520);
						//tipView.setVisible(true);
						table.setVisible(true);
						showTip.setBounds(660, 135, 30, 20);
						showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/hideTip.jpg"));
						showTip.setToolTipText(" \"assert()\" ¹®¿¡ °üÇÑ TipÀ» ¼û±é´Ï´Ù ");
					}
					else{
						text.setBounds(10, 30, 700, 650);
						//tipView.setVisible(false);
						table.setVisible(false);
						showTip.setBounds(660, 5, 30, 20);
						showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
						showTip.setToolTipText(" \"assert()\" ¹®¿¡ °üÇÑ TipÀ» º¸¿©ÁÝ´Ï´Ù ");
					}
					z++;
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			showTip.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					// TODO Auto-generated method stub
					if(z%2 == 0)
						showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTip.jpg"));
					else
						showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/hideTip.jpg"));
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					if(z%2 == 0)
						showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/showTipFocus.jpg"));
					else
						showTip.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/hideTipFocus.jpg"));
				}
			});
			confirm.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					writeFile(text.getText());
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			

		}

	}

	class ButtonSelectedListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			Button btn = new Button(tabComposite[tabFolder.getSelectionIndex()], SWT.BORDER);
			btn.setBounds(5, 10 + tabFolderViewHeight[tabFolder.getSelectionIndex()] * 25, 35, 20);
			btn.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgBtnType.jpg"));
			
			String text = tabFolderViewHeight[tabFolder.getSelectionIndex()] + ". "
					+ (String) arg0.widget.getData();
			
			
			
			Label selected_text = new Label(tabComposite[tabFolder.getSelectionIndex()], SWT.BORDER);
			selected_text.setBounds(40, 10 + tabFolderViewHeight[tabFolder.getSelectionIndex()] * 25, 150, 20);
			// text += " is clicked.\n";
			selected_text.setText(text);
			
			secuential_id1[tabFolder.getSelectionIndex()][tabFolderViewHeight[tabFolder.getSelectionIndex()]] = (String) arg0.widget.getData();
			secuential_input_value1[tabFolder.getSelectionIndex()][tabFolderViewHeight[tabFolder.getSelectionIndex()]] = null;
			
			
			//secuential_id.add((String) arg0.widget.getData());
			//secuential_input_value.add(null);
			tabFolderViewHeight[tabFolder.getSelectionIndex()]++;
		}
	}

	class TextSelectedListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			Button btn = new Button(tabComposite[tabFolder.getSelectionIndex()], SWT.BORDER);
			btn.setBounds(5, 10 + tabFolderViewHeight[tabFolder.getSelectionIndex()] * 25, 35, 20);
			btn.setImage(ResourceManager.getPluginImage("TestersChoice_Plugin", "icons/imgTextType.jpg"));
			
			String text = tabFolderViewHeight[tabFolder.getSelectionIndex()] + ". "
					+ (String) arg0.widget.getData();
			Label selected_text = new Label(tabComposite[tabFolder.getSelectionIndex()], SWT.BORDER);
			selected_text.setBounds(40, 10 + tabFolderViewHeight[tabFolder.getSelectionIndex()] * 25, 150, 20);

			Text edit_text_value = new Text(tabComposite[tabFolder.getSelectionIndex()], SWT.BORDER);
			edit_text_value.setBounds(195, 10 + tabFolderViewHeight[tabFolder.getSelectionIndex()] * 25, 150,
					20);
			//edit_text_value.setText("Input the value");
			edit_text_value.setForeground(edit_text_value.getDisplay().getSystemColor(SWT.COLOR_BLUE));
			
				
			selected_text.setText(text);
			
			secuential_id1[tabFolder.getSelectionIndex()][tabFolderViewHeight[tabFolder.getSelectionIndex()]] = (String) arg0.widget.getData();
			secuential_input_value1[tabFolder.getSelectionIndex()][tabFolderViewHeight[tabFolder.getSelectionIndex()]] = null;
			
			
			//secuential_id.add((String) arg0.widget.getData());
			//secuential_input_value.add(edit_text_value);
			edit_text_value.forceFocus();
			tabFolderViewHeight[tabFolder.getSelectionIndex()]++;
		}

	}

	private void writeFile(String text) {

		FileDialog fd = new FileDialog(new Shell(), SWT.SAVE);
		fd.setText("Save...");
		fd.setFilterNames(new String[] { "Java File" });
		fd.setFilterExtensions(new String[] { "*.java" });
		fd.setFileName("Test" + removeFileExtention(activity_class));
		String fileName = fd.open();

		if (fileName == null) {
			// Show Alert Dialog

			return;
		}

		File file = new File(fileName);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(text);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String removeFileExtention(String fileNameWithExtension) {
		String fileName = "";
		int index = fileNameWithExtension.lastIndexOf(".");
		if (index != -1) {
			fileName = fileNameWithExtension.substring(0, index);
		}
		return fileName;
	}
	/* ¿¬½Ä - Activity¿Í Provider ÀÚ¹ÙÆÄÀÏ¸¸ µû·Îµû·Î ³Ö´Â °Å ÇØº¸´ÂÁß
	public static int fileReads(File f) throws ClassNotFoundException, IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f.getAbsoluteFile()), "UTF8"));
		String[] data;

		int count = 0;
		String temp;

		while ((temp = br.readLine()) != null) {
			data = new String[100];
			StringTokenizer st = new StringTokenizer(temp);

			while (st.hasMoreTokens()) {
				data[count] = st.nextToken();
				// System.out.println(data[count]);

				count++;
			}

			for (int i = 0; i < count - 1; i++) {
				// System.out.println(data[i]);
				if (data[i].equals("extends")) {
					if (data[i + 1].equals("Activity")) {
						return ACTIVITY;
					} else if (data[i + 1].equals("ContentProvider")) {
						return PROVIDER;
					}
				}
			}
		}
		return 0;
	}
	
	public static int fileRead(File file) throws IOException {

		BufferedReader in = null;

		in = new BufferedReader(new FileReader(file));
		String[] data;

		int count = 0;
		String temp;
		while ((temp = in.readLine()) != null) {
			data = new String[100];
			StringTokenizer st = new StringTokenizer(temp);

			while (st.hasMoreTokens()) {
				data[count] = st.nextToken();
				// System.out.println(data[count]);

				count++;
			}

			for (int i = 0; i < count - 1; i++) {
				if (data[i].equals("extends")) {
					if (data[i + 1].equals("Activity")) {
						System.out.println("\n\n\n\n\n"+data[i+1]+"\n\n\n\n\n");
						return ACTIVITY;
					} else if (data[i + 1].equals("ContentProvider")) {
						System.out.println("\n\n\n\n\n"+data[i+1]+"\n\n\n\n\n");
						return PROVIDER;
					}
				}
			}
		}
		return 0;
	}
	*/

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

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}