import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AndroidXmlDomParser {

	public static final String ATTRIBUTE_ID = "android:id";
	
	private String fileName;
	private ArrayList<String> items = new ArrayList<String>();
	
	public AndroidXmlDomParser(String fileName) {
		this.fileName = fileName;
	}
	
	public ArrayList<String> getIdArrayList(String tagName){
		
		try{
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName(tagName);
			
			for(int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				String id = getAttributeId(nNode);
				if(id != null)
					items.add(getRealId(id));
				
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			items = null;
		}
		
		return items;
	}
	
	private String getRealId(String value){
		String id = null;
		
		int index = value.indexOf("/");
		id = value.substring(index + 1); 
		
		return id;
	}
	
	private String getAttributeId(Node node){
		String id = null;
		if(node instanceof Element && node.hasAttributes()){
			NamedNodeMap attrs = node.getAttributes();
			for(int i = 0; i < attrs.getLength(); i++){
				Attr attribute = (Attr)attrs.item(0);
				if(attribute.getName().equals(ATTRIBUTE_ID)){
					id = attribute.getValue();
					return id;
				}
			}
		}
		
		return id;
	}
}
