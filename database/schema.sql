use railway;

drop table if exists customers, orders, order_details, order_status;


create table customers
(
    name varchar(32),
    address varchar(128) not null,
    email varchar(128) not null,

    PRIMARY KEY (name)
);

INSERT into customers
    (name, address, email)
values
    ("fred", "201 Cobblestone Lane", "fredflintstone@bedrock.com"),
    ("sherlock", "221B Baker Street, London", "sherlock@consultingdetective.org"),
    ("spongebob", "124 Conch Street, Bikini Bottom", "spongebob@yahoo.com"),
    ("jessica", "698 Candlewood Land, Cabot Cove", "fletcher@gmail.com"),
    ("dursley", "4 Privet Drive, Little Whinging, Surrey", "dursley@gmail.com");

create table orders
(
    orderid varchar(8),
    name varchar(32) not null,
    address varchar(128) not null,
    email varchar(128) not null,
    orderdate datetime,

    PRIMARY KEY (orderid),
    FOREIGN KEY (name) REFERENCES customers(name)
);

create table order_details
(
    id int
    auto_increment NOT NULL,
    orderid varchar
    (8) NOT NULL,
    item varchar
    (32) NOT NULL,
    quantity INTEGER NOT NULL,
    
    primary key
    (id),
    FOREIGN KEY
    (orderid) REFERENCES orders
    (orderid)
);

    create table order_status (
    orderid varchar(8) NOT NULL,
    delivery_id varchar(128),
    status ENUM('pending', 'dispatched'),
    status_update TIMESTAMP,
    
    PRIMARY KEY
    (orderid)
);