package com.VRJD.Place.StepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;

public class Hooks {

	@Before("@DeletePlace")
	public void beforeScenario() throws IOException { // Execute this code only when place id is null
		Steps steps = new Steps();
		if (Steps.place_ID == null) {
			steps.add_Place_Payload_with("Varadaraj", "Kannada", "Kengeri");
			steps.user_calls_with_http_request("AddPlaceAPI", "POST");
			steps.verify_place_id_created_maps_to_using("Varadaraj", "getPlaceAPI");
		}
	}
}
