package main;

import java.util.ArrayList;
import java.util.List;

import CodeGen.ClassSkeleton;
import CodeGen.MethodSkeleton;
import CodeGen.TestCaseTemplate;

public class CodeGenerator {

	private static final String PKG = "com.testerschoice.moneybook";
	private static final String ACTIVITY_CLASS = "AddNewActivity";
	private static final String PROVIDER_CLASS = "MoneyBookProvider";
	private static final String TESTCASE_CLASS_NAME = "TestAddNewActivity";
	private static final String AUTHORITY = "com.testerschoice.provider.MoneyBook";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestCaseTemplate gen = new TestCaseTemplate();
			
		ClassSkeleton skeleton = new ClassSkeleton(ACTIVITY_CLASS, PROVIDER_CLASS, AUTHORITY);
		skeleton.setClassName(TESTCASE_CLASS_NAME);
		skeleton.setPackageName(PKG);
		
		MethodSkeleton method1 = new MethodSkeleton();
		method1.setMethodName("isValid");
		method1.setVariable(MethodSkeleton.EDIT_TEXT, "purchase_item_name", "hi");
		
		skeleton.setMethod(method1);
		
		MethodSkeleton method2 = new MethodSkeleton();
		method2.setMethodName("isValid2");
		method2.setVariable(MethodSkeleton.EDIT_TEXT, "purchase_item_name", "hi@@@");
		
		skeleton.setMethod(method2);
		
		MethodSkeleton method3 = new MethodSkeleton();
		method3.setMethodName("isValid3");
		method3.setVariable(MethodSkeleton.EDIT_TEXT, "purchase_item_name", "aaaahi@@@");
		method3.setVariable(MethodSkeleton.BUTTON, "button_submit", null);
		
		skeleton.setMethod(method3);
		
		String file = gen.generate(skeleton);
		System.out.println(file);
	}

}
