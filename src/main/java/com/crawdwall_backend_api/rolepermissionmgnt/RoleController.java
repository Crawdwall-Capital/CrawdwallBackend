package com.crawdwall_backend_api.rolepermissionmgnt;


import com.crawdwall_backend_api.rolepermissionmgnt.request.RoleCreateRequest;
import com.crawdwall_backend_api.rolepermissionmgnt.request.RolePermissionsUpdateRequest;
import com.crawdwall_backend_api.rolepermissionmgnt.request.RoleUpdateRequest;
import com.crawdwall_backend_api.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/admin/private/create")
    ResponseEntity<ApiResponse<Object>> createRole(@RequestBody RoleCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role created successfully").data( roleService.createRole(request)).build());
    }


    @PutMapping("/admin/private/update/{id}")
    ResponseEntity<ApiResponse<Object>> updateRole(@PathVariable String id, @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role updated successfully").data( roleService.updateRole(id, request)).build());
    }

    @DeleteMapping("/admin/private/delete/{id}")
    ResponseEntity<ApiResponse<Object>> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role deleted successfully").build());
    }

    @GetMapping("/admin/private/get/{id}")
    ResponseEntity<ApiResponse<Object>> getRoleById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role fetched successfully").data( roleService.getRoleById(id)).build());
    }

    @GetMapping("/admin/private/get-all")
    @PreAuthorize("hasAuthority('VIEW_NOMINEE_DIRECTOR_LIST')")
    ResponseEntity<ApiResponse<Object>> getAllRoles(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("All roles fetched successfully").data( roleService.fetchAllRole(page, size)).build());
    }

    @GetMapping("/admin/private/get-by-name/{name}")
    ResponseEntity<ApiResponse<Object>> getRoleByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role fetched successfully").data( roleService.getRoleByName(name)).build());
    }

    @PutMapping("/admin/private/toggle-active-status/{id}")
    ResponseEntity<ApiResponse<Object>> toggleActiveStatus(@PathVariable String id, @RequestParam(name = "active") boolean active) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role active status toggled successfully").data( roleService.toggleRoleActivation(id, active)).build());
    }

   @PutMapping("/admin/private/update-all-permissions/{id}")
   ResponseEntity<ApiResponse<Object>> updateAllPermissions(@PathVariable String id, @RequestBody RolePermissionsUpdateRequest request) {
    return ResponseEntity.ok(ApiResponse.builder().success(true).message("Role all permissions updated successfully").data( roleService.updateAllPermissions(id, request)).build());
   }

//    @GetMapping("/admin/private/get-all-permissions")
//    ResponseEntity<ApiResponse<Object>> getAllPermissions() {
//        return ResponseEntity.ok(ApiResponse.builder().success(true).message("All permissions fetched successfully").data( roleService.getAllPermissions()).build());
//    }




}
