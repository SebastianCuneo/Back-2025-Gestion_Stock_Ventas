package uy.edu.ucu.inventario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.service.AuditLogService;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditService;

    public AuditLogController(AuditLogService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditService.listAll());
    }

    // (Opcional) endpoint para registrar manualmente logs si se desea
    @PostMapping
    public ResponseEntity<AuditLog> createLog(@RequestBody AuditLog log) {
        return ResponseEntity.ok(auditService.saveLog(
                log.getEntityName(),
                log.getEntityId(),
                log.getOperation(),
                log.getUsername()
        ));
    }
}
