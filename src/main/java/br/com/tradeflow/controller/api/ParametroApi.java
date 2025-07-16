package br.com.tradeflow.controller.api;

import br.com.tradeflow.dto.ParametroDTO;
import br.com.tradeflow.dto.filter.ParametroFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Validated
@Tag(name = "parametros", description = "Api para gestão de parâmetros")
@RequestMapping("/v1")
public interface ParametroApi {

    @Operation(
            operationId = "parametros",
            summary = "Listagem de parâmetros"
    )
    @ApiResponse(responseCode = "200", description = "Lista paginada de parâmetros filtrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ParametroDTO.class))
    )
    @RequestMapping(
            value = "/parametros",
            method = RequestMethod.GET,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    default ResponseEntity<Page<ParametroDTO>> listar (
            @PageableDefault(page = 0, size = 50) Pageable pageable,
            ParametroFilter filter
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "parametros",
            summary = "Cadastro de parâmetro"
    )
    @ApiResponse(responseCode = "201", description = "Parâmetro criado com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ParametroDTO.class))
    )
    @RequestMapping(
            value = "/parametros",
            method = RequestMethod.POST,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    default ResponseEntity<ParametroDTO> cadastrar (
            @RequestBody ParametroDTO parametroDTO,
            JwtAuthenticationToken principal
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "parametros",
            summary = "Atualização de parâmetro"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parâmetro atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ParametroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado")
    })
    @RequestMapping(
            value = "/parametros/{id}",
            method = RequestMethod.PUT,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    default ResponseEntity<ParametroDTO> atualizar (
            @Parameter(description = "ID do parâmetro", required = true) @PathVariable Long id,
            @RequestBody ParametroDTO parametroDTO,
            JwtAuthenticationToken principal
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "parametros",
            summary = "Excluir um parâmetro"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parmâmetro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parmâmetro não encontrado")
    })
    @RequestMapping(
            value = "/parametros/{id}",
            method = RequestMethod.DELETE,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    ResponseEntity<Void> excluir (
            @Parameter(description = "ID do parâmetro", required = true) @PathVariable Long id,
            JwtAuthenticationToken principal
    );
}
