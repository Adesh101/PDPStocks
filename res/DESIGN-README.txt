DESIGN README

The application is built using MVC Design. All the functionalities are implemented in the model of the application.
The model includes two components:
Operation: It contains all the functionalities of the application like creating the portfolio, adding stocks to the portfolio, showing all the portfolios present, returning the composition, and returning the value of the portfolio for a given date.
Stocks: It is the part that deals with the stock data. It calls the Alphavantage API and handles the resulting data.

The controller includes:
Controller: It is responsible for controlling the whole program. This controller handles input and redirects the flow to the appropriate child controllers
Add stock to the portfolio: controls the input and flow for the add stock operation
Create a new portfolio: controls the input and flow for the new portfolio creation through the console
Create new portfolio CSV: controls the input and flow for the creation of a new portfolio from a CSV file
Show amount of portfolio by date: controls the input and flow when the amount of portfolio needs to be estimated for a given date
Show composition: controls the input and flow for the show composition operation 
Show existing portfolios: controls the flow when all existing portfolios need to be displayed


The view includes the text-based interface where the input is read and output messages are displayed. A readable object is used to read the user input and an appendable object is used to return the output