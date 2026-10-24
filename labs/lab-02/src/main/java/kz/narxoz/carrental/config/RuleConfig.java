package kz.narxoz.carrental.config;

import kz.narxoz.carrental.domain.CompositeRule;
import kz.narxoz.carrental.domain.Rule;
import kz.narxoz.carrental.domain.TerminalStatusRule;
import kz.narxoz.carrental.domain.TransitionRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wires the two plain-Java Rule implementations into a single bean.
 * This is the only place that decides which rules apply, and in what order.
 */
@Configuration
public class RuleConfig {

    @Bean
    public Rule rule() {
        return new CompositeRule(List.of(
                new TerminalStatusRule(),
                new TransitionRule()
        ));
    }
}
