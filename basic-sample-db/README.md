# Transaction Isolation - the 'I' in ACID
## Phantom Rows
> https://dev.mysql.com/doc/refman/8.0/en/innodb-next-key-locking.html
## 
## How to analysis read uncommit ,read commit,REPEATABLE-READ and so on
> https://sqlperformance.com/2015/04/t-sql-queries/the-read-uncommitted-isolation-level

# Reference 
> https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-isolation-levels.html



# Read uncommitted
> Transactions are not isolated from each other.If the DBMS supports other transaction isolation levels,it 
ignores whatever mechanism it uses to implement thoes levels.So that they do not adversely affect other transactions,
transactions running on Read Uncommitted level are usually read-only.

# Read commited
>  The transaction waits until rows write-locked by other transactions are uncloked;this prevents it from reading any "dirty" data.
The transaction holds a read lock or write lock on the current row to prevent other transactions from updating or deleting it .
The transaction releases read locks when it moves off the current row.It holds write locks until it is commited  or rolled back.

# Repeatable read
>  The transaction waits until rows write-locked by other transactions are uncloked;this prevents it from reading any "dirty" data.
The transaction holds read locks on all rows it returns to the application and write locks on all rows it inserts, updates, or deletes. 
For examples, if the transaction includes the SQL statement SELECT * FROM ORDERS,The transaction 
read-locks rows as the application fetches them. If the transaction includes the SQL statement DELETE FROM Orders WHERE Status = 'CLOSED', 
the transaction write-locks rows as it deletes them. 
Because other transactions cannot update or delete these rows, the current transaction avoid any nonrepeatable reads.
The transaction release its locks when it is commited or rolled back. 

# Serializable 
> The transaction waits until rows write-locked by other transactions are unlocked; this prevents it from reading any "dirty" data.
The transaction holds a read lock or write lock on the range of rows it affects.
For example, if the transaction includes the SQL statement SELECT * FROM Orders, the range is the entire Orders table; 
the transaction read-locks the table and does not allow any new rows to be inserted into it.
If the transaction includes the SQL statement DELETE FROM Orders WHERE Status = 'CLOSED', the range is all rows with a Status of "CLOSED"; 
the transaction write-locks all rows in the Orders table with a Status of "CLOSED" and does not allow any rows to be inserted or updated such that the resulting row has a Status of "CLOSED".
Because other transactions cannot update or delete the rows in the range,
the current transaction avoids any nonrepeatable reads. 
Because other transactions cannot insert any rows in the range, the current transaction avoids any phantoms.
The transaction releases its lock when it is committed or rolled back.