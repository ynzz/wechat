package com.szl.wechat.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author szl
 * @data 2018年7月21日 下午6:17:42
 *
 */
@Aspect
@Component
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Pointcut("execution(* com.szl.wechat.controller.*.*(..))")
	public void log() {

	}

	@Around("log()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		Object retVal = null;
		try {
			retVal = pjp.proceed();
		} finally {
			time = System.currentTimeMillis() - time;
			logger.info(String.format("process time: %d ms @%s.%s", time, pjp.getTarget().getClass().getName(),
					pjp.getSignature().getName()));
		}
		return retVal;
	}
}
