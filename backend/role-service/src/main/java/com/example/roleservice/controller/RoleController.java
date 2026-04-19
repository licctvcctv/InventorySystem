package com.example.roleservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.roleservice.entity.SysPermission;
import com.example.roleservice.entity.SysRole;
import com.example.roleservice.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {

    private final SysRoleService roleService;

    @GetMapping
    public ResponseEntity<IPage<SysRole>> getRoles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(roleService.page(new Page<>(page, size)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SysRole>> getAllRoles() {
        return ResponseEntity.ok(roleService.list());
    }

    @PostMapping
    public ResponseEntity<Boolean> createRole(@RequestBody SysRole role) {
        return ResponseEntity.ok(roleService.save(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return ResponseEntity.ok(roleService.updateById(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.removeById(id));
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<List<SysPermission>> getRolePermissions(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getPermissions(id));
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<Void> assignPermissions(@PathVariable Long id, @RequestBody Map<String, List<Long>> payload) {
        List<Long> permissionIds = payload.get("permissionIds");
        roleService.assignPermissions(id, permissionIds);
        return ResponseEntity.ok().build();
    }
}
