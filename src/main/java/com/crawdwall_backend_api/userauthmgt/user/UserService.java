package com.crawdwall_backend_api.userauthmgt.user;


import com.crawdwall_backend_api.userauthmgt.user.request.*;
import com.crawdwall_backend_api.userauthmgt.user.response.UserCreateResponse;
import com.crawdwall_backend_api.userauthmgt.user.response.UserForgotPasswordResponse;
import com.crawdwall_backend_api.userauthmgt.user.response.UserResponse;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;
import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpService;
import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.appsecurity.JwtService;
import com.crawdwall_backend_api.utils.emailsenderservice.EmailSenderService;
import com.crawdwall_backend_api.utils.exception.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
@Slf4j
@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserOtpService userOtpService;
	@Qualifier("mailgunEmailSenderServiceImpl")
	private final EmailSenderService emailSenderService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;
    @Value("${application.google.oauth.clientId}")
    private String googleClientId;

	public UserService(UserRepository userRepository, UserOtpService userOtpService, JwtService jwtService,
			EmailSenderService emailSenderService) {
		this.userRepository = userRepository;
		this.userOtpService = userOtpService;
		this.emailSenderService = emailSenderService;
        this.jwtService = jwtService;
	}

	/**
	 * Updates the provided user's information in the database.
	 *
	 * @param existingUser the user entity to be updated (must already exist in the
	 *                     database)
	 */
	public User updateUser(User existingUser) {
		existingUser.setUpdatedAt(LocalDateTime.now());
		return userRepository.save(existingUser);
	}


	/**
	 * Creates a new user account based on the provided user creation request.
	 * Checks for existing email, saves the user, generates an activation OTP,
	 * and sends an account activation email.
	 *
	 * @param userCreateRequest the request object containing user details
	 * @return UserCreateResponse containing the created user's information
	 * @throws ResourceExistsException if a user with the given email already exists
	 */
	public UserCreateResponse createUser(UserCreateRequest userCreateRequest) {

		if(userRepository.existsByEmailAddressIgnoreCase(userCreateRequest.emailAddress())) {
			throw new ResourceExistsException(ApiResponseMessages.ERROR_USER_EMAIL_ALREADY_EXISTS);
		}
	
			User savedUser = User.builder()
			.firstName(userCreateRequest.firstName())
			.lastName(userCreateRequest.lastName())
			.emailAddress(userCreateRequest.emailAddress())
			.userType(userCreateRequest.userType())
			.isDeleted(false)
			.profileColorCode(userCreateRequest.profileColourCode())
			.isActive(false)
			.isVerified(false)
			.verifiedAt(null)
			.lastLoginAt(null)
			.deletedAt(null)
			.passwordChangedAt(null)
			.build();
    		if(userCreateRequest.userType() == UserType.ADMIN){
				String password = generateAdminPassword();
				
				savedUser.setPassword(password);
                savedUser = userRepository.save(savedUser);
                Map<String, String> otp = userOtpService.generateOtpForUser(savedUser.getId(), UserOtpType.ACCOUNT_ACTIVATION);
                emailSenderService.sendAdminAccountActivationEmail(savedUser.getEmailAddress(), userCreateRequest.firstName()+" "+userCreateRequest.lastName(), password,otp.get("otp"));
                return buildUserCreateResponseWithOtp(savedUser, otp.get("otp"));
            }else{
				savedUser.setPassword(passwordEncoder.encode(userCreateRequest.password()));
			}
			savedUser = userRepository.save(savedUser);

        Map<String, String> otp = userOtpService.generateOtpForUser(savedUser.getId(), UserOtpType.ACCOUNT_ACTIVATION);

        if(userCreateRequest.userType() == UserType.COMPANY){
           emailSenderService.sendCompanyAccountActivationEmail(savedUser.getEmailAddress(),savedUser.getFirstName() ,otp.get("expiresAt"), otp.get("otp"));
       }
      


		return buildUserCreateResponseWithOtp(savedUser, otp.get("otp"));
	}

    private String generateAdminPassword() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String all = letters + digits;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Ensure at least one letter
        password.append(letters.charAt(random.nextInt(letters.length())));

        // Ensure at least one digit
        password.append(digits.charAt(random.nextInt(digits.length())));

        // Fill remaining characters
        for (int i = 2; i < 8; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }

        // Shuffle to avoid predictable positions
        char[] chars = password.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }

    /**
	 * Builds a UserCreateResponse object from a User entity.
	 *
	 * @param user the User entity to convert
	 * @return a UserCreateResponse containing the user's details
	 */
	private UserCreateResponse buildUserCreateResponse(User user) {
		return UserCreateResponse.builder()
			.userId(user.getId())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.emailAddress(user.getEmailAddress())
			.phoneNumber(user.getPhoneNumber())
			.userType(user.getUserType())
			.profilePictureUrl(user.getProfilePictureUrl())
			.dateOfBirth(user.getDateOfBirth())
			.isActive(user.isActive())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.build();
	}

	/**
	 * Builds a UserCreateResponse object from a User entity.
	 *
	 * @param user the User entity to convert
	 * @return a UserCreateResponse containing the user's details
	 */
	private UserCreateResponse buildUserCreateResponseWithOtp(User user, String otp) {
		return UserCreateResponse.builder()
			.userId(user.getId())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.emailAddress(user.getEmailAddress())
			.phoneNumber(user.getPhoneNumber())
			.userType(user.getUserType())
			.profilePictureUrl(user.getProfilePictureUrl())
			.dateOfBirth(user.getDateOfBirth())
			.isActive(user.isActive())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.otp(otp)
			.isVerified(user.isVerified())
			.build();
	}
	
	public UserResponse getUserById(String userId) {
		return buildUserResponse(userRepository.findById(userId)
		.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND)));
	}

	private UserResponse buildUserResponse(User user) {
		return UserResponse.builder()
			.userId(user.getId())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.emailAddress(user.getEmailAddress())
			.phoneNumber(user.getPhoneNumber())
			.userType(user.getUserType())
			.profilePictureUrl(user.getProfilePictureUrl())
			.dateOfBirth(user.getDateOfBirth())
			.isActive(user.isActive())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.isVerified(user.isVerified())
			.build();
	}

	/**
	 * Toggles the active status of a user.
	 *
	 * @param userId the unique identifier of the user
	 * @param activeStatus the desired active status (true to activate, false to deactivate)
	 * @return UserResponse containing the updated user's details
	 * @throws ResourceNotFoundException if the user is not found
	 * @throws InvalidOperationException if the user is already in the desired state
	 */
	public UserResponse toggleUserActiveStatus(String userId, boolean activeStatus) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));
				if(user.isActive() == activeStatus) {
					throw new InvalidOperationException(ApiResponseMessages.ERROR_ACCOUNT_ALREADY_IN_DESIRED_STATE);
				}
		user.setActive(activeStatus);
		return buildUserResponse(updateUser(user));
	}


	/**
	 * Authenticates a user based on the provided email address and password.
	 *
	 * @param userAuthRequest the user authentication request containing email and
	 *                        password
	 * @return a UserResponse object containing the authenticated user's details
	 * @throws ResourceNotFoundException if authentication fails due to invalid
	 *                                   credentials, inactive account, or blocked
	 *                                   account
	 */
	public UserResponse authenticateUser(UserAuthRequest userAuthRequest, UserType userType) {

		User user = userRepository.findByEmailAddressIgnoreCaseAndUserTypeAndIsDeleted(userAuthRequest.emailAddress(), userType, false)
				.orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_USER_INVALID_EMAIL_OR_PASSWORD));

		if (!bCryptPasswordEncoder.matches(userAuthRequest.password(), user.getPassword())) {
			throw new InvalidInputException(ApiResponseMessages.ERROR_USER_INVALID_EMAIL_OR_PASSWORD);
		}

		if (!user.isActive() && user.isVerified()) {
			throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_ACCOUNT_NOT_ACTIVE);
		}


