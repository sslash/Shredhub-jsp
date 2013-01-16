-- Get Faneee id
SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = 6

-- Get Fanee as shredders
SELECT * FROM Shredder WHERE Id IN (SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = 6);

-- Get Battle shreds from fanees
SELECT * FROM ShredForBattle sfb, Shred s, Battle b, Shredder sh WHERE 
sfb.ShredId = s.id AND sfb.battleId = b.id and s.owner = sh.id AND s.owner != 6 AND s.owner IN (SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = 6) ORDER BY s.timecreated DESC;
SELECT * FROM Shredder s, GuitarForShredder gs, EquiptmentForShredder es WHERE s.id = gs.ShredderId	and s.id = es.ShredderId;
-- Get battles
SELECT * FROM Battle ORDER BY TimeCreated DESC;
SELECT * FROM Shredder WHERE username like 'slash';
SELECT * FROM Shredder WHERE id = 9;

-- Get battles from fanees
SELECT * FROM Battle WHERE 
	(shredder1 IN (SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = 6)
	OR shredder2 IN (SELECT f.FaneeId FROM Fan f, Shredder s WHERE f.FanerId = s.id AND f.FanerId = 6))
	AND shredder1 != 6 AND shredder2 != 6
	ORDER BY TimeCreated DESC;

SELECT Distinct ON (s.id) s.id, s.username, s.birthdate, s.email, s.password, s.description, s.address, s.timecreated, s.profileimage, s.experiencepoints, s.timeCreated, gs.guitar, es.equiptment FROM Shredder s, GuitarForShredder gs, EquiptmentForShredder es WHERE s.id = gs.ShredderId and s.id = es.ShredderId and s.id != 6 and (s.address LIKE '%Oslo, Norway%' OR gs.guitar like 'Fender Stratocaster' OR es.Equiptment like 'Fender reverb' );

SELECT Distinct ON (s.id) s.id, s.username, s.birthdate, 
				s.email, s.password, s.description, s.address, s.timecreated, s.profileimage,
				 s.experiencepoints, s.timeCreated, gs.guitar, es.equiptment FROM Shredder s, GuitarForShredder
				 gs, EquiptmentForShredder es WHERE s.id = gs.ShredderId and s.id = es.ShredderId
				 and s.id != 6 AND (s.address LIKE 'Oslo, Norway' OR gs.guitar like 'Fender Stratocaster' OR es.Equiptment like 'Fender reverb');
		
		// Add guitars
		for ( String g : shredder.getGuitars() ) {
			sql.append("OR gs.guitar like '").append(g).append("' ");
		}
		
		// Add equiptment
		for ( String e : shredder.getEquiptment() ) {
			sql.append("OR es.Equiptment like '").append(e).append("' 


-- Delete shredder
SELECT * FROM Shredder;
DELETE FROM GuitarForShredder WHERE ShredderId = 22;
DELETE FROM EquiptmentForShredder WHERE ShredderId = 22;
DELETE FROM UserRole WHERE ShredderId = 22;
DELETE FROM Shredder WHERE Id = 22;