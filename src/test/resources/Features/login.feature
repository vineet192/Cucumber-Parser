Feature: Login functionality
  Scenario Outline: Simple login test
    Given user is on login page
    When user enters "<username>" and "<password>"
    And user clicks on login
    Then user is navigated to homepage
    
    #!Simple login test

	
	
	
	Scenario Outline: Another simple login test
    Given user is on login page
    When user enters "<username>" and "<password>"
    And user clicks on login
    Then user is navigated to homepage
    
    #!Another simple login test
