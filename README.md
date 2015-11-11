# LeishVL
A virtual laboratory to help in leishmaniasis disease surveillance

## Considerations

Database object names were designd to fit into the provenance model name-space, in order to avoid name convertion before
operating with the database names.

[Mapping PROV Qualified Names to xsd:QName](https://github.com/lucmoreau/ProvToolbox/wiki/Mapping-PROV-Qualified-Names-to-xsd:QName)

## Environment variables

``LEISHVL_PRINT_TESTS_OUTPUT`` set value to ``true`` to print tests output.

## Development

### Run all tests logging to the console

``$ mvn clean verify -pl leishvl-core -P all-tests -Dleishvl.print.tests.out=true |& tee /tmp/LOGFILE``

### Run functional and sanity tests logging to the console

``$ mvn clean verify -pl leishvl-core -P headless-tests -Dleishvl.print.tests.out=true |& tee /tmp/LOGFILE``

## Continuous integration

``$ mvn clean verify -pl leishvl-core``

## Install from source

``$ mvn clean install -pl leishvl-test``
