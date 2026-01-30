package com.crawdwall_backend_api.utils.appsecurity;


import com.crawdwall_backend_api.rolepermissionmgnt.BusinessManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.NomineeDirectorManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.PaymentManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorityMapper {

    private static final Logger log = LoggerFactory.getLogger(AuthorityMapper.class);

    private AuthorityMapper(){}

    public static Set<GrantedAuthority> toAuthorities(Role r){
        if (log.isDebugEnabled()) {
            log.debug("[AUTH] toAuthorities START role={}", (r == null ? "null" : r.getName()));
        }

        Stream<String> s1 = names(r == null ? null : r.getBusinessManagementPermissions());
        Stream<String> s2 = names(r == null ? null : r.getNomineeDirectorManagementPermissions());
        Stream<String> s3 = names(r == null ? null : r.getPaymentManagementPermissions());

        Set<GrantedAuthority> out =
                Stream.concat(Stream.concat(s1, s2), s3)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        if (log.isDebugEnabled()) {
            log.debug("[AUTH] total authorities mapped={}", out.size());
        }
        return out;
    }

    private static <E extends Enum<E>> Stream<String> names(Set<E> set){
        // There's often no real need to log map-expansion unless for deep debugging
        if (log.isTraceEnabled()) {
            log.trace("[AUTH] names invoked, setNull={}", (set == null));
        }
        return (set == null)
                ? Stream.empty()
                : set.stream().map(Enum::name);
    }

    public static Set<GrantedAuthority> superAdminAuthorities(boolean isDefault) {
        if (!isDefault) return Set.of();

        return Stream.of(
                        BusinessManagement.getAllPermissions(),
                        NomineeDirectorManagement.getAllPermissions(),
                        PaymentManagement.getAllPermissions()
                )
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(Enum::name)
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

}
