# How to use
> * Add annotations of the form "#!<scenario_name>" under all the scenarios.
> * Make sure all the scenarios have their test data stored in the mongo db.
> * Navigate to src/test/java/cucumber_parser/TestNGRunner.java
> * right clickk and choose run with TestNG
> * The tests run with their respective data obtained from the Mongo DB

# How it works
> * Before running any tests, the FeatureParser class will replace every marker ("#!<scenario_name>") with data tables.
> * It now runs the test normally.
> * Once the tests have finished executing, the FeatureParser class will revert all the data tables back to their original markers.
