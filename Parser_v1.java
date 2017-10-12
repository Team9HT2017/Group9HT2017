package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class Parser_v1 { 
	
	private static String str = null;
   public static Map Parse2 (){
	   Map <Object,List <Object>> result = new HashMap <Object,List <Object>>();
     Input inp = new Input();
     inp.frame.setVisible(true);
     while (str==null || str.equals("")){      
    	 System.out.print("");    //wait for input
    str=inp.result;}   //input from start window
     str = str.replaceAll("\\s+"," ");
 
     JSONObject res = new JSONObject(str); //create JSON object from input
     Object type =  res.get("type");
     System.out.println(type); //for testing only
    
     if (type.equals("sequence_diagram")){
     
     JSONArray arr1 = res.getJSONArray("processes"); // pick array that contains names of nodes along with class info
     JSONObject meta = res.getJSONObject("meta");
    
     JSONObject diagramElements = res.getJSONObject("diagram"); // pick array that contains high-level information about messages
     List <Object> processes = arr1.toList();
     List <Object> names = new ArrayList <Object> ();
     for (int i=0;i<processes.size();i++){ // create array that contains ONLY names of nodes, classes are dumped
    	 names.add((processes.get(i).toString().substring(6,processes.get(i).toString().indexOf(","))));  	
     }
     JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages
     JSONArray messages = new JSONArray();  // "digging" into nested JSON that contains messages
  
     for (int i=0;i<diagramElements2.length();i++){
    	 JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
    	 for (int t=0;t<temp.length();t++){
    		 messages.put(temp.get(t)); // finally creating array of messages
    	 }   	
     }
     
     for (int i=0;i<names.size();i++){ // filling the map with <Node name, Message list>-type elements for further usage
    	  List <Object> relationships = new ArrayList <Object> ();
    	 for (int t=0;t<messages.length();t++){
    		 System.out.println("zzzz "+messages.getJSONObject(t).get("from")+" TTT "+names.get(i).toString());
    		 if (messages.getJSONObject(t).get("from").equals(names.get(i).toString())){   			 
    			 relationships.add(messages.getJSONObject(t));
    		 }
    	 }
    	
    	// System.out.println(relationships.toString()); //for testing
    	 result.put(names.get(i), relationships); //actual process of filling of the map
     }
    
     /*String result2 = ("{'meta' : "+meta.toString()+" "+", 'type' : "+"? "+type+" ?," //highlighting type with question marks for easier identification //for testing
      		+ ""+" 'processes' : "+arr1.toString()+" "+"'diagram' : "+diagramElements.toString()); //parsing (to string)
          System.out.println(result.toString()); */
     inp.frame.dispose(); //close the frame after parsing 
      return result; }
     
     
     
     else if (type.equals("class_diagram")){ //we don't need this for now
    	 
    	  JSONArray arr11 = res.getJSONArray("classes");
    	  JSONObject arr12 = res.getJSONObject("meta");
    	  JSONArray arr13 = res.getJSONArray("relationships");
    	  String result2 = ("{'meta' : "+arr12.toString()+" "+", 'type' : "+"? "+type+" ?,"
    	     		+ ""+" 'classes' : "+arr11.toString()+" "+"'relationships' : "+arr13.toString());
    	  System.out.println(result);
    	  inp.frame.dispose(); //close the frame after parsing 
    	  return null ; //change later
     }
     else if (type.equals("deployment_diagram")){ //we don't need this for now
    	  JSONObject arr12 = res.getJSONObject("meta");
    	  JSONArray arr13 = res.getJSONArray("mapping");
    	  String result2 = ("{'meta' : "+arr12.toString()+" "+", 'type' : "+"? "+type+" ?,"
    	     		+" "+"'mapping' : "+arr13.toString());
    	   System.out.println(result);
    	  //inp.frame.dispose(); //close the frame after parsing 
    	  return null;  //change later
     }
	return null;
      
   }
   
   public static void main(String [] args){
	   Parse2();
   }
   
}