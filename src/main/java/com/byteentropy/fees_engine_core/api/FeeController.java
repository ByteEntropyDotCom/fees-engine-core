package com.byteentropy.fees_engine_core.api;

import com.byteentropy.fees_engine_core.service.FeeService;
import com.byteentropy.fees_engine_core.dto.FeeRequest;
import com.byteentropy.fees_engine_core.dto.FeeResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/v1/fees")
public class FeeController {

    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping("/calculate")
    public FeeResponse calculate(@RequestBody FeeRequest request) {
        return feeService.process(request);
    }
}