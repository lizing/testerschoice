package CodeGen;

import java.util.ArrayList;


public class ClassSkeleton {
	
	/**
	 * pkgName 테스트 대상 안드로이드 프로젝트의 패키지 이름
	 * activityName Activity 클래스 이름
	 * providerName ContentProvider 클래스 이름
	 * authority ContentProvider authority
	 * testcaseClassName 자동생성될 테스트케이스의 클래스 이름
	 * methodLists 테스트케이스의 메소드 객체들 (public void testXXXX의 메서드 field들에 대한 내용을 가지고 있음)
	 */
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
