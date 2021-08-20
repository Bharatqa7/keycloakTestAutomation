#Author: your.email@your.domain.com

@UserManagement
Feature: As an authenticated keycloak administrator, I want to create a new user in KC via the Admin REST API in order to set up authentication for the user

  @CreateUser @UserManagement
  Scenario Outline: I want to Create a new user in Keycloak
    Given I am an authenticated keycloak as '<AuthUser>' in '<Realm>' Realm
    When I create a new user with Username : '<Username>' and FirstName : '<Firstname>' and Email : '<Email>'
    Then I get response code of '<ResponseCode>'

    Examples: 
      | AuthUser      | Realm  |Username      | Firstname      | Email              | ResponseCode |
      | Administrator | master |TestUserName1 | TestFirstname1 | TestEmail1@abc.com | 409          |
      | Administrator | master |TestUserName2 | TestFirstname2 | TestEmail2@abc.com | 409          |