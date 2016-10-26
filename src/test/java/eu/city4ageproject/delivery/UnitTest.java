package eu.city4ageproject.delivery;

import com.google.gson.GsonBuilder;

import eu.city4ageproject.delivery.model.DeliveryRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * UnitTest Class, a collection of unitary tests of different methods in the module.
 * 
 * For each set of test create a <code>public void *test()</code> method, then 
 * check the correct status by adding assertions.
 * 
 * @see TestCase
 * @see <a href=http://junit.org/junit4/> JUnit framework</a>
 *
 */
public class UnitTest extends TestCase
{


    public void testDeserialization()
    {
    	String serialised = "{" +
    			"\"userID\": \"007\"," +
    			"\"pilotID\": \"10\"," +
    			"\"contains_variables\": \"false\"," +
    			"\"intervention\": {" +
    				"\"salutation\": \"HOLA\"," +
    				"\"body\": \"comoandamos?\"," +
    				"\"closing\": \"hasta luego\"," +
    				"\"signature\": \"tu medico.\"" +
    			"}" +
    		"}";
    	System.out.println(serialised);
    	
    	GsonBuilder gsonBuilder = new GsonBuilder();
    	DeliveryRequest object = gsonBuilder.create().fromJson(serialised, DeliveryRequest.class);
   
    	assertNotNull(object);
    	
    	assertEquals(7,object.getUserID());
    	assertEquals("HOLA", object.getIntervention().getSalutation());
    }
}
