package com.crawdwall_backend_api.investor;

import com.crawdwall_backend_api.investor.request.InvestorCreateRequest;
import com.crawdwall_backend_api.investor.request.InvestorUpdateRequest;
import com.crawdwall_backend_api.investor.response.InvestorResponse;
import com.crawdwall_backend_api.userauthmgt.user.UserService;
import com.crawdwall_backend_api.userauthmgt.user.UserType;
import com.crawdwall_backend_api.userauthmgt.user.User;
import com.crawdwall_backend_api.userauthmgt.user.request.UserCreateRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserCreateResponse;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;
import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.exception.ResourceNotFoundException;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.Status;
import com.crawdwall_backend_api.utils.PaginatedData;
import com.crawdwall_backend_api.utils.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final UserService userService;
    private final UtilsService utilsService;
    private final MongoTemplate mongoTemplate;

    
    public void createInvestor(InvestorCreateRequest request) {
        validateUniqueConstraints(request);
        
        UserCreateResponse userCreateResponse = userService.createUser(UserCreateRequest.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .emailAddress(request.investorEmail())
                .phoneNumber(request.investorPhone())
                .password(request.password())
                .userType(UserType.INVESTORS)
                .build());
        
        Investor investor = buildInvestor(request, userCreateResponse.userId());
        investorRepository.save(investor);
    }

    
    private void validateUniqueConstraints(InvestorCreateRequest request) {
        if (investorRepository.existsByInvestorEmail(request.investorEmail())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_EMAIL_ALREADY_EXISTS);
        }
        if (investorRepository.existsByInvestorPhone(request.investorPhone())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_PHONE_ALREADY_EXISTS);
        }
        if (investorRepository.existsByInvestorNationalId(request.investorNationalId())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NATIONAL_ID_ALREADY_EXISTS);
        }
        if (investorRepository.existsByInvestorTaxId(request.investorTaxId())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_TAX_ID_ALREADY_EXISTS);
        }
    }

  
    public void updateInvestor(InvestorUpdateRequest request, String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        
        validateUniqueFieldsForUpdate(investor, request);
        
        updateIfChanged(investor, request);
        
        investorRepository.save(investor);
    }

    
    private void validateUniqueFieldsForUpdate(Investor existing, InvestorUpdateRequest request) {

        if (request.investorEmail() != null && !existing.getInvestorEmail().equals(request.investorEmail())) {
            if (investorRepository.existsByInvestorEmail(request.investorEmail())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_EMAIL_ALREADY_EXISTS);
            }
        }
        

        if (request.investorPhone() != null && !existing.getInvestorPhone().equals(request.investorPhone())) {
            if (investorRepository.existsByInvestorPhone(request.investorPhone())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_PHONE_ALREADY_EXISTS);
            }
        }
        

        if (request.investorNationalId() != null && !existing.getInvestorNationalId().equals(request.investorNationalId())) {
            if (investorRepository.existsByInvestorNationalId(request.investorNationalId())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NATIONAL_ID_ALREADY_EXISTS);
            }
        }
        

        if (request.investorTaxId() != null && !existing.getInvestorTaxId().equals(request.investorTaxId())) {
            if (investorRepository.existsByInvestorTaxId(request.investorTaxId())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_TAX_ID_ALREADY_EXISTS);
            }
        }
    }

   
    private void updateIfChanged(Investor investor, InvestorUpdateRequest request) {
        if (request.firstName() != null) {
            investor.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            investor.setLastName(request.lastName());
        }
        if (request.investorEmail() != null) {
            investor.setInvestorEmail(request.investorEmail());
        }
        if (request.investorPhone() != null) {
            investor.setInvestorPhone(request.investorPhone());
        }
        if (request.dateOfBirth() != null) {
            investor.setDateOfBirth(request.dateOfBirth());
        }
        if (request.nationality() != null) {
            investor.setNationality(request.nationality());
        }
        if (request.profilePicture() != null) {
            investor.setProfilePicture(request.profilePicture());
        }
        if (request.investorNationalId() != null) {
            investor.setInvestorNationalId(request.investorNationalId());
        }
        if (request.investorTaxId() != null) {
            investor.setInvestorTaxId(request.investorTaxId());
        }
        if (request.investorType() != null) {
            investor.setInvestorType(request.investorType());
        }
        if (request.riskTolerance() != null) {
            investor.setRiskTolerance(request.riskTolerance());
        }
        if (request.investmentExperience() != null) {
            investor.setInvestmentExperience(request.investmentExperience());
        }
        if (request.minimumInvestmentAmount() != null) {
            investor.setMinimumInvestmentAmount(request.minimumInvestmentAmount());
        }
        if (request.maximumInvestmentAmount() != null) {
            investor.setMaximumInvestmentAmount(request.maximumInvestmentAmount());
        }
        if (request.annualIncome() != null) {
            investor.setAnnualIncome(request.annualIncome());
        }
        if (request.netWorth() != null) {
            investor.setNetWorth(request.netWorth());
        }
        if (request.liquidAssets() != null) {
            investor.setLiquidAssets(request.liquidAssets());
        }
    }

   
    public InvestorResponse getInvestorById(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        return buildInvestorResponse(investor);
    }

   
    public InvestorResponse getInvestorByUserId(String userId) {
        Investor investor = investorRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        return buildInvestorResponse(investor);
    }

   
    public PaginatedData getAllInvestors(int page, int size, String searchParam) {
        Page<Investor> investors = searchInvestorsWithCriteria(searchParam, page, size);
        List<InvestorResponse> investorResponses = investors.getContent()
                .stream()
                .map(this::buildInvestorResponse)
                .collect(Collectors.toList());
        
        return PaginatedData.builder()
                .totalPage(investors.getTotalPages())
                .numberOfElements(investors.getNumberOfElements())
                .totalElements(investors.getTotalElements())
                .data(investorResponses)
                .build();
    }

  
    private Page<Investor> searchInvestorsWithCriteria(String searchParam, int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page - 1), size,
                Sort.by(Sort.Order.desc("createdAt")));

        Query query = new Query();

        query.addCriteria(Criteria.where("status")
                .in(Status.PENDING, Status.ACTIVE, Status.INACTIVE, Status.BLOCKED));

        if (StringUtils.hasText(searchParam)) {
            String trimmed = searchParam.trim();
            List<Criteria> ors = buildSearchCriteria(trimmed);
            query.addCriteria(new Criteria().orOperator(ors.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Investor.class);
        query.with(pageRequest);
        List<Investor> investors = mongoTemplate.find(query, Investor.class);

        return new PageImpl<>(investors, pageRequest, total);
    }


    private List<Criteria> buildSearchCriteria(String searchParam) {
        List<Criteria> ors = new ArrayList<>();
        ors.add(Criteria.where("firstName").regex(searchParam, "i"));
        ors.add(Criteria.where("lastName").regex(searchParam, "i"));
        ors.add(Criteria.where("investorEmail").regex(searchParam, "i"));
        ors.add(Criteria.where("investorPhone").regex(searchParam, "i"));
        ors.add(Criteria.where("investorNationalId").regex(searchParam, "i"));
        ors.add(Criteria.where("investorTaxId").regex(searchParam, "i"));

        String[] parts = searchParam.split("\\s+");
        if (parts.length == 2) {
            ors.add(new Criteria().andOperator(
                    Criteria.where("firstName").regex(parts[0], "i"),
                    Criteria.where("lastName").regex(parts[1], "i")));

            ors.add(new Criteria().andOperator(
                    Criteria.where("firstName").regex(parts[1], "i"),
                    Criteria.where("lastName").regex(parts[0], "i")));
        }
        return ors;
    }


    public void activateInvestor(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        investor.setStatus(Status.ACTIVE);
        investorRepository.save(investor);
    }

    public void deactivateInvestor(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        investor.setStatus(Status.INACTIVE);
        investorRepository.save(investor);
    }

    public void blockInvestor(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        investor.setStatus(Status.BLOCKED);
        investorRepository.save(investor);
    }

    public void unblockInvestor(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND));
        investor.setStatus(Status.ACTIVE);
        investorRepository.save(investor);
    }

    

    public void changeInvestorPassword(String userId, PasswordChangeRequest request) {
        userService.changeUserPassword(userId, UserType.INVESTORS, request);
    }

    public void initiateResetPassword(String emailAddress) {
        userService.initiateResetPassword(emailAddress, UserType.INVESTORS);
    }

    public void verifyOtp(UserVerifyOtpRequest request) {
        User user = userService.verifyOtp(request);
        Investor investor = investorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_INVESTOR_NOT_FOUND, true, false));
        investor.setVerified(true);
        investor.setVerifiedAt(LocalDateTime.now());
        investor.setActive(true);
        investorRepository.save(investor);
    }

    public void resendOtp(String emailAddress, UserOtpType userOtpType) {
        userService.resendOtp(emailAddress, userOtpType);
    }

  
  
    private Investor buildInvestor(InvestorCreateRequest request, String userId) {
        return Investor.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .investorEmail(request.investorEmail())
                .investorPhone(request.investorPhone())
                .dateOfBirth(request.dateOfBirth())
                .nationality(request.nationality())
                .profilePicture(request.profilePicture())
                .investorNationalId(request.investorNationalId())
                .investorTaxId(request.investorTaxId())
                .investorType(request.investorType())
                .riskTolerance(request.riskTolerance())
                .investmentExperience(request.investmentExperience())
                .minimumInvestmentAmount(request.minimumInvestmentAmount())
                .maximumInvestmentAmount(request.maximumInvestmentAmount())
                .annualIncome(request.annualIncome())
                .netWorth(request.netWorth())
                .liquidAssets(request.liquidAssets())
                .userId(userId)
                .isActive(false)
                .isDeleted(false)
                .isVerified(false)
                .status(Status.PENDING)
                .build();
    }

    private InvestorResponse buildInvestorResponse(Investor investor) {
        return InvestorResponse.builder()
                .id(investor.getId())
                .firstName(investor.getFirstName())
                .lastName(investor.getLastName())
                .investorEmail(investor.getInvestorEmail())
                .investorPhone(investor.getInvestorPhone())
                .dateOfBirth(investor.getDateOfBirth())
                .nationality(investor.getNationality())
                .profilePicture(investor.getProfilePicture())
                .investorNationalId(investor.getInvestorNationalId())
                .investorTaxId(investor.getInvestorTaxId())
                .investorType(investor.getInvestorType())
                .riskTolerance(investor.getRiskTolerance())
                .investmentExperience(investor.getInvestmentExperience())
                .minimumInvestmentAmount(investor.getMinimumInvestmentAmount())
                .maximumInvestmentAmount(investor.getMaximumInvestmentAmount())
                .annualIncome(investor.getAnnualIncome())
                .netWorth(investor.getNetWorth())
                .liquidAssets(investor.getLiquidAssets())
                .investorAddress(investor.getInvestorAddress())
                .userId(investor.getUserId())
                .isActive(investor.isActive())
                .isDeleted(investor.isDeleted())
                .isVerified(investor.isVerified())
                .verifiedAt(investor.getVerifiedAt())
                .status(investor.getStatus())
                .createdAt(investor.getCreatedAt())
                .updatedAt(investor.getUpdatedAt())
                .build();
    }
}