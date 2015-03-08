package cinglevue.model;

/*
 * Using an interface can allow for quick replacement of the underlying database implementation.
 */
public interface DBManager {

	/*
	 * updateSchool updates a school with a specific code with new values for all fields (except code).
	 */	
	public void updateSchool(String code, String name, String address,
			String emailDomain);

	/*
	 * Retrieves the collection of all schools from the database
	 */
	public String findAllSchools();
	
	/*
	 * Add new school if this code does not exist.
	 */
	public void addNew(String code, String name, String address, String emailDomain);

}
