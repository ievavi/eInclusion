package org.einclusion.model;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;
import com.google.gson.Gson;
import javax.persistence.*;
import org.apache.log4j.Logger;
/**
 * 	This class is used to connect to the database and manage the data by using entities
 * 	@author student
 */
@Entity
public class ModelManager implements Serializable {
	private static final long serialVersionUID = 1002L;
	protected static String persistenceSet;
	protected static EntityManager entityManager;
	protected static EntityTransaction transaction;
	protected static InstanceManager instanceManager;
	private static Map<String, Object> props;
	private static EntityManagerFactory factory;
	private static final Logger LOG = Logger.getLogger(ModelManager.class);

	/**
	 * All model manager related settings and intermediate data are stored in
	 * Key: Value pairs in ModelManager table
	 */
	@Id
	@Column(nullable = false)
	private String key;

	/**
	 * For better debugging values are stored as Strings If Object structure
	 * needs to be stored it is serialized as JSON using set/getObjectValue
	 * methods
	 */
	@Column(nullable = true)
	private String value;
	
	@Column(nullable = true)
	private String coefficient;
	
	@Column(nullable = true)
	private String relative;

	/**
	 * Used to create common ModelDataManager context for all data operations 
	 * @param persistenceSet - choose PERSISTENCE_SET values from persistence-unit
	 * name="..." value in persistence.xml file
	 */
	public static void initModelManager(String persistenceSet) {
		ModelManager.persistenceSet = persistenceSet;
		factory = Persistence.createEntityManagerFactory(persistenceSet);
		LOG.info("Model manager initialized");
		LOG.debug("Persistence factory settings:\n\t\t"
				+ factory.getProperties().toString().replace(", ", "\n\t\t"));
		entityManager = factory.createEntityManager();
		transaction = entityManager.getTransaction();
	}

	/**
	 * 	Used to close common ModelDataManager
	 */
	public static void closeModelManager() {
		if (transaction != null) {
			transaction = null;
		}
		if (entityManager != null) {
			entityManager.close();
			entityManager = null;
		}
		if (factory != null) {
			factory.close();
			factory = null;
		}
		LOG.info("ModelManager resources released successfully");
	}

	static void setNumberValue(String key, Number value) {
		String str = String.valueOf(value);
		setStringValue(key, str);
	}

	static void setStringValue(String key, String value) {
		try {
			transaction.begin();
			ModelManager data = new ModelManager();
			data.key = key;
			data.value = value;
			entityManager.merge(data);
			LOG.debug("Model data key:" + data.key + " value:" + data.value);
			transaction.commit();
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
	}
	
	static void setStringValue(String key, String value, String coefficient) {
		try {
			transaction.begin();
			ModelManager data = new ModelManager();
			data.key = key;
			data.value = value;
			data.coefficient = coefficient;
			entityManager.merge(data);
			LOG.debug("Model data key:" + data.key + " value:" + data.value + " coef: " + data.coefficient);
			transaction.commit();
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
	}
	
	static void setStringValue(String key, String value, String coefficient, String relative) {
		try {
			transaction.begin();
			ModelManager data = new ModelManager();
			data.key = key;
			data.value = value;
			data.coefficient = coefficient;
			data.relative = relative;
			entityManager.merge(data);
			LOG.debug("Model data key:" + data.key + " value:" + data.value + " coef: " + data.coefficient + " rel: "+ data.relative);
			transaction.commit();
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
	}
	
	static void setStringCoefficient(String key, String coefficient) {
		try {
			transaction.begin();
			ModelManager data = new ModelManager();
			data.key = key;
			data.coefficient = coefficient;
			entityManager.merge(data);
			LOG.debug("Model data key:" + data.key + " coef: " + data.coefficient);
			transaction.commit();
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
	}
	
	static String getStringValue(String key) {
		ModelManager tmp = entityManager.find(ModelManager.class, key);
		return tmp.value;
	}
	
	static String getStringCoefficient(String key) {
		ModelManager tmp = entityManager.find(ModelManager.class, key);
			return tmp.coefficient;
	}
	
	static String getRelativeCoefficient(String key) {
		ModelManager tmp = entityManager.find(ModelManager.class, key);
			return tmp.relative;
	}

	static Integer getIntValue(String key) {
		ModelManager tmp = entityManager.find(ModelManager.class, key);
		return Integer.parseInt(tmp.value);
	}

	static Double getDoubleValue(String key) {
		ModelManager tmp = entityManager.find(ModelManager.class, key);
		return Double.parseDouble(tmp.value);
	}

	/**
	 * @param key — name of the unique value set
	 * @param object — Object which will be serialized and saved in JSON
	 */
	static void setObjectValue(String key, Object object) {
		Gson gson = new Gson();
		// Create JSON data
		String json = gson.toJson(object);
		setStringValue(key, json);
	}
	
	static void setObjectValue(String key, Object object, String coefficient) {
		Gson gson = new Gson();
		// Create JSON data
		String json = gson.toJson(object);
		setStringValue(key, json, coefficient);
	}
	
	static void setObjectValue(String key, Object object, String coefficient, String relative) {
		Gson gson = new Gson();
		// Create JSON data
		String json = gson.toJson(object);
		setStringValue(key, json, coefficient, relative);
	}

	/**
	 * @param key — name of the unique value set
	 * @param objclass — object.getClass() to restore object structure properly
	 * @return Object which should be casted to appropriate type after getting
	 *         reference to it
	 */
	static Object getObjectValue(String key, Class<?> objclass) {
		Gson gson = new Gson();
		StringReader sr = new StringReader(getStringValue(key));
		// convert the JSON back to object using specified class
		Object obj = gson.fromJson(sr, objclass);
		return obj;
	}

	static void getProperties() {
		props = factory.getProperties();
		LOG.debug("ModelProperties: " + props);
	}

	static String getURL() {
		if (props == null)
			getProperties();
		return props.get("hibernate.connection.url").toString();
	}

	static String getUser() {
		String val;
		val = props.get("hibernate.connection.user").toString();
		LOG.debug("ModelProperties:User: " + val);
		return val != null ? val : "";
	}

	static String getPassword() {
		String val = "";
		try {
			val = props.get("hibernate.connection.password").toString();
			LOG.debug("ModelProperties:password: " + val);
		} catch (Exception e) {
			LOG.warn("Could not get ModelProperties:password");
			return val;
		}
		return val;
	}

}
