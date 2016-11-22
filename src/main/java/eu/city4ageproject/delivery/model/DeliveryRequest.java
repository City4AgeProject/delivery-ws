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
package eu.city4ageproject.delivery.model;

/**
 * @author amedrano
 *
 */
public class DeliveryRequest {

	String userID;
	String pilotID;
	
	SimpleIntervention intervention;
	
	boolean contains_variables;

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the pilotID
	 */
	public String getPilotID() {
		return pilotID;
	}

	/**
	 * @param pilotID the pilotID to set
	 */
	public void setPilotID(String pilotID) {
		this.pilotID = pilotID;
	}

	/**
	 * @return the intervention
	 */
	public SimpleIntervention getIntervention() {
		return intervention;
	}

	/**
	 * @param intervention the intervention to set
	 */
	public void setIntervention(SimpleIntervention intervention) {
		this.intervention = intervention;
	}

	/**
	 * @return the contains_variables
	 */
	public boolean isContains_variables() {
		return contains_variables;
	}

	/**
	 * @param contains_variables the contains_variables to set
	 */
	public void setContains_variables(boolean contains_variables) {
		this.contains_variables = contains_variables;
	}
	
}
