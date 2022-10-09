package main.java.tracker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import main.java.tracker.Exceptions.IllegalArgumentPassed;

import static org.junit.jupiter.api.Assertions.*;

class TrackerTest {

    final String firstNameError = "Incorrect first name.";
    final String lastNameError = "Incorrect last name.";
    final String emailError = "Incorrect email.";
    final String credentialError = "Incorrect credentials.";

    @ParameterizedTest
    @ValueSource(strings = {"a-- LastName email@email.com",
            "a LastName email@email.com",
            "ЖЖЖ LastName email@email.com",
            "'andrey LastName email@email.com",
            "-andrey LastName email@email.com",
            "andrey' LastName email@email.com",
            "andrey- LastName email@email.com",
            "and--rey LastName email@email.com",
            "and'-rey LastName email@email.com",
            "and-'rey LastName email@email.com",
            "and''rey LastName email@email.com"})
    void InvalidFirstNameTests(String input) {
        try {
            new Tracker().addStudent(input);
        } catch (IllegalArgumentPassed ex) {
            assertEquals(ex.getMessage(), firstNameError);
        } catch (Exception ex) {
            assert(false);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Andrey 'Last Name email@email.com",
            "Andrey Last Name' email@email.com",
            "Andrey Last 'Name email@email.com",
            "Andrey Last' Name email@email.com",
            "Andrey -LastName email@email.com",
            "Andrey LastName- email@email.com",
            "Andrey Last- Name email@email.com",
            "Andrey Last -Name email@email.com",
            "Andrey -Last Name email@email.com",
            "Andrey Last Name- email@email.com",
            "Andrey L email@email.com",
            "Andrey LasФName email@email.com",
            "Andrey Last.Name email@email.com",
            "Andrey Last--Name email@email.com",
            "Andrey Last-'Name email@email.com",
            "Andrey Last'-Name email@email.com",
            "Andrey Last''Name email@email.com",
            "Andrey Last   Na'-me email@email.com",
    })
    void InvalidLastNameTests(String input){
        try {
            new Tracker().addStudent(input);
        } catch (IllegalArgumentPassed ex) {
            assertEquals(ex.getMessage(), lastNameError);
        } catch (Exception ex) {
            assert(false);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Andrey Last Name noAtSymbol.com",
            "Andrey Last Name @service.com",
            "Andrey Last Name name@com",
            "Andrey Last Name name@domain-com",
            "Andrey Last Name name@name@domain.com",
            "Andrey Last Name name@domain.",
            "Andrey Last Name name@.com",
    })
    void InvalidEmailTests(String input){
        try {
            new Tracker().addStudent(input);
        } catch (IllegalArgumentPassed ex) {
            assertEquals(ex.getMessage(), emailError);
        } catch (Exception ex) {
            assert(false);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Andrey noAtSymbol.com",
            " Name @service.com",
            "Andrey-ast-Name-name@com",
            "An-drey-Last--am-e name@domain-com",
            "adda dada addd@op.pl",
    })
    void InvalidCredentials(String input){
        try {
            new Tracker().addStudent(input);
        } catch (IllegalArgumentPassed ex) {
            assertEquals(ex.getMessage(), credentialError);
        } catch (Exception ex) {
            assert(true);
        }
    }

}
