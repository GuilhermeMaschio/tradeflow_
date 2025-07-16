package br.com.tradeflow.controller.api;

import br.com.tradeflow.dto.EnderecoCepDTO;
import br.com.tradeflow.dto.ParametroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Validated
@Tag(name = "util", description = "APIs úteis")
@RequestMapping("/v1/util")
public interface UtilApi {

    @Operation(
            operationId = "consulta-cep",
            summary = "Consulta de endereço por CEP"
    )
    @ApiResponse(responseCode = "200", description = "Endereço correspondente ao CEP informado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ParametroDTO.class))
    )
    @RequestMapping(
            value = "/consulta-cep/{cep}",
            method = RequestMethod.GET,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    ResponseEntity<EnderecoCepDTO> consultaCep (
            @Parameter(description = "CEP", required = true) @PathVariable String cep
    );
}
