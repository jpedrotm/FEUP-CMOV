# FEUP-CMOV

# Introduction

This app aims to supply a more efficient ordering and delivery system, incentivizing the customers to use it, through a loyalty campaign. The idea is for the customers compose previously their orders in the app, choosing items from a menu and their quantities, and transmit them, together with possible vouchers and identification data, to a terminal inside the house. After that, the customers only need to collect the ordered items at the counter when they are ready.
Using the app, the customers should first make a registration (only once when they use the app for the first time) in the cafeteria remote service, supplying some personal data (name, NIF). The available items in the cafeteria can be obtained from the remote service at any time, as well as available emitted vouchers. These loyalty vouchers are offered whenever the customer accumulates purchases of a certain item and/or a total value.

# Instructions
- Download **ngrok** and run on port 8080.
	>ngrok.exe http 8080 (in case you are running on windows)
	
	>ngrok http 8080 (in case you are running on linux or mac)
	
- Copy the domain corresponding to the HTTP URL given by ngrok and paste it inside **AcmeCoffee/../java/../Utils/HttpHandler** line **18** and inside **ScanCodes../java/Utils/HttpHandler** line **21**.
	>example: "2683b42c.ngrok.io"

- Import the AcmeServer project as a Maven project under any IDE (we use IntelliJ).
- After that, import the json jar that is in AcmeServer/src/main/resources as a librarie to the project. ( CTRL + ALT + SHIFT +S ) in IntelliJ, for example.
- Install AcmeCoffee on one cellphone to run the main app.
- Install ScanCodes on another cellphone to run the QRCode reader app.
  > If you only have access to one cellphone you can run the AcmeCoffee on your Android Emulator and read the QRCode with ScanCodes like you would normally do with 2 cellphones.


# Contributors
- [José Monteiro](https://github.com/jpedrotm)
- [Tomás Caldas](https://github.com/tomasvaldas)

