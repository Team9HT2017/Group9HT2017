
package Haus.TechnicalFramework.DataHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Anthony;
 * Made with usage of JSON.org library

Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 **/


public class Parser {

    private static String str = null;
    public static ArrayList<String> source = new ArrayList<> ();
    public static ArrayList<String> distination = new ArrayList<> ();
    public static Map<String,String> sourceNdistanation = new HashMap<String, String>();

    public static Map Parse2(String toParse,boolean send) {
        Map<Object, Object> result = new HashMap<Object, Object>();


        str = toParse.replaceAll("\\s+", " "); //remove all long spaces (more than 1) to prevent parser from crashing

        JSONObject res = new JSONObject(str); //create JSON object from input
        Object type = res.get("type");
        System.out.println(type); //for testing only

        if (type.equals("sequence_diagram")) {

            JSONArray arr1 = res.getJSONArray("processes"); // pick array that contains names of nodes along with class info
            JSONObject meta = res.getJSONObject("meta");

            JSONObject diagramElements = res.getJSONObject("diagram"); // pick array that contains high-level information about messages
            List<Object> processes = arr1.toList();
            List<Object> names = new ArrayList<Object>();
            List<Object> classes = new ArrayList<Object>();
            for (int i = 0; i < processes.size(); i++) { // create array that contains ONLY names of nodes, classes are dumped
                names.add((processes.get(i).toString().substring(6, processes.get(i).toString().indexOf(","))));
                classes.add((processes.get(i).toString().substring(processes.get(i).toString().indexOf(",") + 8, processes.get(i).toString().length() - 1)));
            }
            JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages
            JSONArray messages = new JSONArray();  // "digging" into nested JSON that contains messages

            for (int i = 0; i < diagramElements2.length(); i++) {
                JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
                for (int t = 0; t < temp.length(); t++) {
                    messages.put(temp.get(t)); // finally creating array of messages
                }
            }

            for (int i = 0; i < names.size(); i++) { // filling the map with <Node name, Message list>-type elements for further usage
                List<Object> relationships = new ArrayList<Object>();
                for (int t = 0; t < messages.length(); t++) {
                    // System.out.println("zzzz "+messages.getJSONObject(t).get("from")+" TTT "+names.get(i).toString());
                    if (messages.getJSONObject(t).get("from").equals(names.get(i).toString())) {
                        relationships.add(messages.getJSONObject(t));
                    }
                }

                // System.out.println(relationships.toString()); //for testing
                if (!send){
                result.put(names.get(i) + "|" + classes.get(i), relationships); //actual process of filling of the map
                }else{ result.put("&"+names.get(i) + "|" + classes.get(i), relationships+"?");}
            }

            String result2 = ("{'meta' : " + meta.toString() + " " + ", 'type' : " + "? " + type + " ?," //highlighting type with question marks for easier identification //for testing
                    + "" + " 'processes' : " + arr1.toString() + " " + "'diagram' : " + diagramElements.toString()); //parsing (to string)
            System.out.println(result2.toString());

            return result;
        }  else if (type.equals("class_diagram")) { 

            JSONArray arr11 = res.getJSONArray("classes");
            JSONObject arr12 = res.getJSONObject("meta");
            
            JSONArray arr13 = res.getJSONArray("relationships");
            String result2 = ("{'meta' : " + arr12.toString() + " " + ", 'type' : " + "? " + type + " ?,"
                    + "" + " 'classes' : " + arr11.toString() + " " + "'relationships' : " + arr13.toString());
            System.out.println(result2);
            System.out.println("classes experiment "+arr11.getJSONObject(0));
            System.out.println("relationships experiment "+arr13.getJSONObject(0));
         
            for(int i=0;i<arr13.length();i++){
            	List <String> subres = new ArrayList <String>();
            	List <String> tores = new ArrayList <String>();
            	JSONObject d1 = arr13.getJSONObject(i);
            	Object supclass = d1.get("superclass");
            	Object subclass = d1.get("subclass");
            	for (int j=0;j<arr11.length();j++){
            		JSONObject d2 = arr11.getJSONObject(j);
            		Object name = d2.get("name");
            		JSONArray inner = d2.getJSONArray("fields");
            		for (int p=0;p<inner.length();p++){
            			JSONObject n1 = inner.getJSONObject(p);
            			Object nm = n1.get("name");
            			Object tp = n1.get("type"); // Gateway=superclass:"Device",fields{subres}
            			System.out.println("name "+nm +" type "+tp);
            			subres.add(tp+" : "+nm);
            		}
            		
            		if (name.equals(supclass)){
            		tores.add(subres.toString());
            		}
            		else if (name.equals(subclass)){
                		tores.add(subres.toString());
                		}
            	}
            	result.put(subclass, " Superclass:" +supclass+" Variables:"+tores.toString()); //\n
            }
            System.out.println(result.toString());
            return result; //change later
        } else if (type.equals("deployment_diagram")) { 
            JSONObject arr12 = res.getJSONObject("meta");
            JSONArray arr13 = res.getJSONArray("mapping");
            for (int l=0;l<arr13.length();l++){
            	JSONObject extract = arr13.getJSONObject(l);
            	Object process = extract.get("process");
            	Object device = extract.get("device");
            	result.put(process, " Device: "+device);
            }
            
          
            
            System.out.println(result);

            return result;  //change later
        }
        return null;

    }

