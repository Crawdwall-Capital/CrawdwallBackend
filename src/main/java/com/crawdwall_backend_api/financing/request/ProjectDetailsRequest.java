package com.crawdwall_backend_api.financing.request;

public record ProjectDetailsRequest(
    String projectName,
    String briefProjectDescription,
    String typeOfProject,
    String otherProjectType,
    String problemSolved,
    String targetAudience
) {}
