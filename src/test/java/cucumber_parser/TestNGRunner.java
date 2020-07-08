package cucumber_parser;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features= {"src/test/resources/Features/login.feature"})
public class TestNGRunner extends AbstractTestNGCucumberTests{

}
