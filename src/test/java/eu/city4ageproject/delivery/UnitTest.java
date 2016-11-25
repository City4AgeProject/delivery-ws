package eu.city4ageproject.delivery;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;

import com.google.gson.GsonBuilder;

import eu.city4ageproject.delivery.model.DeliveryRequest;
import ezvcard.Ezvcard;
import ezvcard.VCard;

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
    	
    	assertEquals("007",object.getUserID());
    	assertEquals("HOLA", object.getIntervention().getSalutation());
    }
    
    public void testJcard() throws IOException{
    	InputStream i = getClass().getClassLoader().getResourceAsStream("officialExample");
    	assertNotNull(i);
    	VCard vcard = Ezvcard.parseJson(i).first();
    	assertNotNull(vcard);
    }
    
    public void testResolution() throws IOException{
    	JUnitModuleContext mc = new JUnitModuleContext();
    	
    	InfoGrabber ig = new InfoGrabber(mc);
    	
    	assertNotNull(ig.resolveConfigFile());
    	assertEquals("file:./target/test-classes/testVcards/", ig.getPilotVcardService("404"));
    	
    	VCard vc = ig.getVCard("404", "1");
    	assertNotNull(vc);
    }
}
