package hello

import groovy.transform.Canonical
import org.seasar.doma.GeneratedValue
import org.seasar.doma.GenerationType
import org.seasar.doma.Id

@Canonical
class PersonG {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id

    String lastName
    String firstName
}