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

import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ontology.profile.User;

import eu.city4ageproject.delivery.model.DeliveryRequest;
import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Telephone;

/**
 * @author amedrano
 *
 */
public class Delivery extends UICaller {

	private InfoGrabber info;

	/**
	 * @param context
	 */
	protected Delivery(ModuleContext context) {
		super(context);
		info = new InfoGrabber(context);
	}
	
	public boolean sendIntervention(DeliveryRequest dreq){
		String userURI = info.getPilotVcardService(dreq.getPilotID()) + dreq.getUserID();
		VCard userInfo = info.getVCard(dreq.getPilotID(), dreq.getUserID());
		String message = dreq.getIntervention().toString();
		
		if (userInfo == null || message == null){
			return false;
		}
		
		//TODO check for Telephone and adjust channel.
		Form f = Form.newDialog((String) null, (Resource) null);
		new SimpleOutput(f.getIOControls(), null, null, processCellPhoneNo(userInfo));
		new SimpleOutput(f.getIOControls(), null, null, message);
		
		//TODO Adjust to actual language
		Locale lang = Locale.ENGLISH;
		
		UIRequest uireq = new UIRequest(new User(userURI), f, LevelRating.high, lang, PrivacyLevel.personal);
		uireq.setPresentationModality(Modality.sms);
		sendUIRequest(uireq);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void communicationChannelBroken() {

	}

	/** {@inheritDoc} */
	@Override
	public void dialogAborted(String dialogID, Resource data) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public void handleUIResponse(UIResponse uiResponse) {
		// TODO Auto-generated method stub

	}
	
	static String processCellPhoneNo(VCard card){
		String phone = "";
		tels:
		for (Telephone tel : card.getTelephoneNumbers()) {
			for (TelephoneType ttype : tel.getTypes()) {
				if (ttype.equals(TelephoneType.CELL)
						|| ttype.equals(TelephoneType.MSG)
						|| ttype.equals(TelephoneType.PAGER)
						|| ttype.equals(TelephoneType.TEXT)
						|| ttype.equals(TelephoneType.TEXTPHONE)){
					phone = tel.toString();
					break tels;
				}
			}
		}
		//remove whitespaces
		phone.replaceAll("\\w", "");
		//remove preceding "+"
		phone.replaceAll("^\\+", "");
		//remove preceding "0"s
		phone.replaceAll("^0+", "");
		
		return phone;
		
	}

}
