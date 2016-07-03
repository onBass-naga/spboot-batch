package hello

import java.sql.ResultSet
import java.sql.SQLException

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class)

	private final JdbcTemplate jdbcTemplate

	@Autowired
	JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate
	}

	@Override
	void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results")

			List<Person> results = jdbcTemplate.query(
					"SELECT first_name, last_name FROM people",
					[mapRow: { rs, row ->
						new Person(rs.getString(1), rs.getString(2)) }] as RowMapper<Person>)

			results.each {
				log.info("Found <" + it + "> in the database.")
			}
		}
	}
}