package CodeGen;

import java.util.ArrayList;

public class MethodSkeleton {
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
