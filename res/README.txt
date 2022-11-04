README

Our application has the following features working:
Portfolio creation through the console
Portfolio creation through CSV file
Adding stocks to the portfolio
Storing the portfolio data in a CSV file
Ability to see the existing portfolio names
Ability to see the composition of the portfolio
Ability to see the total value of a given portfolio for a given date
Locking of portfolio once stocks are added to it


DESCRIPTION

Portfolio creation through the console: The user can create a portfolio by entering the portfolio name in the console and later adding stocks to it.

Portfolio creation through CSV file: The user can choose to create a portfolio by providing an existing CSV file that has portfolio and stock data in it. The format for the recommended CSV file is presented in the res folder. The file name is stocks.csv 

Adding stocks to the portfolio: If the portfolio already has stocks in it, then it cannot be modified. The add stock operation can be performed on only empty portfolios. To add stocks, the user has to provide a valid portfolio name to which stocks are to be added. The user has to provide a valid ticker of the stock whose shares are to be bought followed by the number of shares to be bought. The stocks will be successfully added to the portfolio only if valid inputs are provided. The CSV file for the portfolio will be downloaded once the user has done adding stocks to the portfolio. The file will be downloaded to the res folder.

Storing the portfolio data in a CSV file: After the stocks are successfully added to the portfolio, the application automatically creates a copy of the portfolio and its composition in a CSV file

Ability to see the existing portfolio names: The application allows a user to view all the existing portfolio names

Ability to see the composition of the portfolio: The application lets the user look up the composition of any existing portfolio. The composition includes the 
ticker, quantity, buying price, and the total value of the portfolio

Ability to see the total value of a given portfolio for a given date: The application provides the user with the total value of any given portfolio at the given date.

Locking of portfolio once stocks are added to it: Once the stocks are added to the portfolio and the user decided to complete the order, the application locks the portfolio making sure that the portfolio isn’t modified again.
