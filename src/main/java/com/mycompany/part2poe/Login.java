/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.part2poe;

/**
 *
 * @author st104
 */
public class Login {
    
    // Stores the registered user's details so login can verify against them later
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    
    //Check that the username contains an underscore and is not more than 5 characters long
    public boolean checkUserName(String username){
        if (username == null){
            return false;
        }
        return username.contains("_")&& username.length() <= 5;
    }
    
    //Check that the password meets the requirements
    public boolean checkPasswordComplexity(String password){
        if (password == null || password.length() < 8){
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()){
            if (Character.isUpperCase(c)){
                hasCapital = true;
            } else if (Character.isDigit(c)){
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)){
                hasSpecialChar = true;
            }
        }
        return hasCapital && hasNumber && hasSpecialChar;
    }
    
    // Checks that the cell phone number starts with the South African international code (+27)
    // followed by 9 digits, matching the required international format
    // Reference: regex pattern adapted from general E.164 phone number validation conventions (regular-expressions.info)
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
    if (cellPhoneNumber == null) {
        return false;
    }
    String regex = "^\\+27[0-9]{9,10}$";
    return cellPhoneNumber.matches(regex);
    }
    // Validates the username and password, stores the user's details if both are valid,
    // and returns the appropriate success or failure message
    public String registerUser(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
    if (!checkUserName(username)) {
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }
    if (!checkPasswordComplexity(password)) {
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }
    if (!checkCellPhoneNumber(cellPhoneNumber)){
        return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    // Both checks passed, so store the user's details
    this.username = username;
    this.password = password;
    this.cellPhoneNumber = cellPhoneNumber;
    this.firstName = firstName;
    this.lastName = lastName;

    return "Username successfully captured.\nPassword successfully captured.\nCell number successfully captured.\nRegistration successful.";
    }
    
    //Compares the entered username and password agaist the stored details
    //Return true if they match and false otherwise
    public boolean loginUser(String enteredUsername, String enteredPassword){
        return enteredUsername.equals(this.username) && enteredPassword.equals(this.password);
    }
    
    //Return the correct welcome message if log in succeeded, or a failure message if not
    public String returnLoginStatus(boolean loginSuccess){
        if(loginSuccess){
           return "Welcome " + this.firstName + ", " + this.lastName + " it is great to see you again."; 
        } else{
            return "Username or password incorrect, please try again.";
        }
    }
    
    
}
