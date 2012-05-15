package CodeGen;

import java.util.ArrayList;


public class ClassSkeleton {
	
	//String importContents = "";
	private String pkgName;
	private String activityName;
	private String providerName;
	private String authority;
	private String testCaseClassName;
	
	private ArrayList<MethodSkeleton> methodLists;
	
	public ClassSkeleton(){
		
	}
	
	public ClassSkeleton(String activityName, String providerName,String authority){
		this.activityName = activityName;
		this.providerName = providerName;
		this.authority = authority;
		methodLists = new ArrayList<MethodSkeleton>();
	}
	
	public void setPackageName(String name){
		this.pkgName = name;
	}
	
	public void setClassName(String name){
		this.testCaseClassName = name;
	}
	
	public void setMethod(MethodSkeleton method){
		methodLists.add(method);
	}
	
	public String getActivityName(){
		return activityName;
	}
	
	public String getProviderName(){
		return providerName;
	}
	
	public String getAuthority(){
		return authority;
	}
	
	public String getPackageName(){
		return pkgName;
	}
	
	public String getClassName(){
		return testCaseClassName;
	}
	
	public ArrayList<MethodSkeleton> getMethodLists() {
		if(methodLists.isEmpty())
			return null;
		else
			return methodLists;
	}
}
