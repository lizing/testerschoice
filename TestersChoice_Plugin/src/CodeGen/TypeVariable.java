package CodeGen;

public class TypeVariable{
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