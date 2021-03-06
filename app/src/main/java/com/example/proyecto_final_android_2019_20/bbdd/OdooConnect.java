package com.example.proyecto_final_android_2019_20.bbdd;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



import android.content.ContentValues;
import android.util.Log;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;

/**
 * Original Class: https://github.com/zikzakmedia/android-openerp
 * References Odoo10 Web Service API: https://www.odoo.com/documentation/10.0/api_integration.html
 *
 * This class provides access to basic methods in OpenObject, so you can use
 * them from an Android device. The operations supported are: <br>
 * <ul>
 * <li>login</li>
 * <li>create</li>
 * <li>search / read</li>
 * <li>count</li>
 * <li>write</li>
 * <li>unlink</li>
 * <li>call (This is a generic method to call whatever you need)</li>
 * </ul>
 *
 * You can extend OdooConnect to implement more specific methods of your need.
 *
 * @author Edu <eduojerb@gmail.com>
 */

public class OdooConnect {
    private String mServer;
    private Integer mPort;
    private String mDatabase;
    private String mUserName;
    private String mPassword;
    private Integer mUserId;
    private URL mUrl;

    private static final String CONNECTOR_NAME = "OdooConnect";

    private static OdooConnect odooConnect;

    public static OdooConnect getOdooConnect(String server, Integer port, String db, String user, String pass) throws MalformedURLException {
        if(odooConnect==null)
            odooConnect = connect(server,port,db,user,pass);
        return odooConnect;
    }

    /**
     * You should not use the constructor directly, use connect() instead
     */
    private OdooConnect(String server, Integer port, String db, String user, String pass, Integer id) throws MalformedURLException {
        mServer = server;
        mPort = port;
        mDatabase = db;
        mUserName = user;
        mPassword = pass;
        mUserId = id;
        mUrl = new URL(String.format("http://%s:%s/xmlrpc/2/object", server, port));
    }

    /**
     * @return An OdooConnect instance, which you will use to call the methods.
     */
    public static OdooConnect connect(String server, Integer port, String db, String user, String pass) {
        return login(server, port, db, user, pass);
    }

    public static OdooConnect connect(ContentValues connectionParams) {
        return login(connectionParams);
    }

    /**
     * @return true if the connection could be established, else returns false. The connection will not be stored.
     */
    public static Boolean testConnection(String server, Integer port, String db, String user, String pass) {
        return login(server, port, db, user, pass) != null;
    }

    public static Boolean testConnection(ContentValues connectionParams) {
        return login(connectionParams) != null;
    }

    private static OdooConnect login(ContentValues connectionParams) {
        return login(
                connectionParams.getAsString("server"),
                connectionParams.getAsInteger("port"),
                connectionParams.getAsString("database"),
                connectionParams.getAsString("username"),
                connectionParams.getAsString("password")
        );
    }

    private static OdooConnect login(String server, Integer port, String db, String user, String pass) {
        OdooConnect connection = null;
        try {
            URL loginUrl = new URL(String.format("http://%s:%s/xmlrpc/2/common", server, port));
            XMLRPCClient client = new XMLRPCClient(loginUrl);
            Object[] list = {db, user, pass, emptyMap()};
            int id = (int) client.call("authenticate", list);
            connection = new OdooConnect(server, port, db, user, pass, id);
        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        } catch (MalformedURLException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        } catch (ClassCastException e) {
            Log.d(CONNECTOR_NAME, e.toString()); // Bad login or password
        }
        return connection;
    }

    /**
     * @param conditions You can pass new Object[0] to specify an empty list of conditions and no fields,
     * which will return all the ids for that model.
     * @param fields All the fields that you want to show
     * @return The fields of matching objects and if have relations with other model returns the name of the relation.
     */

    public List<HashMap<String, Object>> search_read(String model, Object[] conditions, String... fields) {
        if (fields.length == 0) {
            Object[] field = {"id"};
            return search_read(model, 0, 0, conditions, field);
        }
        ArrayList<String> field = new ArrayList<String>();
        for (String par : fields) {
            field.add(par);
        }
        Object[] fieldsL = field.toArray();
        return search_read(model, 0, 0, conditions, fieldsL);
    }

    /**
    * @param offset Record where start to show
    * @param limit Number of records to show: 0 -> all
    */
    public List<HashMap<String, Object>> search_read(String model, Integer offset, Integer limit,
                                                     Object[] conditions, String... fields) {
        if (fields.length == 0) {
            Object[] field = {"id"};
            return search_read(model, offset, limit, conditions, field);
        }
        ArrayList<String> field = new ArrayList<String>();
        for (String par : fields) {
            field.add(par);
        }
        Object[] fieldsL = field.toArray();
        return search_read(model, offset, limit, conditions, fieldsL);
    }

