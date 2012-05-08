import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class AndroidXmlSaxParser extends DefaultHandler{

	private SAXParser parser;
	private SAXParserFactory parserFact;
	
	private String elementName;
	private String fileName;
	
	public static final String ATTRIBUTE_ID = "android:id";
	private ArrayList<String> items = new ArrayList<String>();
	
	public AndroidXmlSaxParser(String fileName, String elementName){
		this.fileName = fileName;
		this.elementName = elementName;
		
		try{
			parserFact = SAXParserFactory.newInstance();
			parser = parserFact.newSAXParser();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals(elementName)){
			int length = attributes.getLength();
			
			for(int i = 0; i < length; i++){
				String name = attributes.getQName(i);
				if(name.equals(ATTRIBUTE_ID)){
					String value = attributes.getValue(i);
					items.add(getRealId(value));
				}
			}
		}else
			return;
	}
	
	private String getRealId(String value){
		String id = null;
		
		int index = value.indexOf("/");
		id = value.substring(index + 1); 
		
		return id;
	}
	
	public void parse(){
		try{
			parser.parse(fileName, this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getIdArrayList(){
		return items;
	}
}
