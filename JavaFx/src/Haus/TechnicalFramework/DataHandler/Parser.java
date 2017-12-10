package Haus.TechnicalFramework.DataHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author Anthony;
 * Made with usage of JSON.org library
 * <p>
 * Copyright (c) 2002 JSON.org
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * The Software shall be used for Good, not Evil.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/

public class Parser {

    static ArrayList<ArrayList<Object>> par2 = new ArrayList<>();
    private static String str = null;
    public static Map<Integer, Integer> flows = new HashMap<Integer, Integer>();
    static int flowNumber = 0;
    static int check = 0;

    public static void main(String[] args) { //main method for testing

        File file = new File("C:\\\\Users\\\\Lone ranger\\\\Desktop\\\\testJS1satan.json");
        String toParse = "";
        try {
            toParse = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println("In order = " + Arrays.toString(parParsing(toParse).toArray()));
        } catch (Exception E) {
            System.out.println("Nope");
        }
        System.out.println("New = " + Arrays.toString(parParsingNested(toParse).toArray()));
        ArrayList<ArrayList<Object>> np = new ArrayList<ArrayList<Object>>(new LinkedHashSet<>(parParsingNested(toParse)));

        System.out.println("Flows = " + flows.toString());
        System.out.println("No dups =" + np);
    }


    public static Map Parse2(String toParse, boolean send) {
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

                    if (messages.getJSONObject(t).get("from").equals(names.get(i).toString())) {
                        relationships.add(messages.getJSONObject(t));
                    }
                }


