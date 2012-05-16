package testerschoice_plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import CodeGen.ClassSkeleton;
import CodeGen.MethodSkeleton;
import CodeGen.TestCaseTemplate;
import CodeGen.TypeVariable;

public class View extends ViewPart {
	public View() {
	}

	private static final int MAX_TAB_SIZE = 16;
	public static final String ID = "TestersChoice_Plugin.view";
	private Text text_path;
	private Text text;
	private Text text_1;
	private Text text_method_name;

	/*
	 * ACTIVITY_CLASS, PROVIDER_CLASS, AUTHORITY);
	 * skeleton.setClassName(TESTCASE_CLASS_NAME); skeleton.setPackageName(PKG);
	 */

	String activity_class;
	String provider_class;
	String authority = "com.testerschoice.provider.MoneyBook";
	String pkg_name = "com.testerschoice.moneybook";
	String testcase_class_name;
	String method_name;

	Button btn_load;
	Combo combo_activity, combo_provider, combo_layout_xml;
	Label lbl_app_name;
	Composite composite_view_list;
	TabFolder tabFolder;
	TabItem[] tabItem;
	Composite[] tabComposite;
	String[] methodNames;

	static int tabFolderViewHeight = 0;

	HashMap<String, String> view_property;
	Button[] btn_layout_button;
	Button[] text_layout_Text;
	ArrayList<Text> secuential_input_value;
	ArrayList<String> secuential_id;
	int tabSize = 0;

	// ArrayList<TypeVariable> typeVariable;

	HashMap<String, String> xmlHash = new HashMap<String, String>();
	private Text text_2;
	private Text text_3;
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
		tabComposite = new Composite[MAX_TAB_SIZE];
		methodNames = new String[MAX_TAB_SIZE];
		btn_layout_button = new Button[16];
		text_layout_Text = new Button[32];
		view_property = new HashMap<String, String>();
		secuential_input_value = new ArrayList<Text>();
		secuential_id = new ArrayList<String>();

		Label lblTargetApplication = new Label(composite, SWT.NONE);
		lblTargetApplication.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		lblTargetApplication.setBounds(14, 14, 95, 20);
		lblTargetApplication.setText("Target App:");

		Label lblActivity = new Label(composite, SWT.NONE);
		lblActivity.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		lblActivity.setText("Activity:");
		lblActivity.setBounds(14, 122, 132, 20);

		Label lblProvider = new Label(composite, SWT.NONE);
		lblProvider
				.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		lblProvider.setText("ContentProvider:");
		lblProvider.setBounds(14, 148, 132, 20);

		Label lblXml = new Label(composite, SWT.NONE);
		lblXml.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		lblXml.setText("Layout XML:");
		lblXml.setBounds(14, 185, 95, 20);

		lbl_app_name = new Label(composite, SWT.NONE);
		lbl_app_name.setAlignment(SWT.CENTER);
		lbl_app_name.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		lbl_app_name.setBounds(152, 13, 156, 20);
		lbl_app_name.setText("Application");

		combo_activity = new Combo(composite, SWT.NONE);
		combo_activity.setBounds(152, 119, 239, 23);

		combo_provider = new Combo(composite, SWT.NONE);
		combo_provider.setBounds(152, 145, 239, 23);

		combo_layout_xml = new Combo(composite, SWT.NONE);
		combo_layout_xml.setBounds(152, 182, 239, 23);

		Label lblPath = new Label(composite, SWT.NONE);
		lblPath.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		lblPath.setText("Project Path:");
		lblPath.setBounds(14, 40, 95, 20);

		text_path = new Text(composite, SWT.BORDER);
		text_path.setBounds(152, 37, 239, 21);

		composite_view_list = new Composite(composite, SWT.NONE);
		composite_view_list.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		composite_view_list.setBounds(14, 293, 378, 366);

		Button btn_load = new Button(composite, SWT.NONE);
		btn_load.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		btn_load.setBounds(315, 10, 76, 23);
		btn_load.setText("Browse...");
		btn_load.addSelectionListener(new BrowseButtonListener());

		Button btn_fetch = new Button(composite, SWT.NONE);
		btn_fetch.setFont(SWTResourceManager.getFont("맑은 고딕", 9, SWT.NORMAL));
		btn_fetch.setText("Fetch View(s)");
		btn_fetch.setBounds(296, 252, 95, 35);
		btn_fetch.addSelectionListener(new FetchButtonListener());

		tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setBounds(423, 77, 359, 509);

		text_method_name = new Text(composite, SWT.BORDER);
		text_method_name.setBounds(526, 14, 174, 20);

		Button btn_add_method = new Button(composite, SWT.NONE);
		btn_add_method.setBounds(706, 14, 76, 20);
		btn_add_method.setText("Create");
		btn_add_method.addSelectionListener(new NewMethodButtonListener());

