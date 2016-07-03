package hello

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.dialect.Dialect
import org.seasar.doma.jdbc.dialect.H2Dialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
class DomaConfig implements Config {

    private DataSource datasource
    private Dialect dialect

    @PostConstruct
    void postConstruct() {
        this.dialect = new H2Dialect();
        DataSource datasource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:/database/schema.sql")
//                .addScript("classpath:/database/dataload.sql")
                .build();

        this.datasource = new TransactionAwareDataSourceProxy(datasource)
    }

    @Bean
    @Override
    DataSource getDataSource() {
        return this.datasource
    }

    @Override
    Dialect getDialect() {
        return new H2Dialect();
    }

}
