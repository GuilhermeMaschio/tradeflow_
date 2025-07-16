package br.com.tradeflow.config.filter;

import br.com.tradeflow.domain.entity.LogAcesso;
import br.com.tradeflow.domain.service.LogAcessoService;
import br.com.tradeflow.util.servlet.HttpServletRequestLogAcesso;
import br.com.tradeflow.util.servlet.HttpServletResponseLogAcesso;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.security.Principal;

@Log4j2
@Component
@Order(1)
public class LogAcessoFilter implements Filter {

	private static final String LOG_ACESSO_REQUEST_KEY = "log_acesso_request_key";
	private static ThreadLocal<LogAcesso> threadLocalLogAcesso = new ThreadLocal<>();

	@Override
	public void init(FilterConfig arg0) { }

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		boolean logAcessoIsNew = true;
		LogAcesso logAcesso = (LogAcesso) request.getAttribute(LOG_ACESSO_REQUEST_KEY);
		if(logAcesso != null) {
			logAcessoIsNew = false;
		}

		boolean logarAcesso = getLogarAcesso(request);

		String servletPath0 = request.getServletPath();
		boolean telaErro = servletPath0 != null && servletPath0.contains("/erro/");
		if(telaErro) {
			logarAcesso = false;
		}

		Thread thread = Thread.currentThread();
		if(logarAcesso && logAcessoIsNew) {
			logAcesso = criaLogAcesso(request);
			request.setAttribute(LOG_ACESSO_REQUEST_KEY, logAcesso);
			threadLocalLogAcesso.set(logAcesso);
			response = new HttpServletResponseLogAcesso(response);
			request = new HttpServletRequestLogAcesso(request);

			String threadName = logAcesso.getThreadName();
			thread.setName(threadName);

			Principal userPrincipal = request.getUserPrincipal();
			String userPrincipalName = userPrincipal != null ? userPrincipal.getName() : "";
			String ajax = logAcesso != null ? String.valueOf(logAcesso.getAjax()) : "";
			String parameters = logAcesso != null ? logAcesso.getParameters() : null;
			log.info("LogAcessoFilter.doFilter() - " + threadName + " - login: " + userPrincipalName + ", ajax: " + ajax + ", parameters: " + parameters);
		}
		else if(logAcessoIsNew && !telaErro) {
			int identityHashCode = System.identityHashCode(thread);
			String servletPath = request.getRequestURI();
			String threadName = "thdreqoff-" + servletPath + "-" + identityHashCode;
			thread.setName(threadName);
		}

		try {
			arg2.doFilter(request, response);
		}
		catch (Exception e) {
			if(logAcesso != null) {
				String stackTrace = ExceptionUtils.getStackTrace(e);
				logAcesso.setException(stackTrace);
			}
			throw e;
		}
		finally {
			if(logAcesso != null && logAcessoIsNew) {
				finalizaLogAcesso(logAcesso, (HttpServletRequestLogAcesso) request, (HttpServletResponseLogAcesso) response);
				((HttpServletResponseLogAcesso) response).flushAndClose();
			}
		}
	}

	private void finalizaLogAcesso(LogAcesso logAcesso, HttpServletRequestLogAcesso request, HttpServletResponseLogAcesso response) {

		ServletContext servletContext = request.getServletContext();
		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		LogAcessoService logAcessoService = appContext.getBean(LogAcessoService.class);

		logAcessoService.finalizaLogAcesso(logAcesso, request, response);
	}

	private LogAcesso criaLogAcesso(HttpServletRequest request) {

		ServletContext servletContext = request.getServletContext();
		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		LogAcessoService logAcessoService = appContext.getBean(LogAcessoService.class);

		LogAcesso log = logAcessoService.criaLogAcesso(request);

		return log;
	}

	private boolean getLogarAcesso(HttpServletRequest request) {

		String requestURI = request.getRequestURI();
		boolean logarAcesso = !requestURI.contains("/resources/");
		return logarAcesso;
	}

	public static LogAcesso getLogAcesso() {
		return threadLocalLogAcesso.get();
	}

	public static void setLogAcesso(LogAcesso logAcesso) {
		threadLocalLogAcesso.set(logAcesso);
	}

	@Override
	public void destroy() { }
}