    /**
     * @Author Fahd;
     * use anthony's version to access json objects and creates a new ArrayList of Arraylists to get the messages in order
     **/
    public static ArrayList<ArrayList<Object>> ParseInorder(String Parse) {

        ArrayList<ArrayList<Object>> ordering = new ArrayList<>();

        str = Parse.replaceAll("\\s+", " "); //remove all long spaces (more than 1) to prevent parser from crashing

        JSONObject res = new JSONObject(str); //create JSON object from input
        Object type = res.get("type");

        if (type.equals("sequence_diagram")) {

            JSONObject diagramElements = res.getJSONObject("diagram"); // pick array that contains high-level information about messages

            JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages
            //System.out.println("check herrrrrrrrrrreeeeeee");
            System.out.println(diagramElements2.toString());

            JSONArray messages = new JSONArray();  // "digging" into nested JSON that contains messages

            for (int i = 0; i < diagramElements2.length(); i++) {
                JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
                //System.out.println(diagramElements2.getJSONObject(i));
                for (int t = 0; t < temp.length(); t++) {
                    messages.put(temp.get(t)); // finally creating array of message

                }
            }
           // System.out.println("messagessssss");
            System.out.println(messages.toString());
            Map<Object, Object> User = new HashMap<>();

            for (int t = 0; t < messages.length(); t++) {        // finally creating array of messages
                ArrayList<Object> inner = new ArrayList<>();

                User.put(messages.getJSONObject(t).get("from"), messages.getJSONObject(t).get("to"));
                System.out.println(messages.getJSONObject(t).get("from"));
                System.out.println(messages.getJSONObject(t).get("to"));
                //System.out.println( User.entrySet().toArray());

                // Converting the Json objects into string
                JSONObject sourceJsonObject = messages.getJSONObject(t);
                JSONObject distanatoinJsonObject = messages.getJSONObject(t);
                // storing the users as source and distanation to be used in Dijkstra'a algorithm
                sourceNdistanation.put(sourceJsonObject.getString("from"),distanatoinJsonObject.getString("to"));
                source.add (sourceJsonObject.getString("from"));
                distination.add (distanatoinJsonObject.getString("to"));


                // for (Map.Entry<Object, Object> entry : User.entrySet())
                //  {
                //  if (entry.getKey().toString().equals("" + messages.getJSONObject(t).get("to") ) &&  entry.getValue().toString().equals("" +messages.getJSONObject(t).get("from")) ) {
                if ( ( " "+User.get(messages.getJSONObject(t).get("to"))).equals( " "+messages.getJSONObject(t).get("from"))) {
                    inner.add(" { " + messages.getJSONObject(t).get("from"));
                    inner.add(" Reply back ");
                    inner.add("to " + messages.getJSONObject(t).get("to"));
                    inner.add("the following message " + messages.getJSONObject(t).get("message") + " } ");
                } else {

                    inner.add("{ " + messages.getJSONObject(t).get("from"));
                    inner.add(" " + messages.getJSONObject(t).get("node"));
                    inner.add("to " + messages.getJSONObject(t).get("to"));
                    inner.add(" the following message " + messages.getJSONObject(t).get("message") + " } ");

                    // System.out.println("should be equal" + entry.getKey().toString() + messages.getJSONObject(t).get("to"));
                    // System.out.println("should be equal" + entry.getValue().toString() + messages.getJSONObject(t).get("from"));

                }
                //  }
                ordering.add(inner);
            }

        }


   /*public static void main(String [] args){
	   Parse2();
   }*/

        return ordering;
    }
    public static boolean checkNodesNumber (String Parse) {

        str = Parse.replaceAll("\\s+", " "); //remove all long spaces (more than 1) to prevent parser from crashing

        JSONObject result = new JSONObject(str); //create JSON object from input
        Object type = result.get("type");

        if (type.equals("sequence_diagram")) {

            JSONObject diagram = result.getJSONObject("diagram"); // pick array that contains high-level information about messages

            JSONArray content1 = diagram.getJSONArray("content"); // "digging" into nested JSON that contains messages
            System.out.println(content1.toString());

            if (content1.length()> 0) {

                return true;
            }

        }

        return false;
    }

