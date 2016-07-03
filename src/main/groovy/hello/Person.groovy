package hello

import groovy.transform.Canonical

@Canonical
class Person {
    String lastName
    String firstName
}