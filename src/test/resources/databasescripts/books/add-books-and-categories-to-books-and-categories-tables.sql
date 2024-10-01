INSERT INTO categories (id, name, description, is_deleted) VALUES (1, 'Detective', 'Detective description', 0);
INSERT INTO books (id, title, author, isbn, price, is_deleted) VALUES (1, 'Sherlock Holmes', 'Arthur Conan Doyle', '000-1-00-111111-0', 10.99, 0);
INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