		Button btn_generate = new Button(composite, SWT.NONE);
		btn_generate.setBounds(650, 616, 132, 43);
		btn_generate.setText("Preview Testcase Code");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
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
				method_name = text_method_name.getText();
				tabItem[tabSize].setText(methodNames[tabSize]);
				tabSize++;

			}

		}

	}

	class BrowseButtonListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			String packageName;
			String packagePath;
			String projectName;
			String projectPath;
			// String AddSrc="\\src\\";
			// String[] JavaClass=javafiles.list();
			// File LoadedDirectory=new File(PackagePath+AddSrc);
			combo_activity.removeAll();
			combo_layout_xml.removeAll();
			combo_provider.removeAll();

			/*
			if (packageName != "" || packagePath != "") {

			}
			*/
			
			DirectoryDialog dlg = new DirectoryDialog(new Shell(), SWT.OPEN);
			dlg.setText("Open...");
			dlg.setMessage("Open Target App Project");
			projectPath = dlg.open();
			text_path.setText(projectPath);
			
			if(projectPath == null){
				//Show Alert Dialog
				
				return;
			}

			File selected_directory = new File(projectPath);
			projectName = selected_directory.getName();
			lbl_app_name.setText(projectName);
			ArrayList<File> files = new ArrayList<File>();
			visitAllFiles(files, selected_directory);

			for (File f : files) {
				String javaFile = f.getName();
				if (javaFile.endsWith(".java")) {
					combo_activity.add(javaFile);
					combo_provider.add(javaFile);
				}
			}
			combo_activity.setText("Successful creation of Class list");
			combo_provider.setText("Successful creation of ContentProvider list");

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
				btn_layout_button[i].setBounds(10, 40 + i * 25, 170, 20);
				btn_layout_button[i].setText(Button_id.get(i));
				btn_layout_button[i].setData(Button_id.get(i));
				btn_layout_button[i].addSelectionListener(new ButtonSelectedListener());
			}
			for (int i = 0; i < EditText_id.size(); i++) {
				text_layout_Text[i] = new Button(composite_view_list, SWT.PUSH);
				text_layout_Text[i].setBounds(200, 40 + i * 25, 170, 20);
				text_layout_Text[i].setText(EditText_id.get(i));
				text_layout_Text[i].setData(EditText_id.get(i));
				text_layout_Text[i].addSelectionListener(new TextSelectedListener());
			}
			/*
			 * for (int i = 0; i < EditText_id.size(); i++) {
			 * 
			 * viewEditTextId = new Text(composite_view_list, SWT.PUSH);
			 * 
			 * viewEditTextId.setBounds(200, 40 + i * 25, 170, 20);
			 * viewEditTextId.setText(EditText_id.get(i));
			 * viewEditTextId.addSelectionListener(new SelectionListener() {
			 * 
			 * 
			 * public void widgetSelected(SelectionEvent e) { // TODO
			 * Auto-generated method stub
			 * 
			 * }
			 * 
			 * 
			 * public void widgetDefaultSelected(SelectionEvent e) { // TODO
			 * Auto-generated method stub
			 * 
			 * } });
			 */

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

			ClassSkeleton skeleton = new ClassSkeleton(activity_name, provider_name, authority);
			skeleton.setClassName(testcase_class_name);
			skeleton.setPackageName(pkg_name);

			MethodSkeleton method1 = new MethodSkeleton();
			method1.setMethodName(method_name);
			
			for (int i = 0; i < tabFolderViewHeight; i++) {
				if (secuential_input_value.get(i) != null)
					method1.setVariable(MethodSkeleton.EDIT_TEXT, secuential_id.get(i), secuential_input_value.get(i).getText());
				else
					method1.setVariable(MethodSkeleton.EDIT_TEXT, secuential_id.get(i), null);
			}
			
			skeleton.setMethod(method1);

			String file = gen.generate(skeleton);
			writeFile(file);

		}

	}

	class ButtonSelectedListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			String text = tabFolderViewHeight + ". " + (String)arg0.widget.getData();
			Text selected_text = new Text(tabComposite[0], SWT.BORDER);
			selected_text.setBounds(10, 10 + tabFolderViewHeight * 25, 150, 20);
			// text += " is clicked.\n";
			selected_text.setText(text);
			secuential_id.add((String) arg0.widget.getData());
			secuential_input_value.add(null);
			tabFolderViewHeight++;
		}
	}

	class TextSelectedListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
		}

		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			String text = tabFolderViewHeight + ". " + (String)arg0.widget.getData();
			Text selected_text = new Text(tabComposite[0], SWT.BORDER);
			Text edit_text_value = new Text(tabComposite[0], SWT.BORDER);
			edit_text_value.setBounds(170, 10 + tabFolderViewHeight * 25, 200, 20);
			selected_text.setBounds(10, 10 + tabFolderViewHeight * 25, 150, 20);
			// text += " is\n";

			selected_text.setText(text);
			secuential_id.add((String) arg0.widget.getData());
			secuential_input_value.add(edit_text_value);
			tabFolderViewHeight++;
		}

	}

	private void writeFile(String text) {

		FileDialog fd = new FileDialog(new Shell(), SWT.SAVE);
		fd.setText("Save...");
		fd.setFilterNames(new String[]{"Java File"});
		fd.setFilterExtensions(new String[]{"*.java"});
		fd.setFileName("Test" + removeFileExtention(activity_class));
		String fileName = fd.open();
		
		if(fileName == null){
			//Show Alert Dialog
			
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