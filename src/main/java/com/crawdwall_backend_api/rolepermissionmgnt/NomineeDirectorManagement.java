package com.crawdwall_backend_api.rolepermissionmgnt;



import com.crawdwall_backend_api.utils.exception.InvalidInputException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NomineeDirectorManagement {

    VIEW_NOMINEE_DIRECTOR_LIST("View Nominee Director List"),
   VIEW_NOMINEE_PROFILE_ACTIVITY("View Nominee Profile Activity"),
   APPROVE_REJECT_KYC("Edit Nominee Director"),
   SUSPEND_DEACTIVATE_NOMINEE("Suspend/Deactivate Nominee"),
    APPROVE_REJECT_EXIT_REQUEST("Approve/Reject Exit Request");

    private final String displayName;

    NomineeDirectorManagement(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public static Set<NomineeDirectorManagement> getAllPermissions() {
        return Set.of(values());
    }

    public static Set<String> getAllDisplayNames() {
        return Arrays.stream(values())
                .map(NomineeDirectorManagement::getDisplayName)
                .collect(Collectors.toSet());
    }

    public static NomineeDirectorManagement fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(permission -> permission.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Unknown permission name: " + displayName));
    }
}
