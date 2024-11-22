All POST, PUT, DELETE methods will require an additional header - X-CSRF-TOKEN

Urls that have /admin in them will require admin authentication. Basic auth is used in this project.
username = admin1 password = adminpassword

Urls that have /user in them will require user authentication. Admin will not be able to view them.
username = user1 password = userpassword1
username = user0 password = userpassword0

API's available in this project:
1. Get CSRF Token - http://localhost:8080/admin/csrf-token (GET)
2. Add Grocery - http://localhost:8080/admin/add-grocery (POST)
   Request Body - {
      "name":"GroceryItem1",
      "price":"50000",
      "quantity":"10"
    }
3. Update Grocery -  http://localhost:8080/admin/update/1 (PUT)
   Request Body - {
      "name":"GroceryItem1",
      "price":"20",
      "quantity":2
    }
4. Delete Grocery - http://localhost:8080/admin/delete-grocery/1 (DELETE)
5. Update Inventory details - http://localhost:8080/admin/update-inventory/1 (PUT)
   Request Body - {
      "quantity":20
   }
7. View Available Groceries - http://localhost:8080/user/view-available-groceries (GET)
8. Add items to cart - http://localhost:8080/user/add-items (POST)
   Request Body - {
      "userId":3,
      "cartItemList":[
          {
              "groceryId":1,
              "groceryQuantity":1
          },
          {
              "groceryId":2,
              "groceryQuantity":1
          }
      ]
  }
