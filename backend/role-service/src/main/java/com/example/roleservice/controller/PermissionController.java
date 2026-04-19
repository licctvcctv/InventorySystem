package com.example.roleservice.controller;

import com.example.roleservice.entity.SysPermission;
import com.example.roleservice.mapper.SysPermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@CrossOrigin
public class PermissionController {

    private final SysPermissionMapper permissionMapper;

    @GetMapping
    public ResponseEntity<List<SysPermission>> getAllPermissions() {
        List<SysPermission> all = permissionMapper.selectList(null);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<SysPermission>> getPermissionTree() {
        List<SysPermission> all = permissionMapper.selectList(null);
        List<SysPermission> tree = all.stream()
                .filter(p -> p.getParentId() == 0 || p.getParentId() == null)
                .map(root -> {
                    root.setChildren(getChildren(root, all));
                    return root;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(tree);
    }

    private List<SysPermission> getChildren(SysPermission parent, List<SysPermission> all) {
        return all.stream()
                .filter(p -> p.getParentId().equals(parent.getId()))
                .map(child -> {
                    child.setChildren(getChildren(child, all));
                    return child;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Integer> createPermission(@RequestBody SysPermission permission) {
        return ResponseEntity.ok(permissionMapper.insert(permission));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePermission(@PathVariable Long id, @RequestBody SysPermission permission) {
        permission.setId(id);
        return ResponseEntity.ok(permissionMapper.updateById(permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deletePermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionMapper.deleteById(id));
    }
}
