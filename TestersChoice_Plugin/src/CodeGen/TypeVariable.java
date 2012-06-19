package CodeGen;

public class TypeVariable{
	/**
	 * type Button 혹은 EditText
	 * id 레이아웃 XML의 태그 속성중 @+id/id_name -> id_name
	 * value EditText의 경우 setText(String str)의 str 문자열, Button의 경우 무조건 performClick()을 하기 때문에 null값이 들어감
	 */
	private String type;
	private String id;
	private String value;
	
	public TypeVariable(String type, String id, String value) {
		this.type = type;
		this.id = id;
		this.value = value;
	}
	
	public String getType(){
		return type;
	}
	
	public String getId(){
		return id;
	}
	
	public String getValue(){
		return value;
	}
}