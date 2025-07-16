package br.com.tradeflow.domain.service;

import br.com.tradeflow.config.filter.LogAcessoFilter;
import br.com.tradeflow.domain.entity.LogAcesso;
import br.com.tradeflow.domain.repository.LogAcessoRepository;
import br.com.tradeflow.util.DummyUtils;
import br.com.tradeflow.util.servlet.HttpServletRequestLogAcesso;
import br.com.tradeflow.util.servlet.HttpServletResponseLogAcesso;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class LogAcessoService {

    public static final int EXCEPTION_SIZE_LIMIT = 20000;
    private static final Pattern PARANS_REST_PATTERN = Pattern.compile("/([\\d_]+)");

    private final LogAcessoRepository logAcessoRepository;

    public LogAcessoService(LogAcessoRepository logAcessoRepository) {
        this.logAcessoRepository = logAcessoRepository;
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateNewSession(LogAcesso logAcesso) {
        DummyUtils.mudarNomeThread("tread-log-acesso-saveOrUpdate");
        saveOrUpdate(logAcesso);
    }

    private void saveOrUpdate(LogAcesso logAcesso){
        String exception = logAcesso.getException();
        if (StringUtils.length(exception) > EXCEPTION_SIZE_LIMIT){
            logAcesso.setException(StringUtils.truncate(exception, EXCEPTION_SIZE_LIMIT));
        }

        try {
            logAcessoRepository.saveAndFlush(logAcesso);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Ocorreu um erro ao salver o LogAcesso");
        }
    }

    public LogAcesso criaLogAcesso(HttpServletRequest request) {
        LogAcesso logAcesso = new LogAcesso();

        Date dataInicio = new Date();
        logAcesso.setInicio(dataInicio);

        int contentLength = request.getContentLength();
        logAcesso.setContentLength(contentLength);

        String servletPath = request.getServletPath();
        logAcesso.setServletPath(servletPath);

        String server = DummyUtils.getServer();
        logAcesso.setServer(server);

        boolean ajax = request.getHeader("X-Requested-With") != null;
        logAcesso.setAjax(ajax);

        Locale locale = request.getLocale();
        String localeStr = locale != null ? locale.toString() : null;
        logAcesso.setLocale(localeStr);

        String method = request.getMethod();
        logAcesso.setMethod(method);

        String userAgent = request.getHeader("User-Agent");
        logAcesso.setUserAgent(userAgent);

        String protocol = request.getProtocol();
        logAcesso.setProtocol(protocol);

        String remoteHost = request.getRemoteHost();
        logAcesso.setRemoteHost(remoteHost);

        String scheme = request.getScheme();
        logAcesso.setScheme(scheme);

        String serverName = request.getServerName();
        logAcesso.setServerName(serverName);

        int serverPort = request.getServerPort();
        logAcesso.setServerPort(serverPort);

        String contextPath = request.getContextPath();
        logAcesso.setContextPath(contextPath);

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headersMap = new LinkedHashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            if(StringUtils.isNotBlank(value)) {
                value = value.replace("\"", "'");
            }
            headersMap.put(name, value);
        }
        String headers = DummyUtils.objectToJson(headersMap);
        logAcesso.setHeaders(headers);

        String requestURI = request.getRequestURI();
        servletPath = requestURI.replace(contextPath, "");

        Map<String, String> paramsMap = new LinkedHashMap();
        Matcher matcher = PARANS_REST_PATTERN.matcher(servletPath);
        int i = 0;
        while(matcher.find()) {
            String param = matcher.group(1);
            paramsMap.put("param" + i++, param);
        }

        for (i = 0; i < paramsMap.size(); i++) {
            servletPath = servletPath.replaceFirst(PARANS_REST_PATTERN.pattern(), "/{param" + i + "}");
        }

        if(!paramsMap.isEmpty()) {
            String parameters = DummyUtils.objectToJson(paramsMap);
            logAcesso.setParameters(parameters);
        }
        logAcesso.setServletPath(servletPath);

        Thread thread = Thread.currentThread();
        int identityHashCode = System.identityHashCode(thread);
        String threadName = "thdreq-" + servletPath + "-" + identityHashCode;
        logAcesso.setThreadName(threadName);

        saveOrUpdateNewSession(logAcesso);

        threadName = threadName + "-LA" + logAcesso.getId();
        logAcesso.setThreadName(threadName);

        return logAcesso;
    }

    public LogAcesso get(Long logAcessoId) {
        return logAcessoRepository.getById(logAcessoId);
    }

    public void finalizaLogAcesso(LogAcesso logAcesso, HttpServletRequestLogAcesso request, HttpServletResponseLogAcesso response) {

        String remoteUser = request.getRemoteUser();
        logAcesso.setRemoteUser(remoteUser);

        int contentSize = response.getSizeCount();
        logAcesso.setContentSize(contentSize);

        String contentType = response.getContentType();
        logAcesso.setContentType(contentType);

        int status = response.getStatus();
        logAcesso.setStatus(status);

        String forward = request.getForward();
        logAcesso.setForward(forward);

        String redirect = response.getRedirect();
        logAcesso.setRedirect(redirect);

        String servletPath = request.getServletPath();
        String method = logAcesso.getMethod();
        String headers = logAcesso.getHeaders();
        if(
                ("POST".equals(method) || "PUT".equals(method) || "OPTIONS".equals(method))
                        && !StringUtils.contains(headers, "multipart")//pra não salvar imagens
        ) {
            byte[] contentAsByteArray = request.getContentAsByteArray();
            try {
                String body = new String(contentAsByteArray, "UTF-8");

                String parameters = logAcesso.getParameters();
                if(StringUtils.isNotBlank(parameters)) {
                    Map<Object, Object> paramsMap = (Map<Object, Object>) DummyUtils.jsonStringToMap(parameters);
                    paramsMap = paramsMap != null ? paramsMap : new LinkedHashMap<>();
                    Map<?, ?> bodyMap = DummyUtils.jsonStringToMap(body);
                    if(bodyMap != null) {
                        paramsMap.put("body", bodyMap);
                    } else {
                        paramsMap.put("body", body);
                    }
                    String json = DummyUtils.objectToJson(paramsMap);
                    logAcesso.setParameters(json);
                }
                else {

                    if("/rest/signer/v1/startSign".equals(servletPath)
                            || "/rest/signer/v1/completeSign".equals(servletPath)) {
                        Map<?, ?> map = DummyUtils.jsonStringToMap(body);
                        if(map != null) {
                            map.remove("certContent");
                            map.remove("fileBase64");
                            body = DummyUtils.objectToJson(map);
                        }
                    }

                    logAcesso.setParameters(body);
                }
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            Enumeration<String> parameterNames = request.getParameterNames();
            Map<String, String> parametersMap = new LinkedHashMap<>();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String value = request.getParameter(name);
                if(StringUtils.isNotBlank(value)) {
                    parametersMap.put(name, value);
                }
            }
            if(!parametersMap.isEmpty()) {
                String parameters = logAcesso.getParameters();
                Map<Object, Object> paramsMap = (Map<Object, Object>) DummyUtils.jsonStringToMap(parameters);
                paramsMap = paramsMap != null ? paramsMap : new LinkedHashMap<>();
                paramsMap.putAll(parametersMap);
                String json = DummyUtils.objectToJson(paramsMap);
                logAcesso.setParameters(json);
            }
        }

        long fim = System.currentTimeMillis();
        Date dataFim = new Date(fim);
        logAcesso.setFim(dataFim);

        Date inicio = logAcesso.getInicio();
        long tempo = fim - inicio.getTime();
        logAcesso.setTempo(tempo);

        saveOrUpdateNewSession(logAcesso);
    }

    @Transactional(rollbackFor=Exception.class)
    public LogAcesso criaLogJob(Class jobClass) {

        final LogAcesso log = new LogAcesso();
        String server = DummyUtils.getServer();
        log.setServer(server);
        log.setServerName(server);
        log.setServletPath(jobClass.getSimpleName());

        log.setInicio(new Date());

        String threadName = Thread.currentThread().getName();
        log.setThreadName(threadName);

        saveOrUpdateNewSession(log);

        LogAcessoFilter.setLogAcesso(log);

        return log;
    }

    @Transactional(rollbackFor = Exception.class)
    public int limpaLogAcesso() {

        int numPreservar = 2000000;
        LogAcesso logA = LogAcessoFilter.getLogAcesso();
        DummyUtils.addParameter(logA, "numPreservar", numPreservar);

        Integer idInicio = logAcessoRepository.getIdExclusao(numPreservar);
        DummyUtils.addParameter(logA, "idExclusao", idInicio);

        if(idInicio == null || idInicio == 0) {
            log.info("LimpaLogAcessoJob, não vai deletar nenhum log, total é menor que o número preservado: " + numPreservar + ".");
            DummyUtils.addParameter(logA, "deletados", 0);
            return 0;
        } else {
            log.info("LimpaLogAcessoJob, idExclusao: " + idInicio + ".");
        }

        int deletados = logAcessoRepository.excluirAnteriorA(idInicio);

        DummyUtils.addParameter(logA, "deletados", deletados);
        log.info("" + deletados + " logs de acesso deletados.");
        return deletados;
    }
}
