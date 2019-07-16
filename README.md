# CheckoutEWB2: Fundraiser Management System

## Program Overview

CheckoutEWB2 is a complete redesign of the original CheckoutEWB project (https://github.com/Senarii/CheckoutEWB). This is a system designed for the Engineers Without Borders - UMass Chapter to better keep track of details during the yearly auction in which items brought back from Kenya and Ghana are sold, and to improve efficiency of the check-out/check-in process.

The program is meant to manage all of the transactions that take place at the auction, which means that it tracks guests, auction items, add-on items, donations, etc in order to remove the excessive amounts of folders and paper forms that were formerly used to complete this process.

## Data Management

The data modified in the program can be located in a local file share directory, and any number of instances of the program will interact with the directory to load/update the data for the respective record. At the end, the .csv files can be taken from the directory and imported into Excel for analysis/logging.

## Layout

CheckoutEWB2 is split into the following pages:

1) Guest Management \
   1.1. Add-on Item Management \
   1.2. Payment Tracking
2) Item Management

### Guest Management
This is the main landing page for the program, and the one that gets the most use. Here, it is possible to create/remove guests, fill out contact details, view items won, and modify add-on items and payments.

#### Add-on Item Management
Add-on items are those objects such as T-shirts, glasses with our logo, and other small things which aren't directly included in the auction but are still being offered at the checkout counter to those who are interested. It is possible to add/remove multiple of a single item, and to track tickets through this menu as well.

#### Payment Tracking
This tracks all guest payments, including showing a total due, total remaining, change required, and other information to the user. Payments can be made with multiple different methods, as a donation or direct payment, and with comments.

### Item Management
Items that are being auctioned are added to this page, and can have their name and price edited. Additionally, it is possible to assign an item to an Guest as an owner, and it will populate on the guest's page for pricing and details.
