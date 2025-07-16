package br.com.tradeflow.util.servlet;

import br.com.tradeflow.util.other.Bolso;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

public class HttpServletRequestLogAcesso extends ContentCachingRequestWrapper {

	private Bolso<String> forward = new Bolso<>();

	public HttpServletRequestLogAcesso(HttpServletRequest request) {
		super(request);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(final String path) {

		final RequestDispatcher requestDispatcher = super.getRequestDispatcher(path);

		return new RequestDispatcher() {

			@Override
			public void include(ServletRequest paramServletRequest, ServletResponse paramServletResponse) throws ServletException, IOException {
				requestDispatcher.include(paramServletRequest, paramServletResponse);
			}

			@Override
			public void forward(ServletRequest paramServletRequest, ServletResponse paramServletResponse) throws ServletException, IOException {
				requestDispatcher.forward(paramServletRequest, paramServletResponse);
				forward.setObjeto(path);
			}
		};
	}

	public String getForward() {
		return forward.getObjeto();
	}
}
