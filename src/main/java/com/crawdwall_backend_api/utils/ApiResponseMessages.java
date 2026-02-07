package com.crawdwall_backend_api.utils;

public interface ApiResponseMessages {

    // User-related messages
    String ERROR_USER_INVALID_ACCOUNT = "Your account appears to be invalid. Please contact support for assistance.";
    String ERROR_USER_NOT_FOUND = "Error fetching user information.";
    String ERROR_USER_ACCOUNT_ALREADY_ACTIVATED = "Your account has already been activated. You can proceed to login.";
    String ERROR_USER_EXPIRED_INVITE_LINK = "This activation link has expired or is no longer valid.";
    String ERROR_USER_ACCOUNT_BLOCKED = "Your account has been temporarily blocked. Please contact our support team for help.";
    String ERROR_USER_ACCOUNT_NOT_VERIFIED = "Your account hasn't been verified yet. Please check your email for the activation link.";
    String ERROR_USER_INVALID_EMAIL_OR_PASSWORD = "The email or password you entered doesn't match.";
    String ERROR_USER_INCORRECT_OLD_PASSWORD = "The current password you entered is incorrect. Please try again.";
    String ERROR_USER_SAME_PASSWORD = "The new password cannot be the same as the current password.";
    String ERROR_USER_EMAIL_ALREADY_EXISTS = "This email is already in use.";
    String ERROR_USER_2FA_ALREADY_ENABLED = "Two-factor authentication is already enabled on your account.";
    String ERROR_USER_2FA_ALREADY_DISABLED = "Two-factor authentication is already disabled on your account.";
    String DOCUMENT_NOT_FOUND = "Document not found.";
    String ERROR_ACCOUNT_ALREADY_IN_DESIRED_STATE =   "account already in desired status";
    String ERROR_USER_PHONE_NUMBER_ALREADY_EXISTS = "This phone number is already in use.";
    String ERROR_PROCESSING_REQUEST_INVALID_FILE_CATEGORY = "Invalid file category.";

    String ERROR_USER_ACCOUNT_NOT_ACTIVE =  " account is not active.";
    String ERROR_ROLE_NOT_FOUND = "Role not found.";
    String ERROR_FETCHING_ADMINS_DETAILS = "Error fetching admins details.";
    String ERROR_ROLE_NAME_ALREADY_EXISTS = "Role Name Already Exist";
    String ERROR_FETCHING_ROLES_DETAILS = "Error Fetching Role Details";
    String ERROR_ADMIN_NOT_FOUND = "Admin Information Does not Exist";
    String ERROR_PROCESSING_QUEST = "Error processing question.";
    String ERROR_COMPANY_NAME_ALREADY_EXISTS = "Company name already exists.";
    String ERROR_COMPANY_EMAIL_ALREADY_EXISTS = "Company email already exists.";
    String ERROR_COMPANY_PHONE_ALREADY_EXISTS = "Company phone already exists.";
    String ERROR_COMPANY_REGISTRATION_NUMBER_ALREADY_EXISTS = "Company registration number already exists.";
    String ERROR_COMPANY_NOT_FOUND = "Company not found.";
    String ERROR_TERMS_AND_CONDITION_ERROR = "You must accept the terms and conditions to proceed";
}
