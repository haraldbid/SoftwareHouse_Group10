package acceptance_test.steps;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

/**
*@author Harald
*/

@RunWith(Cucumber.class)
@CucumberOptions(features = "use_cases", 
	plugin = { "html:target/cucumber/wikipedia.html"}, 
	monochrome=true, 
	snippets = SnippetType.CAMELCASE, 
	glue = { "acceptance_test"},
	strict = true)
public class AcceptanceTest {

}
