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
    String ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_DOES_NOT_HAVE_MAIN_FOUNDER = "Company leader and ownership setup has main admin.";
    String ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_HAS_MULTIPLE_MAIN_FOUNDERS = "Company leader and ownership setup has multiple main founders. Only one main founder is allowed.";
    String ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_HAS_DUPLICATE_ADMIN_EMAILS = "Company leader and ownership setup has duplicate admin emails. Each admin email must be unique.";
    String ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_HAS_DUPLICATE_ADMIN_PHONES = "Company leader and ownership setup has duplicate admin phones. Each admin phone must be unique.";
    String ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_REQUEST_IS_EMPTY = "At least one admin is required to setup company leader and ownership.";
    String ERROR_COMPANY_TRACK_RECORD_CREDIBILITY_MAJOR_PROJECTS_DELIVERED_SOCIAL_MEDIA_LINKS_IS_REQUIRED = "At least one major project delivered social media link is required to setup company track record credibility.";
    String ERROR_COMPANY_APP_ACCESS_DISABLED = "Company app access is disabled. Please contact support for assistance.";
    
    // KYC Level 2 - Banking and Financial Accounts
    String ERROR_KYC_LEVEL_ONE_NOT_COMPLETED = "KYC Level 1 must be completed before starting KYC Level 2.";
    String ERROR_BANK_NAME_REQUIRED = "Bank name is required.";
    String ERROR_ACCOUNT_NAME_REQUIRED = "Account name is required.";
    String ERROR_ACCOUNT_NUMBER_REQUIRED = "Account number is required.";
    String ERROR_ACCOUNT_TYPE_REQUIRED = "Account type is required.";
    String ERROR_ACCOUNT_CURRENCY_REQUIRED = "Account currency is required.";
    
    // KYC Level 2 - Authorized Signatories and Control
    String ERROR_PRIMARY_SIGNATORY_NAME_REQUIRED = "Primary signatory name is required.";
    String ERROR_SIGNATORY_ROLE_REQUIRED = "Signatory role is required.";
    String ERROR_SIGNATORY_EMAIL_REQUIRED = "Signatory email is required.";
    String ERROR_SIGNATORY_PHONE_REQUIRED = "Signatory phone number is required.";
    
    // KYC Level 2 - Financial Integrity and Risk Controls
    String ERROR_PRIMARY_REVENUE_SOURCES_REQUIRED = "At least one primary revenue source is required.";
    String ERROR_EXPECTED_TRANSACTION_VOLUME_REQUIRED = "Expected transaction volume is required.";
    String ERROR_SOURCE_OF_FUNDS_DECLARATION_REQUIRED = "At least one source of funds declaration is required.";
    String ERROR_PEP_ROLE_REQUIRED = "PEP role is required when politically exposed person is Yes.";
    String ERROR_PEP_COUNTRY_REQUIRED = "PEP country is required when politically exposed person is Yes.";
    String ERROR_PEP_YEAR_REQUIRED = "PEP year is required when politically exposed person is Yes.";
    String ERROR_LITIGATION_NATURE_REQUIRED = "Litigation nature is required when litigation/bankruptcy/insolvency is Yes.";
    String ERROR_LITIGATION_YEAR_REQUIRED = "Litigation year is required when litigation/bankruptcy/insolvency is Yes.";
    String ERROR_LITIGATION_STATUS_REQUIRED = "Litigation current status is required when litigation/bankruptcy/insolvency is Yes.";
    String ERROR_SANCTIONS_PARTY_REQUIRED = "Sanctions party affected is required when subject to sanctions is Yes.";
    String ERROR_SANCTIONS_NATURE_REQUIRED = "Sanctions nature is required when subject to sanctions is Yes.";
    String ERROR_SANCTIONS_STATUS_REQUIRED = "Sanctions current status is required when subject to sanctions is Yes.";
    
    // KYC Level 2 - Execution and Reporting Readiness
    String ERROR_FINANCE_LEAD_FULL_NAME_REQUIRED = "Finance lead full name is required.";
    String ERROR_FINANCE_LEAD_ROLE_REQUIRED = "Finance lead role is required.";
    String ERROR_FINANCE_LEAD_EMAIL_REQUIRED = "Finance lead email is required.";
    String ERROR_FINANCE_LEAD_PHONE_REQUIRED = "Finance lead phone number is required.";
    String ERROR_ACCOUNTING_SYSTEM_REQUIRED = "Accounting system used is required.";
    String ERROR_FINANCIAL_REPORTING_FREQUENCY_REQUIRED = "Financial reporting frequency is required.";
    String ERROR_TYPE_OF_ARRANGEMENT_REQUIRED = "At least one type of arrangement is required when past escrow use is Yes.";
    String ERROR_PURPOSE_OF_ESCROW_REQUIRED = "Purpose of escrow is required when past escrow use is Yes.";
    String ERROR_COUNTERPARTY_PLATFORM_REQUIRED = "Counterparty/platform used is required when past escrow use is Yes.";
    String ERROR_DURATION_OF_AGREEMENT_REQUIRED = "Duration of arrangement is required when past escrow use is Yes.";
    
    // KYC Level 2 - Capital Governance Agreement
    String ERROR_CONSENT_MILESTONE_DISBURSEMENT_REQUIRED = "Consent to milestone-based disbursement is required.";
    String ERROR_CONSENT_ESCROW_ACCOUNT_REQUIRED = "Consent to escrow or controlled account is required.";
    String ERROR_CONSENT_THIRD_PARTY_MONITORING_REQUIRED = "Consent to third-party monitoring is required.";
    String ERROR_UNDERSTAND_SUSPENSION_POLICY_REQUIRED = "Understanding of suspension policy is required.";
    String ERROR_DIGITAL_SIGNATURE_REQUIRED = "Digital signature is required.";
    String ERROR_AGREEMENT_DATE_REQUIRED = "Agreement date is required.";
}
