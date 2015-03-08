package cinglevue.model;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

/*
 * MongoDBManager works with MongoDB to retrieve schools list in a JSON collection format.
 * It allows the following operations: finding all schools, updating, inserting and deleting.
 */
public class MongoDBManager implements DBManager {
	DB db = null;
	
	/*
	 * This function is used to populate the initial school list.
	 */
	@SuppressWarnings("unused")
	private void createInitialData() {
		insertNewSchool("Francis Park High School", "004", "21 Richmond Road", "@francis-park.com.au");
		insertNewSchool("Straw Nest Primary School", "003", "10 Showground Road", "@straw-nest.com.au");
		insertNewSchool("Maria Teresa Piccio", "MTP", "Cape Street", "@maria-teresa.com.au");
		insertNewSchool("Nomen Nescio", "NN", "McDonald Street", "@nomen-nescio.com.au");
	}
	
	/*
	 * Connect to the DB and return the DB object
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	private DB getDB() {
		if (db == null) {
			db = (new MongoClient("localhost", 27017)).getDB("cinglevue");
		}
		return db;
	}
	
	/*
	 * Add an new school to the database
	 */
	private void insertNewSchool (String name, String code, String address, String emailDomain) {
		DBCollection dbCollection = getDB().getCollection("Schools");
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("name", name);
		basicDBObject.put("code", code);
		basicDBObject.put("address", address);
		basicDBObject.put("emailDomain", emailDomain);
		dbCollection.insert(basicDBObject);
	}
	
	/*
	 * Find one specific school. 
	 */
	private BasicDBObject findOneSchool(String code) {
		DBCollection dbCollection = getDB().getCollection("Schools");
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("code", code);
		DBCursor dbCursor = dbCollection.find(basicDBObject);
		if (dbCursor.hasNext()) {
			return basicDBObject;
		}
		return null;
	}
	
	/*
	 * deleteSchool deletes a specific school.
	 */	
	public void deleteSchool(String code) {
		DBCollection dbCollection = getDB().getCollection("Schools");
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("code", code);
		DBCursor dbCursor = dbCollection.find(basicDBObject);
		
		if (dbCursor.hasNext()) {
			BasicDBObject objectFound = (BasicDBObject) dbCursor.next();
			dbCollection.remove(objectFound);
		}
		
	}
	
	/*
	 * Retrieves the collection of all schools from the database
	 */
	public String findAllSchools() {
		DBCollection dbCollection = getDB().getCollection("Schools");
		DBCursor dbCursor = dbCollection.find();
		StringBuffer out = new StringBuffer ("[");
		while (dbCursor.hasNext()) {
			out.append(dbCursor.next());
			if (dbCursor.hasNext()) {
				out.append(",");
			}
		}
		out.append("]");
		System.out.println(out.toString());
		return out.toString();
	}
	
	/*
	 * Adds new school if this school code does not exist.
	 */
	public void addNew(String code, String name, String address, String emailDomain) {
		if (findOneSchool(code) == null) {
			insertNewSchool (name, code, address, emailDomain);
		}
	}
	
	/*
	 * updateSchool updates a school with a specific code with new values for all fields (except code).
	 */	
	public void updateSchool(String code, String name, String address, String emailDomain) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("code", code);
		
		BasicDBObject newBasicDBObject = new BasicDBObject();
		newBasicDBObject.put("code", code);
		newBasicDBObject.put("name", name);
		newBasicDBObject.put("address", address);
		newBasicDBObject.put("emailDomain", emailDomain);
		
		getDB().getCollection("Schools").findAndModify(basicDBObject, newBasicDBObject);		
	}
	
	/*
	 * Test only
	 */
	public static void main(String[] args) {
		MongoDBManager dbManager = new MongoDBManager();
		System.out.println("before update");
		dbManager.findAllSchools();
		
		dbManager.addNew("012", "trynew", "trynew", "trynew");
		System.out.println("after update");
		dbManager.findAllSchools();
	}


}
