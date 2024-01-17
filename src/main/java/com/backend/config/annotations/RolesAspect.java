package com.backend.config.annotations;

import com.backend.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class RolesAspect {
    private final AuthorizeImpl authBean;

    @Around("@annotation(com.backend.config.annotations.Roles)")
    public Object doSomething(ProceedingJoinPoint jp) throws Throwable {

        Set<UserRole> roles = Arrays.stream(((MethodSignature) jp.getSignature()).getMethod()
                .getAnnotation(Roles.class).value()).collect(Collectors.toSet());

        HttpServletRequest httpServletRequest = getRequest();
        if (authBean.authorize(httpServletRequest, roles))
            return jp.proceed();
        throw new ForbiddenException();
    }

    private HttpServletRequest getRequest() {

        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return servletRequestAttributes.getRequest();
    }
}