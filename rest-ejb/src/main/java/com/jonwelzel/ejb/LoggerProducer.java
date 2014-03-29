package com.jonwelzel.ejb;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.ejb.annotations.Log;

public class LoggerProducer {

	@Produces
	@Log
	public Logger getLogger(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

}