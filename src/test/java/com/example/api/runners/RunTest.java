package com.example.api.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.example.api.stepdefinitions",
        plugin = {
                "pretty",
                "summary",
                "json:target/cucumber-report/cucumber.json",
                "html:target/cucumber-report/cucumber.html"
        },
        monochrome = true
)
public class RunTest extends AbstractTestNGCucumberTests {
}