                if (!send) {
                    result.put(names.get(i) + "|" + classes.get(i), relationships); //actual process of filling of the map
                } else {
                    result.put("&" + names.get(i) + "|" + classes.get(i), relationships + "?");
                }
            }

            String result2 = ("{'meta' : " + meta.toString() + " " + ", 'type' : " + "? " + type + " ?," //highlighting type with question marks for easier identification //for testing
                    + "" + " 'processes' : " + arr1.toString() + " " + "'diagram' : " + diagramElements.toString()); //parsing (to string)
            System.out.println("sequence " + result2.toString());

            return result;
        } else if (type.equals("class_diagram")) { // parsing class diagram

            JSONArray arr11 = res.getJSONArray("classes");
            JSONObject arr12 = res.getJSONObject("meta");

            JSONArray arr13 = res.getJSONArray("relationships");
            String result2 = ("{'meta' : " + arr12.toString() + " " + ", 'type' : " + "? " + type + " ?,"
                    + "" + " 'classes' : " + arr11.toString() + " " + "'relationships' : " + arr13.toString());
            System.out.println(result2);
            System.out.println("classes experiment " + arr11.getJSONObject(0));
            System.out.println("relationships experiment " + arr13.getJSONObject(0));

            for (int i = 0; i < arr13.length(); i++) {
                List<String> subres = new ArrayList<String>();
                List<String> tores = new ArrayList<String>();
                JSONObject d1 = arr13.getJSONObject(i);
                Object supclass = d1.get("superclass");
                Object subclass = d1.get("subclass");
                for (int j = 0; j < arr11.length(); j++) {
                    JSONObject d2 = arr11.getJSONObject(j);
                    Object name = d2.get("name");
                    JSONArray inner = d2.getJSONArray("fields");
                    for (int p = 0; p < inner.length(); p++) {
                        JSONObject n1 = inner.getJSONObject(p);
                        Object nm = n1.get("name");
                        Object tp = n1.get("type"); // Gateway=superclass:"Device",fields{subres}
                        System.out.println("name " + nm + " type " + tp);
                        subres.add(tp + " : " + nm);
                    }

                    if (name.equals(supclass)) {
                        tores.add(subres.toString());
                    } else if (name.equals(subclass)) {
                        tores.add(subres.toString());
                    }
                }
                result.put(subclass, " Superclass:" + supclass + " Variables:" + tores.toString()); //\n
            }
            System.out.println(result.toString());
            return result;
        } else if (type.equals("deployment_diagram")) { // parsing deployment diagram
            JSONObject arr12 = res.getJSONObject("meta");
            JSONArray arr13 = res.getJSONArray("mapping");
            for (int l = 0; l < arr13.length(); l++) {
                JSONObject extract = arr13.getJSONObject(l);
                Object process = extract.get("process");
                Object device = extract.get("device");
                result.put(process, " Device: " + device);
            }


            System.out.println(result);

            return result;
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

            JSONArray messages = new JSONArray();  // "digging" into nested JSON that contains messages

            for (int i = 0; i < diagramElements2.length(); i++) {
                JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
                for (int t = 0; t < temp.length(); t++) {
                    messages.put(temp.get(t)); // finally creating array of message

                }
            }

            Map<Object, Object> User = new HashMap<>();

            for (int t = 0; t < messages.length(); t++) {        // finally creating array of messages
                ArrayList<Object> inner = new ArrayList<>();

                User.put(messages.getJSONObject(t).get("from"), messages.getJSONObject(t).get("to"));

                {

                    inner.add("{ " + messages.getJSONObject(t).get("from"));
                    inner.add(" " + messages.getJSONObject(t).get("node"));
                    inner.add("to " + messages.getJSONObject(t).get("to"));
                    inner.add(" the following message " + messages.getJSONObject(t).get("message") + " } " + "?" + t);

                }

                ordering.add(inner);
            }
        }
        return ordering;
    }


    public static ArrayList<ArrayList<Object>> parParsing(String Parse) {

        ArrayList<ArrayList<Object>> par = new ArrayList<>();
        ArrayList<Object> contents = new ArrayList<>();
        str = Parse.replaceAll("\\s+", " "); //remove all long spaces (more than 1) to prevent parser from crashing
        JSONObject res = new JSONObject(str); //create JSON object from input
        Object type = res.get("type");

        if (type.equals("sequence_diagram")) {

            JSONObject diagramElements = res.getJSONObject("diagram"); // pick array that contains high-level information about messages
            for (int d = 0; d < diagramElements.length(); d++) {

                if (!diagramElements.get("node").toString().equals("par")) {


                    JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages

                    Map<Object, Object> checkReply = new HashMap<>();


                    for (int i = 0; i < diagramElements2.length(); i++) {
                        if (!flows.containsKey(i)) {
                            flows.put(i, 0);
                        }

                        JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages

                        for (int j = 0; j < temp.length(); j++) {

                            {
                                contents.add(" { " + temp.getJSONObject(j).get("from"));
                                contents.add(" " + temp.getJSONObject(j).get("node"));
                                contents.add("to " + temp.getJSONObject(j).get("to"));
                                contents.add("the following message " + temp.getJSONObject(j).get("message") + " } " + "=" + i + "@" + j + "|");
                            }
                        }
                        if (!par.contains(contents)) {
                            par.add(contents);
                            flowNumber++;
                        }

                    }


                } else if (diagramElements.get("node").toString().equals("par")) {

                    JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages

                    Map<Object, Object> checkReply = new HashMap<>();

                    for (int i = 0; i < diagramElements2.length(); i++) {
                        if (!flows.containsKey(i)) {
                            flows.put(i, 0);
                        }

                        JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
                        flowNumber++;

                        for (int j = 0; j < temp.length(); j++) {
                            {

                                contents.add(" { " + temp.getJSONObject(j).get("from"));
                                contents.add(" " + temp.getJSONObject(j).get("node"));
                                contents.add("to " + temp.getJSONObject(j).get("to"));
                                contents.add("the following message " + temp.getJSONObject(j).get("message") + " } " + "=" + i + "@" + j + "|");
                            }
                        }
                        if (!par.contains(contents)) {
                            par.add(contents);
                        }
                    }
                }
            }
        }
        return par;

    }


    public static ArrayList<ArrayList<Object>> parParsingNested(String Parse) {

        ArrayList<ArrayList<Object>> par = new ArrayList<>();
        ArrayList<Object> contents = new ArrayList<>();
        str = Parse.replaceAll("\\s+", " "); //remove all long spaces (more than 1) to prevent parser from crashing
        JSONObject res = new JSONObject(str); //create JSON object from input
        Object type = res.get("type");

        if (type.equals("sequence_diagram")) {

            JSONObject diagramElements = res.getJSONObject("diagram"); // pick array that contains high-level information about messages
            for (int d = 0; d < diagramElements.length(); d++) {
                check = d;
                if (!diagramElements.get("node").toString().equals("par")) {


                    JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages

                    Map<Object, Object> checkReply = new HashMap<>();

                    try {
                        for (int i = 0; i < diagramElements2.length(); i++) {

                            if (!flows.containsKey(d)) {
                                flows.put(d, 0);
                            }

                            JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages

                            for (int j = 0; j < temp.length(); j++) {

                                {

                                    contents.add(" { " + temp.getJSONObject(j).get("from"));
                                    contents.add(" " + temp.getJSONObject(j).get("node"));
                                    contents.add("to " + temp.getJSONObject(j).get("to"));
                                    contents.add("the following message " + temp.getJSONObject(j).get("message") + " } " + "=" + (d) + "@" + j + "|");//indexes are buggy
                                }
                            }
                            if (!par2.contains(contents.toString())) {

                                par2.add(contents);
                                flowNumber++;
                            }

                        }
                    } catch (Exception e) {
                        dissect(diagramElements2, d);
                    }
                } else if (diagramElements.get("node").toString().equals("par")) {

                    JSONArray diagramElements2 = diagramElements.getJSONArray("content"); // "digging" into nested JSON that contains messages

                    Map<Object, Object> checkReply = new HashMap<>();

                    try {
                        for (int i = 0; i < diagramElements2.length(); i++) {
                            if (!flows.containsKey(i + d)) {
                                flows.put(i + d, 0);
                            }

                            JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
                            flowNumber++;

                            for (int j = 0; j < temp.length(); j++) {
                                {

                                    contents.add(" { " + temp.getJSONObject(j).get("from"));
                                    contents.add(" " + temp.getJSONObject(j).get("node"));
                                    contents.add("to " + temp.getJSONObject(j).get("to"));
                                    contents.add("the following message " + temp.getJSONObject(j).get("message") + " } " + "=" + (i + d) + "@" + j + "|");//indexes are buggy
                                }
                            }
                            if (!par2.contains(contents)) {
                                par2.add(contents);
                            }
                        }
                    } catch (Exception e) {
                        dissect(diagramElements2, d);
                    }
                }
            }
        }
        return par2;

    }

    public static void dissect(JSONArray diagramElements, int h) {
        ArrayList<Object> contents = new ArrayList<>();
        for (int d = 0; d < diagramElements.length(); d++) {

            if (!diagramElements.getJSONObject(d).get("node").toString().equals("par")) {

                Map<Object, Object> checkReply = new HashMap<>();

                JSONArray temp = diagramElements.getJSONObject(d).getJSONArray("content"); // "digging" into nested JSON that contains messages

                for (int j = 0; j < temp.length(); j++) {
                    if (!flows.containsKey(h)) {
                        flows.put(h, 0);
                    }
                    {

                        contents.add(" { " + temp.getJSONObject(j).get("from"));
                        contents.add(" " + temp.getJSONObject(j).get("node"));
                        contents.add("to " + temp.getJSONObject(j).get("to"));
                        contents.add("the following message " + temp.getJSONObject(j).get("message") + " } " + "=" + (h) + "@" + j + "|");//indexes are buggy
                    }
                }
                if (!par2.contains(contents)) {
                    par2.add(contents);
                    flowNumber++;
                }

            } else if (diagramElements.getJSONObject(d).get("node").toString().equals("par")) {

                JSONArray diagramElements2 = diagramElements.getJSONObject(d).getJSONArray("content"); // "digging" into nested JSON that contains messages

                Map<Object, Object> checkReply = new HashMap<>();

                for (int i = 0; i < diagramElements2.length(); i++) {
                    if (!flows.containsKey(i + h)) {
                        flows.put(i + h, 0);
                    }

                    JSONArray temp = diagramElements2.getJSONObject(i).getJSONArray("content"); // "digging" into nested JSON that contains messages
                    flowNumber++;

                    for (int j = 0; j < temp.length(); j++) {
                        {

                            contents.add(" { " + temp.getJSONObject(j).get("from"));
                            contents.add(" " + temp.getJSONObject(j).get("node"));
                            contents.add("to " + temp.getJSONObject(j).get("to"));
                            contents.add("the following message " + temp.getJSONObject(j).get("message") + " } " + "=" + (i + h) + "@" + j + "|");//indexes are buggy
                        }
                    }
                    if (!par2.contains(contents)) {
                        par2.add(contents);
                    }
                }
            }
        }
    }
}
