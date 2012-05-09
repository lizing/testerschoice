package main;

import java.util.ArrayList;
import java.util.List;

import CodeGen.ClassSkeleton;
import CodeGen.MethodSkeleton;
import CodeGen.TestCaseTemplate;

public class CodeGenerator {

	private static final String PKG = "com.testerschoice.moneybook.test";
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
		method1.setVariable(MethodSkeleton.EDIT_TEXT, "purchase_item_name");
		
		skeleton.setMethod(method1);
		
		String file = gen.generate(skeleton);
		System.out.println(file);
	}

}
