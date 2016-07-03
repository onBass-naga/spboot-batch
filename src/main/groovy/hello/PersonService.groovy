package hello

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class)

    @Autowired(required=true)
    PersonDao dao;

    public void printAll() {

        this.dao.findAll().each {
            log.info("Found <" + it + "> in the database.")
        }
    }
}
