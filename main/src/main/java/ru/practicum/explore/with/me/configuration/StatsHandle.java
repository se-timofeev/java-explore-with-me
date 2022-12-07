package ru.practicum.explore.with.me.configuration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.practicum.explore.with.me.service.StatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class StatsHandle implements HandlerInterceptor {

    private final StatService statsService;

    public StatsHandle(StatService statsService) {
        this.statsService = statsService;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           HttpServletResponse response, @NonNull Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("StatsHandle postHandle {},{}", request, response);
        if (response.getStatus() == 200) {
            statsService.statsHit(request);
        }

    }

}
