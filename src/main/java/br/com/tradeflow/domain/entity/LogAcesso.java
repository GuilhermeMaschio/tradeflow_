package br.com.tradeflow.domain.entity;

import br.com.tradeflow.util.ddd.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="LOG_ACESSO")
public class LogAcesso extends AbstractEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String server;
    private String locale;
    private String method;
    private String parameters;
    private String protocol;
    private String remoteHost;
    private String scheme;
    private String serverName;
    private Integer serverPort;
    private String contextPath;
    private String servletPath;
    private String remoteUser;
    private Date inicio;
    private String exception;
    private String forward;
    private String redirect;
    private String contentType;
    private Integer status;
    private Date fim;
    private Long tempo;
    private Boolean ajax;
    private String userAgent;
    private Integer contentSize;
    private Integer contentLength;
    private String headers;
    private String threadName;
    private String metadados;

    public void setException(String exception) {

        if(exception != null && exception.length() > 50000) {
            exception = exception.substring(0, 50000);
        }

        this.exception = exception;
    }
}
