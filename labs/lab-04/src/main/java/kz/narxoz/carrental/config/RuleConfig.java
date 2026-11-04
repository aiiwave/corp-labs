package kz.narxoz.carrental.config;

import java.util.List;
import kz.narxoz.carrental.domain.CompositeRule;
import kz.narxoz.carrental.domain.Rule;
import kz.narxoz.carrental.domain.TerminalStatusRule;
import kz.narxoz.carrental.domain.TransitionRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleConfig {

    @Bean
    public Rule rule() {
        return new CompositeRule(List.of(new TerminalStatusRule(), new TransitionRule()));
    }
}
