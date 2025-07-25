CREATE TABLE groups (
    id INT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    salary NUMERIC(10, 2) NOT NULL
);

CREATE TABLE customers_groups (
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    group_id INTEGER NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    PRIMARY KEY (customer_id, group_id)
);

INSERT INTO groups (name)
VALUES 
  ('customer'),
  ('silver'),
  ('gold'),
  ('platinum');