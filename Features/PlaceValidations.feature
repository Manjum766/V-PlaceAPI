Feature: Validating Place API's

  @Sanity @Regression
  Scenario Outline: Verify if Place is being Succesfully added using AddPlaceAPI
    Given Add Place Payload with "<Name>"  "<Language>" "<Address>"
    When User Calls "AddPlaceAPI" with "POST" Http Request
    Then API Call got Success with Status Code <StatusCode>
    And Verify "status" in Response Body is "OK"
    And Verify "scope" in Response Body is "APP"
    And Verify place_Id created maps to "<Name>" using "GetPlaceAPI"

    Examples: 
      | Name      | Language | Address | StatusCode |
      | Varadaraj | Kannada  | Kengeri |        200 |

  @Sanity @Regression
  Scenario: Verify if Delete Place Functionality is Working
    Given Delete Place Payload
    When User Calls "DeletePlaceAPI" with "POST" Http Request
    Then API Call got Success with Status Code 200
    And Verify "status" in Response Body is "OK"