    public static Map<ArrayList<Object>, Integer> priority1node(String Parse) {

        ArrayList<Object> priority;
        Map<ArrayList<Object>, Integer> oneNodeMap = new HashMap<>();
        int orderNumber;
        str = Parse.replaceAll("\\s+", " "); //remove all long spaces (more than 1) to prevent parser from crashing

        JSONObject result = new JSONObject(str); //create JSON object from input
        Object type = result.get("type");

        if (type.equals("sequence_diagram")) {

            JSONObject diagram = result.getJSONObject("diagram"); // pick array that contains high-level information about messages

            JSONArray content1 = diagram.getJSONArray("content"); // "digging" into nested JSON that contains messages
            System.out.println(content1.toString());
            Map<Object, Object> student = new HashMap<>();


            for (int i = 0; i < content1.length(); i++) {
                JSONArray nestedContent = content1.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages

                for (int j = 0; j < nestedContent.length(); j++) {

                    student.put(nestedContent.getJSONObject(j).get("from"), nestedContent.getJSONObject(j).get("to"));

                    if ((" " + student.get(nestedContent.getJSONObject(j).get("to"))).equals(" " + nestedContent.getJSONObject(j).get("from"))) {
                        priority = new ArrayList<>(0);
                        priority.add(" { " + nestedContent.getJSONObject(j).get("from"));
                        priority.add(" Reply back ");
                        priority.add("to " + nestedContent.getJSONObject(j).get("to"));
                        priority.add("the following message " + nestedContent.getJSONObject(j).get("message") + " } ");
                        orderNumber = j;
                        oneNodeMap.put(priority, orderNumber);

                    } else {
                        priority = new ArrayList<>(0);
                        priority.add("{ " + nestedContent.getJSONObject(j).get("from"));
                        priority.add(" " + nestedContent.getJSONObject(j).get("node"));
                        priority.add("to " + nestedContent.getJSONObject(j).get("to"));
                        priority.add("the following message " + nestedContent.getJSONObject(j).get("message") + " } ");
                        orderNumber = j;
                        oneNodeMap.put(priority, orderNumber);
                    }
                }


            }

        }

        return oneNodeMap;
    }
}