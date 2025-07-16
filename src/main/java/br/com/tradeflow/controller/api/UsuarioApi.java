package br.com.tradeflow.controller.api;

import br.com.tradeflow.dto.UsuarioCadastroDTO;
import br.com.tradeflow.dto.UsuarioDTO;
import br.com.tradeflow.dto.filter.UsuarioFilter;
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
@Tag(name = "usuarios", description = "Api para gestão de usuários")
@RequestMapping("/v1")
public interface UsuarioApi {

    @Operation(
            operationId = "usuarios",
            summary = "Listagem de usuários"
    )
    @ApiResponse(responseCode = "200", description = "Lista paginada de usuários filtrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UsuarioDTO.class))
    )
    @RequestMapping(
            value = "/usuarios",
            method = RequestMethod.GET,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    default ResponseEntity<Page<UsuarioDTO>> listar (
            @PageableDefault(page = 0, size = 50) Pageable pageable,
            UsuarioFilter filter
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "usuarios",
            summary = "Cadastro de usuário"
    )
    @ApiResponse(responseCode = "201", description = "Parâmetro criado com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UsuarioDTO.class))
    )
    @RequestMapping(
            value = "/usuarios",
            method = RequestMethod.POST,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    default ResponseEntity<UsuarioDTO> cadastrar (
            @RequestBody UsuarioCadastroDTO usuarioDTO,
            JwtAuthenticationToken principal
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "usuarios",
            summary = "Atualização de usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parâmetro atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado")
    })
    @RequestMapping(
            value = "/usuarios/{id}",
            method = RequestMethod.PUT,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    default ResponseEntity<UsuarioDTO> atualizar (
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id,
            @RequestBody UsuarioCadastroDTO usuarioDTO,
            JwtAuthenticationToken principal
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "usuarios",
            summary = "Excluir um usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parmâmetro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parmâmetro não encontrado")
    })
    @RequestMapping(
            value = "/usuarios/{id}",
            method = RequestMethod.DELETE,
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    ResponseEntity<Void> excluir (
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id,
            JwtAuthenticationToken principal
    );
}
