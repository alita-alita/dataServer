package cn.idicc.taotie.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class MdcFilter extends OncePerRequestFilter {

	private static final String TRACE_ID = "traceId";
	private static final String REMOTE_IP = "remoteIp";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain chain) throws ServletException, IOException {

		// 生成唯一 traceId（可从 header 获取，用于链路追踪）
		String traceId = request.getHeader("X-Trace-ID");
		if (traceId == null || traceId.isEmpty()) {
			traceId = UUID.randomUUID().toString().replace("-", "");
		}

		String remoteIp = request.getRemoteAddr();

		// 将信息放入 MDC
		MDC.put(TRACE_ID, traceId);
		MDC.put(REMOTE_IP, remoteIp);

		try {
			chain.doFilter(request, response);
		} finally {
			// 必须清除！防止内存泄漏和线程复用导致脏数据
			MDC.clear();
		}
	}
}