//		if (!user.isVerified()) {
//			throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_ACCOUNT_NOT_VERIFIED);
//		}

		return buildUserResponse(user);
	}


	/**
	 * Resets the user's password after validating the provided OTP and ensuring the new password
	 * is different from the current password.
	 *
	 * @param userResetPasswordRequest The request object containing the user ID, OTP, and new password
	 * @throws ResourceNotFoundException if the user account is not found
	 * @throws InvalidPasswordException  if the new password matches the current password
	 */
	public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
		User user = userRepository.findByEmailAddressIgnoreCaseAndIsDeleted(userResetPasswordRequest.emailAddress(), false)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));
        if(!user.isActive()){
            throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_ACCOUNT_NOT_ACTIVE);
        }
        if(passwordEncoder.matches(userResetPasswordRequest.newPassword(),user.getPassword())){
            throw new InvalidPasswordException(ApiResponseMessages.ERROR_USER_SAME_PASSWORD);
        }
		userOtpService.validateOtp(userResetPasswordRequest.otp(), user.getId(), UserOtpType.PASSWORD_RESET, true);

		user.setPassword(passwordEncoder.encode(userResetPasswordRequest.newPassword()));
		updateUser(user);
	}

		/**
	 * Initiates the password reset process for a user by generating an OTP and
	 * sending it to their email.
	 *
	 * @param emailAddress the email address of the user requesting the password
	 *                     reset
	 * @throws ResourceNotFoundException if no user is found with the provided email
	 *                                   address
	 */
	public void initiateResetPassword(String emailAddress, UserType userType) {

		User user = userRepository.findByEmailAddressIgnoreCaseAndUserTypeAndIsDeleted(emailAddress, userType, false)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));
        if (!user.isActive()) {
            throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_ACCOUNT_NOT_ACTIVE);
        }

		Map<String, String> otp = userOtpService.generateOtpForUser(user.getId(), UserOtpType.PASSWORD_RESET);
        if(user.getUserType() == UserType.ADMIN){
            emailSenderService.sendAdminPasswordResetEmail(user.getEmailAddress(), user.getId(), otp.get("otp"), user.getFirstName() + " " + user.getLastName());
        }
        if(user.getUserType() == UserType.COMPANY){
            emailSenderService.sendCompanyPasswordResetEmail(user.getEmailAddress(), user.getId(), otp.get("otp"), user.getFirstName());
        }
	}


	public boolean existsByEmailAddressIgnoreCase(String emailAddress) {
		return userRepository.existsByEmailAddressIgnoreCase(emailAddress);
	}

	/**
	 * Changes the password of a user based on their user ID and role. This method
	 * retrieves the user by ID and role, validates the provided old password, and
	 * updates the user's password with the new one. If the user is not found or the
	 * old password is incorrect, appropriate exceptions are thrown.
	 *
	 * @param userId                the unique identifier of the user
	 * @param passwordChangeRequest the request object containing the old and new
	 *                              passwords
	 * @throws ResourceNotFoundException if the user with the given ID and role is
	 *                                   not found
	 * @throws InvalidPasswordException  if the provided old password is incorrect
	 */
	public void changeUserPassword(String userId, UserType userType, PasswordChangeRequest passwordChangeRequest) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));

        if(!user.isActive()){
            throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_ACCOUNT_NOT_ACTIVE);
        }

		if (!bCryptPasswordEncoder.matches(passwordChangeRequest.oldPassword(), user.getPassword())) {
			throw new InvalidPasswordException(ApiResponseMessages.ERROR_USER_INCORRECT_OLD_PASSWORD);
		}

		if (bCryptPasswordEncoder.matches(passwordChangeRequest.newPassword(), user.getPassword())) {
			throw new InvalidPasswordException(ApiResponseMessages.ERROR_USER_SAME_PASSWORD);
		}
		user.setPassword(bCryptPasswordEncoder.encode(passwordChangeRequest.newPassword()));
		updateUser(user);
	}
		
	public User verifyOtp(UserVerifyOtpRequest userVerifyOtpRequest) {
		User user = userRepository.findByEmailAddressIgnoreCaseAndIsDeleted(userVerifyOtpRequest.userEmail(),false)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));;
		userOtpService.validateOtp(userVerifyOtpRequest.otp(), user.getId(), userVerifyOtpRequest.userOtpType(), true);
		if(user.getUserType() == UserType.ADMIN && userVerifyOtpRequest.userOtpType() == UserOtpType.ACCOUNT_ACTIVATION){
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setPasswordChangedAt(LocalDateTime.now());
		}
		user.setVerifiedAt(LocalDateTime.now());
		user.setActive(true);
		user.setVerified(true);
	return 	updateUser(user);
	}

	public void resendOtp(String emailAddress, UserOtpType userOtpType) {
		User user = userRepository.findByEmailAddressIgnoreCaseAndIsDeleted(emailAddress,  false)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));
		Map<String, String> otp = userOtpService.generateOtpForUser(user.getId(), userOtpType);
		if(user.getUserType() == UserType.ADMIN){
			emailSenderService.sendAdminAccountActivationEmail(user.getEmailAddress(), user.getFirstName() + " " + user.getLastName(), user.getPassword(), otp.get("otp"));
		 }

		 if(user.getUserType() == UserType.COMPANY && userOtpType == UserOtpType.ACCOUNT_ACTIVATION){
			emailSenderService.sendCompanyAccountActivationEmail(user.getEmailAddress(), user.getFirstName(), otp.get("expiresAt"), otp.get("otp"));
		 }

	}

	public List<UserResponse> getAllUsersByType(UserType userType) {
		return userRepository.findAllByUserTypeAndIsDeleted(userType, false).stream().map(this::buildUserResponse).toList();
	}
	public List<UserResponse> getAllUsersByIds(List<String> userIds) {
		return userRepository.findAllById(userIds).stream().map(this::buildUserResponse).toList();
	}

	public void updateUserProfile(String userId, UserUpdateRequest userUpdateRequest) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));
        if(!user.isActive() || !user.isVerified()){
            throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT);
        }
        if (user.isDeleted()){
            throw new InvalidOperationException(ApiResponseMessages.ERROR_USER_NOT_FOUND);
        }
		user.setFirstName(userUpdateRequest.firstName());
		user.setLastName(userUpdateRequest.lastName());
		user.setPhoneNumber(userUpdateRequest.phoneNumber());
        user.setDateOfBirth(userUpdateRequest.dateOfBirth());
		user.setProfilePictureUrl(userUpdateRequest.profilePictureUrl());
		updateUser(user);
	}

	public void deleteUserById(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));
		userRepository.delete(user);
	}


	public List<User> getUsersByIds(List<String> userIds) {
		return userRepository.findAllById(userIds);
	}

	public void sendAccountSetupCompletionEmail(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_INVALID_ACCOUNT));
		emailSenderService.sendAccountSetupCompletionEmail(user.getEmailAddress(), user.getFirstName() + " " + user.getLastName());
	}

	public User getUserByEmailAddress(String emailAddress) {
		return userRepository.findByEmailAddressIgnoreCaseAndIsDeleted(emailAddress, false)
				.orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));

	}

    public Optional<User>  findByEmail(String email) {
		return userRepository.findByEmailAddressIgnoreCaseAndIsDeleted(email, false);
    }



    public User findById(String userId) {
            return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND, true, false));
    }





    /**
     * Verifies a Google ID token using Google's official verification library.
     * Ensures the token is valid, has not expired, and matches the expected client ID.
     *
     * @param idToken the token received from the client
     * @return the Google token payload if the token is valid
     * @throws InvalidOperationException if verification fails or token is invalid
     */
    private GoogleIdToken.Payload verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new InvalidOperationException("Invalid Google token");
            }

            return googleIdToken.getPayload();

        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidOperationException(ApiResponseMessages.ERROR_PROCESSING_QUEST);
        }
    }
}
