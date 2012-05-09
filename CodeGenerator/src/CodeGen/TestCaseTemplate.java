package CodeGen;

import java.util.*;

public class TestCaseTemplate
{
  protected static String nl;
  public static synchronized TestCaseTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    TestCaseTemplate result = new TestCaseTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "package ";
  protected final String TEXT_3 = ";" + NL + "" + NL + "import android.app.Activity;" + NL + "import android.widget.Button;" + NL + "import android.widget.EditText;" + NL + "" + NL + "import ";
  protected final String TEXT_4 = ";" + NL + "import ";
  protected final String TEXT_5 = ";" + NL + "" + NL + "public class ";
  protected final String TEXT_6 = " extends ActivityProviderInstrumentationTestCase2<";
  protected final String TEXT_7 = ", ";
  protected final String TEXT_8 = ">{" + NL + "" + NL + "\tActivity testActivity;" + NL + "\t" + NL + "\tprivate static final String AUTHORITY = \"";
  protected final String TEXT_9 = "\";" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_10 = "(){" + NL + "\t\tsuper(";
  protected final String TEXT_11 = ".class, ";
  protected final String TEXT_12 = ".class, AUTHORITY);" + NL + "\t}" + NL + "\t" + NL + "\tpublic void setUp() throws Exception{" + NL + "\t" + NL + "\t}" + NL + "\t" + NL + "\tpublic void tearDown(){" + NL + "\t" + NL + "\t}" + NL + "\t" + NL + "\t";
  protected final String TEXT_13 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     ClassSkeleton skeleton = (ClassSkeleton)argument; 
     String pkgName = ""; 
     String activityClass = ""; 
     String providerClass = ""; 
     String className = ""; 
     String authority = ""; 
    stringBuffer.append(TEXT_1);
     pkgName = skeleton.getPackageName(); 
     activityClass = skeleton.getActivityName(); 
     providerClass = skeleton.getProviderName(); 
     className = skeleton.getClassName(); 
     authority = skeleton.getAuthority(); 
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pkgName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(pkgName+activityClass);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(pkgName+providerClass);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(activityClass);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(providerClass);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(authority);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(activityClass);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(providerClass);
    stringBuffer.append(TEXT_12);
    if(skeleton.getMethodLists() != null){
    		ArrayList<MethodSkeleton> methodLists = skeleton.getMethodLists();
    		for(Iterator i = methodLists.iterator(); i.hasNext();){
    			MethodSkeleton method = (MethodSkeleton)i.next();
    			stringBuffer.append("public void testInput_" + method.getMethodName() + "{" + NL);
    			ArrayList<TypeVariable> viewLists = method.getTypeVariables();
    			if(viewLists.size() != 0){
    				for(int j = 0; j < viewLists.size(); j++){
    					TypeVariable tv = viewLists.get(j);
    					String type = tv.getType();
    					String id = tv.getId();
    					String s = "\t\t" + type + " variable" + j + " = (" + type + ")testActivity.findViewById(" + pkgName + ".R.id." + id +");";
    					stringBuffer.append(s + NL);
    				}
    			}
    			stringBuffer.append("\t}" + NL); 
    		} 
    } 
    stringBuffer.append(TEXT_13);
    return stringBuffer.toString();
  }
}
