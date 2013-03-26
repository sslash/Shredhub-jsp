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



LOADING SCRIPTS:
In order to avoid blocking shiet:
Don’t docwrite scripts
April 10, 2012 5:29 pm | 32 Comments

In yesterday’s blog post, Making the HTTP Archive faster, one of the biggest speedups came from not using a script loader. It turns out that script loader was using document.write to load scripts dynamically. I wrote about the document.write technique in Loading Script Without Blocking back in April 2009, as well as in Even Faster Web Sites (chapter 4). It looks something like this:

document.write('<script src="' + src + '" type="text/javascript"><\/script>'):
The problem with document.write for script loading is:

Every DOM element below the inserted script is blocked from rendering until the script is done downloading (example).
It blocks other dynamic scripts (example). One exception is if multiple scripts are inserted using document.write within the same SCRIPT block (example).
Because the script loader was using document.write, the page I was optimizing rendered late and other async scripts in the page took longer to download. I removed the script loader and instead wrote my own code to load the script asynchronously following the createElement-insertBefore pattern popularized by the Google Analytics async snippet:

var sNew = document.createElement("script");
sNew.async = true;
sNew.src = "http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js";
var s0 = document.getElementsByTagName('script')[0];
s0.parentNode.insertBefore(sNew, s0);
Why does using document.write to dynamically insert scripts produce these bad performance effects?

It’s really not surprising if we walk through it step-by-step: We know that loading scripts using normal SCRIPT SRC= markup blocks rendering for all subsequent DOM elements. And we know that document.write is evaluated immediately before script execution releases control and the page resumes being parsed. Therefore, the document.write technique inserts a script using normal SCRIPT SRC= which blocks the rest of the page from rendering.

On the other hand, scripts inserted using the createElement-insertBefore technique do not block rendering. In fact, if document.write generated a createElement-insertBefore snippet then rendering would also not be blocked.

At the bottom of my Loading Script Without Blocking blog post is a decision tree to help developers choose which async technique to use under different scenarios. If you look closely you’ll notice that document.write is never recommended. A lot of things change on the Web, but that advice was true in 2009 and is still true today.
  
 