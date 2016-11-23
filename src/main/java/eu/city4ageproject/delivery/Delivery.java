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

import eu.city4ageproject.delivery.model.DeliveryRequest;
import ezvcard.VCard;

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
	
	public void sendIntervention(DeliveryRequest dreq){
		String userURI = info.getPilotVcardService(dreq.getPilotID()) + dreq.getUserID();
		VCard userInfo = info.getVCard(dreq.getPilotID(), dreq.getUserID());
		String message = dreq.getIntervention().toString();
		
		Form f = Form.newDialog((String) null, (Resource) null);
		new SimpleOutput(f.getIOControls(), null, null, userInfo.getTelephoneNumbers().get(0));
		new SimpleOutput(f.getIOControls(), null, null, message);
		
		//TODO Adjust to actual language
		Locale lang = Locale.ENGLISH;
		
		UIRequest uireq = new UIRequest(new Resource(userURI), f, LevelRating.high, lang, PrivacyLevel.personal);
		uireq.setPresentationModality(Modality.sms);
		sendUIRequest(uireq);
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

}
