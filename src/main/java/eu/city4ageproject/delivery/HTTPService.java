/*******************************************************************************
 * Copyright 2016 Universidad Politécnica de Madrid UPM
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

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.ri.servicegateway.GatewayPort;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import eu.city4ageproject.delivery.model.DeliveryRequest;

/**
 * @author amedrano
 *
 */
public final class HTTPService extends GatewayPort {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8571701583484962621L;
	private Delivery delivery;

	
	/**
	 * @param mcontext
	 */
	public HTTPService(ModuleContext mcontext) {
		super(mcontext);
		delivery = new Delivery(mcontext);
	}

	private GsonBuilder gsonBuilder = new GsonBuilder();
	
	/**{@inheritDoc} */
	@Override
	public String url() {
		return "/delivery";
	}

	/**{@inheritDoc} */
	@Override
	public String dataDir() {
		return "dir";
	}

	
	
	
	/**{@inheritDoc} */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getContentType()!= null 
				&& req.getContentType().toLowerCase().contains("application/json")
//				&& req.getCharacterEncoding() != null
//				&& req.getCharacterEncoding().toLowerCase().contains("utf")
//				&& req.getCharacterEncoding().toLowerCase().contains("8")
				){
			ServletInputStream is = req.getInputStream();
			String request = IOUtils.toString(is, "UTF-8"); 
			try {
				DeliveryRequest drequest = gsonBuilder.create().fromJson(request, DeliveryRequest.class);
				if (drequest.getPilotID() != null 
						&& !drequest.getPilotID().isEmpty()
						&& drequest.getUserID() != null
						&& !drequest.getUserID().isEmpty()
						&& drequest.getIntervention() !=null) {
					//connect to uAAL
					if (delivery.sendIntervention(drequest))
						resp.setStatus(HttpServletResponse.SC_ACCEPTED);
					else
						resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return;
				}
				
				//TODO get parameters into the Actual delivery
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				
			}
			resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			PrintStream ps = new PrintStream(resp.getOutputStream());
			ps.print("Request not acceptable.\n cotnent is not compliant to schema.");
		}
		else {
			resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			PrintStream ps = new PrintStream(resp.getOutputStream());
			ps.print("Request not acceptable.<br> " +
					"Content-Type: application/json <br>" 
//					+" Content-Encoding: UTF-8"
					);
		}
	}

	
	
}
