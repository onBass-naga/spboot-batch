package hello

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.batch.item.ItemProcessor

class PersonItemProcessor implements ItemProcessor<PersonG, PersonG> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class)

    @Override
    PersonG process(final PersonG person) throws Exception {
        final String firstName = person.firstName.toUpperCase()
        final String lastName = person.lastName.toUpperCase()

        final PersonG transformedPerson = new PersonG(
                [firstName: firstName, lastName: lastName])

        log.info("Converting (" + person + ") into (" + transformedPerson + ")")

        return transformedPerson
    }

}