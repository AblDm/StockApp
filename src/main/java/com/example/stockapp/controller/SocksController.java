package com.example.stockapp.controller;

import com.example.stockapp.DTO.Socks;
import com.example.stockapp.exceptions.InvalidRequestException;
import com.example.stockapp.service.SocksServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socks/")
public class SocksController {


    private final SocksServiceImpl socksService;
    private static final Logger logger = LoggerFactory.getLogger(SocksController.class);

    @Autowired
    public SocksController(SocksServiceImpl socksService) {
        this.socksService = socksService;
    }

    @PostMapping("/income")
    @Operation(summary = "Register income of socks", responses = {
            @ApiResponse(description = "Income registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or params"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> registerIncome(@RequestBody Socks request) {
        logger.debug("Entering registerIncome method");
        try {
            socksService.validateCottonPart(request.getCottonPart());
            socksService.registerIncome(request);
            logger.info("Income registered successfully");
            return ResponseEntity.ok("Income registered successfully");
        } catch (InvalidRequestException e) {
            logger.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/outcome")
    @Operation(summary = "Register outcome of socks", responses = {
            @ApiResponse(description = "Outcome registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or params"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> registerOutcome(@RequestBody Socks request) {
        try {
            socksService.validateCottonPart(request.getCottonPart());
            socksService.registerOutcome(request);
            return ResponseEntity.ok("Outcome registered successfully");
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping()
    @Operation(summary = "Get total socks count", responses = {
            @ApiResponse(description = "Total socks count"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or params"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getTotalSocks(
            @Parameter(description = "Color of socks", required = true) @RequestParam String color,
            @Parameter(description = "Comparison operator", required = true, schema = @Schema(allowableValues = {"moreThan", "lessThan", "equal"})) @RequestParam String operation,
            @Parameter(description = "Cotton percentage", required = true) @RequestParam int cottonPart) {
        try {
            socksService.validateCottonPart(cottonPart);
            List<Socks> totalSocks = socksService.getTotalSocks(color, operation, cottonPart);
            String formattedResponse = socksService.formatSocksList(totalSocks);
            return ResponseEntity.ok(formattedResponse);
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }




}