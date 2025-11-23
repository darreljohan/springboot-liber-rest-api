-- ROLES (must be inserted first due to foreign key relationship)
IF NOT EXISTS (SELECT 1 FROM Roles WHERE id = 1)
INSERT INTO Roles (id, roleName)
VALUES (1, 'USER');

IF NOT EXISTS (SELECT 1 FROM Roles WHERE id = 2)
INSERT INTO Roles (id, roleName)
VALUES (2, 'ADMIN');

-- USERS
SET IDENTITY_INSERT users ON;

IF NOT EXISTS (SELECT 1 FROM users WHERE id = 1)
INSERT INTO users (id, username, email, password, gender, role_id, deactivated, firstName, lastName)
VALUES (1, 'alice', 'alice@example.com', '{noop}Password1!', 'F', 1, 0, 'Alice', 'Smith');

IF NOT EXISTS (SELECT 1 FROM users WHERE id = 2)
INSERT INTO users (id, username, email, password, gender, role_id, deactivated, firstName, lastName)
VALUES (2, 'bob', 'bob@example.com', '{noop}Password2!', 'M', 1, 0, 'Bob', 'Johnson');

IF NOT EXISTS (SELECT 1 FROM users WHERE id = 3)
INSERT INTO users (id, username, email, password, gender, role_id, deactivated, firstName, lastName)
VALUES (3, 'charlie', 'charlie@example.com', '{noop}Password3!', 'M', 2, 0, 'Charlie', 'Brown');

SET IDENTITY_INSERT users OFF;

-- AUTHORS
SET IDENTITY_INSERT Authors ON;

IF NOT EXISTS (SELECT 1 FROM Authors WHERE id = 1)
INSERT INTO Authors (id, name, description)
VALUES (1, 'Haruki Murakami', 'Japanese novelist known for surreal narratives.');

IF NOT EXISTS (SELECT 1 FROM Authors WHERE id = 2)
INSERT INTO Authors (id, name, description)
VALUES (2, 'Ursula K. Le Guin', 'American author of speculative fiction.');

IF NOT EXISTS (SELECT 1 FROM Authors WHERE id = 3)
INSERT INTO Authors (id, name, description)
VALUES (3, 'George Orwell', 'English novelist, essayist, and critic.');

SET IDENTITY_INSERT Authors OFF;

-- BOOKS
SET IDENTITY_INSERT books ON;

IF NOT EXISTS (SELECT 1 FROM books WHERE id = 1)
INSERT INTO books (id, name, authorId, cover, releaseDate, isDeleted)
VALUES (1, 'Norwegian Wood', 1, '9780375704024', '1987-1-1', 0);

IF NOT EXISTS (SELECT 1 FROM books WHERE id = 2)
INSERT INTO books (id, name, authorId, cover, releaseDate, isDeleted)
VALUES (2, 'The Left Hand of Darkness', 2, '9780441478125', '1969-1-1', 0);

IF NOT EXISTS (SELECT 1 FROM books WHERE id = 3)
INSERT INTO books (id, name, authorId, cover, releaseDate, isDeleted)
VALUES (3, '1984', 3, '9780451524935', '1949-1-1', 0);

SET IDENTITY_INSERT books OFF;

-- REVIEWS (composite key: user_id + book_id)
IF NOT EXISTS (SELECT 1 FROM reviews WHERE userId = 1 AND bookId = 1)
INSERT INTO reviews (userId, bookId, rating, title, description, readStatus)
VALUES (1, 1, 5, 'Beautiful and melancholic.', 'Here is description', 'READING');

IF NOT EXISTS (SELECT 1 FROM reviews WHERE userId = 2 AND bookId = 2)
INSERT INTO reviews (userId, bookId, rating, title, description, readStatus)
VALUES (2, 2, 4, 'Thought-provoking worldbuilding.', 'Here is description', 'READING');

IF NOT EXISTS (SELECT 1 FROM reviews WHERE userId = 3 AND bookId = 3)
INSERT INTO reviews (userId, bookId, rating, title, description, readStatus)
VALUES (3, 3, 5, 'Chilling and prescient.', 'Here is description', 'FINISHED');

-- HIGHLIGHTED BOOKS

SET IDENTITY_INSERT HighlightBooks ON;

IF NOT EXISTS (SELECT 1 FROM HighlightBooks WHERE id = 1)
INSERT INTO HighlightBooks (id, orderNumber, bookId, addedDate)
VALUES (1, 1, 1, '2000-1-1');

IF NOT EXISTS (SELECT 1 FROM HighlightBooks WHERE id = 2)
INSERT INTO HighlightBooks (id, orderNumber, bookId, addedDate)
VALUES (2, 2, 2, '2000-1-1');

IF NOT EXISTS (SELECT 1 FROM HighlightBooks WHERE id = 3)
INSERT INTO HighlightBooks (id, orderNumber, bookId, addedDate)
VALUES (3, 3, 3, '2000-1-1');

SET IDENTITY_INSERT HighlightBooks OFF;
