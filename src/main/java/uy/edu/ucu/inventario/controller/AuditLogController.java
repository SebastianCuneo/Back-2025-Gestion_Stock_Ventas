package uy.edu.ucu.inventario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.service.AuditLogService;

import java.util.List;

@RestController
@RequestMapping("/api/audit-log")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(auditLogService.listAll());
    }

    @PostMapping
    public ResponseEntity<AuditLog> create(@RequestBody AuditLog log) {
        return ResponseEntity.ok(auditLogService.saveLog(
                log.getEntityName(),
                log.getEntityId(),
                log.getOperation(),
                log.getUsername()
        ));
    }
}