package hello

import javax.sql.DataSource

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
@EnableBatchProcessing
class BatchConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory

    @Autowired
    StepBuilderFactory stepBuilderFactory

    @Autowired
    DataSource dataSource


    @Bean
    FlatFileItemReader<PersonG> reader() {
        FlatFileItemReader<PersonG> reader = new FlatFileItemReader<PersonG>()
        reader.setResource(new ClassPathResource("sample-data.csv"))
        reader.setLineMapper(new DefaultLineMapper<PersonG>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(["firstName", "lastName"] as String[])
            }})
            setFieldSetMapper(new BeanWrapperFieldSetMapper<PersonG>() {{
                setTargetType(PersonG.class)
            }})
        }})
        return reader
    }

    @Bean
    PersonItemProcessor processor() {
        return new PersonItemProcessor()
    }

    @Bean
    JdbcBatchItemWriter<PersonG> writer() {
        JdbcBatchItemWriter<PersonG> writer = new JdbcBatchItemWriter<PersonG>()
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<PersonG>())
        writer.setSql("INSERT INTO PERSON (first_name, last_name) VALUES (:firstName, :lastName)")
        writer.setDataSource(dataSource)
        return writer
    }
    

    @Bean
    JobExecutionListener listener() {
        return new JobCompletionNotificationListener(new JdbcTemplate(dataSource))
    }

    @Bean
    Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build()
    }

    @Bean
    Step step1() {
        return stepBuilderFactory.get("step1")
                .<PersonG, PersonG> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build()
    }
    
}