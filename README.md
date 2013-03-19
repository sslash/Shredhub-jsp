Shredhub-jsp
============


The F is stored procedures?
==========================
Might ever wondered what stored procedures are, and why/when/how to use them?
Stored procedures are functions you implement in a language provided by some database vendor, e.g PostgreSQL.
You would use it when you want to run code-ish stuff in a queries; liek if you want to do more then a simple, declarative SQL query.
Normally if you have a transaction that includes more then a set of simple query statements, but stuff that requires
conditional executions, you can save heaps of time by using stored procedures. These are executed 100% in the database,
super duper fast. Imagene it being a function you implement, and add it to your database, so you can call it from you
SQL client layer in your application.  <br><br>

Example: <br>


Lets say you have two SQL tables; Pary and Place. Now you want to store shit loads of parties. But you haven't saved every
possible places yet. Now, each Party must have a place Id. You could do this in code (e.g Java) and go for something liek:<br>
<pre>

for ( all parties p ) 
	get place with name p.placename
	if not exists 
		create place and return place id $
	create party with place id = $
 </pre>
Now this could take some time.. A lot of time really, if you have crazy amounts of parties that you need to save at 1nze.
Instead, you wanna do aaaall this in your database. 
What you do is you create a stored procedure, which is implemented in a database vendor specific language (pseudo-code-ish):

<br>
<pre>
CREATE OR REPLCATE FUNCTION SAVE_PARTY (<party stuff, place_name> {
	SELECT PLACE ID FROM PLACE WHERE PLACE NAME = <place_name>
	IF !PLACE ID
		THEN INSERT SHIET INTO PLACE AND RETURN PLACE ID

	INSERT (<party stuff>, PLACE ID>) INTO PARTY 
}
</pre>

This stuff is saved in a SQL file, and you would normally just import it from your database client shell.
Then you can call the function liek this: <br> 

<pre>
SAVE_EVENT('OPEN HOUSE PARTY WITH HEAPS OF 14 YR OLDS', 'CRAIGs HOUSE');
</pre>

Using stored procedures is worth considering if you have lots of transactions that depends on conditionals.
They allow you to do everything in the database, instead of doing mixed DB-client code / DB operations. <br><br>

Check out <a href ="http://www.postgresql.org/docs/8.0/static/plpgsql.html">PostgreSQL stored procedures </a> for PostgreSQL examples.

Another sweet use of these is to create a trigger that (for insntace on an update) fires of the execution of a stored procedure. 
E.f we want to store an instance of a given party in a log file everytime this party is updated:
Something like:
created function (save-current-party-instance)

and then a trigger:
create trigger on save-current-party-instance
	ater update on parties :
		for each row execute procedure
		
Legit!




USING VIEWS
====================================
If you have reoccuring select statements, like the tedious get fanees,
you can create a view for this.
CREATE VIEW fanees AS:
SELECT this and that FROM this and that WHERE this=that

However, if you add new stuff to any of the tables, you must create an
UPDATE RULE, a CREATE RULE, AND DELETE RULE to allow new manipulations that includes these columns.


USING CROSSTABS
======================
Cross tabs creates a categorized overview of an aggregation of some sort.
For instance, one can create a calendar, which gives a categorized overview of the sum of all events for each month in a year.

Like:
jan | feb | march | april | ... | dec
5	+  0  +  40   +    34 +     + 49




  
 