package org.ecostanzi.jmint.cucumber.stepdefs;

import org.ecostanzi.jmint.JmintApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = JmintApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
