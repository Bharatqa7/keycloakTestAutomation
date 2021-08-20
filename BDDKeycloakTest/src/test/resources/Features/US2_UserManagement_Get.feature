#Author: your.email@your.domain.com

@UserManagement
Feature: As an authenticated keycloak administrator, I want to retrieve the user representation of an existing user in KC in order to view the user’s detail

  @GetUser @UserManagement
  Scenario Outline: I want to get a existing user in Keycloak
    Given I am an authenticated keycloak as '<AuthUser>' in '<Realm>' Realm
    When I want to get deatils of user with ID : '<UserID>'
    Then I get response code of '<ReponseCode>'
    And I get get response body of type '<ResponseBodyType>'
    And I get key '<JsonKey1>' with value '<JsonValue1>' in response body
    And I get key '<JsonKey2>' with value '<JsonValue2>' in response body
    And I get key '<JsonKey3>' with value '<JsonValue3>' in response body
    Examples: 
      | AuthUser      | Realm  |UserID                               | ReponseCode | ResponseBodyType | JsonKey1  | JsonValue1    | JsonKey2  | JsonValue2     | JsonKey3  | JsonValue3         |
      | Administrator | master |ed235b3a-82f5-438a-b54d-37f51325060b | 200         | JSON             | username  | testusername1 | firstName | TestFirstname1 | email     | testemail1@abc.com |