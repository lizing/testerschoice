package testerschoice_plugin;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class AndroidXmlSaxParser extends DefaultHandler {

	
	//final static String filename = "C:\\Yeon_Sik\\prj\\HouseholdBook2\\res\\layout\\addpage.xml";
	String filename,elementName, pkg, authority;
	ArrayList<String> idList = new ArrayList<String>();
	ArrayList<String> typeList = new ArrayList<String>();
	SAXParserFactory factory;
	SAXParser saxParser;
	
	public AndroidXmlSaxParser(String filename, String elementName) {
		// TODO Auto-generated constructor stub
		
		this.filename = filename;
		this.elementName = elementName;
		this.pkg = null;
		
		factory = SAXParserFactory.newInstance();
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		
		if(qName.equals(elementName) ){
				int length = attributes.getLength();
			
				for(int i=0; i<length; i++){
					String name = attributes.getQName(i);
					if(name.equals("android:id")){
						String value = attributes.getValue(i);
						idList.add(getRealId(value));
					}
					
					if(name.equals("package")){
						String value = attributes.getValue(i);
						pkg = value;
					}
					
					if(name.equals("android:authorities")){
						String value = attributes.getValue(i);
						authority = value;
					}
				}		
		}
	}
	
	public String getPackageName(){
		return pkg;
	}
	
	public String getRealId(String value){
		String id = null;
		
		int index = value.indexOf("/");
		return id = value.substring(index+1);
	}
	
	public String getAuthority(){
		return authority;
	}
	
	public void parse(){
		try {
			saxParser.parse(filename, this);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public ArrayList<String> getIdArrayList(){ 
		return idList; 
	} 
}
