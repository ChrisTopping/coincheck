# Coincheck

Coincheck is a simple alerting program for crypto-currency market capitalization.

Once set up, Coincheck will send email alerts to a provided email address based on:

* Market cap increasing above upper threshold
* Market cap decreasing below lower threshold
* Market cap increasing by more than a specified percentage in a given time window
* Market cap decreasing by more than a specified percentage in a given time window

Due to the polling delay incurred by CoinGecko, the market cap is calculated from historical spot price values and can be significantly outdated. 

## Requirements

Coincheck requires:

* a running installation of Java JRE v1.8
* a Gmail account with `Allow less secure apps` toggled on
* a free BscScan API key

Currently Coincheck only works with BSC tokens, as the market cap is taken from BscScan.

## Set Up

### Java

Coincheck runs using Java JRE v1.8. To install the JRE visit https://www.oracle.com/java/technologies/javase-downloads.html, scroll down to `Java SE 8` and click `JRE Download`. 
Then select the download corresponding to your operating system. 

### JAR File

The Coincheck JAR file can be generated using the gradle command line.

To do so, run the following command in the project's root directory:

* `./gradlew clean build` (Unix)
* `gradlew clean build` (Windows)

### Properties

An example application.yml file can be found in the /src/main/resources/ directory.

`call-rate-milliseconds`: Period between polling the market cap. 
This should be at least 10 seconds to ensure that the calls are not rate limited by 3rd party APIs.

`email`
    
* `from`: Sender address for email alerts. Must be a gmail account.
* `password`: Password for sender account.
* `to`: Recipient address for email alerts.

`coins`

* `name`: Human readable token name
* `platform-id`: Id of token platform for use in CoinGecko API calls. A full list can be found here https://api.coingecko.com/api/v3/asset_platforms
* `contract-address`: Contract address for the token.
* `alert-configuration`
    * `upper-million`: Upper market cap threshold (in $m) to trigger an alert
    * `lower-million`: Lower market cap threshold (in $m) to trigger an alert
    * `increase-percentage`: Market cap percentage increase threshold to trigger an alert
    * `increase-minutes`: Number of minutes over which to evaluate `increase-percentage`
    * `decrease-percentage`: Market cap percentage decrease threshold to trigger an alert
    * `decrease-minutes`: Number of minutes over which to evaluate `increase-percentage`
    * `cooldown-minutes`: Number of minutes Coincheck will prevent a duplicate alert in the case than an alert has already been sent

Multiple coins can be configured, and will be checked simultaneously. For each coin, include each of the above configuration properties, e.g.:

    -
        name: GUH
        platform-id: binance-smart-chain
        ...
    -
        name: GDL
        platform-id: binance-smart-chain
        ...

If you do not wish to receive a specific type of alert, simply don't include the alert in the configuration. E.g. to not receive lower threshold alerts, remove the `lower-millions` property from the .yml file.

### Email

In order for Coincheck to be able to access google's SMTP functionality, visit https://myaccount.google.com/lesssecureapps and ensure than `Allow less secure apps` is turned on.

## Running

Once the Coincheck jar file has been generated, and the `application.yml` file has been populated correctly, place the .yml in a /config subdirectory of the same directory as the .jar and run the command:

    java -jar .\coincheck-0.0.1-SNAPSHOT.jar --spring.config.location=file:./config/application.yml
    
To terminate the application, press CTRL+C in the terminal.

To change configuration properties, the application must be terminated and restarted.