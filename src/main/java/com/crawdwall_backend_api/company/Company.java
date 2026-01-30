package com.crawdwall_backend_api.company;

import com.crawdwall_backend_api.utils.BaseEntity;
import com.crawdwall_backend_api.utils.Address;
import org.springframework.data.mongodb.core.mapping.Document;
import com.crawdwall_backend_api.utils.Status;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company extends BaseEntity {
@Indexed(unique = true)
private   String companyName;
@Indexed(unique = true)
private   String companyEmail;
@Indexed(unique = true)
private   String companyPhone;
private   String companyWebsite;
private   String companyLogo;
@Indexed(unique = true)
private   String companyRegistrationNumber;
private   String companyRegistrationDate;
private   CompanyType companyType;
private   Address companyAddress;
private   boolean isActive;
private   boolean isDeleted;
private   boolean isVerified;
private   LocalDateTime verifiedAt;
private   LocalDateTime deletedAt;
private   LocalDateTime createdAt;
private   LocalDateTime updatedAt;
private   String userId;
private   Status status;



    }
