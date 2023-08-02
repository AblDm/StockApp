package com.example.stockapp.controller;

import com.example.stockapp.DTO.Socks;
import com.example.stockapp.exceptions.InvalidRequestException;
import com.example.stockapp.service.SocksServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socks/")
public class SocksController {


    private final SocksServiceImpl socksService;

    @Autowired
    public SocksController(SocksServiceImpl socksService) {
        this.socksService = socksService;
    }

    @PostMapping("/income")
    @Operation(summary = "Register income of socks", responses = {
            @ApiResponse(description = "Income registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> registerIncome(@RequestBody Socks request) {
        try {
            if (request.getCottonPart() < 0 || request.getCottonPart()  > 100) {
                return ResponseEntity.badRequest().body("Invalid value for CottonPart. It should be in the range (0, 100].");
            }
            socksService.registerIncome(request);
            return ResponseEntity.ok("Income registered successfully");
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/outcome")
    @Operation(summary = "Register outcome of socks", responses = {
            @ApiResponse(description = "Outcome registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> registerOutcome(@RequestBody Socks request) {
        try {
            if (request.getCottonPart() < 0 || request.getCottonPart()  > 100) {
                return ResponseEntity.badRequest().body("Invalid value for CottonPart. It should be in the range (0, 100].");
            }
            socksService.registerOutcome(request);
            return ResponseEntity.ok("Outcome registered successfully");
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Get total socks count", responses = {
            @ApiResponse(description = "Total socks count"),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getTotalSocks(
            @Parameter(description = "Color of socks", required = true) @RequestParam String color,
            @Parameter(description = "Comparison operator", required = true, schema = @Schema(allowableValues = {"moreThan", "lessThan", "equal"})) @RequestParam String operation,
            @Parameter(description = "Cotton percentage", required = true) @RequestParam int cottonPart) {
        try {
            if (cottonPart < 0 || cottonPart > 100) {
                return ResponseEntity.badRequest().body("Invalid value for CottonPart. It should be in the range [0, 100].");
            }
            List<Socks> totalSocks = socksService.getTotalSocks(color, operation, cottonPart);
            return ResponseEntity.ok(totalSocks.toString());
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }



}