Ola Drive Backend API Application

- Maven based REST implementation using spark java framework

- JSON based API endpoints 

- PostGres backend. DB design has been kept simple

- All assumptions are taken into consideration in the design and development aspects 
	like 5 drivers, driver servicing time is 5 minutes

- Blocking queue has been set to a size of 1000 after which it starts blocking 

- Duplicate requests from customer cannot be added if there is a driver 
    servicing request from the same customer already

- Findbugs check has been integrated to ensure there are no preventable bugs in the code

- Lombok has been used for the default generation of getters and setters in model classes
    for simplicity sake    