    @SuppressWarnings("unchecked")
    private List<HashMap<String, Object>> search_read(String model, final Integer offset,
                                                      final Integer limit, Object[] conditions,
                                                      final Object[] field) {
        List<HashMap<String, Object>> result = null;
        try {
            XMLRPCClient client = new XMLRPCClient(mUrl);

            Object[] parameters = {mDatabase, mUserId, mPassword,
                    model, "search_read", conditions, new HashMap() {{
                put("fields", field);
                put("limit", limit);
                put("offset", offset);
            }}};

            Object[] record = (Object[]) client.call("execute_kw", parameters);
            result = new ArrayList<>(record.length);

            for (Object oField : record) {

                HashMap<String, Object> listFields = (HashMap<String, Object>) oField;
                Set<String> keys = listFields.keySet();

                Object[] param = {mDatabase, mUserId, mPassword,
                        model, "fields_get", new Object[]{keys}, new HashMap() {{
                    put("attributes", new Object[]{"relation", "type"});
                }}};
                Map<String, Map<String, Object>> attrRelation =
                        (Map<String, Map<String, Object>>) client.call("execute_kw", param);

                for (String key : keys) {

                    if (attrRelation.get(key).containsValue("many2one")) {
                        System.out.println(listFields.get(key).toString());
                        if(!listFields.get(key).toString().equals("country_id")) {
                            System.out.println(listFields.get(key));
                            List fRelation = asList((Object[]) listFields.get(key));
                            Object f = (Object) fRelation.get(1); // 1 => name
                            listFields.put(key, f);
                        }

                    } else if (attrRelation.get(key).containsValue("many2many") ||
                            attrRelation.get(key).containsValue("one2many")) {
                        List fRelation = asList((Object[]) listFields.get(key));

                        String modelR = attrRelation.get(key).get("relation").toString();
                        final Object[] fieldR = {"name"};

                        Object[] parame = {mDatabase, mUserId, mPassword,
                                modelR, "read", new Object[]{fRelation}, new HashMap() {{
                            put("fields", fieldR);
                        }}};
                        Object[] recordd = (Object[]) client.call("execute_kw", parame);

                        /*
                         * You can change the string format of this result like you prefer.
                         */
                        String extra = "";
                        for (Object r : recordd){
                            extra += r;
                        }
                        Object fResult = (Object) extra;
                        listFields.put(key, fResult);
                    }
                }
                result.add((HashMap<String, Object>) oField);
            }
        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        }
        return result;
    }

    /**
     * You can pass new Object[0] to specify an empty list of conditions,
     * which will return all the ids for that model.
     *
     * @return The ids of matching objects.
     */

    public Integer search_count(String model, Object[] conditions) {
        Integer result = null;
        try {
            XMLRPCClient client = new XMLRPCClient(mUrl);

            Object[] parameters = {mDatabase, mUserId, mPassword, model, "search_count", conditions};
            result = (Integer) client.call("execute_kw", parameters);

        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        }
        return result;
    }

    /**
     * Creates a new record for the given model width the values supplied.
     *
     * @param values
     * Remember: In order to add different types in a Collection use Object, e.g. <br>
     * <code>
     * HashMap values = new HashMap(); <br>
     * values.put("name", "hello"); <br>
     * values.put("number", 10); <br>
     * </code>
     */
    public Integer create(String model, HashMap values) {
        Integer newObjectId = null;
        try {
            XMLRPCClient client = new XMLRPCClient(mUrl);

            Object[] parameters = {mDatabase, mUserId, mPassword, model, "create", new Object[]{values}};
            newObjectId = (Integer) client.call("execute_kw", parameters);

        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        }
        return newObjectId;
    }

    /**
     * Used to modify an existing object.
     *
     * @param values fields to change
     */
    public Boolean write(String model, Object[] id, HashMap values) {
        Boolean writeOk = false;
        try {
            XMLRPCClient client = new XMLRPCClient(mUrl);
            Object[] parameters = {mDatabase, mUserId, mPassword, model, "write", new Object[]{
                    id, values}};
            writeOk = (Boolean) client.call("execute_kw", parameters);
        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        }
        return writeOk;
    }

    /**
     * A method to delete the matching records width the ids given
     */
    public Boolean unlink(String model, Object[] ids) {
        Boolean unlinkOk = false;
        try {
            XMLRPCClient client = new XMLRPCClient(mUrl);
            Object[] parameters = {mDatabase, mUserId, mPassword, model, "unlink",
                    new Object[]{ids}};
            unlinkOk = (Boolean) client.call("execute_kw", parameters);
        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        }
        return unlinkOk;
    }

    /**
     * This is a generic method to call any WS.
     */
    @SuppressWarnings("unchecked")
    public Object[] call(String model, String method, final Integer offset,
                       final Integer limit, Object[] conditions,
                       final Object[] field) {
        Object[] response = null;
        try {
            XMLRPCClient client = new XMLRPCClient(mUrl);

            Object[] parameters = {mDatabase, mUserId, mPassword,
                    model, method, conditions, new HashMap() {{
                put("fields", field);
                put("limit", limit);
                put("offset", offset);
            }}};

            response = (Object[]) client.call("execute_kw", parameters);
        } catch (XMLRPCException e) {
            Log.d(CONNECTOR_NAME, e.toString());
        }
        return response;
    }

    /**
     * @return String representation of the OdooConnection instance, good for
     * debugging purposes. You can comment the password if you want.
     */
    public String toString() {
        StringBuilder stringConn = new StringBuilder();
        stringConn.append("server: " + mServer + "\n");
        stringConn.append("port: " + mPort + "\n");
        stringConn.append("database: " + mDatabase + "\n");
        stringConn.append("user: " + mUserName + "\n");
        stringConn.append("password: " + mPassword + "\n");
        stringConn.append("id: " + mUserId + "\n");
        return stringConn.toString();
    }
}
