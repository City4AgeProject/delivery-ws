/*******************************************************************************
 * Copyright 2016 2011 Universidad Politécnica de Madrid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.city4ageproject.delivery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.universAAL.middleware.container.ModuleContext;


import ezvcard.Ezvcard;
import ezvcard.VCard;

/**
 * @author amedrano
 *
 */
public class InfoGrabber {
	
	/**
	 * 
	 */
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static String PROP_PREFIX = "delivery.vcard.server.";
	
	enum VcardType { VCARD, XCARD, JCARD, HCARD, TURTLE};
	
	private ModuleContext mc;
	
	/**
	 * 
	 */
	public InfoGrabber(ModuleContext mctxt) {
		mc = mctxt;
	}
	
	public VCard getVCard(int pilotID, int userID){
		
		//resolve pilot URL Service
		URL service = null;
		try {
			service = new URL(getPilotVcardService(pilotID) + Integer.toString(userID));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//get Serialised card for userID
		ServiceGrabber sg = new ServiceGrabber(service);
		
		//parse vCard
		if (sg.type != null){
			VCard res = parse(sg.serialised, sg.type);
			if (res != null){
				return res;
			}
		}
		for (VcardType t : VcardType.values()) {
			VCard res = parse(sg.serialised, t);
			if (res != null){
				return res;
			}
		}
		
		return null;
	}
	
	InputStream resolveConfigFile() throws FileNotFoundException{
		File configDir = mc.getConfigHome();
		File conf = new File(configDir,CONFIG_FILE_NAME);
		if (conf.exists()){
			return new FileInputStream(conf);
		}
		configDir = mc.getDataFolder();
		conf = new File(configDir,CONFIG_FILE_NAME);
		if (conf.exists()){
			return new FileInputStream(conf);
		}
		return getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
	}
	
	String getPilotVcardService(int pilotID){
		String service = "";
		try {
			InputStream i = resolveConfigFile();
			Properties props = new Properties();
			props.load(i);
			service = props.getProperty(PROP_PREFIX + Integer.toString(pilotID));
			if (!service.endsWith("/")){
				service += "/";
			}
		} catch (IOException e) {
			System.err.println("PANIC: Unable to load config.properties");
			e.printStackTrace();
		}
		return service;
	}
	
	class ServiceGrabber{
		String serialised;
		VcardType type;
		/**
		 * 
		 */
		public ServiceGrabber(URL service) {
			if (service.getProtocol().startsWith("http")){
				//get through the HTTPClient
				try {
					CloseableHttpClient httpclient = HttpClients.createDefault();
					HttpGet httpget = new HttpGet(service.toString());
					CloseableHttpResponse response = httpclient.execute(httpget);
					InputStream is = response.getEntity().getContent();
					serialised = IOUtils.toString(is); 
					Header cth = response.getEntity().getContentType();
					if (cth != null){
						String type = cth.getValue();
						if (type.toLowerCase().contains("json")
								|| type.toLowerCase().contains("jcard")){
							this.type = VcardType.JCARD;
						}
						if (type.toLowerCase().contains("xml")
								|| type.toLowerCase().contains("xcard")){
							this.type = VcardType.XCARD;
						}
						if (type.toLowerCase().contains("html")
								|| type.toLowerCase().contains("hcard")){
							this.type = VcardType.HCARD;
						}
						if (type.toLowerCase().contains("vcard")){
							this.type = VcardType.VCARD;
						}
						if (type.toLowerCase().contains("ttl")){
							this.type = VcardType.TURTLE;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				//standard get
				try {
					InputStream i = service.openStream();
					serialised = IOUtils.toString(i); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	VCard parse(String serial, VcardType type){
		switch (type) {
		case VCARD:
			return Ezvcard.parse(serial).first();
			
		case JCARD:
			return Ezvcard.parseJson(serial).first();
			
		case XCARD:
			return Ezvcard.parseXml(serial).first();
			
		case HCARD:
			return Ezvcard.parseHtml(serial).first();
			
		case TURTLE:
			//TODO
			break;

		default:
			break;
		}
		return null;
	}
}
