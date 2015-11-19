# LeishVL
A virtual laboratory to help in leishmaniasis disease surveillance

## Master Build Status

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/eubrazilcc/leishvl/blob/master/LICENSE)
[![Build Status](https://api.travis-ci.org/eubrazilcc/leishvl.svg)](https://travis-ci.org/eubrazilcc/leishvl/builds)
[![Coverage Status](https://coveralls.io/repos/eubrazilcc/leishvl/badge.svg?branch=master&service=github)](https://coveralls.io/github/eubrazilcc/leishvl?branch=master)

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
