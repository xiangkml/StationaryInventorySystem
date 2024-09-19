/*

page and word design
forgot pw function
display rules for each input
link stock receives with po /
list out the product instead of user input
edit staff

checking function
small details haven't check for

checking confirmation and design
supplier /
product /
warehouse c/ dx
whProd c/ dx
staff no confirmation / design /
purchase order c/ dx
summary dx




doing add purchase order/
debug product edit SKU /
debug staff ID and pswd, seperate validation /
supplier, add supplier, no of product have problem /
supplier, add supplier, confirmation /
supplier, view sup info, view specific sup info, if enter invalid supID no invalid input msg come out /
supplier, edit sup, enter supID but no invalid input msg /

edit warehouse, warehouse name, if enter '-1', cant exit but change the warehouse name to -1 /
for edit XXXXX, if enter '-1' cant exit (warehouse done)
add po, how many product want to order (if exceed the no of product supplied by supplier, should be invalid)


if got time: supplier, edit sup prod supplied, if the supplier originally supply 3 prods and after this can only supply 2 prods, in edit function only can change from old prod sku to new prod sku, cannot inc or dec prod supplied

debug display supplier /
debug delete product /
register password valid first enter, if valid then re-enter /
supplier become supplier name change file , read and write /
supplier validate tel /
supplier validate number of product -1 cant return /

doing: let user set reorder lv/
supplier crud /
warehouse crud /
warehouse & product /
product crud /

supplier product change to product type list /
stock trsf and return check validation /
add ui, polymorphism /

Supplier ViewAllSupplier(), get what product the supplier supply from arraylist /
Supplier addSupplier() enter no of product, do validation for negative input /
*/


/*
baobei :

pw rules add can contain special character

test product confirmation

until supplier 158 /
display supplier design /

add header and menu design /  IN MAIN
add staff validate all input /
add space between word
add header and design
add subtitle for each menu / IN MAIN
add rules for name, tel, email / IN SUPPLIER

******* have a function to display , find it!!

for supplier method: add supplier  /
- display supplier info to user before save
- double confirm from the user

for supplier method: delete supplier /
- display supplier info to user before delete
- double confirm from the user

for supplier method: viewAllSupplier /
- display all supplier info

for supplier method: viewOneSupplier /
- display supplier info

for warehouse method: add warehouse
- display warehouse info to user before save
- double confirm from the user

for warehouse method: delete warehouse
- display warehouse info to user before delete
- double confirm from the user

for warehouse method: viewAllWarehouse
- display all warehouse info
- design product listed

for warehouse method: viewOneWarehouse
- display warehouse info
- design product listed
                      ^
same thing for product|



for purchaseOrder method: add purchase order
- add header,rules,-1 use to exit design
- add rules for input
- design input

for purchaseOrder method: displayPO
- design

for summary report all
- design

for summary report one
- header
-design

*/


/*  Assumption

 1. when add new product, all warehouse will automatically add the product but the quantity will be zero
 2. all product import from supplier will store in main warehouse which is KL warehouse
 3. other branches will import the stock from the main warehouse
 4. when user add new supplier and product supplied, must add the product first
 5. same things apply to goods receives
 6. all branch can only perform the stock transfer and stock return between main warehouse and itself
 7. reorder level must at least 5
 8. when confirmation, -1 to exit doesn't function

*/














