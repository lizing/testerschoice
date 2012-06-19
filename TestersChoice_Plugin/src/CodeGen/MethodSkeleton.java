package CodeGen;

import java.util.ArrayList;

public class MethodSkeleton {
	/**
	 * EDIT_TEXT, BUTTON 안드로이드 layout의 Button과 EditText의 XML 태그 이름 상수
	 * methodName 메서드의 이름 (public void testXXXX - 여기서 XXXX 내용)
	 * viewLists 사용자가 선택한 EditText와 Button 변수를 담는 ArrayList
	 */
	public static final String EDIT_TEXT = "EditText";
	public static final String BUTTON = "Button";
	
	private String methodName;
	private ArrayList<TypeVariable> viewLists;
	
	public MethodSkeleton() {
		viewLists = new ArrayList<TypeVariable>();
	}

	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void setVariable(String type, String id, String value){
		viewLists.add(new TypeVariable(type, id, value));
	}
	
	public String getButtonEvent(String viewId){
		return viewId+".PerformClick();";
	}
	
	public String getEditTextEvent(String viewId,String text){
		return viewId+".setText("+text+");";
	}
	
	public ArrayList<TypeVariable> getTypeVariables(){
		return viewLists;
	}
}
